package com.simform.ssfurnicraftar.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.domain.GetProductsUseCase
import com.simform.ssfurnicraftar.domain.GetProductCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    getProductCategories: GetProductCategories
) : ViewModel() {

    val categories = getProductCategories()

    private val _currentCategory = MutableStateFlow(Category.TABLE)
    val currentCategory = _currentCategory.asStateFlow()

    /**
     * Collect [currentCategory] and update models when category changes.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val modelsFlow = currentCategory.flatMapLatest { categories ->
        Timber.d("Updating category: $categories")
        getProductsUseCase(categories)
    }

    fun changeCategory(category: Category) {
        viewModelScope.launch {
            _currentCategory.update { category }
        }
    }
}
