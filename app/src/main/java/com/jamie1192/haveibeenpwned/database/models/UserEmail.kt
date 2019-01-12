package com.jamie1192.haveibeenpwned.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jamie1192 on 7/1/19.
 */

@Entity(tableName = "userEmail")
data class UserEmail (
    @PrimaryKey
    var email : String,
    var breachCount : Int?,
    var notifyNewBreaches : Boolean?
)