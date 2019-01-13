package com.jamie1192.haveibeenpwned.storedEmails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel;
import com.jamie1192.haveibeenpwned.database.models.UserEmail
import com.jamie1192.haveibeenpwned.repositories.UserEmailsRepository

class UserEmailsViewModel : ViewModel() {

    private val repository : UserEmailsRepository = UserEmailsRepository()

    fun insertEmail(email : String) = repository.insertEmail(email)

    fun deleteEmail(email : String) = repository.deleteEmail(email)

    fun getAllEmails() : LiveData<List<UserEmail>> = repository.getUserEmails()

    override fun onCleared() {
        super.onCleared()
        repository.dispose()
    }
}
