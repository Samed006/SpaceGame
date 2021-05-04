package com.example.project




import android.graphics.*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.blue
import java.util.*
import java.util.Random
import kotlin.collections.ArrayList


class Enemies(var x1: Float, var y1: Float, val diametre: Float, canonView: CanonView) {
    val enemies = RectF(x1, y1, x1+diametre, y1+diametre)
    var enemiesHit: Boolean = false
    val paint = Paint()
    val random = Random()
    var color = Color.argb(255, random.nextInt(256),
            random.nextInt(256), random.nextInt(256))
    var enemiesSpeedX = 0f
    var enemiesSpeedY = 0f


    init {
        if (random.nextFloat() > 0.5) enemiesSpeedX= 1f else enemiesSpeedX = -1f
        if (random.nextFloat() < 0.5) enemiesSpeedY = 1f else enemiesSpeedY = -1f
    }
    fun setRect(){
        enemies.set(x1,y1, x1+diametre, y1+diametre)
    }


    fun draw(canvas: Canvas) {
        if (!enemiesHit) {
            paint.color = Color.BLUE
            canvas.drawOval(enemies, paint)
        }
    }



    fun bouge(lesParois: Array<Parois>, lesEnemies: ArrayList<Enemies>) {
        enemies.offset(5.0F * enemiesSpeedX, 5.0F * enemiesSpeedY)
        for (p in lesParois) {
            p.gereEnemies(this)
        }
        for (b in lesEnemies) {
            if (this !== b && RectF.intersects(enemies, b.enemies)) {
                b.rebondit()
                rebondit()

                break
            }
        }


    }
    fun rebondit() {
        enemiesSpeedX = -enemiesSpeedX
        enemiesSpeedY = -enemiesSpeedY
        enemies.offset(3.0F*enemiesSpeedX, 3.0F*enemiesSpeedY)
    }

        /*fun update(interval: Double) {
            var down = (interval * enemiesSpeed).toFloat()
            var R_L = (interval * enemiesSpeed).toFloat()
            val max = 300f
            val min = 000f
            var random: Double = min + Math.random() * (max - min)
            enemies.offset(R_L, (interval * random).toFloat())
            if (enemies.left < 0 || enemies.right > view.screenWidth + enemiesLength) {
                enemiesSpeed *= -1f
                R_L = (interval * 3 * enemiesSpeed).toFloat()
                enemies.offset(R_L, (interval * random).toFloat())
            }
            if (enemies.top < 0 || enemies.bottom > view.screenHeight) {
                enemiesSpeed *= -1f
                down = (interval * 3 * enemiesSpeed).toFloat()
                enemies.offset((interval * random).toFloat(), down)
            }

        }*/

        fun changeDirection(x: Boolean) {
            if (x) {
                this.enemiesSpeedY = -1 * enemiesSpeedY
            } else {
                this.enemiesSpeedX = -1 * enemiesSpeedX
            }
            enemies.offset(3.0f * enemiesSpeedX, 3.0f * enemiesSpeedY)
        }

    fun detectChoc(balle: Balle) {
// Le game over pour une seule touche (fenetre)
            this.enemiesHit = true
            balle.resetCanonBall()


    }

    /*fun changeCouleur() {
        color = Color.argb(255, random.nextInt(256),
                random.nextInt(256), random.nextInt(256))
    }*/


    }