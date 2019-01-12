package com.jamie1192.haveibeenpwned.repositories

import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jamie1192.haveibeenpwned.api.ApiService
import com.jamie1192.haveibeenpwned.database.AppDatabase
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.di.App
import com.jamie1192.haveibeenpwned.utils.NoNetworkException
import com.jamie1192.haveibeenpwned.utils.Response
import com.jamie1192.haveibeenpwned.utils.Response.Companion.error
import com.jamie1192.haveibeenpwned.utils.Response.Companion.loading
import com.jamie1192.haveibeenpwned.utils.Response.Companion.success
import com.jamie1192.haveibeenpwned.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import org.threeten.bp.Duration
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber


/**
 * Created by jamie1192 on 16/12/18.
 */

class BreachedSitesRepository {

    private val snackbarMessage : SingleLiveEvent<Response<String>> = SingleLiveEvent()

    private val apiService : ApiService = App().get()
    private val appDatabase : AppDatabase = App().get()
    private val sharedPrefs : SharedPreferences = App().get()

    private val compDisposable = CompositeDisposable()

    fun checkLastUpdated() {

        sharedPrefs.getString("lastUpdated", null)?.let {
            if (Duration.between(ZonedDateTime.now(ZoneOffset.UTC).toInstant(),
                    ZonedDateTime.parse(it)).toHours() > 24 ) {
                updateDatabase()
            }
            else {
                val dateTime = ZonedDateTime.parse(it).toLocalDateTime()
                                    .format(DateTimeFormatter
                                    .ofPattern("dd/MM/yyyy ',' hh:mm a"))
                Timber.i(dateTime)
                snackbarMessage.postValue(success("Last updated at $dateTime."))
            }

        } ?: run {
            updateDatabase()
            snackbarMessage.postValue(loading("Updating database..."))
        }

    }

    private fun updateDatabase() {

        snackbarMessage.postValue(loading("Updating database..."))


        val disposable : Disposable = apiService.getAllBreaches()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ breaches ->
                insertUpdateBreaches(breaches)
            }, {
                if(it is NoNetworkException) {
                    snackbarMessage.postValue(error("No Network connection."))
                    Timber.w(it)
                }
            })

        compDisposable.add(disposable)

    }

    fun getSnackbarMessage() = snackbarMessage

    private fun insertUpdateBreaches(list : List<Breach>) {

        sharedPrefs.edit().putString("lastUpdated", ZonedDateTime.now(ZoneOffset.UTC).toString()).apply()
        appDatabase.breachDao().insertBreaches(list)

        System.out.println("Saved breaches to database ")

        snackbarMessage.postValue(success("Updated local database."))
    }


    private val queryMutableData: MutableLiveData<String> = MutableLiveData()

    fun getQuerySwitchMap(): LiveData<PagedList<Breach>> = Transformations.switchMap(queryMutableData) { query ->
        when(query) {
            "nameAsc" -> LivePagedListBuilder(appDatabase.breachDao().getBreachesByNameAsc(), 20).build()
            "nameDesc" -> LivePagedListBuilder(appDatabase.breachDao().getBreachesByNameDesc(), 20).build()
            "breachAsc" -> LivePagedListBuilder(appDatabase.breachDao().getBreachesByDateAsc(), 20).build()
            "breachDesc" -> LivePagedListBuilder(appDatabase.breachDao().getBreachesByDateDesc(), 20).build()
            "pwnAsc" -> LivePagedListBuilder(appDatabase.breachDao().getBreachesByPwnAsc(), 20).build()
            "pwnDesc" -> LivePagedListBuilder(appDatabase.breachDao().getBreachesByPwnDesc(), 20).build()
            else -> {
                LivePagedListBuilder(appDatabase.breachDao().getBreachesByNameAsc(), 20).build()
            }
        }
    }

    fun setQuery(query: String) {
        queryMutableData.postValue(query)
    }


    fun getSiteByName(name : String) : LiveData<Breach> {
        return appDatabase.breachDao().getSiteByName(name)
    }

    fun dispose() {
        compDisposable.dispose()
    }
}

