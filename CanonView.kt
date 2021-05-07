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
    val activity = context as FragmentActivity
    lateinit var lesParois : Array<Parois>
    var lesEnemies = arrayListOf<Enemies>(Enemies(30f,800f,80f,this),Enemies(800f,30f,80f,this),Enemies(30f,30f,80f,this),Enemies(200f,200f,80f,this),Enemies(400f,400f,80f,this))
    var timeLeft = 0.0
    var gameOver = false
    val MISS_PENALTY = 2
    val HIT_REWARD = 3
    val vagueUne = EnemiesWave(30f,800f,80f,this);

    init {
        backgroundPaint.color = Color.GRAY
        textPaint.textSize = screenWidth / 20
        textPaint.color = Color.BLACK
        timeLeft = 10.0
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
        for(b in vagueUne.vagueUne){
            b.setRect()
        }
        // Initilalise les parois
        lesParois = arrayOf(Parois(5f,5f,25f,h.toFloat()),Parois(5f, 5f, w.toFloat(), 25f),
                Parois(5f, h.toFloat()-25f, w.toFloat(), h.toFloat() ),
                Parois(w.toFloat()-25f, 5f, w.toFloat() , h.toFloat()))


        textPaint.setTextSize(w / 20f)
        textPaint.isAntiAlias = true

        newGame()


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
            val formatted = String.format("%.2f", timeLeft)
            canvas.drawText("Il reste $formatted secondes. ",
                    30f, 100f, textPaint)
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
            for(t in vagueUne.vagueUne){
                if(!t.enemiesHit) {
                    t.draw(canvas)
                    t.bouge(lesParois,vagueUne)
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

        balle.add(Balle(this, vagueUne))
        balle.get(i).launch(e.x, e.y)
        i += 1
    }

    fun updatePositions(elpsTimeMS: Double) {
        val interval = elpsTimeMS / 1000.0
       // enemies.update(interval)
        timeLeft -= interval

        if (balle.size != 0) {
            for (b in balle) {
                b.update(interval)
            }

        }
        if (timeLeft <= 0.0) {
            timeLeft = 0.0
            gameOver = true
            drawing = false
            showGameOverDialog(R.string.lose)
        }
    }
    fun showGameOverDialog(messageId: Int) {
        class GameResult: DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle(resources.getString(messageId))
                builder.setMessage(
                        resources.getString(
                                R.string.results_format, i, totalElapsedTime
                        )
                )
                builder.setPositiveButton(R.string.reset_game,
                        DialogInterface.OnClickListener { _, _->newGame()}
                )
                return builder.create()
            }
        }

        activity.runOnUiThread(
                Runnable {
                    val ft = activity.supportFragmentManager.beginTransaction()
                    val prev =
                            activity.supportFragmentManager.findFragmentByTag("dialog")
                    if (prev != null) {
                        ft.remove(prev)
                    }
                    ft.addToBackStack(null)
                    val gameResult = GameResult()
                    gameResult.setCancelable(false)
                    gameResult.show(ft,"dialog")
                }
        )
    }
    fun newGame() {
        vagueUne.resetEnemies()
        for(k in balle) {
            k.resetCanonBall()

        }
        timeLeft = 10.0
        i = 0
        totalElapsedTime = 0.0
        drawing = true
        if (gameOver) {
            gameOver = false
            thread = Thread(this)
            thread.start()
        }
    }

    fun reduceTimeLeft() {

        timeLeft -= MISS_PENALTY

    }

    fun increaseTimeLeft() {

        timeLeft += HIT_REWARD

    }

    fun gameOver() {
        drawing = false
        showGameOverDialog(R.string.win)
        gameOver = true
    }
}
