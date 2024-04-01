package com.simform.ssfurnicraftar.data.remote.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Image(
    @SerializedName("height")
    val height: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)
