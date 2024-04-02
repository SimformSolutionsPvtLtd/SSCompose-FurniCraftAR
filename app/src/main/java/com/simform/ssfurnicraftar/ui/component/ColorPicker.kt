package com.simform.ssfurnicraftar.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.simform.ssfurnicraftar.ui.theme.LocalDimens
import com.simform.ssfurnicraftar.utils.saver.ColorSaver

/**
 * Default colors to show in picker
 */
private val colors = with(Color) {
    listOf(Red, Green, Blue, Yellow, Black, Gray, White, Cyan, Magenta)
}

/**
 * Color picker to pick one color from [colors]
 *
 * [onSelect] will be called when color is selected.
 */
@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    initialColor: Color? = null,
    onSelect: (Color) -> Unit
) {
    var currentColor by rememberSaveable(initialColor, stateSaver = ColorSaver) {
        mutableStateOf(initialColor)
    }

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(LocalDimens.SpacingSmall)
    ) {
        colors.forEach { color ->
            val isSelected = currentColor == color
            Box(
                modifier = Modifier
                    .padding(LocalDimens.NoSpacing)
                    .size(LocalDimens.ARView.OptionsIconSize)
                    .clip(CircleShape)
                    .border(
                        width = with(LocalDimens.ColorPicker) {
                            if (isSelected) SelectionBorderSize else NoSelectionBorderSize
                        },
                        color = Color.Black,
                        shape = CircleShape
                    )
                    .background(color)
                    .clickable {
                        currentColor = color
                        onSelect(color)
                    }
            )
        }
    }
}
