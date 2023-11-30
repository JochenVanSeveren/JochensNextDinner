package be.hogent.jochensnextdinner.data

import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.network.CantEatService

interface JochensNextDinnerRepository {
    suspend fun getCantEats(): List<CantEat>

}

class NetworkJochensNextDinnerRepository(private val cantEatService: CantEatService) :
    JochensNextDinnerRepository {
        override suspend fun getCantEats(): List<CantEat> {
        return cantEatService.getAllCantEats()
    }
}