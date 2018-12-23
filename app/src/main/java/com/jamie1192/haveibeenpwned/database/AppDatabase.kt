package com.jamie1192.haveibeenpwned.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jamie1192.haveibeenpwned.database.dao.BreachDao
import com.jamie1192.haveibeenpwned.database.models.Breach

/**
 * Created by jamie1192 on 27/11/18.
 */
@Database(entities = [Breach::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun breachDao(): BreachDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "App_Database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}