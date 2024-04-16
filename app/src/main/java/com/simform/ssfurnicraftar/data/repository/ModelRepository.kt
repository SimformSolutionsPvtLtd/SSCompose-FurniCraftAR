package com.simform.ssfurnicraftar.data.repository

import androidx.paging.PagingData
import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.model.CategoryInfo
import com.simform.ssfurnicraftar.data.model.DownloadInfo
import com.simform.ssfurnicraftar.data.model.Product
import com.simform.ssfurnicraftar.data.remote.model.DownloadStatus
import com.simform.ssfurnicraftar.utils.result.Result
import kotlinx.coroutines.flow.Flow
import java.nio.file.Path

interface ModelRepository {

    suspend fun getModels(category: Category): Flow<PagingData<Product>>

    fun getCategories(): List<Category>

    suspend fun getProductCategory(productId: String): CategoryInfo

    suspend fun getDownloadInfo(modelId: String): Flow<Result<DownloadInfo>>

    suspend fun download(path: Path, url: String): Flow<DownloadStatus>
}
