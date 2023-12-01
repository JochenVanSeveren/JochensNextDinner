package be.hogent.jochensnextdinner.data

import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.network.CantEatService

interface JochensNextDinnerRepository {
    suspend fun getCantEats(): List<CantEat>
    suspend fun addCantEat(cantEat: CantEat): CantEat
    suspend fun updateCantEat(cantEat: CantEat): CantEat
    suspend fun deleteCantEat(cantEat: CantEat): CantEat


}

class NetworkJochensNextDinnerRepository(private val cantEatService: CantEatService) :
    JochensNextDinnerRepository {
    override suspend fun getCantEats(): List<CantEat> {
        return cantEatService.getAllCantEats()
    }

    override suspend fun addCantEat(cantEat: CantEat): CantEat {
        return cantEatService.createCantEat(cantEat)
    }

    override suspend fun updateCantEat(cantEat: CantEat): CantEat {
        return cantEatService.updateCantEat(cantEat.id, cantEat)
    }

    override suspend fun deleteCantEat(cantEat: CantEat): CantEat {
        return cantEatService.deleteCantEat(cantEat.id)
    }
}