package com.jamie1192.haveibeenpwned

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamie1192.haveibeenpwned.api.ApiService
import com.jamie1192.haveibeenpwned.database.adapters.BreachedRecyclerAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = BreachedRecyclerAdapter()

    private val apiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecycler()

        getBreachedSites()
    }




    private fun getBreachedSites() {
        apiService.getAllBreaches()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { breaches ->
                run {
                    System.out.println(breaches)
                    adapter.setList(breaches)
                    swipeRefresh.isRefreshing = false
                }
            }
    }

    private fun setupRecycler() {
        swipeRefresh.isRefreshing = true
        breached_sites_recycler.layoutManager = LinearLayoutManager(this)
        breached_sites_recycler.adapter = adapter
    }
}
