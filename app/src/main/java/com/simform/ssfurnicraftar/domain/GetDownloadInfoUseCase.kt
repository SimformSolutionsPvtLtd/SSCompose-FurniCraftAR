package com.simform.ssfurnicraftar.domain

import com.simform.ssfurnicraftar.data.model.DownloadInfo
import com.simform.ssfurnicraftar.data.repository.ModelRepository
import com.simform.ssfurnicraftar.utils.result.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDownloadInfoUseCase @Inject constructor(
    private val modelRepository: ModelRepository,
) {

    suspend operator fun invoke(productId: String): Flow<Result<DownloadInfo>> =
        modelRepository.getDownloadInfo(productId)
}
