package com.simform.ssfurnicraftar.data.model

import androidx.annotation.StringRes
import com.simform.ssfurnicraftar.R

enum class Category(
    val slug: String
) {
    TABLE("table"),
    CHAIR("chair"),
    BED("bed"),
    SOFA("sofa"),
    DESK("desk");

    companion object {
        fun valueOrNull(slug: String): Category? = entries.find { it.slug == slug }
    }
}
