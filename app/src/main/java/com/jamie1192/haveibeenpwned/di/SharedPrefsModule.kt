package com.jamie1192.haveibeenpwned.di

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by jamie1192 on 23/12/18.
 */
class SharedPrefsModule  {

    companion object Factory {

        fun getSharedPrefs(context: Context) : SharedPreferences {
            return context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        }
    }
}