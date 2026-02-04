package com.example.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.entity.BusinessEntity

@Database(entities = [BusinessEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // You must include an abstract getter for your DAO
    abstract fun businessDao(): BusinessDao

    // Companion object for Singleton access (optional, often handled by DI)
    // ...
}
