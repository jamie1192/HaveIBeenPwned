package com.jamie1192.haveibeenpwned

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamie1192.haveibeenpwned.breachedSites.BreachedSitesViewModel
import com.jamie1192.haveibeenpwned.database.adapters.BreachedRecyclerAdapter
import com.jamie1192.haveibeenpwned.database.models.Breach
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = BreachedRecyclerAdapter()
    private lateinit var viewModel : BreachedSitesViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(BreachedSitesViewModel::class.java)

        setupRecycler()

        getBreachedSites()

    }


    private fun getSavedBreaches() {
//        var list = appDatabase.tableEntityDao().getAllTableEntities().value
//        adapter.setList(list)

    }


    private fun getBreachedSites() {

        viewModel.getBreachedSites().observe(this, Observer {
            adapter.setList(it)
        })
        swipeRefresh.isRefreshing = false
    }

    private fun setupRecycler() {
        swipeRefresh.isRefreshing = true
        breached_sites_recycler.layoutManager = LinearLayoutManager(this)
        breached_sites_recycler.adapter = adapter
    }

    private fun saveBreaches(breaches : List<Site>) {

        var breachList: MutableList<Breach> = mutableListOf()
//        for (obj in breaches) {
//            var br = obj.name?.let { Breach(it, obj) }
//
////            br?.key = obj.name!!
////            br?.data = obj
//////            if (br != null) {
//            breachList.add(br!!)
////            }
//        }

//        appDatabase.tableEntityDao().insertBreaches(breachList)
    }
}
