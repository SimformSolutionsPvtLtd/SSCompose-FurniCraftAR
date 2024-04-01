package com.simform.ssfurnicraftar.data.remote.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Cursors(
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?
)
