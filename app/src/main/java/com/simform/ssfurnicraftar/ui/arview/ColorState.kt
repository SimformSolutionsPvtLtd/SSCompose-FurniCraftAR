package com.simform.ssfurnicraftar.ui.arview

import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.toArgb

sealed interface ColorState {

    data object None: ColorState

    data class Color(val value: ComposeColor): ColorState

    data object Dynamic: ColorState

    /**
     * Get string representation of color.
     *
     * It can be used to share in URL.
     * To convert back to the [ColorState] see [parseFrom].
     */
    val stringValue: String?
        get() = when(this) {
            is Color -> value.toArgb().toString()
            Dynamic -> DYNAMIC_COLOR
            None -> null
        }

    companion object {
        const val DYNAMIC_COLOR = "dynamic"

        /**
         * Parse [stringValue] and provide [ColorState].
         *
         * If it fails to parse given [value or the [value] is null,
         * [ColorState.None] will be returned as default value.
         *
         * @param value The [stringValue] to parse, it can be null.
         * @return Returns the parsed [ColorState] from [stringValue].
         */
        fun parseFrom(value: String?): ColorState {

            if (value == DYNAMIC_COLOR) return Dynamic

            val colorValue = value?.toIntOrNull()?.let { Color(ComposeColor(it)) }
            return colorValue ?: None
        }
    }
}
