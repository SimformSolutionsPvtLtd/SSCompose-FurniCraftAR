package com.simform.ssfurnicraftar.data.local.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.utils.constant.Constants

@Entity(
    tableName = Constants.TABLE_CATEGORY,
    indices = [
        Index(value = ["category"], unique = true)
    ]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: Category
)

fun Category.asEntity() = CategoryEntity(category = this)
