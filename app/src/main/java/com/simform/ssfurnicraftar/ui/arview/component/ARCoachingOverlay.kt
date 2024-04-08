package com.simform.ssfurnicraftar.ui.arview.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.ar.core.TrackingFailureReason
import com.simform.ssfurnicraftar.R
import com.simform.ssfurnicraftar.ui.theme.LocalDimens
import io.github.sceneview.ar.getDescription

@Composable
internal fun ARCoachingOverlay(
    modifier: Modifier = Modifier,
    failureReason: TrackingFailureReason
) {
    val failureMessage = failureReason.getDescription(LocalContext.current)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ar_coaching_overlay))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = modifier
            .wrapContentHeight()
            .padding(LocalDimens.SpacingLarge)
    ) {
        Text(
            text = failureMessage,
            style = MaterialTheme.typography.titleLarge
        )

        LottieAnimation(
            composition = composition,
            progress = { progress }
        )
    }
}
