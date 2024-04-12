package com.simform.ssfurnicraftar.data.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NetworkDownload(
    @SerializedName("source")
    val source: NetworkDownloadInfo,
    @SerializedName("gltf")
    val gltf: NetworkDownloadInfo,
    @SerializedName("glb")
    val glb: NetworkDownloadInfo,
    @SerializedName("usdz")
    val usdz: NetworkDownloadInfo,
)
