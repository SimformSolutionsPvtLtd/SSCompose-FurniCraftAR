package com.simform.ssfurnicraftar.ui.products

import com.simform.ssfurnicraftar.data.model.Product

sealed class ProductsUiState {

    data object Loading : ProductsUiState()

    data class Products(
        val models: List<Product>
    ) : ProductsUiState()

    data object Empty : ProductsUiState()
}
