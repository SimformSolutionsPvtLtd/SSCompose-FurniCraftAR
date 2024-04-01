package com.simform.ssfurnicraftar.data.remote.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Archives(
    @SerializedName("glb")
    val glb: ModelData,
    @SerializedName("gltf")
    val gltf: ModelData,
    @SerializedName("gltf-ar")
    val gltfAr: ModelData?,
    @SerializedName("source")
    val source: ModelData,
    @SerializedName("usdz")
    val usdz: ModelData
)
