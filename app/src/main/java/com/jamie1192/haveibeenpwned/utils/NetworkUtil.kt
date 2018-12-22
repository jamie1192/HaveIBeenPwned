package com.jamie1192.haveibeenpwned.utils

import android.content.Context
import android.net.ConnectivityManager
import java.io.IOException

/**
 * Created by jamie1192 on 22/12/18.
 */
object NetworkUtil {

    @JvmStatic
    fun isOnline(context: Context) : Boolean {
        val connectivityManager : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return (networkInfo != null && networkInfo.isConnected)
    }
}

class NoNetworkException : IOException() {

    override val message: String?
         get() = super.message

}
