package com.simform.ssfurnicraftar.utils.extension

import android.content.Context
import com.google.ar.core.Config.PlaneFindingMode
import com.simform.ssfurnicraftar.R
import com.simform.ssfurnicraftar.data.model.PlaneType

val PlaneType.arFindingMode: PlaneFindingMode
    get() = when (this) {
        PlaneType.HORIZONTAL -> PlaneFindingMode.HORIZONTAL
        PlaneType.VERTICAL -> PlaneFindingMode.VERTICAL
        PlaneType.HORIZONTAL_AND_VERTICAL -> PlaneFindingMode.HORIZONTAL_AND_VERTICAL
    }

fun PlaneFindingMode.getDescription(context: Context): String =
    when (this) {
        PlaneFindingMode.HORIZONTAL -> R.string.message_scan_horizontal_plane
        PlaneFindingMode.VERTICAL -> R.string.message_scan_vertical_plane
        else -> R.string.message_scan_any_plane
    }.let { res ->
        context.getString(res)
    }
