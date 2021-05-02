package com.example.project



import android.graphics.*
import kotlin.random.Random

class Enemies(var x1: Float,var y1: Float, var cote: Float, var enemiesSpeedInit: Float, var width : Float, var view: CanonView) {
    val ENE_NUM = 7
    val enemies = RectF(x1, y1, x1, y1 + width)
    var enemiesHit = BooleanArray(ENE_NUM)
    val paint = Paint()
    var color = Color.argb(255,255,255,255)
    var enemiesLength = 0f
    var enemiesHitNum = 0
    var enemiesSpeed = enemiesSpeedInit




    fun draw(canvas: Canvas){
        val currentPoint = PointF()
        currentPoint.x = enemies.left
        currentPoint.y = enemies.top
        for (i in 0 until ENE_NUM) {
            if (!enemiesHit[i]) {
                if (i % 2 != 0)
                    paint.color = Color.BLUE
                else
                    paint.color = Color.GREEN
                canvas.drawRect(currentPoint.x,currentPoint.y,currentPoint.x + enemiesLength ,enemies.bottom,
                        paint)
            }
            currentPoint.x += enemiesLength
        }
    }
    fun setRect() {
        enemies.set(x1, y1,
                 cote + x1, width)
        enemiesSpeed = enemiesSpeedInit
        enemiesLength = (cote-x1) / ENE_NUM
    }
    fun update(interval : Double){
        var down = (interval*enemiesSpeed).toFloat()
        var R_L = (interval*enemiesSpeed).toFloat()
        val max = 150f
        val min = -150f
        var random: Double = min + Math.random() *(max-min)
        enemies.offset(R_L, down)
        if(enemies.left < 0|| enemies.right > view.screenWidth + enemiesLength){
            enemiesSpeed *= -1f
            R_L = (interval * 3 * enemiesSpeed).toFloat()
            enemies.offset(R_L, (interval*random).toFloat())
        }
        if(enemies.top < 0|| enemies.bottom > view.screenHeight/2){
            enemiesSpeed *= -1f
            down = (interval * 3 * enemiesSpeed).toFloat()
            enemies.offset((interval*random).toFloat(), down)
        }

    }

    fun detectChoc(balle: Balle) {
        val section = ((balle.canonball.x - enemies.left) / enemiesLength).toInt()
        if (section >= 0 && section < ENE_NUM
                && !enemiesHit[section]) {
            enemiesHit[section] = true
            balle.resetCanonBall()


        }
    }

    fun changeColor(i:Int){
        when(i){
            1 -> color = Color.argb(255,255,0,0)
            2 -> color = Color.argb(255, 0, 0, 255)
            3 -> color = Color.argb(255,0,255,0)
        }
    }


    }
