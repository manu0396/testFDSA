package com.example.test.domain.useCase

import com.example.test.data.local.LocalDatabase
import com.example.test.data.local.models.DestinationData
import com.example.test.data.models.Destination
import com.example.test.data.remote.HotelBediaXApi
import com.example.test.utils.WrapperResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityRetainedScoped
class GetLocalDestinationsUseCase @Inject constructor(
    private val localDatabaseUseCase: LocalDatabase,
) {

    suspend fun getResults(): WrapperResponse<List<DestinationData>> {
        val resp = try {
            localDatabaseUseCase.dao().getAll()
        }catch (e: Exception){
            return WrapperResponse.Error("Se ha producido un error: ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }
}