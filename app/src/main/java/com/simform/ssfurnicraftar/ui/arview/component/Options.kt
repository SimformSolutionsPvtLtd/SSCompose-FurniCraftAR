package com.simform.ssfurnicraftar.ui.arview.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.simform.ssfurnicraftar.R
import com.simform.ssfurnicraftar.ui.arview.ARViewUiState
import com.simform.ssfurnicraftar.ui.arview.ColorState
import com.simform.ssfurnicraftar.ui.component.ColorPicker
import com.simform.ssfurnicraftar.ui.theme.LocalDimens
import com.simform.ssfurnicraftar.utils.constant.Constants

@Composable
internal fun Options(
    modifier: Modifier = Modifier,
    arViewUiState: ARViewUiState,
    rotationEnabled: Boolean,
    onRotationToggle: () -> Unit,
    onShare: () -> Unit,
    onColorChange: (ColorState) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(LocalDimens.SpacingMedium)
    ) {
        RotationOption(
            rotationEnabled = rotationEnabled,
            onClick = onRotationToggle
        )

        ShareOption(
            modifier = Modifier.align(Alignment.TopEnd),
            onShare = onShare
        )

        ColorOption(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedColor = arViewUiState.modelColor,
            onSelect = onColorChange
        )
    }
}

@Composable
fun RotationOption(
    modifier: Modifier = Modifier,
    rotationEnabled: Boolean,
    onClick: () -> Unit,
) {
    val contentColor by animateColorAsState(
        targetValue = if (rotationEnabled) MaterialTheme.colorScheme.primary else Color.LightGray,
        label = "RotationIconColor"
    )

    Icon(
        modifier = modifier
            .size(LocalDimens.ARView.OptionsIconSize)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onClick() }
            .padding(LocalDimens.SpacingSmall),
        painter = painterResource(R.drawable.ic_rotate_360),
        contentDescription = stringResource(R.string.cd_rotate_360),
        tint = contentColor
    )
}

@Composable
private fun ShareOption(
    modifier: Modifier = Modifier,
    onShare: () -> Unit
) {
    Icon(
        modifier = modifier
            .size(LocalDimens.ARView.OptionsIconSize)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onShare() }
            .padding(LocalDimens.ARView.ShareOptionSpacing),
        imageVector = Icons.Default.Share,
        contentDescription = stringResource(R.string.share)
    )
}

@Composable
private fun ColorOption(
    modifier: Modifier = Modifier,
    selectedColor: ColorState,
    onSelect: (ColorState) -> Unit
) {
    var showPicker by rememberSaveable { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(LocalDimens.ARView.OptionsIconSize)
                .clip(CircleShape)
        ) {
            AnimatedVisibility(showPicker) {
                Row(
                    modifier = Modifier
                        .widthIn(
                            max = this@BoxWithConstraints.maxWidth - LocalDimens.ARView.OptionsIconSize
                        )
                        .padding(end = LocalDimens.SpacingSmall),
                    horizontalArrangement = Arrangement.spacedBy(LocalDimens.SpacingSmall)
                ) {
                    Button(
                        modifier = Modifier.size(LocalDimens.ARView.OptionsIconSize),
                        onClick = {
                            onSelect(ColorState.None)
                        },
                        contentPadding = PaddingValues(LocalDimens.NoSpacing)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = stringResource(R.string.reset_color)
                        )
                    }

                    DynamicColorButton(
                        isSelected = selectedColor is ColorState.Dynamic
                    ) {
                        onSelect(ColorState.Dynamic)
                    }

                    ColorPicker(
                        modifier = Modifier,
                        initialColor = (selectedColor as? ColorState.Color)?.value,
                        onSelect = { onSelect(ColorState.Color(it)) }
                    )
                }
            }

            Button(
                modifier = Modifier.size(LocalDimens.ARView.OptionsIconSize),
                onClick = { showPicker = !showPicker },
                contentPadding = PaddingValues(LocalDimens.NoSpacing)
            ) {
                val iconDescription = stringResource(R.string.color_picker)
                AnimatedContent(targetState = showPicker, label = Constants.ANIM_LABEL_COLOR_PICKER) { showPicker ->
                    if (showPicker) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.Default.Close,
                            contentDescription = iconDescription,
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.ic_color_picker),
                            contentDescription = iconDescription,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DynamicColorButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onSelect: () -> Unit
) {
    Icon(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .size(LocalDimens.ARView.OptionsIconSize)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = Color.Black,
                shape = CircleShape
            )
            .padding(LocalDimens.SpacingXS)
            .clickable { onSelect() },
        imageVector = Icons.Default.AutoAwesome,
        contentDescription = stringResource(R.string.dynamic_color),
        tint = MaterialTheme.colorScheme.onPrimary
    )

}
