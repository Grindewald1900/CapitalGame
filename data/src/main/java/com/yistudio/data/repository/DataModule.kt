package com.yistudio.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.yistudio.data.entity.TaskPreferences
import com.yistudio.data.local.TaskSerializer
import com.yistudio.domain.repository.BusinessRepository
import com.yistudio.domain.repository.EconomyRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.taskDataStore by dataStore(
    fileName = "task_prefs.json",
    serializer = TaskSerializer
)

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindBusinessRepository(
        impl: BusinessRepositoryImpl
    ): BusinessRepository

    @Binds
    @Singleton
    abstract fun bindEconomyRepository(
        impl: EconomyRepositoryImpl
    ): EconomyRepository

    companion object {
        @Provides
        @Singleton
        fun provideTaskDataStore(
            @ApplicationContext context: Context
        ): DataStore<TaskPreferences> {
            return context.taskDataStore
        }
    }
}