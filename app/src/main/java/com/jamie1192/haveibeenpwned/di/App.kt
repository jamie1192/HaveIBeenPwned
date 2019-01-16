package com.jamie1192.haveibeenpwned.di

import android.app.Application
import androidx.work.*
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jamie1192.haveibeenpwned.BuildConfig
import com.jamie1192.haveibeenpwned.api.ApiService
import com.jamie1192.haveibeenpwned.database.AppDatabase
import com.jamie1192.haveibeenpwned.workers.BreachWorker
import com.jamie1192.haveibeenpwned.workers.EmailWorker
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module
import timber.log.Timber
import timber.log.Timber.plant
import java.util.concurrent.TimeUnit

/**
 * Created by jamie1192 on 16/12/18.
 */

class App : Application() {

    val appModule = module {

        single { ApiService.create(get()) }
        single { AppDatabase.getDatabase(get()) }
        single { SharedPrefsModule.create(get()) }

    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
        AndroidThreeTen.init(this)
        startKoin(this, listOf(appModule))

        val networkConstraint = Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()

        val databaseWorker = PeriodicWorkRequestBuilder<BreachWorker>(2, TimeUnit.HOURS,
                                                            10, TimeUnit.MINUTES)
                                                .setConstraints(networkConstraint)
                                                .build()

        val emailBreachWorker = PeriodicWorkRequestBuilder<EmailWorker>(1, TimeUnit.MINUTES)
//                                                                    20, TimeUnit.MINUTES)
                                                .setConstraints(networkConstraint)
                                                .build()



//        val workerBuilder = worker.build()

        WorkManager.getInstance().enqueueUniquePeriodicWork("updateDatabase",
                                ExistingPeriodicWorkPolicy.KEEP,
                                databaseWorker)

//        WorkManager.getInstance().enqueueUniquePeriodicWork("checkForBreaches",
//                                ExistingPeriodicWorkPolicy.REPLACE,
//                                emailBreachWorker)

//        val workContinuation = WorkManager.getInstance()
//            .beginWith("databaseWorker", ExistingWorkPolicy.KEEP, databaseWorker)

    }
}