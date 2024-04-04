package com.simform.ssfurnicraftar.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.simform.ssfurnicraftar.data.local.LocalDataSource
import com.simform.ssfurnicraftar.data.local.database.SSFurniCraftARDatabase
import com.simform.ssfurnicraftar.data.local.database.dao.CategoryAndProductDao
import com.simform.ssfurnicraftar.data.local.database.model.ProductEntity
import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.model.Product
import com.simform.ssfurnicraftar.data.model.asExternalModel
import com.simform.ssfurnicraftar.data.remote.NetworkDataSource
import com.simform.ssfurnicraftar.data.remote.mediator.ModelRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ModelRepositoryImpl @Inject constructor(
    private val categoryAndModelDao: CategoryAndProductDao,
    private val database: SSFurniCraftARDatabase,
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource
) : ModelRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getModels(category: Category): Flow<PagingData<Product>> =
        Pager(
            config = PagingConfig(
                pageSize = 24
            ),
            remoteMediator = ModelRemoteMediator(category, networkDataSource, database),
            pagingSourceFactory = {
                categoryAndModelDao.getProductsByCategory(category)
            }
        ).flow.map { it.map(ProductEntity::asExternalModel) }

    override fun getCategories(): List<Category> = localDataSource.getCategories()
}
