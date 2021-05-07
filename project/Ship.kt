package com.example.project

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class Ship(var shipRadius:Float, var shipCanonLh:Float, var hauteur: Float, var largeur: Float, val view: CanonView){
    val shipPaint = Paint()
    var shipCanonSt = PointF(shipCanonLh, hauteur)
    var ship = PointF(largeur,hauteur)
    var shipLife  = 0
    var shipHit =  false


    // Dessine le vaisseau
    fun draw(canvas: Canvas) {

        shipPaint.strokeWidth = largeur * 1.5f // C'est ici que le canon est dessiné
        canvas.drawCircle(ship.x,ship.y, shipRadius,
                shipPaint)
    }
    // Initialise la position initiale du vaisseau
    fun setship(hauteur: Float, largeur: Float) {
        ship.set(largeur,hauteur)
    }

    // Enregistre les nouvelles positions du vaisseau
   fun align(positionX: Float, positionY:Float){
       ship.x = largeur + positionX
       ship.y = hauteur + positionY
   }
   /*fun getPositionX(): Float{
       return ship.x
   }
    fun getPositionY(): Float{
        return ship.y
    }*/
    // Permet le vaisseau de bouger et verifie si elle se fait toucher
    fun update(enemiesWave: EnemiesWave){
        for(t in enemiesWave.vagueUne){
            if(ship.x + shipRadius > t.enemies.right &&
                    ship.x - shipRadius < t.enemies.left &&
                    ship.y - shipRadius < t.enemies.bottom
                    && ship.y + shipRadius > t.enemies.top){
                shipLife  = shipLife +1
                t.enemiesHit = true
                /*if(shipLife == 3){
                    shipHit = true
                    view.gameOver = true
                    view.drawing = false
                    view.showGameOverDialog(R.string.lose)
                }*/

            }
        }
    }

}