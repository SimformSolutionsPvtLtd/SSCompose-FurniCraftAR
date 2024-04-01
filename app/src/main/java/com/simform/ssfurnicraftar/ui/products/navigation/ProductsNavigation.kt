package com.simform.ssfurnicraftar.ui.products.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.simform.ssfurnicraftar.ui.products.ProductsRoute

const val PRODUCTS_ROUTE = "products_route"

fun NavGraphBuilder.productsScreen(onProductClick: (String) -> Unit) {
    composable(
        route = PRODUCTS_ROUTE
    ) {
        ProductsRoute(onProductClick = onProductClick)
    }
}
