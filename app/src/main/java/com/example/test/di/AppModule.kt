package com.example.test.di

import com.example.test.data.ApiClient
import com.example.test.data.ApiRandomUser
import com.example.test.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    private val httpClient = OkHttpClient.Builder()

    @Singleton
    @Provides
    fun provideAPi():ApiRandomUser{
        httpClient.addInterceptor(getInterceptor())
        return ApiClient(
            baseUrl = Constants.BASE_URL,
            converterFactory = GsonConverterFactory.create(),
            okHttpClientBuilder = httpClient
        ).createService(ApiRandomUser::class.java)
    }

    private fun getInterceptor(): Interceptor {
        try {
            return Interceptor {
                val request = it.request()
                it.proceed(
                    request.newBuilder()
                        .header("Accept", "application/json")
                        .build()
                )
            }
        } catch (exception: Exception) {
            throw Exception(exception.message)
        }
    }
}