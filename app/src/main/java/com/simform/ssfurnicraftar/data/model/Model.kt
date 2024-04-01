package com.simform.ssfurnicraftar.data.model

import com.simform.ssfurnicraftar.data.local.database.model.ModelEntity
import com.simform.ssfurnicraftar.data.remote.model.NetworkModel

data class Model(
    val id: String,
    val name: String,
    val description: String = "",
    val thumbnail: String,
    val likes: Int = 0,
    val license: String?,
)

fun NetworkModel.asExternalModel() = Model(
    id = uid,
    name = name,
    description = description,
    thumbnail = thumbnails.images.run { firstOrNull { it.width == 720 } ?: last() }.url,
    likes = likeCount,
    license = license?.label
)

fun ModelEntity.asExternalModel() = Model(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail,
    likes = likes,
    license = license
)
