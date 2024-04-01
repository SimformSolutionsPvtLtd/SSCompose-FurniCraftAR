package com.simform.ssfurnicraftar.data.local

import com.simform.ssfurnicraftar.data.model.Category
import javax.inject.Inject

class LocalDataSource @Inject constructor() {

    fun getCategories(): List<Category> = Category.entries
}
