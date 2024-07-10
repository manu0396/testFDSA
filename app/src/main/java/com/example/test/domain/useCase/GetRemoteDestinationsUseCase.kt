package com.example.test.domain.useCase

import com.example.test.data.models.Destination
import com.example.test.data.remote.HotelBediaXApi
import com.example.test.utils.WrapperResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityRetainedScoped
class GetRemoteDestinationsUseCase @Inject constructor(
    private val hotelBediaXApi: HotelBediaXApi
) {

   suspend fun getResults():WrapperResponse<List<Destination>>{
        val resp = try {
            hotelBediaXApi.getAll()
        }catch (e:Exception){
            return WrapperResponse.Error("Se ha producido un error: ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }
}