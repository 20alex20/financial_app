package com.example.financial_app.ui.components

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
import com.example.financial_app.R
import com.example.financial_app.ui.theme.LightArrowColor

sealed class Trail {
    data class LightArrow(val onClick: () -> Unit) : Trail()
    data class DarkArrow(val onClick: () -> Unit) : Trail()
    data class InvisibleButton(val onClick: () -> Unit) : Trail()
    data class Custom(val customTrail: @Composable () -> Unit) : Trail()
}

enum class ListItemHeight(val value: Dp) {
    HIGH(70.dp), LOW(56.dp)
}

enum class ListItemColorScheme {
    SURFACE, PRIMARY_CONTAINER
}

@Composable
fun ListItem(
    content: String,
    modifier: Modifier = Modifier,
    height: ListItemHeight = ListItemHeight.HIGH,
    colorScheme: ListItemColorScheme = ListItemColorScheme.SURFACE,
    comment: String? = null,
    rightText: String? = null,
    additionalRightText: String? = null,
    emoji: String? = null,
    trail: Trail? = null,
    dividerEnabled: Boolean = true,
    paddingValues: PaddingValues = PaddingValues(16.dp, 0.dp)
) {
    val colors = getColorScheme(colorScheme)

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxWidth()
            .height(height.value)
            .background(colors.background)

    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = when (trail) {
                is Trail.LightArrow -> Modifier
                    .fillMaxSize()
                    .clickable { trail.onClick() }
                    .padding(paddingValues)

                is Trail.DarkArrow -> Modifier
                    .fillMaxSize()
                    .clickable { trail.onClick() }
                    .padding(paddingValues)

                is Trail.InvisibleButton -> Modifier
                    .fillMaxSize()
                    .clickable { trail.onClick() }
                    .padding(paddingValues)

                else -> Modifier
                    .fillMaxSize()
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
                    text = content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.content,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                if (comment != null) Text(
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
                is Trail.LightArrow -> Icon(
                    painter = painterResource(R.drawable.light_arrow),
                    contentDescription = content,
                    tint = LightArrowColor,
                    modifier = Modifier.size(24.dp)
                )
                is Trail.DarkArrow -> Icon(
                    painter = painterResource(R.drawable.dark_arrow),
                    contentDescription = content,
                    tint = colors.darkArrow,
                    modifier = Modifier.size(24.dp)
                )
                is Trail.Custom -> trail.customTrail()
                else -> {}
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

internal data class ColorScheme(
    val background: Color,
    val emoji: Color,
    val emojiBackground: Color,
    val content: Color,
    val comment: Color,
    val rightText: Color,
    val darkArrow: Color
)

@Composable
internal fun getColorScheme(colorSchemeType: ListItemColorScheme): ColorScheme {
    if (colorSchemeType == ListItemColorScheme.SURFACE)
        return ColorScheme(
            background = MaterialTheme.colorScheme.surface,
            emoji = MaterialTheme.colorScheme.onSurface,
            emojiBackground = MaterialTheme.colorScheme.inverseSurface,
            content = MaterialTheme.colorScheme.onSurface,
            comment = MaterialTheme.colorScheme.onSurfaceVariant,
            rightText = MaterialTheme.colorScheme.onSurface,
            darkArrow = MaterialTheme.colorScheme.onSurfaceVariant
        )
    else
        return ColorScheme(
            background = MaterialTheme.colorScheme.primaryContainer,
            emoji = MaterialTheme.colorScheme.onPrimaryContainer,
            emojiBackground = MaterialTheme.colorScheme.inversePrimary,
            content = MaterialTheme.colorScheme.onPrimaryContainer,
            comment = MaterialTheme.colorScheme.onPrimary,
            rightText = MaterialTheme.colorScheme.onPrimaryContainer,
            darkArrow = MaterialTheme.colorScheme.onPrimary
        )
}
