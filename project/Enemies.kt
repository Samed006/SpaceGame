package com.example.project




import android.graphics.*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.blue
import java.util.*
import java.util.Random
import kotlin.collections.ArrayList


open class Enemies(var x1: Float, var y1: Float, val diametre: Float, var canonView: CanonView) {
    val enemies = RectF(x1, y1, x1+diametre, y1+diametre)
    var enemiesHit: Boolean = false
    val paint = Paint()
    val random = Random()
    var color = Color.argb(255, random.nextInt(256),
            random.nextInt(256), random.nextInt(256))
    var enemiesSpeedX = 0f
    var enemiesSpeedY = 0f


// Initialise les vitesses de façon aléatoire
    init {
        if (random.nextFloat() > 0.5) enemiesSpeedX= 1f else enemiesSpeedX = -1f
        if (random.nextFloat() < 0.5) enemiesSpeedY = 1f else enemiesSpeedY = -1f
    }
    // Initialise l'enemmie
    fun setRect(){
        enemies.set(x1,y1, x1+diametre, y1+diametre)
    }

// Dessine l'enemie si elle n'est pas touché
    fun draw(canvas: Canvas) {
        if (!enemiesHit) {
            paint.color = Color.BLUE
            canvas.drawRect(enemies, paint)
        }
    }


    //FOnction qui permet à l'enemie de bouger
    fun bouge(lesParois: Array<Parois>, vague : EnemiesWave) {
        enemies.offset(5.0F * enemiesSpeedX, 5.0F * enemiesSpeedY)
        for (p in lesParois) {
            p.gereEnemies(this)
        }
        for (b in vague.vagueUne) {
            if (this !== b && RectF.intersects(enemies, b.enemies)) {
                b.rebondit()
                rebondit()

                break
            }
        }


    }
    //les enemies rebondis etre eux
    fun rebondit() {
        enemiesSpeedX = -enemiesSpeedX
        enemiesSpeedY = -enemiesSpeedY
        enemies.offset(3.0F*enemiesSpeedX, 3.0F*enemiesSpeedY)
    }

    //Rebondi sur la parois
    fun changeDirection(x: Boolean) {
        if (x) {
            this.enemiesSpeedY = -1 * enemiesSpeedY
        } else {
            this.enemiesSpeedX = -1 * enemiesSpeedX
        }
        enemies.offset(3.0f * enemiesSpeedX, 3.0f * enemiesSpeedY)
        }

    // Fonction qui s'occupe de supprimer les enemies touché et d'ajouter du temps de jeu
    fun detectChoc( enemiesWave: EnemiesWave) {
// Le game over pour une seule touche (fenetre)
        this.enemiesHit = true

        canonView.increaseTimeLeft()
        enemiesWave.sendMessage()

        }

}






    /*fun changeCouleur() {
        color = Color.argb(255, random.nextInt(256),
                random.nextInt(256), random.nextInt(256))
    }*/


