package com.jamie1192.haveibeenpwned.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jamie1192.haveibeenpwned.api.ApiService
import com.jamie1192.haveibeenpwned.database.AppDatabase
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.di.App
import com.jamie1192.haveibeenpwned.di.SharedPrefsModule
import com.jamie1192.haveibeenpwned.utils.NoNetworkException
import com.jamie1192.haveibeenpwned.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import org.threeten.bp.LocalDateTime
import timber.log.Timber


/**
 * Created by jamie1192 on 16/12/18.
 */

class BreachedSitesRepository {

    private var snackbarMessage : SingleLiveEvent<String> = SingleLiveEvent()
    private var breachesLiveData : MutableLiveData<List<Breach>> = MutableLiveData()
    private var pagedMutableBreaches : LiveData<PagedList<Breach>> = MutableLiveData()

    private val apiService : ApiService = App().get()
    private val appDatabase : AppDatabase = App().get()
//    private val sharedPrefs : SharedPrefsModule = App().get()

    private val compDisposable = CompositeDisposable()

    fun getBreachedSites() : LiveData<List<Breach>> {
        val disposable : Disposable = apiService.getAllBreaches()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ breaches ->

//                breachesLiveData.postValue(breaches)
                saveBreaches(breaches)

            }, {
                if(it is NoNetworkException) {
                    snackbarMessage.postValue("No Network connection.")
                    Timber.w(it)
                    loadSavedBreaches()
                }
            })

        compDisposable.add(disposable)
        return breachesLiveData
    }


    private fun saveBreaches(list : List<Breach>) {
        appDatabase.breachDao().insertBreaches(list)
        Timber.w("saving to database")
//        sharedPrefs.getPrefs().edit().putString("breachUpdated", LocalDateTime.now().toString()).apply()
        snackbarMessage.postValue("Updated local database.")
    }

    fun paged(query: String) {

        pagedMutableBreaches = LivePagedListBuilder(appDatabase.breachDao().getBreachesByDateAsc(), 30).build()

        return when(query) {
            "breachDateAsc" -> pagedMutableBreaches = LivePagedListBuilder(appDatabase.breachDao().getBreachesByDateAsc(), 30).build()
            "breachDateDesc" -> pagedMutableBreaches = LivePagedListBuilder(appDatabase.breachDao().getBreachesByDateDesc(), 30).build()
            "nameAsc" -> pagedMutableBreaches = LivePagedListBuilder(appDatabase.breachDao().getBreachesByNameAsc(), 30).build()
            "nameDesc" -> pagedMutableBreaches = LivePagedListBuilder(appDatabase.breachDao().getBreachesByNameDesc(), 30).build()

            else -> pagedMutableBreaches = LivePagedListBuilder(appDatabase.breachDao().getBreachesByNameAsc(), 30).build()
        }
    }

    fun getQueriedPaged() : LiveData<PagedList<Breach>> = LivePagedListBuilder(appDatabase.breachDao().getBreachesByDateAsc(), 30).build()

    private fun loadSavedBreaches() {

        val disposable : Disposable = appDatabase.breachDao().getAllTableEntities()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ breaches ->

                if (breaches.isEmpty()) {
                    getBreachedSites()
                } else {
                    Timber.i("Loading from database")
                    breachesLiveData.postValue(breaches)
                }
            }, {
                Timber.w(it)
            })
        compDisposable.add(disposable)
    }

    fun getSiteByName(name : String) : LiveData<Breach> {
        return appDatabase.breachDao().getSiteByName(name)
    }

    fun getSnackbarMessage() : LiveData<String> {
        return snackbarMessage
    }

    fun dispose() {
        compDisposable.dispose()
    }
}