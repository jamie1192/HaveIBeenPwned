package com.jamie1192.haveibeenpwned.repositories

import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.work.ListenableWorker
import com.jamie1192.haveibeenpwned.api.ApiService
import com.jamie1192.haveibeenpwned.database.AppDatabase
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.database.models.EmailBreach
import com.jamie1192.haveibeenpwned.di.App
import com.jamie1192.haveibeenpwned.utils.NoNetworkException
import com.jamie1192.haveibeenpwned.utils.Response
import com.jamie1192.haveibeenpwned.utils.Response.Companion.error
import com.jamie1192.haveibeenpwned.utils.Response.Companion.loading
import com.jamie1192.haveibeenpwned.utils.Response.Companion.success
import com.jamie1192.haveibeenpwned.utils.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit


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
            val hoursBetween = ChronoUnit.HOURS.between(Instant.parse(it), Instant.now()).apply {
                Timber.i("Database updated $this hours ago")
            }

            if (hoursBetween > 24 ) {
                updateDatabase()
            }
            else {
            Instant.parse(it).atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy ',' hh:mm a", Locale.ENGLISH)).apply {
                    Timber.i(this)
                    snackbarMessage.postValue(success("Last updated at $this."))
                }
            }

        } ?: run {
            updateDatabase()
            snackbarMessage.postValue(loading("Updating database..."))
        }

    }

    fun testWorker() {

        val disposable : Disposable = appDatabase.userDao().getAllAccountsObsv()
            .flatMap {
                Observable.fromIterable(it)
            }
            .map { userEmail ->
                apiService.getBreachedAccount(userEmail.email)
                    .delay(1800, TimeUnit.MILLISECONDS)
                    .subscribe {
                        val listToSave = mutableListOf<EmailBreach>()
                        for (breach in it) {
                            val emailBreach = EmailBreach(0, breach.name, breach, userEmail.email )
                            listToSave.add(emailBreach)
                        }
                        appDatabase.emailBreachDao().insertEmailBreachList(listToSave)
                    }
            }
            .subscribe {
                Timber.i("Worker successful")
            }
        compDisposable.add(disposable)
    }

    private fun updateDatabase() {

        snackbarMessage.postValue(loading("Updating database..."))

        val disposable : Disposable = apiService.getAllBreaches()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ breaches ->
                Timber.i("Breach list size of ${breaches.size}")
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

        sharedPrefs.edit().putString("lastUpdated", Instant.now().toString()).apply()
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
            "addedAsc" -> LivePagedListBuilder(appDatabase.breachDao().getBreachesByAddedAsc(), 20).build()
            "addedDesc" -> LivePagedListBuilder(appDatabase.breachDao().getBreachesByAddedDesc(), 20).build()
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

