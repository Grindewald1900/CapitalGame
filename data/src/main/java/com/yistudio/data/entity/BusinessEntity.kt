package com.yistudio.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "businesses") // Use a clearer table name
data class BusinessEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "level")
    val level: Int,

    @ColumnInfo(name = "base_income_per_sec")
    val baseIncomePerSec: Double,

    @ColumnInfo(name = "automated")
    val automated: Boolean
)