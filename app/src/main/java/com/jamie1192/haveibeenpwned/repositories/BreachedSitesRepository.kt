package com.jamie1192.haveibeenpwned.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jamie1192.haveibeenpwned.api.ApiService
import com.jamie1192.haveibeenpwned.api.models.Site
import com.jamie1192.haveibeenpwned.database.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by jamie1192 on 16/12/18.
 */
class BreachedSitesRepository(context : Context) {

    private var breachesLiveData : MutableLiveData<List<Site>> = MutableLiveData()

    private val apiService by lazy {
        ApiService.create()
    }

    private val appDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    fun getBreachedSites() : LiveData<List<Site>> {
        apiService.getAllBreaches()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { breaches ->
                run {
                    breachesLiveData.postValue(breaches)
                }
            }
        return breachesLiveData
    }

}