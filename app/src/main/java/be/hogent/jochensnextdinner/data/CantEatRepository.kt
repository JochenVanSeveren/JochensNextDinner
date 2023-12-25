package be.hogent.jochensnextdinner.data

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.work.WorkInfo
import androidx.work.WorkManager
import be.hogent.jochensnextdinner.data.database.CantEatDao
import be.hogent.jochensnextdinner.data.database.asDomainCantEats
import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.model.asApiObject
import be.hogent.jochensnextdinner.model.asDbCantEat
import be.hogent.jochensnextdinner.network.CantEatApiService
import be.hogent.jochensnextdinner.network.asDomainObject
import be.hogent.jochensnextdinner.network.deleteCantEatAsFlow
import be.hogent.jochensnextdinner.network.getCantEatsAsFlow
import be.hogent.jochensnextdinner.network.postCantEatAsFlow
import be.hogent.jochensnextdinner.network.putCantEatAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.net.SocketTimeoutException
import java.util.UUID


interface CantEatRepository {
    fun getCantEats(): Flow<List<CantEat>>
    suspend fun insertCantEat(cantEat: CantEat)
    suspend fun createCantEat(cantEat: CantEat): CantEat
    suspend fun deleteCantEat(cantEat: CantEat)
    suspend fun updateCantEat(cantEat: CantEat)
    suspend fun refresh()
    var wifiWorkInfo: Flow<WorkInfo>
}

class CachingCantEatRepository(
    private val cantEatDao: CantEatDao,
    private val cantEatApiService: CantEatApiService,
    context: Context
) : CantEatRepository {

    override fun getCantEats(): Flow<List<CantEat>> {
        return cantEatDao.getAllItems().map {
            it.asDomainCantEats()
        }
    }

    override suspend fun insertCantEat(cantEat: CantEat) {
        cantEatDao.insert(cantEat.asDbCantEat())
    }

    override suspend fun createCantEat(cantEat: CantEat): CantEat {
        val apiCantEat = cantEat.asApiObject()
        Log.d(TAG, "createCantEat:  $apiCantEat")
        val response = cantEatApiService.postCantEatAsFlow(apiCantEat).first()
        val createdCantEat = response.asDomainObject()
        cantEatDao.insert(createdCantEat.asDbCantEat())
        return createdCantEat
    }

    override suspend fun deleteCantEat(cantEat: CantEat) {
        val apiCantEat = cantEat.asApiObject()
        cantEatApiService.deleteCantEatAsFlow(apiCantEat.name).first()
        cantEatDao.delete(cantEat.asDbCantEat())
    }

    override suspend fun updateCantEat(cantEat: CantEat) {
        val apiCantEat = cantEat.asApiObject()
        val response = cantEatApiService.putCantEatAsFlow(apiCantEat).first()
        cantEatDao.update(response.asDomainObject().asDbCantEat())
    }

    private var workID = UUID(1, 2)
    private val workManager = WorkManager.getInstance(context)
    override var wifiWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfoByIdFlow(workID)

    override suspend fun refresh() {
//        val constraints =
//            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
//                .build()
//        val requestBuilder = OneTimeWorkRequestBuilder<WifiNotificationWorker>()
//        val request = requestBuilder.setConstraints(constraints).build()
//        workManager.enqueue(request)
//        workID = request.id
//        wifiWorkInfo = workManager.getWorkInfoByIdFlow(request.id)

        try {
            // Fetch all items from the API
            val apiCantEats = cantEatApiService.getCantEatsAsFlow().firstOrNull()?.map { it.asDomainObject() }

            if (apiCantEats != null) {
                // Fetch all items from the local database
                val dbCantEats = cantEatDao.getAllItems().first().asDomainCantEats()

                // Find the items that are in the local database but not in the API
                val itemsToDelete = dbCantEats.filter { dbCantEat ->
                    apiCantEats.none { apiCantEat ->
                        apiCantEat.serverId == dbCantEat.serverId
                    }
                }

                // Delete these items from the local database
                for (item in itemsToDelete) {
                    cantEatDao.delete(item.asDbCantEat())
                }

                // Insert or update the items from the API into the local database
                for (cantEat in apiCantEats) {
                    cantEatDao.insert(cantEat.asDbCantEat())
                }
            }
        } catch (e: SocketTimeoutException) {
            // log something
        }
    }
}