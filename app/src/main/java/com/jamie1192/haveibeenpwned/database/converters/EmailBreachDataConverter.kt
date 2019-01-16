package com.jamie1192.haveibeenpwned.database.converters

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.jamie1192.haveibeenpwned.database.models.Breach

/**
 * Created by jamie1192 on 15/1/19.
 */
object EmailBreachDataConverter {

    @TypeConverter
    @JvmStatic
    fun fromJsonString(data : String?) : Breach? {
        val gson = GsonBuilder().create()
        return if (data == null) null else gson.fromJson(data, Breach::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun toString(data: Breach?): String? {
        val gson = GsonBuilder().create()
        return if (data == null) null else gson.toJson(data)
    }
}