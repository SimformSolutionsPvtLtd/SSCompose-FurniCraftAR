package com.simform.ssfurnicraftar.data.model

import androidx.annotation.StringRes
import com.simform.ssfurnicraftar.R

enum class Category(
    val slug: String,
    @StringRes val nameRes: Int
) {
    TABLE("table", R.string.category_table),
    CHAIR("chair", R.string.category_chair),
    BED("bed", R.string.category_bed),
    SOFA("sofa", R.string.category_sofa),
    DESK("desk", R.string.category_desk);

    companion object {
        fun valueOrNull(slug: String): Category? = entries.find { it.slug == slug }
    }
}
