package com.jamie1192.haveibeenpwned.workers

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.jamie1192.haveibeenpwned.api.ApiService
import com.jamie1192.haveibeenpwned.database.AppDatabase
import com.jamie1192.haveibeenpwned.di.App
import io.reactivex.Observable
import io.reactivex.Single
import org.koin.android.ext.android.get
import timber.log.Timber

/**
 * Created by jamie1192 on 1/1/19.
 */

class BreachWorker(context: Context, workerParams: WorkerParameters) : RxWorker(context, workerParams) {

    private val apiService : ApiService = App().get()

    override fun createWork(): Single<Result> {
        return apiService.getAllBreachesWorker()
            .doOnSuccess {
                val database = AppDatabase.getDatabase(applicationContext)
                database.breachDao().insertBreaches(it)
                Timber.i("${it.size} items inserted")
            }
            .map {  Result.success() }
            .onErrorReturn { Result.failure() }

    }

    //https://www.androidhive.info/RxJava/rxjava-operators-introduction/
    //get list of emails stored and check breaches for each with Observables
    val list : List<String> = listOf("name", "123", "asdasd")
    val obs = Observable.fromArray(list)
                        .subscribe ({

                        }, {

                        })

}