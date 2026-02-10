package com.yistudio.data.repository

import com.yistudio.data.mapper.toDomainModel
import com.yistudio.data.mapper.toEntity
import com.yistudio.data.storage.BusinessDao
import com.yistudio.domain.entity.BusinessModel
import com.yistudio.domain.repository.BusinessRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor(private val businessDao: BusinessDao) :
    BusinessRepository {
    override fun getAllBusinesses(): Flow<List<BusinessModel>> {
        return businessDao.getAll().map { entities ->
            entities.map { it.toDomainModel() }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun saveAllBusinesses(businesses: List<BusinessModel>) {
        // First, convert the Domain Models to Entities
        val entities = businesses.map { it.toEntity() }

        // Then, save the entities using the DAO
        businessDao.insertAll(entities)
    }
}