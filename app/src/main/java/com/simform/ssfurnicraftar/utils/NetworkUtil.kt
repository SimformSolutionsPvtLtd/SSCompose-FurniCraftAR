package com.simform.ssfurnicraftar.utils

import timber.log.Timber
import kotlin.properties.Delegates

/**
 * Utility to help with network.
 */
object NetworkUtil {

    var isNetworkConnected by Delegates.observable(false) { _, _, newValue ->
        Timber.d(
            "Network status ${if (newValue) "CONNECTED" else "DISCONNECTED"}"
        )
    }
}
