package com.simform.ssfurnicraftar.data.remote.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class NetworkModels(
    @SerializedName("cursors")
    val cursors: Cursors,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<NetworkModel>
)
