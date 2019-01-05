package com.jamie1192.haveibeenpwned.breachedSites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.repositories.BreachedSitesRepository

/**
 * Created by jamie1192 on 16/12/18.
 */
class BreachedSitesViewModel : ViewModel() {

    private var repository : BreachedSitesRepository = BreachedSitesRepository()


    fun getBreachedSites(): LiveData<List<Breach>> = repository.getBreachedSites()
    fun setBreachedPagedList(query: String) = repository.paged(query)
    fun getBreachedPagedList() : LiveData<PagedList<Breach>> = repository.getQueriedPaged()

    fun getSiteDetails(name : String) : LiveData<Breach> = repository.getSiteByName(name)

    fun getSnackbarMessage() : LiveData<String> = repository.getSnackbarMessage()


    override fun onCleared() {
        super.onCleared()
        repository.dispose()
    }
}