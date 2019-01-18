package com.jamie1192.haveibeenpwned.utils

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by jamie1192 on 22/12/18.
 */
class NetworkInterceptor(context: Context) : Interceptor {

    private val ctxt = context

    override fun intercept(chain: Interceptor.Chain): Response {

        if (!NetworkUtil.isOnline(ctxt)) {
            throw NoNetworkException()
        }

        val builder : Request.Builder = chain.request().newBuilder()
            .header("User-Agent", "HaveIBeenPwned-for-Android")

        return chain.proceed(builder.build())
    }
}