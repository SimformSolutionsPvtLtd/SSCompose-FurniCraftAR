package com.simform.ssfurnicraftar.data.utils

import kotlinx.coroutines.flow.Flow

/**
 * Utility for reporting app connectivity status
 */
interface NetworkMonitor {

    val isOnline: Flow<Boolean>
}
