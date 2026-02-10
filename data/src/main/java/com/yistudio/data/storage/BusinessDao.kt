package com.yistudio.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yistudio.data.entity.BusinessEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(businesses: List<BusinessEntity>)

    @Query("SELECT * FROM businesses ORDER BY id ASC")
    fun getAll(): Flow<List<BusinessEntity>> // Flow of Entities!
}