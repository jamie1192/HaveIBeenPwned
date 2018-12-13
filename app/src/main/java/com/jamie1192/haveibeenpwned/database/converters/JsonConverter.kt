package com.jamie1192.haveibeenpwned.database.converters

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.jamie1192.haveibeenpwned.api.models.Site

/**
 * Created by jamie1192 on 11/12/18.
 */
class JsonConverter {

    companion object {

        @TypeConverter
        @JvmStatic
        fun fromJsonString(data : String) : Site {
            val gson = GsonBuilder().create()
            return gson.fromJson(data, Site::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun toJsonString(site: Site) : String {
            val gson = GsonBuilder().create()
            return gson.toJson(site)
        }
    }

}