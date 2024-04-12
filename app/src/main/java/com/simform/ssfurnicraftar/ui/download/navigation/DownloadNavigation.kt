package com.simform.ssfurnicraftar.ui.download.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.simform.ssfurnicraftar.ui.download.DownloadRoute
import java.nio.file.Path

private const val DOWNLOAD_ROUTE_BASE = "download_route"
private const val PRODUCT_ID_ARG = "productId"
private const val DOWNLOAD_ROUTE = "$DOWNLOAD_ROUTE_BASE/{$PRODUCT_ID_ARG}"

fun NavController.navigateToDownload(productId: String) {
    navigate("$DOWNLOAD_ROUTE_BASE/$productId")
}

fun NavGraphBuilder.downloadScreen(
    onDownloadComplete: (String, Path) -> Unit
) {
    composable(
        route = DOWNLOAD_ROUTE,
        arguments = listOf(
            navArgument(PRODUCT_ID_ARG) { type = NavType.StringType }
        )
    ) {
        DownloadRoute(onDownloadComplete = onDownloadComplete)
    }
}

internal data class DownloadArgs(
    val productId: String
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        productId = requireNotNull(savedStateHandle[PRODUCT_ID_ARG])
    )
}
