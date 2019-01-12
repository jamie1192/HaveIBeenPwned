package com.jamie1192.haveibeenpwned.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jamie1192.haveibeenpwned.database.dao.BreachDao
import com.jamie1192.haveibeenpwned.database.dao.UserDao
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.database.models.UserEmail

/**
 * Created by jamie1192 on 27/11/18.
 */
@Database(entities = [Breach::class, UserEmail::class],
            version = 2,
            exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun breachDao(): BreachDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            val MIGRATION_1_2 = object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("CREATE TABLE IF NOT EXISTS `userEmail` (`email` TEXT NOT NULL, `breachCount` INTEGER, `notifyNewBreaches` INTEGER, PRIMARY KEY(`email`))")
                }
            }

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "App_Database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }



        }
    }



}