package com.simform.ssfurnicraftar.data.remote.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Avatar(
    @SerializedName("images")
    val images: List<AvtarImage>,
    @SerializedName("uri")
    val uri: String
)
