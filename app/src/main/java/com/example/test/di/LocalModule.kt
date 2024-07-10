package com.example.test.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.paytef.cepsastandalone.data.local.CepsaDAO
import es.paytef.cepsastandalone.data.local.LocalDatabase
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
            DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(localDatabase: LocalDatabase): CepsaDAO {
        return localDatabase.dao()
    }

    @Provides
    @Singleton
    fun provideLocalDatabaseUseCase(@ApplicationContext context: Context): LocalDatabaseUseCase {
        return LocalDatabaseUseCase(provideDB(context), context = context)
    }
}