package com.nabeel.themoviedbtest.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nabeel.themoviedbtest.data.database.dao.UpcomingDao
import com.nabeel.themoviedbtest.data.database.entity.Upcoming
import com.nabeel.themoviedbtest.data.database.typeconverter.ResultTypeConverter

@Database(
    entities = [Upcoming::class],
    version = 1, exportSchema = false
)

@TypeConverters(ResultTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun upcomingDao(): UpcomingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, AppDatabase::class.java,
                        "AppDatabase_Main"
                    ).build()
                }
            }
            return INSTANCE as AppDatabase
        }
    }
}