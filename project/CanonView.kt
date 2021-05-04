package com.example.project

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

class CanonView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback, Runnable {
    lateinit var canvas: Canvas
    val backgroundPaint = Paint()
    val textPaint = Paint()
    var screenWidth = 0f
    var screenHeight = 0f
    var drawing = false
    lateinit var thread: Thread
    val ship = Ship(6f, 3f, 3f, 3f, this)
    val balle = ArrayList<Balle>()
    var totalElapsedTime = 0.0
    var i: Int = 0
    lateinit var lesParois : Array<Parois>
    var lesEnemies = arrayListOf<Enemies>(Enemies(30f,800f,80f,this),Enemies(800f,30f,80f,this),Enemies(30f,30f,80f,this),Enemies(200f,200f,80f,this),Enemies(400f,400f,80f,this))

    init {
        backgroundPaint.color = Color.GRAY
        textPaint.textSize = screenWidth / 20
        textPaint.color = Color.BLACK
    }

    // Function thread qui sont obligatoirement presente
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w.toFloat()
        screenHeight = h.toFloat()
        // Initialise le canon
        ship.shipRadius = (h / 18f)
        ship.shipCanonLh = (w / 8f)
        ship.largeur = (w / 24f)
        ship.setship(h / 2f, w / 2f)
        //Initialise les balles
        for (b in balle) {
            b.canonballRadius = (w.toFloat())
            b.canonBallVitesse = (w.toFloat())
        }
        //Initialise les enemies
        for(b in lesEnemies){
            b.setRect()
        }
        // Initilalise les parois
        lesParois = arrayOf(Parois(5f,5f,25f,h.toFloat()),Parois(5f, 5f, w.toFloat(), 25f),
                Parois(5f, h.toFloat()-25f, w.toFloat(), h.toFloat() ),
                Parois(w.toFloat()-25f, 5f, w.toFloat() , h.toFloat()))


    }

    // Function thread qui sont obligatoirement presente
    override fun run() {
        var previousFrameTime = System.currentTimeMillis()
        while (drawing) {
            val currentTime = System.currentTimeMillis()
            val elapsedTimeMS = (currentTime - previousFrameTime).toDouble()
            totalElapsedTime += elapsedTimeMS / 1000.0
            updatePositions(elapsedTimeMS)
            draw()
            previousFrameTime = currentTime

        }
    }

    // Function thread qui sont obligatoirement presente
    override fun surfaceChanged(holder: SurfaceHolder, format: Int,
                                width: Int, height: Int) {
    }

    // Function thread qui sont obligatoirement presente
    override fun surfaceCreated(holder: SurfaceHolder) {}

    // Function thread qui sont obligatoirement presente
    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    fun pause() {
        drawing = false
        thread.join()
    }

    fun resume() {
        drawing = true
        thread = Thread(this)
        thread.start()
    }

    fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawRect(0f, 0f, canvas.width.toFloat(),
                    canvas.height.toFloat(), backgroundPaint)
            ship.draw(canvas)
            if (balle.size != 0) {
                for (b in balle) {
                    if (b.canonballOnScreen) {
                        b.draw(canvas)
                    }
                }
            }
            for (p in lesParois){
                p.draw(canvas)
            }
            for(t in lesEnemies){
                if(!t.enemiesHit) {
                    t.draw(canvas)
                    t.bouge(lesParois,lesEnemies)
                }
            }

            holder.unlockCanvasAndPost(canvas)
        }
    }


    override fun onTouchEvent(e: MotionEvent): Boolean {

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                ship.align(e.x, e.y)
                ship.update(e.x, e.y)
            }
            MotionEvent.ACTION_DOWN -> {
                fireball(e)

            }
        }

        return true
    }

    fun fireball(e: MotionEvent) {

        balle.add(Balle(this, lesEnemies))
        balle.get(i).launch(e.x, e.y)
        i += 1
    }

    fun updatePositions(elpsTimeMS: Double) {
        val interval = elpsTimeMS / 1000.0
       // enemies.update(interval)

        if (balle.size != 0) {
            for (b in balle) {
                b.update(interval)
            }

        }
    }
}
