package com.jamie1192.haveibeenpwned.workers

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.jamie1192.haveibeenpwned.api.ApiService
import com.jamie1192.haveibeenpwned.database.AppDatabase
import com.jamie1192.haveibeenpwned.database.models.EmailBreach
import com.jamie1192.haveibeenpwned.di.App
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by jamie1192 on 15/1/19.
 */
class EmailWorker(appContext: Context, workerParams: WorkerParameters) : RxWorker(appContext, workerParams) {

    private val apiService : ApiService = App().get()
    private val database : AppDatabase = App().get()

//    override fun createWork(): Single<Result> {
//        return database.userDao().getAllAccountsSingle()
//            .doOnSuccess {
//                Observable.fromIterable(it)
//                    .subscribe({ roomResult ->
//                        apiService.getBreachedAccount(roomResult.email)
//                            .subscribe ({ breaches ->
//                                for (breach in breaches) {
//                                    val emailBreach = EmailBreach(0, breach.name, breach, roomResult.email)
//                                    database.emailBreachDao().insertEmailBreach(emailBreach)
//                                }
//
//                            }, { apiErr ->
//                                Timber.w(apiErr)
//                            })
//                    }, { err ->
//                        Timber.e(err)
//                    })
//
//            }
//            .map { Result.success() }
//    }

    override fun createWork(): Single<Result>? {

        return database.userDao().getAllAccountsSingle()
            .doOnSuccess {
                database.userDao().getAllAccountsObsv()
                    .flatMap {
                        Observable.fromIterable(it)
                    }
                    .map { userEmail ->
                        apiService.getBreachedAccount(userEmail.email)
                            .delay(1800, TimeUnit.MILLISECONDS)
                            .subscribe ({ breachList ->
                                val listToSave = mutableListOf<EmailBreach>()
                                for (breach in breachList) {
                                    Timber.i("${breachList.size} emails")
                                    val emailBreach = EmailBreach(0, breach.name, breach, userEmail.email)
                                    listToSave.add(emailBreach)
                                }
                                database.emailBreachDao().insertEmailBreachList(listToSave)
                            }, { throwable ->
                                Timber.e(throwable)
                            })
                    }

            }.map { Result.success() }
            .onErrorReturn { Result.failure() }
    }


//                Observable.fromIterable(it)
//                    .subscribe ({
//                        apiService.getBreachedAccount(it.email)
//                            .delay(1800, TimeUnit.MILLISECONDS)
//                            .subscribe {
//                                val listToSave = mutableListOf<EmailBreach>()
//                                for (breach in it) {
//                                    val emailBreach = EmailBreach(0, breach.name, breach, userEmail.email )
//                                    listToSave.add(emailBreach)
//                                }
//                                database.emailBreachDao().insertEmailBreachList(listToSave)
//                            }
//                    }, {
//
//                    })



        //working as disposable?
//        val disposable : Disposable = database.userDao().getAllAccountsObsv()
//            .flatMap {
//                Observable.fromIterable(it)
//            }
//            .map { userEmail ->
//                apiService.getBreachedAccount(userEmail.email)
//                    .delay(1800, TimeUnit.MILLISECONDS)
//                    .subscribe {
//                        val listToSave = mutableListOf<EmailBreach>()
//                        for (breach in it) {
//                            val emailBreach = EmailBreach(0, breach.name, breach, userEmail.email )
//                            listToSave.add(emailBreach)
//                        }
//                        database.emailBreachDao().insertEmailBreachList(listToSave)
//                    }
//            }
//            .subscribe {
//                Result.success()
//            }


//                Observable.fromIterable(it)
//                    .subscribe({ roomResult ->
//                        apiService.getBreachedAccount(roomResult.email)
//                            .subscribe ({ breaches ->
//                                for (breach in breaches) {
//                                    val emailBreach = EmailBreach(0, breach.name, breach, roomResult.email)
//                                    database.emailBreachDao().insertEmailBreach(emailBreach)
//                                }
//
//                            }, { apiErr ->
//                                Timber.w(apiErr)
//                            })
//                    }, { err ->
//                        Timber.e(err)
//                    })

//            }
//            .map { Result.success() }
    }



