package be.hogent.jochensnextdinner.fake

import be.hogent.jochensnextdinner.data.CantEatRepository
import be.hogent.jochensnextdinner.model.CantEat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCantEatRepository : CantEatRepository {
    private val cantEats = mutableListOf<CantEat>()

    override fun getCantEats(): Flow<List<CantEat>> {
        return flow {
            emit(cantEats)
        }
    }

    override suspend fun insertCantEat(cantEat: CantEat) {
        // No-op for testing
    }

    override suspend fun saveCantEat(cantEat: CantEat): CantEat {
        cantEats.add(cantEat)
        return cantEat
    }

    override suspend fun deleteCantEat(cantEat: CantEat) {
        // No-op for testing
    }

    override suspend fun updateCantEat(cantEat: CantEat) {
        // No-op for testing
    }

    override suspend fun refresh() {
        // No-op for testing
    }
}