package com.example.data.repository

import com.example.domain.main.BusinessRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindBusinessRepository(
        impl: BusinessRepositoryImpl
    ): BusinessRepository
}