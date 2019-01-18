package com.jamie1192.haveibeenpwned.api

import android.content.Context
import com.jamie1192.haveibeenpwned.BuildConfig
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.jamie1192.haveibeenpwned.utils.NetworkInterceptor
import io.reactivex.Observable
import io.reactivex.Single
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

    @GET("/api/v2/breachedaccount/{email}")
    fun getBreachedAccountSingle(@Path("email") email : String?) : Single<List<Breach>>

    @GET("/api/v2/breaches")
    fun getAllBreaches() : Observable<List<Breach>>

    @GET("/api/v2/breaches")
    fun getAllBreachesWorker() : Single<List<Breach>>

    companion object Factory {

        fun create(context : Context): ApiService {

            fun httpClient() = OkHttpClient.Builder().apply {
                addInterceptor(NetworkInterceptor(context))
                if (BuildConfig.DEBUG) addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }.build()


            fun retrofit() = retrofit2.Retrofit.Builder().apply {
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                addConverterFactory(GsonConverterFactory.create())
                baseUrl("https://www.haveibeenpwned.com")
                client(httpClient())
            }.build().create(ApiService::class.java)

            return retrofit()
        }
    }

}

