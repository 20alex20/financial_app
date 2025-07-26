package com.example.finances.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.finances.core.R
import com.example.finances.core.ui.theme.LightArrowColor

enum class ListItemHeight(val value: Dp) {
    HIGH(70.dp), LOW(56.dp)
}

enum class ListItemColorScheme {
    SURFACE, PRIMARY
}

sealed interface ListItemTrail {
    data object LightArrow : ListItemTrail
    data object DarkArrow : ListItemTrail
    data class Custom(val customTrail: @Composable () -> Unit) : ListItemTrail
}

@Composable
fun ListItem(
    mainText: String,
    modifier: Modifier = Modifier,
    height: ListItemHeight = ListItemHeight.HIGH,
    colorScheme: ListItemColorScheme = ListItemColorScheme.SURFACE,
    paddingValues: PaddingValues = PaddingValues(16.dp, 0.dp),
    dividerEnabled: Boolean = true,
    emoji: String? = null,
    comment: String? = null,
    rightText: String? = null,
    additionalRightText: String? = null,
    trail: ListItemTrail? = null,
    onClick: (() -> Unit)? = null
) {
    val colors = getColorScheme(colorScheme)
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxWidth()
            .height(height.value)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = when (onClick) {
                null -> Modifier
                    .fillMaxSize()
                    .background(colors.background)
                    .padding(paddingValues)

                else -> Modifier
                    .fillMaxSize()
                    .background(colors.background)
                    .clickable { onClick() }
                    .padding(paddingValues)
            }
        ) {
            if (emoji != null) Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(colors.emojiBackground)
            ) {
                Text(
                    text = emoji,
                    style = when (emoji.matches(Regex("^[0-9a-zA-Zа-яА-Я]*$"))) {
                        true -> MaterialTheme.typography.bodySmall
                        false -> MaterialTheme.typography.bodyLarge
                    },
                    color = colors.emoji
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = mainText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.content,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                if (!comment.isNullOrEmpty()) Text(
                    text = comment,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.comment,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxHeight()
            ) {
                if (rightText != null) Text(
                    text = rightText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.rightText,
                    softWrap = false
                )
                if (additionalRightText != null) Text(
                    text = additionalRightText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.rightText,
                    softWrap = false
                )
            }

            when (trail) {
                null -> {}

                is ListItemTrail.LightArrow -> Icon(
                    painter = painterResource(R.drawable.light_arrow),
                    contentDescription = mainText,
                    tint = LightArrowColor,
                    modifier = Modifier.size(24.dp)
                )

                is ListItemTrail.DarkArrow -> Icon(
                    painter = painterResource(R.drawable.dark_arrow),
                    contentDescription = mainText,
                    tint = colors.darkArrow,
                    modifier = Modifier.size(24.dp)
                )

                is ListItemTrail.Custom -> trail.customTrail()
            }
        }

        if (dividerEnabled) HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

private data class ColorScheme(
    val background: Color,
    val emoji: Color,
    val emojiBackground: Color,
    val content: Color,
    val comment: Color,
    val rightText: Color,
    val darkArrow: Color
)

@Composable
private fun getColorScheme(colorSchemeType: ListItemColorScheme): ColorScheme {
    return if (colorSchemeType == ListItemColorScheme.SURFACE) ColorScheme(
        background = Color.Transparent,
        emoji = MaterialTheme.colorScheme.onSurface,
        emojiBackground = MaterialTheme.colorScheme.inverseSurface,
        content = MaterialTheme.colorScheme.onSurface,
        comment = MaterialTheme.colorScheme.onSurfaceVariant,
        rightText = MaterialTheme.colorScheme.onSurface,
        darkArrow = MaterialTheme.colorScheme.onSurfaceVariant
    ) else ColorScheme(
        background = MaterialTheme.colorScheme.primaryContainer,
        emoji = MaterialTheme.colorScheme.onPrimaryContainer,
        emojiBackground = MaterialTheme.colorScheme.inversePrimary,
        content = MaterialTheme.colorScheme.onPrimaryContainer,
        comment = MaterialTheme.colorScheme.onPrimary,
        rightText = MaterialTheme.colorScheme.onPrimaryContainer,
        darkArrow = MaterialTheme.colorScheme.onPrimary
    )
}
