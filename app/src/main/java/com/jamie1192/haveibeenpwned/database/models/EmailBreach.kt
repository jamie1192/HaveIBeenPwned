package com.jamie1192.haveibeenpwned.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jamie1192.haveibeenpwned.database.converters.EmailBreachDataConverter

/**
 * Created by jamie1192 on 15/1/19.
 */

@Entity(tableName = "emailBreach",
        foreignKeys = arrayOf(ForeignKey(entity = UserEmail::class,
            parentColumns = arrayOf("email"),
            childColumns = arrayOf("emailId"),
            onDelete = ForeignKey.CASCADE)))
@TypeConverters(EmailBreachDataConverter::class)

data class EmailBreach (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name : String,
    var breachData : Breach,
    val emailId : String
)