package com.jamie1192.haveibeenpwned.di

import android.app.Application
import com.jamie1192.haveibeenpwned.BuildConfig
import com.jamie1192.haveibeenpwned.api.ApiService
import com.jamie1192.haveibeenpwned.database.AppDatabase
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module
import timber.log.Timber
import timber.log.Timber.plant

/**
 * Created by jamie1192 on 16/12/18.
 */
class App : Application() {

    val appModule = module {

        single { ApiService.create(get()) }
        single { AppDatabase.getDatabase(get()) }
        single { SharedPrefsModule.Factory.create(get()) }

    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
        startKoin(this, listOf(appModule))
    }
}