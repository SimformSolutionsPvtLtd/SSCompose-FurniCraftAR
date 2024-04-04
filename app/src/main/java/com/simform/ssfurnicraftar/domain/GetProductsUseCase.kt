package com.simform.ssfurnicraftar.domain

import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.model.Product
import com.simform.ssfurnicraftar.data.repository.ModelRepository
import com.simform.ssfurnicraftar.utils.extension.onError
import com.simform.ssfurnicraftar.utils.extension.onSuccess
import com.simform.ssfurnicraftar.utils.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val modelRepository: ModelRepository,
) {

    operator fun invoke(category: Category): Flow<Result<List<Product>>> = flow {
        emit(Result.Loading)
        modelRepository.getModels(category)
            .onSuccess {
                if (it.isNotEmpty()) {
                    emit(Result.Success(it))
                } else {
                    emit(Result.Error(Exception("No data.")))
                }
            }
            .onError { _, message ->
                emit(Result.Error(Exception(message)))
            }
    }
}
