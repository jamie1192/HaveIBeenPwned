package com.jamie1192.haveibeenpwned.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jamie1192.haveibeenpwned.api.models.Site

/**
 * Created by jamie1192 on 13/12/18.
 */
object GsonConverter {

    @TypeConverter
    @JvmStatic
    fun fromJsonString(data: String?): Site? {
        val gson = GsonBuilder().create()
        return if (data == null) null else gson.fromJson(data, Site::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun toString(data: Site?): String? {
        val gson = GsonBuilder().create()
        return if (data == null) null else gson.toJson(data)
    }

}
