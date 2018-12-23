package com.jamie1192.haveibeenpwned.api

import android.content.Context
import com.jamie1192.haveibeenpwned.BuildConfig
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.utils.NetworkInterceptor
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by jamie1192 on 4/12/18.
 */

interface ApiService {

    @GET("/api/v2/breachedaccount/{email}")
    fun getBreachedAccount(@Path("email") email : String?) : Observable<List<Breach>>

    @GET("/api/v2/breaches")
    fun getAllBreaches() : Observable<List<Breach>>

    companion object Factory {

        fun create(context : Context): ApiService {

            val httpClient = OkHttpClient().newBuilder().addInterceptor(NetworkInterceptor(context))
                        if (BuildConfig.DEBUG) {
                httpClient.addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }

            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://haveibeenpwned.com")
                .client(httpClient.build())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

}

