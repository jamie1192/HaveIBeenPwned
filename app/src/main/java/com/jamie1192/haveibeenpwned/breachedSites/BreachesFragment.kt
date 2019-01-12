package com.jamie1192.haveibeenpwned.breachedSites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import com.jamie1192.haveibeenpwned.R
import com.jamie1192.haveibeenpwned.database.adapters.BreachedPagedAdapter
import com.jamie1192.haveibeenpwned.utils.Response
import com.jamie1192.haveibeenpwned.utils.Response.Companion.STATUS_ERROR
import com.jamie1192.haveibeenpwned.utils.Response.Companion.STATUS_LOADING
import com.jamie1192.haveibeenpwned.utils.Response.Companion.STATUS_SUCCESS
import com.mikepenz.iconics.utils.IconicsMenuInflaterUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_breaches.*
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BreachesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel : BreachedSitesViewModel
    private val adapter = BreachedPagedAdapter()
    private val compDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_breaches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compDisposable.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        IconicsMenuInflaterUtil.inflate(inflater, context, R.menu.toolbar_breaches, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            R.id.sort_name_asc -> {
                viewModel.setQuery("nameAsc")
            }
            R.id.sort_name_desc -> {
                viewModel.setQuery("nameDesc")
            }
            R.id.sort_date_asc -> {
                Timber.w("ASCENDING")
                viewModel.setQuery("breachAsc")
            }
            R.id.sort_date_desc -> {
                Timber.w("DESCENDING")
                viewModel.setQuery("breachDesc")
            }
            R.id.sort_pwn_asc -> {
                viewModel.setQuery("pwnAsc")
            }
            R.id.sort_pwn_desc -> {
                viewModel.setQuery("pwnDesc")
            }

        }
        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(BreachedSitesViewModel::class.java)

        getSnackbarMessage()
        getBreachedSites()
        getOnClickSite()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BreachesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setupRecycler() {
        swipeRefresh.isRefreshing = true
        breached_sites_recycler.layoutManager = LinearLayoutManager(context)
        breached_sites_recycler.adapter = adapter
    }

    private fun getBreachedSites() {

        viewModel.checkLastUpdated()

        viewModel.setQuery("nameAsc")

        viewModel.getMediator().observe(this, Observer {
            adapter.submitList(it)
            breached_sites_recycler.layoutManager?.smoothScrollToPosition(breached_sites_recycler, RecyclerView.State(), 0)
            swipeRefresh.isRefreshing = false

        })
    }

    private fun getOnClickSite() {
        val disposable : Disposable = adapter.getOnClickSite().subscribe {

            val modal = BreachedSiteModalFragment.newInstance(it)
            modal.show(childFragmentManager, "siteModal")
        }
        compDisposable.add(disposable)
    }

    private fun getSnackbarMessage() {
        viewModel.getSnackbarMessage().observe(this, Observer {

            it?.let { response: Response<String> ->

                when(response.status) {

                    STATUS_LOADING -> {
                        showSnackbar(response.data, false)
                    }
                    STATUS_ERROR -> {
                        showSnackbar(response.data, true)
                    }
                    STATUS_SUCCESS -> {
                        showSnackbar(response.data, false)
                    }
                }
            }

        })

    }

    private fun showSnackbar(msg : String?, isRetry : Boolean) {
        view?.let { view ->
            if (isRetry) {
                msg?.let {
                    Snackbar.make(view, it, Snackbar.LENGTH_INDEFINITE)
                        .setAction("retry") { viewModel.checkLastUpdated() }
                        .show()
                }
            } else {
                msg?.let {
                    Snackbar.make(view, it, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}
