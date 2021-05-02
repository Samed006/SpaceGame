package com.example.project

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Parois (x1: Float, y1: Float, x2: Float, y2: Float) {
    val r = RectF(x1, y1, x2, y2) // Initialisation des contours des parois avec la fonction prédefinie RectF
    val paint = Paint()

    fun draw(canvas: Canvas){
        paint.color = Color.GREEN

        canvas.drawRect(r,paint)

    }


    }
