package com.simform.ssfurnicraftar.data.model

import com.simform.ssfurnicraftar.data.local.database.model.ProductEntity
import com.simform.ssfurnicraftar.data.remote.model.NetworkModel

data class Product(
    val id: String,
    val name: String,
    val description: String = "",
    val thumbnail: String,
    val likes: Int = 0,
    val license: String?,
)

fun NetworkModel.asExternalModel() = Product(
    id = uid,
    name = name,
    description = description,
    thumbnail = thumbnails.images.run { firstOrNull { it.width == 720 } ?: last() }.url,
    likes = likeCount,
    license = license?.label
)

fun ProductEntity.asExternalModel() = Product(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail,
    likes = likes,
    license = license
)
