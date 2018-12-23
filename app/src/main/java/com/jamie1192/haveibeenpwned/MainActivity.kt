package com.jamie1192.haveibeenpwned

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import com.google.android.material.snackbar.Snackbar
import com.jamie1192.haveibeenpwned.breachedSites.BreachedSitesViewModel
import com.jamie1192.haveibeenpwned.database.adapters.BreachedRecyclerAdapter
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = BreachedRecyclerAdapter()
    private lateinit var viewModel : BreachedSitesViewModel
    private lateinit var drawer : Drawer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)

        viewModel = ViewModelProviders.of(this).get(BreachedSitesViewModel::class.java)

        getDrawer()
        setupRecycler()
        getBreachedSites()
        getSnackbarMessage()
    }

    private fun getDrawer() {
        drawer = drawer {
        toolbar = this@MainActivity.main_toolbar
            primaryItem("Home") {  }
        }
    }

    private fun getBreachedSites() {

        viewModel.getBreachedSites().observe(this, Observer {
            adapter.setList(it)
        })
        swipeRefresh.isRefreshing = false
    }

    private fun getSnackbarMessage() {
        viewModel.getSnackbarMessage().observe(this, Observer {
            val snackbar = Snackbar.make(findViewById(R.id.main_coord_layout), it, Snackbar.LENGTH_LONG)
            snackbar.show()
        })
    }

    private fun setupRecycler() {
        swipeRefresh.isRefreshing = true
        breached_sites_recycler.layoutManager = LinearLayoutManager(this)
        breached_sites_recycler.adapter = adapter
    }


}
