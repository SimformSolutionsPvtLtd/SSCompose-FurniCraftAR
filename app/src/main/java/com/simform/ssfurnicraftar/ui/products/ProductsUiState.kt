package com.simform.ssfurnicraftar.ui.products

import com.simform.ssfurnicraftar.data.model.Model

sealed class ProductsUiState {

    data object Loading : ProductsUiState()

    data class Products(
        val models: List<Model>
    ) : ProductsUiState()

    data object Empty : ProductsUiState()
}
