package be.hogent.jochensnextdinner.data

import Like
import android.util.Log
import be.hogent.jochensnextdinner.data.database.LikeDao
import be.hogent.jochensnextdinner.data.database.asDomainLikes
import be.hogent.jochensnextdinner.network.LikeApiService
import be.hogent.jochensnextdinner.network.asDomainObject
import be.hogent.jochensnextdinner.network.asDomainObjects
import be.hogent.jochensnextdinner.network.deleteLikeAsFlow
import be.hogent.jochensnextdinner.network.getLikesAsFlow
import be.hogent.jochensnextdinner.network.postLikeAsFlow
import be.hogent.jochensnextdinner.network.putLikeAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.net.SocketTimeoutException

interface LikeRepository {
    fun getLikes(): Flow<List<Like>>
    suspend fun insertLike(like: Like)
    suspend fun saveLike(like: Like): Like
    suspend fun deleteLike(like: Like)
    suspend fun updateLike(like: Like)
    suspend fun refresh()
}

class CachingLikeRepository(
    private val likeDao: LikeDao,
    private val likeApiService: LikeApiService
) : LikeRepository {

    override fun getLikes(): Flow<List<Like>> {
        return likeDao.getAllItems().map {
            it.asDomainLikes()
        }.onEach { likes ->
            if (likes.isEmpty()) {
                refresh()
            }
        }
    }

    override suspend fun insertLike(like: Like) {
        likeDao.insert(like.asDbLike())
    }

    override suspend fun saveLike(like: Like): Like {
        val apiLike = like.asApiObject()
        val response = if (like.serverId != null)
            likeApiService.putLikeAsFlow(apiLike).first()
        else
            likeApiService.postLikeAsFlow(apiLike).first()
        val createdLike = response.asDomainObject()
        likeDao.insert(createdLike.asDbLike())
        return createdLike
    }

    override suspend fun deleteLike(like: Like) {
        val apiLike = like.asApiObject()
        apiLike.id?.let { likeApiService.deleteLikeAsFlow(it).first() }
        likeDao.delete(like.asDbLike())
    }

    override suspend fun updateLike(like: Like) {
        val apiLike = like.asApiObject()
        val response = likeApiService.putLikeAsFlow(apiLike).first()
        likeDao.update(response.asDomainObject().asDbLike())
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
            val dbLikes = likeDao.getAllItems().first().asDomainLikes()

            likeApiService.getLikesAsFlow().asDomainObjects().collect { apiLikes ->
                val itemsToDelete = dbLikes.filter { dbLike ->
                    apiLikes.none { apiLike ->
                        apiLike.serverId == dbLike.serverId
                    }
                }

                for (item in itemsToDelete) {
                    likeDao.delete(item.asDbLike())
                }

                for (like in apiLikes) {
                    likeDao.insert(like.asDbLike())
                }
            }
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeoutException", e.message ?: "No message")
        }
    }
}