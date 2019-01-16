package com.jamie1192.haveibeenpwned.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jamie1192.haveibeenpwned.database.dao.BreachDao
import com.jamie1192.haveibeenpwned.database.dao.EmailBreachDao
import com.jamie1192.haveibeenpwned.database.dao.UserDao
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.database.models.EmailBreach
import com.jamie1192.haveibeenpwned.database.models.UserEmail

/**
 * Created by jamie1192 on 27/11/18.
 */
@Database(entities = [Breach::class, UserEmail::class, EmailBreach::class],
            version = 3,
            exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun breachDao(): BreachDao
    abstract fun userDao(): UserDao
    abstract fun emailBreachDao() : EmailBreachDao

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

            val MIGRATION_2_3 = object : Migration(2, 3) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("CREATE TABLE IF NOT EXISTS `emailBreach` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `breachData` TEXT NOT NULL, `emailId` TEXT NOT NULL, FOREIGN KEY(`emailId`) REFERENCES `userEmail`(`email`) ON UPDATE NO ACTION ON DELETE CASCADE )")
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                return instance
            }



        }
    }



}