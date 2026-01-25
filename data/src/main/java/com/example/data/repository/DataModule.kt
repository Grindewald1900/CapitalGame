package com.example.data.repository

import com.example.domain.repository.BusinessRepository
import com.example.domain.repository.EconomyRepository
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

    @Binds
    @Singleton
    abstract fun bindEconomyRepository(
        impl: EconomyRepositoryImpl
    ): EconomyRepository

}