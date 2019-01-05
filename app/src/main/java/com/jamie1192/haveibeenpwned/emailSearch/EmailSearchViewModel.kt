package com.jamie1192.haveibeenpwned.emailSearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.repositories.EmailSearchRepository

class EmailSearchViewModel : ViewModel() {

    private val repository : EmailSearchRepository = EmailSearchRepository()

    fun searchEmail( email : String) = repository.searchEmail(email)

    fun observeEmail() : LiveData<List<Breach>> = repository.getEmailLiveData()

    fun getSnackbarMessage() : LiveData<Any> = repository.getSnackbarMessage()

    override fun onCleared() {
        super.onCleared()

        repository.dispose()
    }
}
