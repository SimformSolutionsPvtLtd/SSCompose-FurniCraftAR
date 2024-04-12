package com.simform.ssfurnicraftar.domain

import com.simform.ssfurnicraftar.data.remote.model.DownloadStatus
import com.simform.ssfurnicraftar.data.repository.ModelRepository
import kotlinx.coroutines.flow.Flow
import java.nio.file.Path
import javax.inject.Inject

class DownloadModelUseCase @Inject constructor(
    private val modelRepository: ModelRepository,
) {

    suspend operator fun invoke(path: Path, url: String): Flow<DownloadStatus> =
        modelRepository.download(path, url)
}
