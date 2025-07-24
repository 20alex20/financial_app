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
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
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
                        value > 0 -> Color(0xFF00D26A)
                        value < 0 -> Color(0xFFFF5C00)
                        else -> Color(0xFFFFD600)
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
