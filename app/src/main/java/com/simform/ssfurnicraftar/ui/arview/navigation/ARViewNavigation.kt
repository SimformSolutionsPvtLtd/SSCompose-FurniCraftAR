package com.simform.ssfurnicraftar.ui.arview.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.simform.ssfurnicraftar.ui.arview.ARViewRoute

internal const val PRODUCT_ID_ARG = "productId"
internal const val MODEL_COLOR_ARG = "modelColor"
const val ARVIEW_ROUTE_BASE = "arview_route"
const val ARVIEW_ROUTE = "$ARVIEW_ROUTE_BASE/{$PRODUCT_ID_ARG}?$MODEL_COLOR_ARG={$MODEL_COLOR_ARG}"

fun NavController.navigateToARView(productId: String, modelColor: Int? = null) {
    val baseRoute = "$ARVIEW_ROUTE_BASE/$productId"
    val route = modelColor?.let { "$baseRoute?$MODEL_COLOR_ARG=$modelColor" } ?: baseRoute
    navigate(route)
}

fun NavGraphBuilder.arViewScreen(
    onNavigateBack: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = ARVIEW_ROUTE,
        arguments = listOf(
            navArgument(PRODUCT_ID_ARG) { type = NavType.StringType },
            navArgument(MODEL_COLOR_ARG) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        ARViewRoute(onNavigateBack = onNavigateBack, onShowSnackbar = onShowSnackbar)
    }
}

internal data class ARViewArgs(
    val productId: String,
    val modelColor: Int?
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        productId = requireNotNull(savedStateHandle[PRODUCT_ID_ARG]),
        modelColor = (savedStateHandle.get<String>(MODEL_COLOR_ARG))?.toInt()
    )
}
