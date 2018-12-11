package com.jamie1192.haveibeenpwned.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jamie1192 on 27/11/18.
 */
@Entity(tableName = "table_entity")
class TableEntity(@PrimaryKey
           @ColumnInfo(name = "primaryKey")
           val key: String)
