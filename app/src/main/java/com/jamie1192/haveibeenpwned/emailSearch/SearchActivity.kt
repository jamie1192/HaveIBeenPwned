package com.jamie1192.haveibeenpwned.emailSearch

import android.os.Bundle
import android.app.Activity
import com.jamie1192.haveibeenpwned.R

import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
