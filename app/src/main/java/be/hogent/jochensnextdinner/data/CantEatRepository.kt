package be.hogent.jochensnextdinner.data

import android.util.Log
import be.hogent.jochensnextdinner.data.database.CantEatDao
import be.hogent.jochensnextdinner.data.database.asDomainCantEats
import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.network.CantEatApiService
import be.hogent.jochensnextdinner.network.asDomainObject
import be.hogent.jochensnextdinner.network.asDomainObjects
import be.hogent.jochensnextdinner.network.deleteCantEatAsFlow
import be.hogent.jochensnextdinner.network.getCantEatsAsFlow
import be.hogent.jochensnextdinner.network.postCantEatAsFlow
import be.hogent.jochensnextdinner.network.putCantEatAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.net.SocketTimeoutException


interface CantEatRepository {
    fun getCantEats(): Flow<List<CantEat>>
    suspend fun insertCantEat(cantEat: CantEat)
    suspend fun saveCantEat(cantEat: CantEat): CantEat
    suspend fun deleteCantEat(cantEat: CantEat)
    suspend fun updateCantEat(cantEat: CantEat)
    suspend fun refresh()
//    var wifiWorkInfo: Flow<WorkInfo>
}

class CachingCantEatRepository(
    private val cantEatDao: CantEatDao,
    private val cantEatApiService: CantEatApiService,
) : CantEatRepository {

    override fun getCantEats(): Flow<List<CantEat>> {
        return cantEatDao.getAllItems().map {
            it.asDomainCantEats()
        }.onEach { cantEats ->
            if (cantEats.isEmpty()) {
                refresh()
            }
        }
    }

    override suspend fun insertCantEat(cantEat: CantEat) {
        cantEatDao.insert(cantEat.asDbCantEat())
    }

    override suspend fun saveCantEat(cantEat: CantEat): CantEat {
        val apiCantEat = cantEat.asApiObject()
        val response = if (cantEat.serverId != null)
            cantEatApiService.putCantEatAsFlow(apiCantEat).first()
        else
            cantEatApiService.postCantEatAsFlow(apiCantEat).first()
        val createdCantEat = response.asDomainObject()
        cantEatDao.insert(createdCantEat.asDbCantEat())
        return createdCantEat
    }

    override suspend fun deleteCantEat(cantEat: CantEat) {
        if (cantEat.serverId != null) {
            val apiCantEat = cantEat.asApiObject()
            cantEatApiService.deleteCantEatAsFlow(apiCantEat.id!!).first()
        }
        cantEatDao.delete(cantEat.asDbCantEat())
    }

    override suspend fun updateCantEat(cantEat: CantEat) {
        val apiCantEat = cantEat.asApiObject()
        val response = cantEatApiService.putCantEatAsFlow(apiCantEat).first()
        cantEatDao.update(response.asDomainObject().asDbCantEat())
    }

//    private var workID = UUID(1, 2)
//    private val workManager = WorkManager.getInstance(context)
//    override var wifiWorkInfo: Flow<WorkInfo> =
//        workManager.getWorkInfoByIdFlow(workID)

    /**
     * Refreshes the local database with data fetched from the remote API.
     *
     * This function performs the following steps:
     * 1. Fetches all items from the local database.
     * 2. Fetches all items from the remote API.
     * 3. Compares the local and remote items:
     *    - If an item is present in the local database but not in the remote, it is deleted from the local database.
     *    - If an item is present in the remote but not in the local database, it is added to the local database.
     *    - If an item is present in both the local database and the remote, the local item is updated with the data from the remote.
     *
     * @throws SocketTimeoutException If a timeout occurs while fetching data from the remote API.
     */
    override suspend fun refresh() {
        try {
            // Fetch all items from the API
            val dbCantEats = cantEatDao.getAllItems().first().asDomainCantEats()

            cantEatApiService.getCantEatsAsFlow().asDomainObjects().collect { apiCantEats ->
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
            Log.e("SocketTimeoutException", e.message ?: "No message")
        }
    }
}