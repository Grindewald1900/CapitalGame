package com.yistudio.data.repository

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yistudio.data.storage.AppDatabase
import com.yistudio.data.storage.BusinessDao
import com.yistudio.data.storage.BusinessDefinition
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
        provider: Provider<BusinessDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "game_database"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val initialData = BusinessDefinition
                        provider.get().insertAll(initialData)
                        // Log.d("DB", "Seeding successful")
                    } catch (e: Exception) {
                        // Log.e("DB", "Seeding failed", e)
                    }
                }
            }
        }).build()
    }

    @Provides
    @Singleton
    fun provideBusinessDao(database: AppDatabase): BusinessDao {
        return database.businessDao()
    }
}