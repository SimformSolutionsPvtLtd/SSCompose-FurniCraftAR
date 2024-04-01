package com.simform.ssfurnicraftar.data.remote.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Thumbnails(
    @SerializedName("images")
    val images: List<Image>
)
