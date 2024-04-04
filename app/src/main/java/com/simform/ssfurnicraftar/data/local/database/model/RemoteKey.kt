package com.simform.ssfurnicraftar.data.local.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import com.simform.ssfurnicraftar.utils.constant.Constants

@Entity(
    tableName = Constants.TABLE_REMOTE_KEY,
    primaryKeys = ["productId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RemoteKey(
    val productId: String,
    val categoryId: Long,
    val previous: String?,
    val next: String?
)
