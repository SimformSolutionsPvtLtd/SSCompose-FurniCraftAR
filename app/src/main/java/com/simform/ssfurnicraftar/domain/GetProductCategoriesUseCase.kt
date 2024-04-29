package com.simform.ssfurnicraftar.domain

import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.repository.ModelRepository
import javax.inject.Inject

class GetProductCategoriesUseCase @Inject constructor(
    private val modelRepository: ModelRepository
) {

    operator fun invoke(): List<Category> = modelRepository.getCategories()
}
