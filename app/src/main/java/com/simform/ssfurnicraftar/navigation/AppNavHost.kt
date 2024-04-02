package com.simform.ssfurnicraftar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.simform.ssfurnicraftar.ui.AppState
import com.simform.ssfurnicraftar.ui.arview.navigation.arViewScreen
import com.simform.ssfurnicraftar.ui.arview.navigation.navigateToARView
import com.simform.ssfurnicraftar.ui.download.navigation.downloadScreen
import com.simform.ssfurnicraftar.ui.download.navigation.navigateToDownload
import com.simform.ssfurnicraftar.ui.products.navigation.PRODUCTS_ROUTE
import com.simform.ssfurnicraftar.ui.products.navigation.navigateToProducts
import com.simform.ssfurnicraftar.ui.products.navigation.productsScreen
import timber.log.Timber

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    appState: AppState,
    startDestination: String = PRODUCTS_ROUTE,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {

    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        productsScreen { productId ->
            Timber.d("Clicked: $productId")
            navController.navigateToDownload(productId)
        }

        downloadScreen { productId, _ ->
            navController.navigateToARView(productId)
        }

        arViewScreen {
            navController.navigateToProducts()
        }
    }
}
