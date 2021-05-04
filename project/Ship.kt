package com.example.project

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class Ship(var shipRadius:Float, var shipCanonLh:Float, var hauteur: Float, var largeur: Float, val view: CanonView ){
    val shipPaint = Paint()
    var shipCanonSt = PointF(shipCanonLh, hauteur)
    var ship = PointF(largeur,hauteur)


    fun draw(canvas: Canvas) {
        shipPaint.strokeWidth = largeur * 1.5f // C'est ici que le canon est dessiné
        canvas.drawLine(shipCanonSt.x,shipCanonSt.y, shipCanonSt.x,
                shipCanonSt.y, shipPaint)
        canvas.drawCircle(ship.x,ship.y, shipRadius,
                shipPaint)
    }
    fun setship(hauteur: Float, largeur: Float) {
        shipCanonSt.set(shipCanonLh, hauteur)
        ship.set(largeur,hauteur)
    }
   fun align(positionX: Float, positionY:Float){
       shipCanonSt.x = shipCanonLh + positionX
       shipCanonSt.y = shipCanonLh  +positionY
       ship.x = largeur + positionX
       ship.y = hauteur + positionY
   }
   fun getPositionX(): Float{
       return ship.x
   }
    fun getPositionY(): Float{
        return ship.y
    }
    fun update(positionX: Float, positionY:Float){
        ship.x = positionX
        ship.y = positionY
    }

}