package com.jamie1192.haveibeenpwned.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jamie1192.haveibeenpwned.api.models.Site
import com.jamie1192.haveibeenpwned.database.converters.GsonConverter

/**
 * Created by jamie1192 on 27/11/18.
 */
@Entity(tableName = "breaches")
@TypeConverters(GsonConverter::class)
data class Breach(
        @PrimaryKey
        @ColumnInfo(name = "key")
        var key: String,

        @ColumnInfo(name = "data")
        var data: Site)
