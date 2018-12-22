package com.jamie1192.haveibeenpwned.BreachedSites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jamie1192.haveibeenpwned.api.models.Site
import com.jamie1192.haveibeenpwned.repositories.BreachedSitesRepository

/**
 * Created by jamie1192 on 16/12/18.
 */
class BreachedSitesViewModel(application: Application) : AndroidViewModel(application) {

    private var repository : BreachedSitesRepository = BreachedSitesRepository(application)


    fun getBreachedSites(): LiveData<List<Site>> {
        return repository.getBreachedSites()
    }

}