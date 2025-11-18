package com.example.sporthelper.presentation.component

import androidx.compose.animation.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sporthelper.domain.model.BodyPartValue
import com.example.sporthelper.presentation.theme.SportHelperTheme
import com.example.sporthelper.presentation.util.changeLocalDateToGraphDate
import com.example.sporthelper.presentation.util.roundToDecimal
import java.time.LocalDate
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.drawscope.clipRect


@Composable
fun LineGraph(
    modifier: Modifier = Modifier,
    bodyPartValue: List<BodyPartValue>,
    pathAndCirclesWidth: Float = 5f,
    helperLinesColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    pathAndCirclesColor: Color = MaterialTheme.colorScheme.secondary,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    ),
) {
    val textMeasurer = rememberTextMeasurer()
    val dates = bodyPartValue.asReversed().map { it.date.changeLocalDateToGraphDate() }
    val dataPointValues = bodyPartValue.asReversed().map { it.value }

    val highestValue = dataPointValues.maxOrNull() ?: 0f
    val lowestValue = dataPointValues.minOrNull() ?: 0f
    val noOfparts = 3
    val difference = (highestValue - lowestValue) / noOfparts
    val valueList = listOf(
        highestValue.roundToDecimal(),
        (highestValue - difference).roundToDecimal(),
        (lowestValue + difference).roundToDecimal(),
        lowestValue.roundToDecimal()
    )

    val firstDate = dates.firstOrNull() ?: ""
    val date2 = dates.getOrNull((dates.size * 0.33).toInt()) ?: ""
    val date3 = dates.getOrNull((dates.size * 0.67).toInt()) ?: ""
    val lastDate = dates.lastOrNull() ?: ""
    val dateList = listOf(
        firstDate,
        date2,
        date3,
        lastDate
    )

    val animationProgress = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 3000
            )
        )
    }

    Canvas(
        modifier = modifier
    ) {
        val graphWidth = size.width
        val graphHeight = size.height
        val points = calculatePoints(
            dataPoints = dataPointValues,
            graphWidth = graphWidth,
            graphHeight = graphHeight
        )
        val path = createPath(points)
        val filledPath = createFilledPath(
            path = path,
            graphWidth = graphWidth,
            graphHeight = graphHeight
        )

        valueList.forEachIndexed { index, value ->
            val graph80PercentHeight = graphHeight * 0.8f
            val graph5PercentHeight = graphHeight * 0.05f
            val graph10PercentWidth = graphWidth * 0.1f
            val xPosition = 0f
            val yPosition = (graph80PercentHeight / noOfparts) * index
            drawText(
                textMeasurer = textMeasurer,
                text = "$value",
                style = textStyle,
                topLeft = Offset(x = xPosition, y = yPosition)
            )
            drawLine(
                color = helperLinesColor,
                strokeWidth = pathAndCirclesWidth,
                start = Offset(
                    x = xPosition + graph10PercentWidth,
                    y = (yPosition + graph5PercentHeight)
                ),
                end = Offset(x = graphWidth, y = (yPosition + graph5PercentHeight))
            )
        }
        dateList.forEachIndexed { index, date ->
            val graph10PercentWidth = graphWidth * 0.1f
            val graph77PercentWidth = graphWidth * 0.77f
            val xPosition = (graph77PercentWidth / noOfparts) * index + graph10PercentWidth
            val yPosition = graphHeight * 0.9f
            drawText(
                textMeasurer = textMeasurer,
                text = date,
                style = textStyle,
                topLeft = Offset(x = xPosition, y = yPosition)
            )
        }

        clipRect(
            right = graphWidth * animationProgress.value
        ) {
            points.forEach { point ->
                drawCircle(
                    color = pathAndCirclesColor,
                    radius = pathAndCirclesWidth,
                    center = point
                )
            }
            drawPath(
                path = path,
                color = pathAndCirclesColor,
                style = Stroke(
                    width = pathAndCirclesWidth
                )
            )
            if (dataPointValues.isNotEmpty()) {
                drawPath(
                    path = filledPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            pathAndCirclesColor.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
            }
        }
    }
}

private fun calculatePoints(
    dataPoints: List<Float>,
    graphWidth: Float,
    graphHeight: Float,
): List<Offset> {
    val graph90PercentWidth = graphWidth * 0.9f
    val graph10PercentWidth = graphWidth * 0.1f

    val graph80PercentHeight = graphHeight * 0.8f
    val graph5PercentHeight = graphHeight * 0.05f

    val highestValue = dataPoints.maxOrNull() ?: 0f
    val lowestValue = dataPoints.minOrNull() ?: 0f
    val valueRange = highestValue - lowestValue

    val xPositions = dataPoints.indices.map { index ->
        (graph90PercentWidth / (dataPoints.size - 1)) * index + graph10PercentWidth
    }
    val yPositions = dataPoints.map { value ->
        val normalizedValue = (value - lowestValue) / valueRange
        val yPosition = (graph80PercentHeight * (1 - normalizedValue)) + graph5PercentHeight
        yPosition
    }

    return xPositions.zip(yPositions) { x, y -> Offset(x, y) }
}

private fun createPath(points: List<Offset>): Path {
    val path = Path()
    if (points.isNotEmpty()) {
        path.moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            val currentPoint = points[i]
            val previousPoint = points[i - 1]
            val controlPointX = (previousPoint.x + currentPoint.x) / 2f
            path.cubicTo(
                controlPointX, previousPoint.y,
                controlPointX, currentPoint.y,
                currentPoint.x, currentPoint.y
            )
        }
    }
    return path
}

private fun createFilledPath(
    path: Path,
    graphWidth: Float,
    graphHeight: Float,
): Path {
    val filledPath = Path()
    val graph85PercentHeight = graphHeight * 0.85f
    val graph10PercentWidth = graphWidth * 0.1f

    filledPath.addPath(path)
    filledPath.lineTo(graphWidth, graph85PercentHeight)
    filledPath.lineTo(graph10PercentWidth, graph85PercentHeight)
    filledPath.close()
    return filledPath
}

@Preview(showBackground = true)
@Composable
private fun LineGraphPreview() {
    val dummyBodyPartValues = listOf(
        BodyPartValue(value = 72.0f, date = LocalDate.of(2023, 5, 10)),
        BodyPartValue(value = 76.84865145f, date = LocalDate.of(2023, 5, 1)),
        BodyPartValue(value = 74.0f, date = LocalDate.of(2023, 4, 20)),
        BodyPartValue(value = 75.1f, date = LocalDate.of(2023, 4, 5)),
        BodyPartValue(value = 66.3f, date = LocalDate.of(2023, 3, 15)),
        BodyPartValue(value = 67.2f, date = LocalDate.of(2023, 3, 10)),
        BodyPartValue(value = 73.5f, date = LocalDate.of(2023, 3, 1)),
        BodyPartValue(value = 69.8f, date = LocalDate.of(2023, 2, 18)),
        BodyPartValue(value = 68.4f, date = LocalDate.of(2023, 2, 1)),
        BodyPartValue(value = 72.0f, date = LocalDate.of(2023, 1, 22)),
        BodyPartValue(value = 70.5f, date = LocalDate.of(2023, 1, 14))
    )
    SportHelperTheme {
        LineGraph(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 2 / 1f)
                .padding(16.dp),
            bodyPartValue = dummyBodyPartValues
        )
    }

}