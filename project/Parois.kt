package com.example.project

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Parois (x1: Float, y1: Float, x2: Float, y2: Float) {
    val r = RectF(x1, y1, x2, y2) // Initialisation des contours des parois avec la fonction prédefinie RectF
    val paint = Paint()


    fun draw(canvas: Canvas){
        paint.color = Color.CYAN
        canvas.drawRect(r,paint)
    }

    //Fonction qui verifie sur les enemies touche la parois pour les faire changer de direction
    fun gereEnemies( e :Enemies){
        if (RectF.intersects(r,e.enemies)) {
            if (r.width() > r.height()) {
                e.changeDirection (true)
            }
            else {
                e.changeDirection(false)
            }
        }
    }


    }
