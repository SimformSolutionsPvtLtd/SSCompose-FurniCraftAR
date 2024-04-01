package com.simform.ssfurnicraftar.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.simform.ssfurnicraftar.data.remote.model.NetworkModel

@Entity(
    tableName = "model"
)
data class ModelEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo(defaultValue = "")
    val description: String,
    val thumbnail: String,
    @ColumnInfo(defaultValue = "0")
    val likes: Int,
    @ColumnInfo(defaultValue = "NULL")
    val license: String?
)

fun NetworkModel.asEntity() = ModelEntity(
    id = uid,
    name = name,
    description = description,
    thumbnail = thumbnails.images.run { firstOrNull { it.width == 720 } ?: last() }.url,
    likes = likeCount,
    license = license?.label
)
