package com.simform.ssfurnicraftar.data.repository

import androidx.paging.PagingData
import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.model.Model
import kotlinx.coroutines.flow.Flow

interface ModelRepository {

    suspend fun getModels(category: Category): Flow<PagingData<Model>>

    fun getCategories(): List<Category>
}
