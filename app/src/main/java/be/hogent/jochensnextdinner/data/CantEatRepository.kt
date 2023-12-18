package be.hogent.jochensnextdinner.data

import WifiNotificationWorker
import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import be.hogent.jochensnextdinner.data.database.CantEatDao
import be.hogent.jochensnextdinner.data.database.asDbCantEat
import be.hogent.jochensnextdinner.data.database.asDomainCantEat
import be.hogent.jochensnextdinner.data.database.asDomainCantEats
import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.network.CantEatApiService
import be.hogent.jochensnextdinner.network.asDomainObjects
import be.hogent.jochensnextdinner.network.getCantEatsAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.net.SocketTimeoutException
import java.util.UUID


interface CantEatRepository {
    fun getCantEats(): Flow<List<CantEat>>
    fun getCantEat(id: String): Flow<CantEat?>
    suspend fun insertCantEat(cantEat: CantEat)
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
        }.onEach {
            if (it.isEmpty()) {
                refresh()
            }
        }
    }

    override fun getCantEat(name: String): Flow<CantEat?> {
        return cantEatDao.getItem(name).map {
            it.asDomainCantEat()
        }
    }

    override suspend fun insertCantEat(cantEat: CantEat) {
        cantEatDao.insert(cantEat.asDbCantEat())
    }

    override suspend fun deleteCantEat(cantEat: CantEat) {
        cantEatDao.delete(cantEat.asDbCantEat())
    }

    override suspend fun updateCantEat(cantEat: CantEat) {
        cantEatDao.update(cantEat.asDbCantEat())
    }

    private var workID = UUID(1, 2)
    private val workManager = WorkManager.getInstance(context)
    override var wifiWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfoByIdFlow(workID)

    override suspend fun refresh() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val requestBuilder = OneTimeWorkRequestBuilder<WifiNotificationWorker>()
        val request = requestBuilder.setConstraints(constraints).build()
        workManager.enqueue(request)
        workID = request.id
        wifiWorkInfo = workManager.getWorkInfoByIdFlow(request.id)

        try {
            cantEatApiService.getCantEatsAsFlow().asDomainObjects().collect { value ->
                for (cantEat in value) {
                    Log.i("TEST", "refresh: $value")
                    insertCantEat(cantEat)
                }
            }
        } catch (e: SocketTimeoutException) {
            // log something
        }
    }
}