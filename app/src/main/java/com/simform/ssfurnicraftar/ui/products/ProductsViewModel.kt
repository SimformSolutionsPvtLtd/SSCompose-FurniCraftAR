package com.simform.ssfurnicraftar.ui.products

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simform.ssfurnicraftar.data.model.Category
import com.simform.ssfurnicraftar.data.model.Model
import com.simform.ssfurnicraftar.domain.GetModelsUseCase
import com.simform.ssfurnicraftar.domain.GetProductCategories
import com.simform.ssfurnicraftar.utils.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getModelsUseCase: GetModelsUseCase,
    getProductCategories: GetProductCategories
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProductsUiState> =
        MutableStateFlow(ProductsUiState.Loading)
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    val categories = getProductCategories()

    var currentCategory by mutableStateOf(Category.TABLE)
        private set

    init {
        fetchProducts(currentCategory)
    }


    /**
     * Refresh model using current category.
     */
    fun refreshData() {
        fetchProducts(currentCategory)
    }

    /**
     * Change category and load data for that category.
     */
    fun changeCategory(category: Category) {
        if (currentCategory == category) return
        currentCategory = category
        fetchProducts(currentCategory)
    }

    private fun fetchProducts(category: Category) {
        viewModelScope.launch {
            getModelsUseCase(category).collectLatest { result ->
                _uiState.emit(result.mapToProductsUiState())
            }
        }
    }


    /**
     * Map to products results to [ProductsUiState]
     */
    private fun Result<List<Model>>.mapToProductsUiState() = when (this) {
        Result.Loading -> ProductsUiState.Loading
        is Result.Success -> ProductsUiState.Products(data)
        is Result.Error -> ProductsUiState.Empty
    }
}
