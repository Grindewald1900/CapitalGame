package com.example.data.repository

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.storage.AppDatabase
import com.example.data.storage.BusinessDao
import com.example.data.storage.BusinessDefinition
import com.example.domain.repository.BusinessRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        // We use a Provider to get the DAO late, after the DB is built
        provider: Provider<BusinessDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "game_database"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Seed the database on a background thread
                CoroutineScope(Dispatchers.IO).launch {
                    val initialData = BusinessDefinition
                    provider.get().insertAll(initialData)
                }
            }
        }).build()
    }

    @Provides
    @Singleton
    fun provideBusinessDao(database: AppDatabase): BusinessDao {
        // Provide the DAO using the database instance
        return database.businessDao()
    }
}