package com.example.ideaplatform.di

import android.content.Context
import androidx.room.Room
import com.example.ideaplatform.data.local.room.AppDatabase
import com.example.ideaplatform.data.local.room.item.dao.ItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val databaseName = "app_database"
private const val initialDataBaseDataPath = "data.db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            AppDatabase::class.java,
            databaseName
        )
            .createFromAsset(initialDataBaseDataPath)
            .build()
    }
    @Provides
    @Singleton
    fun provideItemDao(db: AppDatabase): ItemDao {
        return db.itemDao()
    }


}