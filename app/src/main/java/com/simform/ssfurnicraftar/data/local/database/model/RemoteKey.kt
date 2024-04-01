package com.simform.ssfurnicraftar.data.local.database.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "remote_key",
    primaryKeys = ["modelId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ModelEntity::class,
            parentColumns = ["id"],
            childColumns = ["modelId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RemoteKey(
    val modelId: String,
    val categoryId: Long,
    val previous: String?,
    val next: String?
)
