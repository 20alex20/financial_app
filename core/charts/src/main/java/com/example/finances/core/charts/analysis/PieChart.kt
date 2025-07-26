package com.example.finances.core.charts.analysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import com.example.finances.core.charts.analysis.models.AnalysisRecord
import com.example.finances.core.charts.analysis.models.ShortAnalysisRecord
import kotlin.math.roundToInt

@Composable
fun PieChart(
    data: List<ShortAnalysisRecord>,
    modifier: Modifier = Modifier,
    maxItemsInLegend: Int = 6
) {
    val sortedData = data.sortedByDescending { it.percent }
    val total = sortedData.sumOf { it.percent }

    val displayData = mutableListOf<AnalysisRecord>()
    var sumOthers = 0.0
    sortedData.forEachIndexed { index, (name, percent) ->
        if (sortedData.size <= maxItemsInLegend || index < maxItemsInLegend - 1) {
            displayData.add(AnalysisRecord(name, percent, colorFromName(name)))
        } else {
            sumOthers += percent
        }
    }
    if (sumOthers > 0)
        displayData.add(AnalysisRecord("Другое", sumOthers, colorFromName("Другое")))

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val style = Stroke(width = 8.dp.toPx())
            val diameter = size.minDimension
            val size = Size(diameter, diameter)
            val centerOffset = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)

            var startAngle = -90f
            displayData.forEach { (_, percent, color) ->
                val sweepAngle = (percent / total * 360.0).toFloat()
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = centerOffset,
                    size = size,
                    style = style
                )
                startAngle += sweepAngle
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(4.dp)
        ) {
            displayData.forEach { (name, percent, color) ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(color, shape = CircleShape)
                    )
                    Text(
                        text = if (percent < 1.0) "<1% $name" else "${percent.roundToInt()}% $name",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

fun colorFromName(name: String): Color {
    val hash = name.hashCode()
    val hue = (hash and 0xFFFFFF) % 360
    return Color.hsv(hue.toFloat(), 0.8f, 0.9f)
}
