package be.hogent.jochensnextdinner.fake

import be.hogent.jochensnextdinner.data.CantEatRepository
import be.hogent.jochensnextdinner.model.CantEat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCantEatRepository : CantEatRepository {
    private val cantEats = mutableListOf(
        CantEat(name = "Test Item 1"),
        CantEat(name = "Test Item 2")
    )

    override fun getCantEats(): Flow<List<CantEat>> {
        return flow {
            emit(cantEats)
        }
    }

    override suspend fun insertCantEat(cantEat: CantEat) {
        TODO("Not yet implemented")
    }

    override suspend fun saveCantEat(cantEat: CantEat): CantEat {
        cantEats.add(cantEat)
        return cantEat
    }

    override suspend fun deleteCantEat(cantEat: CantEat) {
        cantEats.remove(cantEat)
    }

    override suspend fun updateCantEat(cantEat: CantEat) {
        TODO("Not yet implemented")
    }

    override suspend fun refresh() {
        // No-op for testing
    }
}