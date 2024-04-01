package com.simform.ssfurnicraftar.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.simform.ssfurnicraftar.data.local.LocalDataSource
import com.simform.ssfurnicraftar.data.local.database.SSFurniCraftARDatabase
import com.simform.ssfurnicraftar.data.local.database.dao.CategoryAndModelDao
import com.simform.ssfurnicraftar.data.local.database.model.ModelEntity
import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.model.Model
import com.simform.ssfurnicraftar.data.model.asExternalModel
import com.simform.ssfurnicraftar.data.remote.NetworkDataSource
import com.simform.ssfurnicraftar.data.remote.mediator.ModelRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ModelRepositoryImpl @Inject constructor(
    private val categoryAndModelDao: CategoryAndModelDao,
    private val database: SSFurniCraftARDatabase,
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource
) : ModelRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getModels(category: Category): Flow<PagingData<Model>> =
        Pager(
            config = PagingConfig(
                pageSize = 24
            ),
            remoteMediator = ModelRemoteMediator(category, networkDataSource, database),
            pagingSourceFactory = {
                categoryAndModelDao.getModelsByCategory(category)
            }
        ).flow.map { it.map(ModelEntity::asExternalModel) }

    override fun getCategories(): List<Category> = localDataSource.getCategories()
}
