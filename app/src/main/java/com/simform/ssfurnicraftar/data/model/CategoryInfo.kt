package com.simform.ssfurnicraftar.data.model

import com.simform.ssfurnicraftar.data.local.database.model.CategoryEntity

data class CategoryInfo(
    val category: Category,
    val planeType: PlaneType
)

fun CategoryEntity.asExternalModel() = CategoryInfo(
    category = category,
    planeType = planeType
)
