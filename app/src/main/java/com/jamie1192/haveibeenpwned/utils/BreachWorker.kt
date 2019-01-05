package com.jamie1192.haveibeenpwned.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Created by jamie1192 on 1/1/19.
 */
class BreachWorker(context : Context, params : WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}