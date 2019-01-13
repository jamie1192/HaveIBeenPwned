package com.jamie1192.haveibeenpwned.repositories

import androidx.lifecycle.LiveData
import com.jamie1192.haveibeenpwned.database.AppDatabase
import com.jamie1192.haveibeenpwned.database.models.UserEmail
import com.jamie1192.haveibeenpwned.di.App
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import timber.log.Timber

/**
 * Created by jamie1192 on 13/1/19.
 */
class UserEmailsRepository {

    private val appDatabase : AppDatabase = App().get()
    private val compDisposable = CompositeDisposable()

    fun getUserEmails() : LiveData<List<UserEmail>> {
        return appDatabase.userDao().getAllAccounts()
    }

    fun insertEmail(email : String) {
        val obj = UserEmail(email, 0, true)

        val disposable : Disposable = Completable.fromAction { appDatabase.userDao().insertEmail(obj) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                Timber.w("$email stored in database.")
            }, {
                Timber.w(it)
            })

        compDisposable.add(disposable)
    }

    fun deleteEmail(email : String) {

        val disposable : Disposable = Completable.fromAction {
            appDatabase.userDao().deleteEmail(email)
        }.subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                Timber.i("$email was deleted.")
            }, {
                Timber.e(it)
            })
        compDisposable.add(disposable)
    }

    fun deleteAllEmails() = appDatabase.userDao().deleteAllEmails()

    fun dispose() = compDisposable.dispose()
}