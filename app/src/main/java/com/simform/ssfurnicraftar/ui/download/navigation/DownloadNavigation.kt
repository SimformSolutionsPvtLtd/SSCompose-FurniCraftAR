package com.simform.ssfurnicraftar.ui.download.navigation

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.simform.ssfurnicraftar.ui.arview.ColorState
import com.simform.ssfurnicraftar.ui.download.DownloadRoute
import com.simform.ssfurnicraftar.utils.constant.Urls
import java.nio.file.Path

private const val DOWNLOAD_ROUTE_BASE = "download_route"
private const val PRODUCT_ID_ARG = "productId"
private const val MODEL_COLOR_ARG = "modelColor"
private const val DOWNLOAD_ROUTE =
    "$DOWNLOAD_ROUTE_BASE/{$PRODUCT_ID_ARG}?$MODEL_COLOR_ARG={$MODEL_COLOR_ARG}"
private const val DOWNLOAD_URL_PATTERN =
    "${Urls.MODEL}/{$PRODUCT_ID_ARG}?$MODEL_COLOR_ARG={$MODEL_COLOR_ARG}"

fun NavController.navigateToDownload(productId: String) {
    navigate("$DOWNLOAD_ROUTE_BASE/$productId")
}

fun NavGraphBuilder.downloadScreen(
    onDownloadComplete: (String, Path, ColorState) -> Unit
) {
    composable(
        route = DOWNLOAD_ROUTE,
        arguments = listOf(
            navArgument(PRODUCT_ID_ARG) { type = NavType.StringType }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DOWNLOAD_URL_PATTERN
                action = Intent.ACTION_VIEW
            }
        )
    ) {
        DownloadRoute(onDownloadComplete = onDownloadComplete)
    }
}

internal data class DownloadArgs(
    val productId: String,
    val modelColor: ColorState
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        productId = requireNotNull(savedStateHandle[PRODUCT_ID_ARG]),
        modelColor = ColorState.parseFrom(savedStateHandle.get<String>(MODEL_COLOR_ARG))
    )
}
