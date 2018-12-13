package com.jamie1192.haveibeenpwned.api.models

import com.google.gson.annotations.SerializedName

data class Site (
    @SerializedName("Name")
    var name : String?,
    @SerializedName("Title")
    var title : String?,
    @SerializedName("DomainName")
    var domainName : String?,
    @SerializedName("BreachDate")
    var breachDate : String?,
    @SerializedName("AddedDate")
    var addedDate : String?,
    @SerializedName("ModifiedDate")
    var modifiedDate : String?,
    @SerializedName("PwnCount")
    var pwnCount : String?,
    @SerializedName("Description")
    var description : String?,
    @SerializedName("LogoPath")
    var logoPath : String?,
    @SerializedName("DataClasses")
    var dataclasses : List<String>?,
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
