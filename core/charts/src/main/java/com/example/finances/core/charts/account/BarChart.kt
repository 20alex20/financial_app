package com.example.finances.core.charts.account

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.min

@Composable
fun BarChart(
    month: Int,
    values: List<Double>,
    modifier: Modifier = Modifier
) {
    val daysInMonth = values.size
    val maxAbsValue = values.maxOf { abs(it) }
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        val colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary,
            averageColor(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
        )
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val barWidth = 6.dp.toPx()
            val cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
            val spacing = (size.width - daysInMonth * barWidth) / (daysInMonth - 1)
            values.forEachIndexed { index, value ->
                val barHeight = if (maxAbsValue == 0.0) {
                    barWidth
                } else {
                    min((abs(value) / maxAbsValue * size.height).toFloat() + barWidth, size.height)
                }
                drawRoundRect(
                    topLeft = Offset(index * (barWidth + spacing), size.height - barHeight),
                    size = Size(barWidth, barHeight),
                    cornerRadius = cornerRadius,
                    color = when {
                        value > 0.0 -> colors[0]
                        value < 0.0 -> colors[1]
                        else -> colors[2]
                    }
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        ) {
            val monthStr = ".${month.toString().padStart(2, '0')}"
            val labels = listOf(
                "01$monthStr",
                ((daysInMonth + 1) / 2).toString().padStart(2, '0') + monthStr,
                daysInMonth.toString().padStart(2, '0') + monthStr
            )
            labels.forEach { label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

fun averageColor(color1: Color, color2: Color) = Color(
    red = (color1.red + color2.red) / 2f,
    green = (color1.green + color2.green) / 2f,
    blue = (color1.blue + color2.blue) / 2f,
    alpha = (color1.alpha + color2.alpha) / 2f
)
