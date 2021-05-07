package com.example.project

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF

class Balle(var view: CanonView, val enemies: EnemiesWave) {
    var canonball = PointF()
    var canonBallVitesse = 300f
    var canonBallVitesseX = 0f
    var canonballOnScreen = true
    var canonballRadius = 50f
    var canonballPaint = Paint()

    init {
        canonballPaint.color = Color.RED // It fix the color of the ball (here RED)
    }

    fun launch(positionX: Float, positionY: Float) {
        canonball.x = positionX
        canonball.y = positionY
        canonBallVitesseX = canonBallVitesse.toFloat()
        canonballOnScreen = true
    }

    fun update(interval: Double) {
        if (canonballOnScreen) {
            canonball.y -= (interval * canonBallVitesseX).toFloat()

            if (canonball.y + canonballRadius > view.screenHeight
                    || canonball.y - canonballRadius < 0) {
                canonballOnScreen = false
            }else {
            for (t in enemies.vagueUne) {
                if (canonball.x + canonballRadius > t.enemies.right &&
                        canonball.x - canonballRadius < t.enemies.left &&
                        canonball.y - canonballRadius < t.enemies.bottom
                        && canonball.y + canonballRadius > t.enemies.top) {
                    view.reduceTimeLeft()
                    t.detectChoc(this, enemies )
                    
                }
            }
        }}}
        fun resetCanonBall() {
            canonballOnScreen = false
        }

        fun draw(canvas: Canvas) {
            canvas.drawCircle(canonball.x, canonball.y, canonballRadius,
                    canonballPaint)
        }
    }