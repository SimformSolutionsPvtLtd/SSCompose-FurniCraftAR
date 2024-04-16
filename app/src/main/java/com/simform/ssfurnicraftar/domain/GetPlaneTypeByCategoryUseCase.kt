package com.simform.ssfurnicraftar.domain

import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.model.PlaneType
import javax.inject.Inject

private val horizontalCategories = setOf(Category.TABLE, Category.CHAIR, Category.BED, Category.SOFA, Category.DESK)

private val verticalCategories = setOf(Category.AC)

class GetPlaneTypeByCategoryUseCase @Inject constructor() {

    operator fun invoke(category: Category): PlaneType = when {
        category == Category.UNKNOWN -> PlaneType.HORIZONTAL_AND_VERTICAL
        verticalCategories.contains(category) -> PlaneType.VERTICAL
        else -> PlaneType.HORIZONTAL
    }
}
