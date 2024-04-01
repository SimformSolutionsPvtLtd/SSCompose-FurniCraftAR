package com.simform.ssfurnicraftar.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "category_model",
    primaryKeys = ["categoryId", "modelId"],
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
data class CategoryModelCrossRef(
    @ColumnInfo(index = true)
    val categoryId: Long,
    @ColumnInfo(index = true)
    val modelId: String
)
