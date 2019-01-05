package com.jamie1192.haveibeenpwned.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jamie1192.haveibeenpwned.api.ApiService
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.di.App
import com.jamie1192.haveibeenpwned.utils.NoNetworkException
import com.jamie1192.haveibeenpwned.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import retrofit2.HttpException
import timber.log.Timber

/**
 * Created by jamie1192 on 5/1/19.
 */
class EmailSearchRepository {

    private val apiService : ApiService = App().get()
    private var snackbarMessage : SingleLiveEvent<Any> = SingleLiveEvent()
    private var breachesLiveData : MutableLiveData<List<Breach>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    fun searchEmail(email : String) {

        val disposable : Disposable = apiService
            .getBreachedAccount(email)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ breaches ->
                breachesLiveData.postValue(breaches)
            }, {
                breachesLiveData.postValue(emptyList())
                when(it) {
                    is NoNetworkException -> snackbarMessage.postValue("No Network connection.")
                    is HttpException -> snackbarMessage.postValue("Good news - no pwnage found!")
                }
                Timber.w(it)
            })
        compositeDisposable.add(disposable)
    }

    fun getEmailLiveData() : LiveData<List<Breach>> = breachesLiveData

    fun getSnackbarMessage() : SingleLiveEvent<Any> = snackbarMessage

    fun dispose() = compositeDisposable.dispose()
}