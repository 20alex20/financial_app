package com.example.financial_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.financial_app.ui.theme.TrailColor

sealed class Trail {
    data class LightArrow(val onClick: () -> Unit) : Trail()
    data class DarkArrow(val onClick: () -> Unit) : Trail()
    data class Custom(val customTrail: @Composable () -> Unit) : Trail()
}

data class Lead(val emoji: String?)

@Composable
fun ListItem(
    content: String,
    modifier: Modifier = Modifier,
    height: Dp = 70.dp,
    color: Color = MaterialTheme.colorScheme.surface,
    comment: String? = null,
    rightText: String? = null,
    lead: Lead? = null,
    trail: Trail? = null
) {
    val boxModifier = modifier
        .fillMaxWidth()
        .height(height)
        .background(color)
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = when (trail) {
            is Trail.LightArrow -> boxModifier.clickable { trail.onClick }
            is Trail.DarkArrow -> boxModifier.clickable { trail.onClick }
            else -> boxModifier
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp)
        ) {
            if (lead != null) Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.inverseSurface)
            ) {
                if (lead.emoji != null) Text(
                    text = lead.emoji,
                    style = MaterialTheme.typography.bodyLarge
                )
                else Text(
                    text = content.split(" ")
                        .slice(0..1)
                        .joinToString("") { it[0].uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
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
                    color = MaterialTheme.colorScheme.onSurface,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                if (comment != null) Text(
                    text = comment,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (rightText != null) Text(
                text = rightText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                softWrap = false
            )

            when (trail) {
                null -> {}

                is Trail.LightArrow -> Icon(
                    painter = painterResource(R.drawable.light_arrow),
                    contentDescription = content,
                    tint = TrailColor.Arrow.color,
                    modifier = Modifier.size(24.dp)
                )

                is Trail.DarkArrow -> Icon(
                    painter = painterResource(R.drawable.dark_arrow),
                    contentDescription = content,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )

                is Trail.Custom -> trail.customTrail()
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}
