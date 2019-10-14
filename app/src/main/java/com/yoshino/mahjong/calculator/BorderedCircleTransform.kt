package com.yoshino.mahjong.calculator

import android.graphics.*
import com.squareup.picasso.Transformation

class BorderedCircleTransform(
        private val borderColor: Int,
        private val borderSize: Int
) : Transformation {

    override fun transform(source: Bitmap?): Bitmap? {
        source ?: return null

        val size = Math.min(
                source.width,
                source.height
        )

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(
                source,
                x,
                y,
                size,
                size
        )

        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(
                size,
                size,
                source.config
        )

        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            shader = BitmapShader(
                    squaredBitmap,
                    Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP
            )
            isAntiAlias = true
        }

        val r = size / 2f
        Canvas(bitmap).drawCircle(
                r,
                r,
                r,
                paint
        )

        val paintBg = Paint().apply {
            color = borderColor
            isAntiAlias = true
        }

        canvas.drawCircle(
                r,
                r,
                r,
                paintBg
        )

        canvas.drawCircle(
                r,
                r,
                r - borderSize,
                paint
        )

        squaredBitmap.recycle()

        return bitmap
    }

    override fun key(): String = "bordered_circle"
}