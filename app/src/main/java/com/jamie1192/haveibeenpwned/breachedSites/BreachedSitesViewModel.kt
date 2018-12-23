package com.jamie1192.haveibeenpwned.breachedSites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.repositories.BreachedSitesRepository

/**
 * Created by jamie1192 on 16/12/18.
 */
class BreachedSitesViewModel : ViewModel() {

    private var repository : BreachedSitesRepository = BreachedSitesRepository()


    fun getBreachedSites(): LiveData<List<Breach>> = repository.getBreachedSites()

    fun getSnackbarMessage() : LiveData<String> = repository.getSnackbarMessage()


    override fun onCleared() {
        super.onCleared()
        repository.dispose()
    }
}