package com.simform.ssfurnicraftar.data.repository

import com.simform.ssfurnicraftar.data.local.LocalDataSource
import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.model.Model
import com.simform.ssfurnicraftar.data.model.asExternalModel
import com.simform.ssfurnicraftar.data.remote.NetworkDataSource
import com.simform.ssfurnicraftar.data.remote.apiresult.ApiResult
import com.simform.ssfurnicraftar.data.remote.model.NetworkModel
import com.simform.ssfurnicraftar.utils.extension.mapOnSuccess
import javax.inject.Inject

class ModelRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource,
) : ModelRepository {

    override suspend fun getModels(category: Category): ApiResult<List<Model>> =
        networkDataSource.getModels(category.name.lowercase())
            .mapOnSuccess {
                it.results.map(NetworkModel::asExternalModel)
            }

    override fun getCategories(): List<Category> = localDataSource.getCategories()
}
