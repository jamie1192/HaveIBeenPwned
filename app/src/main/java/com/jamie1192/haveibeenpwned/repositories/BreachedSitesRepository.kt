package com.jamie1192.haveibeenpwned.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jamie1192.haveibeenpwned.api.ApiService
import com.jamie1192.haveibeenpwned.database.AppDatabase
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.di.App
import com.jamie1192.haveibeenpwned.utils.NoNetworkException
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import timber.log.Timber


/**
 * Created by jamie1192 on 16/12/18.
 */

class BreachedSitesRepository {

    private var breachesLiveData : MutableLiveData<List<Breach>> = MutableLiveData()
    private val apiService : ApiService = App().get()
    private val appDatabase : AppDatabase = App().get()

    fun getBreachedSites() : LiveData<List<Breach>> {
        var disposable : Disposable = apiService.getAllBreaches()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ breaches ->

                breachesLiveData.postValue(breaches)
                saveBreaches(breaches)

            }, {
                if(it is NoNetworkException) {
                    Timber.w(it)
                    loadSavedBreaches()
                }
            })

        return breachesLiveData
    }


    private fun saveBreaches(list : List<Breach>) {
        appDatabase.tableEntityDao().insertBreaches(list)
    }

    private fun loadSavedBreaches() {

        var disposable : Disposable = appDatabase.tableEntityDao().getAllTableEntities()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ breaches ->
                breachesLiveData.postValue(breaches)
            }, {
                Timber.w(it)
            })
    }
}