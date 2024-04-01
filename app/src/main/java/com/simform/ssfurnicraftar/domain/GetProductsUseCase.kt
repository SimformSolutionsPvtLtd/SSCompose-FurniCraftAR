package com.simform.ssfurnicraftar.domain

import androidx.paging.PagingData
import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.model.Product
import com.simform.ssfurnicraftar.data.repository.ModelRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val modelRepository: ModelRepository,
) {

    suspend operator fun invoke(category: Category): Flow<PagingData<Product>> =
        modelRepository.getModels(category)
}
