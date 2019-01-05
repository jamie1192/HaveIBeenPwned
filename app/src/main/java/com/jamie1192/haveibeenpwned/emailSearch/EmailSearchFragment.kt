package com.jamie1192.haveibeenpwned.emailSearch

import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.jamie1192.haveibeenpwned.R
import com.jamie1192.haveibeenpwned.utils.ColorUtil
import kotlinx.android.synthetic.main.fragment_email_search.*
import timber.log.Timber

class EmailSearchFragment : Fragment() {

    private val adapter = EmailSearchAdapter()
    private lateinit var anim : ValueAnimator

    companion object {
        fun newInstance() = EmailSearchFragment()
    }

    private lateinit var viewModel: EmailSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        Timber.w("EmailSearchFragment created")
        return inflater.inflate(R.layout.fragment_email_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EmailSearchViewModel::class.java)

        setupListeners()
        getSnackbarMessage()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if(anim.isRunning) anim.cancel()
    }

    private fun setupRecycler() {
        email_search_results_recycler.layoutManager = LinearLayoutManager(context)
        email_search_results_recycler.adapter = adapter
    }

    private fun setupListeners() {

        viewModel.observeEmail().observe(this, Observer { breaches ->
            adapter.setList(breaches)
            animateToolbarColor(true)
        })

        searchButton.setOnClickListener {
            email_search_input.text.toString().trim().let { text ->
                viewModel.searchEmail(text)
            }
        }

        email_search_input.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE
                && email_search_input.text.toString().trim() != ""
            ) {
                viewModel.searchEmail(email_search_input.text.toString())
            }
            false
        }

        //TODO onTextChangedListener to disable button?

    }


    private fun getSnackbarMessage() {
        viewModel.getSnackbarMessage().observe(this, Observer { message ->
            view?.let {
                Snackbar.make(it, message.toString(), Snackbar.LENGTH_SHORT).show()
                adapter.emptyList()
                animateToolbarColor(false)
            }
        })
    }

    private fun animateToolbarColor(isBreach : Boolean) {
        val toolbar = activity?.findViewById<Toolbar>(R.id.main_toolbar)
        val cd : ColorDrawable = toolbar?.background as ColorDrawable
        val backgroundCd : ColorDrawable = emailSearchContainer?.background as ColorDrawable

        val currentToolbar = cd.color
        var currentStatusBar = ContextCompat.getColor(this.requireContext(), R.color.hibpPrimaryDark)
        val currentBackground = backgroundCd.color

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor?.let {
                currentStatusBar = it
            }
        }

        anim = ValueAnimator.ofFloat(0F, 1F)

        if(isBreach) {

            val finalToolbar = ContextCompat.getColor(this.requireContext(), R.color.hibpBreached)
            val finalStatus = ContextCompat.getColor(this.requireContext(), R.color.hibpBreachedDark)
            val finalBackground = ContextCompat.getColor(this.requireContext(), R.color.hibpBreachedLight)

            anim.addUpdateListener {
                val position = it.animatedFraction
                val blendedStatus = ColorUtil.blendColors(currentStatusBar, finalStatus, position)
                val blendedToolbar = ColorUtil.blendColors(currentToolbar, finalToolbar, position)
                val blendedBackground = ColorUtil.blendColors(currentBackground, finalBackground, position)

                toolbar.setBackgroundColor(blendedToolbar)
                emailSearchContainer.setBackgroundColor(blendedBackground)

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity?.window?.statusBarColor = blendedStatus
                }
            }
            anim.doOnEnd {
                Timber.w("anim end")
            }

            anim.setDuration(800).start()
        }
        else {
            val finalToolbar = ContextCompat.getColor(this.requireContext(), R.color.hibpNoPwnage)
            val finalStatus = ContextCompat.getColor(this.requireContext(), R.color.hibpNoPwnageDark)
            val finalBackground = ContextCompat.getColor(this.requireContext(), R.color.hibpNoPwnageLight)

            anim.addUpdateListener {
                val position = it.animatedFraction
                val blendedToolbar = ColorUtil.blendColors(currentToolbar, finalToolbar, position)
                val blendedStatus = ColorUtil.blendColors(currentStatusBar, finalStatus, position)
                val blendedBackground = ColorUtil.blendColors(currentBackground, finalBackground, position)

                toolbar.setBackgroundColor(blendedToolbar)
                emailSearchContainer.setBackgroundColor(blendedBackground)

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity?.window?.statusBarColor = blendedStatus
                }
            }
            anim.doOnEnd {
                Timber.w("anim end")
            }

            anim.setDuration(800).start()
        }

    }

}
