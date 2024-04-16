package com.simform.ssfurnicraftar.domain

import com.simform.ssfurnicraftar.data.model.CategoryInfo
import com.simform.ssfurnicraftar.data.repository.ModelRepository
import javax.inject.Inject

class GetProductCategoryUseCase @Inject constructor(
    private val modelRepository: ModelRepository,
) {

    suspend operator fun invoke(productId: String): CategoryInfo =
        modelRepository.getProductCategory(productId)
}
