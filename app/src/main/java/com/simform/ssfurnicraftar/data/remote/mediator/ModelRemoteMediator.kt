package com.simform.ssfurnicraftar.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.simform.ssfurnicraftar.data.local.database.SSFurniCraftARDatabase
import com.simform.ssfurnicraftar.data.local.database.model.ProductEntity
import com.simform.ssfurnicraftar.data.local.database.model.RemoteKey
import com.simform.ssfurnicraftar.data.local.database.model.asEntity
import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.model.CategoryInfo
import com.simform.ssfurnicraftar.data.remote.NetworkDataSource
import com.simform.ssfurnicraftar.data.remote.apiresult.ApiError
import com.simform.ssfurnicraftar.data.remote.apiresult.ApiException
import com.simform.ssfurnicraftar.data.remote.apiresult.ApiSuccess
import com.simform.ssfurnicraftar.data.remote.model.NetworkModel
import com.simform.ssfurnicraftar.data.remote.model.NetworkModels
import com.simform.ssfurnicraftar.domain.GetPlaneTypeByCategoryUseCase
import com.simform.ssfurnicraftar.utils.extension.encodeToBase64
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ModelRemoteMediator @Inject constructor(
    private val category: Category,
    private val networkDataSource: NetworkDataSource,
    private val database: SSFurniCraftARDatabase,
    private val getPlaneTypeByCategoryUseCase: GetPlaneTypeByCategoryUseCase
) : RemoteMediator<Int, ProductEntity>() {

    /**
     * Initial key used for pagination
     */
    private val startingKey = "o=0".encodeToBase64()

    /**
     * [androidx.paging.RemoteMediator.InitializeAction] type defining how to refresh initial data
     */
    private val refreshType = InitializeAction.LAUNCH_INITIAL_REFRESH

    private val categoryAndModelDao = database.categoryAndProductDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun initialize(): InitializeAction = refreshType

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, ProductEntity>
    ): MediatorResult {

        val currentKey: String = when (loadType) {
            // Used during initial loading or pull to refresh action
            LoadType.REFRESH -> startingKey

            // Used when scrolling upwards
            LoadType.PREPEND -> state.getRemoteKeyForFirstItem()?.previous
                ?: return MediatorResult.Success(endOfPaginationReached = true)

            // Used to append data downwards when no data is available in room and
            // need to pull from upstream
            LoadType.APPEND -> state.getRemoteKeyForLastItem()?.next
                ?: return MediatorResult.Success(endOfPaginationReached = false)
        }

        val response = networkDataSource.getModels(tag = category.slug, cursor = currentKey)

        return when (response) {
            is ApiSuccess -> handleData(loadType == LoadType.REFRESH, response.data)
            // In case of [ApiError] or [ApiException] the cached data from room will be used
            is ApiError -> MediatorResult.Error(Exception(response.message))
            is ApiException -> MediatorResult.Error(response.exception)
        }
    }

    private suspend fun handleData(
        isRefresh: Boolean,
        data: NetworkModels
    ): MediatorResult {
        val models = data.results.map(NetworkModel::asEntity)

        // No further data is available
        val endOfResult = data.next == null
        Timber.d("Response last: ${models.last().id}")

        database.withTransaction {
            if (isRefresh) {
                // If refreshing for particular category delete all models belongs to that category
                categoryAndModelDao.deleteProductsByCategory(category)
            }

            val categoryInfo = CategoryInfo(category, getPlaneTypeByCategoryUseCase(category))
            categoryAndModelDao.insertCategoryWithProducts(categoryInfo.asEntity(), models)
            categoryAndModelDao.findCategory(category)?.id?.let { categoryId ->
                // Create remote keys for pagination
                val keys = models.map {
                    RemoteKey(
                        productId = it.id,
                        categoryId = categoryId,
                        previous = data.cursors.previous,
                        next = data.cursors.next
                    )
                }
                remoteKeyDao.upsertRemoteKeys(keys)
            }
        }
        return MediatorResult.Success(endOfPaginationReached = endOfResult)
    }

    /**
     * Get remote key for last item
     *
     * @return Returns the remote key if found, otherwise null
     */
    private suspend fun PagingState<Int, ProductEntity>.getRemoteKeyForLastItem(): RemoteKey? =
        lastItemOrNull()?.id?.let { remoteKeyDao.getRemoteKeyByProductId(it) }

    /**
     * Get remote key for first item
     *
     * @return Returns the remote key if found, otherwise null
     */
    private suspend fun PagingState<Int, ProductEntity>.getRemoteKeyForFirstItem(): RemoteKey? =
        firstItemOrNull()?.id?.let { remoteKeyDao.getRemoteKeyByProductId(it) }
}
