package com.start.STart.ui.theme

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.shadow(
    color: Color = Color.Black,
    topLeftRadius: Dp = 0.dp,
    topRightRadius: Dp = 0.dp,
    bottomRightRadius: Dp = 0.dp,
    bottomLeftRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawSpecificCornerRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                topLeftRadius = topLeftRadius.toPx(),
                topRightRadius = topRightRadius.toPx(),
                bottomRightRadius = bottomRightRadius.toPx(),
                bottomLeftRadius = bottomLeftRadius.toPx(),
                paint
            )
        }
    }
)

fun Canvas.drawSpecificCornerRoundRect(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    topLeftRadius: Float,
    topRightRadius: Float,
    bottomRightRadius: Float,
    bottomLeftRadius: Float,
    paint: Paint
) {
    val path = Path()

    val topLeftRect = Rect(left, top, left + 2 * topLeftRadius, top + 2 * topLeftRadius)
    val topRightRect = Rect(right - 2 * topRightRadius, top, right, top + 2 * topRightRadius)
    val bottomRightRect = Rect(right - 2 * bottomRightRadius, bottom - 2 * bottomRightRadius, right, bottom)
    val bottomLeftRect = Rect(left, bottom - 2 * bottomLeftRadius, left + 2 * bottomLeftRadius, bottom)

    path.moveTo(left + topLeftRadius, top)
    path.lineTo(right - topRightRadius, top)
    path.arcTo(topRightRect, -90f, 90f, false)
    path.lineTo(right, bottom - bottomRightRadius)
    path.arcTo(bottomRightRect, 0f, 90f, false)
    path.lineTo(left + bottomLeftRadius, bottom)
    path.arcTo(bottomLeftRect, 90f, 90f, false)
    path.lineTo(left, top + topLeftRadius)
    path.arcTo(topLeftRect, 180f, 90f, false)
    path.close()

    drawPath(path, paint)
}