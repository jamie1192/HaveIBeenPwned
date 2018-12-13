package com.jamie1192.haveibeenpwned

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamie1192.haveibeenpwned.api.ApiService
import com.jamie1192.haveibeenpwned.api.models.Site
import com.jamie1192.haveibeenpwned.database.AppDatabase
import com.jamie1192.haveibeenpwned.database.adapters.BreachedRecyclerAdapter
import com.jamie1192.haveibeenpwned.database.models.Breach
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = BreachedRecyclerAdapter()

    private val apiService by lazy {
        ApiService.create()
    }

    private val appDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecycler()

        getBreachedSites()
    }


    private fun getSavedBreaches() {
        var list = appDatabase.tableEntityDao().getAllTableEntities().value
//        adapter.setList(list)

    }


    private fun getBreachedSites() {
        apiService.getAllBreaches()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe { breaches ->
                run {
                    System.out.println(breaches)
                    saveBreaches(breaches)


//                    adapter.setList(breaches)
                    swipeRefresh.isRefreshing = false
                }
            }
    }

    private fun setupRecycler() {
        swipeRefresh.isRefreshing = true
        breached_sites_recycler.layoutManager = LinearLayoutManager(this)
        breached_sites_recycler.adapter = adapter
    }

    private fun saveBreaches(breaches : List<Site>) {

        var breachList: MutableList<Breach> = mutableListOf()
        for (obj in breaches) {
            var br = obj.name?.let { Breach(it, obj) }

//            br?.key = obj.name!!
//            br?.data = obj
////            if (br != null) {
            breachList.add(br!!)
//            }
        }

        appDatabase.tableEntityDao().insertBreaches(breachList)
    }
}
