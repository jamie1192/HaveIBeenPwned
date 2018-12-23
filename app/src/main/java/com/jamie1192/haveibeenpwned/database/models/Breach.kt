package com.jamie1192.haveibeenpwned.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.jamie1192.haveibeenpwned.database.converters.BreachConverter

/**
 * Created by jamie1192 on 27/11/18.
 */

@Entity(tableName = "Breaches")
@TypeConverters(BreachConverter::class)
data class Breach(
        @PrimaryKey
        @SerializedName("Name")
        var name : String,
        @SerializedName("Title")
        var title : String?,
        @SerializedName("Domain")
        var domain : String?,
        @SerializedName("BreachDate")
        var breachDate : String?,
        @SerializedName("AddedDate")
        var addedDate : String?,
        @SerializedName("ModifiedDate")
        var modifiedDate : String?,
        @SerializedName("PwnCount")
        var pwnCount : Int?,
        @SerializedName("Description")
        var description : String?,
        @SerializedName("LogoPath")
        var logoPath : String?,
        @SerializedName("DataClasses")
        var dataClasses : List<String>?,
        @SerializedName("IsVerified")
        var isVerified : Boolean?,
        @SerializedName("IsFabricated")
        var isFabricated : Boolean?,
        @SerializedName("IsSensitive")
        var isSensitive : Boolean?,
        @SerializedName("IsRetired")
        var isRetired : Boolean?,
        @SerializedName("IsSpamList")
        var isSpamList : Boolean?
)
