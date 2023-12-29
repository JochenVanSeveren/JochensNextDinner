package be.hogent.jochensnextdinner.fake

import be.hogent.jochensnextdinner.network.CantEatApiService
import be.hogent.jochensnextdinner.network.ApiCantEat

class FakeCantEatApiService : CantEatApiService {
    override suspend fun getCantEats(): List<ApiCantEat> {
        return FakeCantEatDataSource.cantEats
    }

    override suspend fun postCantEat(cantEat: ApiCantEat): ApiCantEat {
        return cantEat
    }

    override suspend fun putCantEat(id: String, cantEat: ApiCantEat): ApiCantEat {
        return cantEat
    }

    override suspend fun deleteCantEat(id: String): ApiCantEat {
        return FakeCantEatDataSource.cantEats.first { it.id == id }
    }
}