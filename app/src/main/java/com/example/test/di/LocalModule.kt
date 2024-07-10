package com.example.test.di

import android.content.Context
import androidx.room.Room
import com.example.test.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.test.data.local.HotelDAO
import com.example.test.data.local.LocalDatabase
import com.example.test.domain.useCase.GetLocalDestinationsUseCase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {
    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocalDatabase::class.java,
            BuildConfig.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(localDatabase: LocalDatabase): HotelDAO {
        return localDatabase.dao()
    }

    @Provides
    @Singleton
    fun provideLocalDatabaseUseCase(@ApplicationContext context: Context): GetLocalDestinationsUseCase {
        return GetLocalDestinationsUseCase(provideDB(context))
    }
}