package com.jamie1192.haveibeenpwned

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import com.google.android.material.snackbar.Snackbar
import com.jamie1192.haveibeenpwned.breachedSites.BreachedSiteModalFragment
import com.jamie1192.haveibeenpwned.breachedSites.BreachedSitesViewModel
import com.jamie1192.haveibeenpwned.breachedSites.BreachesFragment
import com.jamie1192.haveibeenpwned.database.adapters.BreachedPagedAdapter
import com.jamie1192.haveibeenpwned.emailSearch.EmailSearchFragment
import com.jamie1192.haveibeenpwned.emailSearch.SearchActivity
import com.mikepenz.iconics.utils.IconicsMenuInflaterUtil
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic
import com.mikepenz.materialdrawer.Drawer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {

    private val adapter = BreachedPagedAdapter()
    private lateinit var viewModel : BreachedSitesViewModel
    private lateinit var drawer : Drawer
    private val compDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)



//        viewModel = ViewModelProviders.of(this).get(BreachedSitesViewModel::class.java)

        getDrawer()
//        setupRecycler()
//        getBreachedSites()
//        getOnClickSite()
//        getSnackbarMessage()
        setFragment(BreachesFragment.newInstance("one", "two"))
    }



    override fun onDestroy() {
        compDisposable.dispose()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        IconicsMenuInflaterUtil.inflate(menuInflater, this, R.menu.toolbar_breaches, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.sort_date_asc -> viewModel.setBreachedPagedList("breachDateAsc")
            R.id.sort_date_desc -> viewModel.setBreachedPagedList("breachDateDesc")

        }
        return true
    }

    private fun getDrawer() {
        drawer = drawer {
            accountHeader {

            }
            toolbar = this@MainActivity.main_toolbar
            primaryItem("Home") {
                iicon = MaterialDesignIconic.Icon.gmi_home

            }
            primaryItem("Search") {
                iicon = MaterialDesignIconic.Icon.gmi_search
//                onClick(openActivity(SearchActivity::class))
                onClick { _ ->
                    setFragment(EmailSearchFragment.newInstance())
                    false
                }
            }
        }
    }

    private fun getBreachedSites() {

//        viewModel.getBreachedSites().observe(this, Observer {
//            adapter.setList(it)
//
//        })
//        viewModel.setBreachedPagedList("nameAsc") //.observe(this, Observer {
//            adapter.submitList(it)
//        })
        viewModel.getBreachedPagedList().observe(this, Observer {
            adapter.submitList(it)
        })
//        swipeRefresh.isRefreshing = false
    }

    private fun getOnClickSite() {
        val disposable : Disposable = adapter.getOnClickSite().subscribe {

            val modal = BreachedSiteModalFragment.newInstance(it)
            modal.show(supportFragmentManager, "siteModal")
        }
        compDisposable.add(disposable)
    }

    private fun getSnackbarMessage() {
        viewModel.getSnackbarMessage().observe(this, Observer {
            val snackbar = Snackbar.make(findViewById(R.id.main_coord_layout), it, Snackbar.LENGTH_LONG)
            snackbar.show()
        })
    }

//    private fun setupRecycler() {
//        swipeRefresh.isRefreshing = true
//        breached_sites_recycler.layoutManager = LinearLayoutManager(this)
//        breached_sites_recycler.adapter = adapter
//    }

    private fun <T : Activity> openActivity(activity: KClass<T>): (View?) -> Boolean = {
        startActivity(Intent(this@MainActivity, activity.java))
        false
    }

    private fun setFragment(fragment : Fragment) {
        val ft = supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_activity_main, fragment, fragment::class.simpleName)
//            .commit()
        ft.commit()
    }

}
