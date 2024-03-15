package com.simform.ssfurnicraftar.ui.arview.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.simform.ssfurnicraftar.ui.arview.ARViewRoute

internal const val PRODUCT_ID_ARG = "productId"
const val ARVIEW_ROUTE_BASE = "arview_route"
const val ARVIEW_ROUTE = "$ARVIEW_ROUTE_BASE/{$PRODUCT_ID_ARG}"

fun NavController.navigateToARView(productId: String) {
    navigate("$ARVIEW_ROUTE_BASE/$productId")
}

fun NavGraphBuilder.arViewScreen() {
    composable(
        route = ARVIEW_ROUTE,
        arguments = listOf(
            navArgument(PRODUCT_ID_ARG) { type = NavType.StringType }
        )
    ) {
        ARViewRoute()
    }
}

internal data class ARViewArgs(
    val productId: String
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        productId = requireNotNull(savedStateHandle[PRODUCT_ID_ARG])
    )
}
