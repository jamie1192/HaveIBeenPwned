package com.jamie1192.haveibeenpwned.breachedSites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.repositories.BreachedSitesRepository
import com.jamie1192.haveibeenpwned.utils.Response

/**
 * Created by jamie1192 on 16/12/18.
 */
class BreachedSitesViewModel : ViewModel() {

    private var repository : BreachedSitesRepository = BreachedSitesRepository()

    fun getMediator() : LiveData<PagedList<Breach>> = repository.getQuerySwitchMap()

    fun setQuery(query: String) = repository.setQuery(query)

    fun getSiteDetails(name : String) : LiveData<Breach> = repository.getSiteByName(name)

    fun getSnackbarMessage() : LiveData<Response<String>> = repository.getSnackbarMessage()

    fun checkLastUpdated() = repository.checkLastUpdated()

    override fun onCleared() {
        super.onCleared()
        repository.dispose()
    }
}