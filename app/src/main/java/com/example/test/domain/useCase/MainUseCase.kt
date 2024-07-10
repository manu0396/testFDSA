package com.example.test.domain.useCase

import android.content.Context
import com.example.test.data.ApiRandomUser
import com.example.test.utils.WrapperResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityRetainedScoped
class MainUseCase @Inject constructor(
    private val apiRandomUser: ApiRandomUser
) {

   suspend fun getResults():WrapperResponse<UserModel>{
        val resp = try {
            apiRandomUser.getResults()
        }catch (e:Exception){
            return WrapperResponse.Error("Se ha producido un error: ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }
}