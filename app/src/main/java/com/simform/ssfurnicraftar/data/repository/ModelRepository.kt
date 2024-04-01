package com.simform.ssfurnicraftar.data.repository

import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.model.Model
import com.simform.ssfurnicraftar.data.remote.apiresult.ApiResult

interface ModelRepository {

    suspend fun getModels(category: Category): ApiResult<List<Model>>

    fun getCategories(): List<Category>
}
