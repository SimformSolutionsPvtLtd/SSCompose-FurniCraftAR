package com.simform.ssfurnicraftar.utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Resource helper is specially for [androidx.lifecycle.ViewModel] when we need resource but
 * we don't want to inject [Context] directly.
 */
interface ResourceHelper {
    /**
     * Returns [String] of string [id] from resource.
     */
    fun getString(@StringRes id: Int): String
}

/**
 * Implementation of [ResourceHelper].
 */
@Singleton
class ResourceHelperImpl @Inject constructor(@ApplicationContext private val context: Context) :
    ResourceHelper {
    override fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
}
