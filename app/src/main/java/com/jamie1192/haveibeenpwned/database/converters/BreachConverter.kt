package com.jamie1192.haveibeenpwned.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

/**
 * Created by jamie1192 on 13/12/18.
 */

object BreachConverter {

    @TypeConverter
    @JvmStatic
    fun listToJson(value: List<String>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToList(value: String): List<String>? {

        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        return objects.toList()
    }

}
