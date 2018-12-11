package com.jamie1192.haveibeenpwned.api

import com.jamie1192.haveibeenpwned.api.models.Site
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
    fun getBreachedAccount(@Path("email") email : String?) : Observable<List<Site>>

    @GET("/api/v2/breaches")
    fun getAllBreaches() : Observable<ArrayList<Site>>

    companion object Factory {

        private var client = OkHttpClient().newBuilder().addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://haveibeenpwned.com")
                .client(client.build())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

}
