package com.simform.ssfurnicraftar.data.remote.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class User(
    @SerializedName("account")
    val account: String,
    @SerializedName("avatar")
    val avatar: Avatar,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("profileUrl")
    val profileUrl: String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("uri")
    val uri: String,
    @SerializedName("username")
    val username: String
)
