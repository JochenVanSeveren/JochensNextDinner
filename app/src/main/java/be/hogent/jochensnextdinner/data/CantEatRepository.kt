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

/**
 * Interface for the CantEatRepository.
 * It contains the methods for managing CantEat data.
 */
interface CantEatRepository {
    fun getCantEats(): Flow<List<CantEat>>
    suspend fun insertCantEat(cantEat: CantEat)
    suspend fun saveCantEat(cantEat: CantEat): CantEat
    suspend fun deleteCantEat(cantEat: CantEat)
    suspend fun updateCantEat(cantEat: CantEat)
    suspend fun refresh()
}

/**
 * Implementation of the CantEatRepository interface.
 * It uses a local database and a remote API for managing CantEat data.
 */
class CachingCantEatRepository(
    private val cantEatDao: CantEatDao,
    private val cantEatApiService: CantEatApiService,
) : CantEatRepository {

    /**
     * Fetches all CantEat items from the local database.
     * If the local database is empty, it refreshes the data from the remote API.
     */
    override fun getCantEats(): Flow<List<CantEat>> {
        return cantEatDao.getAllItems().map {
            it.asDomainCantEats()
        }.onEach { cantEats ->
            if (cantEats.isEmpty()) {
                refresh()
            }
        }
    }

    /**
     * Inserts a CantEat item into the local database.
     */
    override suspend fun insertCantEat(cantEat: CantEat) {
        cantEatDao.insert(cantEat.asDbCantEat())
    }

    /**
     * Saves a CantEat item to the remote API and the local database.
     * If the item already exists in the remote API, it updates the item.
     * Otherwise, it creates a new item.
     */
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

    /**
     * Deletes a CantEat item from the remote API and the local database.
     */
    override suspend fun deleteCantEat(cantEat: CantEat) {
        if (cantEat.serverId != null) {
            val apiCantEat = cantEat.asApiObject()
            cantEatApiService.deleteCantEatAsFlow(apiCantEat.id!!).first()
        }
        cantEatDao.delete(cantEat.asDbCantEat())
    }

    /**
     * Updates a CantEat item in the remote API and the local database.
     */
    override suspend fun updateCantEat(cantEat: CantEat) {
        val apiCantEat = cantEat.asApiObject()
        val response = cantEatApiService.putCantEatAsFlow(apiCantEat).first()
        cantEatDao.update(response.asDomainObject().asDbCantEat())
    }

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