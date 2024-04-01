package com.simform.ssfurnicraftar.data.remote.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ModelData(
    @SerializedName("faceCount")
    val faceCount: Int?,
    @SerializedName("size")
    val size: Int,
    @SerializedName("textureCount")
    val textureCount: Int?,
    @SerializedName("textureMaxResolution")
    val textureMaxResolution: Int?,
    @SerializedName("type")
    val type: String,
    @SerializedName("vertexCount")
    val vertexCount: Int?
)
