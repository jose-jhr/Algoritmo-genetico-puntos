package com.ingenieria.algoritmogenetico

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.lang.Math.random
import kotlin.math.cos
import kotlin.math.sin

class Lienzo: View {

    constructor(context: Context): super(context) {
        this.setBackgroundColor(Color.WHITE)
    }


    var points = ArrayList<Point>()

    var withDisplay = 0
    var heightDisplay = 0

    lateinit var thread: Thread
    var playthread = false

    var velocidad = 50f
    var radio = 10f
    var obstaculos = 1

    var pointsDeadCount = 0
    var pointBeastCount = 0

    var arrayListBestPosition = ArrayList<Point>()

    lateinit var metaPos: MetaPos

    //array list objetos obstaculos
    var arrayListColisiones = ArrayList<Obstaculos>()

    private lateinit var requestListener: deadPointListener

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        movePoints(canvas)
        //draw Button view
    }

    /**
     * funcion recibe points
     */
    fun initPoints(points: ArrayList<Point>){
        clearPoints()
        this.points.addAll(points)
    }

    /**
     * init meta pos
     */
    fun initMetaPos(metaPos: MetaPos){
        this.metaPos = metaPos
    }


    /**
     * clear points
     */
    fun clearPoints(){
        this.points.clear()
    }


    fun initObstaculos(obstaculos: ArrayList<Obstaculos>){
        clearObstaculos()
        this.arrayListColisiones.addAll(obstaculos)
    }

    fun clearObstaculos(){
        this.arrayListColisiones.clear()
    }


    private fun movePoints(canvas: Canvas) {
        if (points.size > 0) {
            for ((count,point) in points.withIndex()) {
                if (!point.dead){
                    if (testColisionDisplay(point)){
                        point.dead = true
                        pointsDeadCount++
                    }else{
                        if (testColisionObstaculos(point)){
                            point.dead = true
                            pointsDeadCount++
                        }else{
                            if (testColisionMeta(point)){
                                point.dead = true
                                pointBeastCount++
                                pointsDeadCount++
                                arrayListBestPosition.add(point)
                            }else {
                                canvas.drawCircle(point.x, point.y, radio, point.paint)
                                if (playthread) {
                                    point.move()
                                }
                            }
                        }
                    }
                }else{
                    canvas.drawCircle(point.x, point.y, radio, point.paint)
                }
            }
            if (pointsDeadCount == points.size){
                playthread = false
                Toast.makeText(context, "Todos los puntos murieron excepto $pointBeastCount", Toast.LENGTH_SHORT).show()
                pointsDeadCount = 0
                pointBeastCount = 0
            }
            requestListener.deadPoint(pointsDeadCount)
        }
    }

    private fun testColisionMeta(point: Point): Boolean {
        if (playthread) {
            if (point.x >= metaPos.x && point.x <= metaPos.x + metaPos.ratio) {
                if (point.y >= metaPos.y && point.y <= metaPos.y + metaPos.ratio) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * test colision obstaculos
     */
    private fun testColisionObstaculos(point: Point): Boolean {
        if (arrayListColisiones.size>0){
            for (obstaculo in arrayListColisiones){
                if (point.x > obstaculo.x_pos && point.x < obstaculo.x_pos + obstaculo.ancho_obs &&
                    point.y > obstaculo.y_pos && point.y < obstaculo.y_pos + obstaculo.alto_obs){
                    return true
                }
            }
        }
        return false
    }

    /**
     * comprubea si hace colision con el display
     */
    private fun testColisionDisplay(point: Point):Boolean {
        point.dead = !(point.x < withDisplay.toFloat() && point.y < heightDisplay.toFloat()
                && point.x > 0 && point.y > 0)
        return point.dead
    }

    private fun newPositionPoint() :PointPos{
        val angle = (0..360).random()
        //genera un radio al azar
        val radius = (0..100).random()
        //calcula la posicion x y y
        val x = (radius * cos(angle.toDouble())).toFloat()
        val y = (radius * sin(angle.toDouble())).toFloat()
        return PointPos(x,y)
    }

    /**
     * establecemos los limites de la pantalla
     */
    fun setDisplay(with: Int, height: Int) {
        this.withDisplay = with
        this.heightDisplay = height
    }

    //reload draw method thread
    fun reload() {
        (context as Activity).runOnUiThread {
            invalidate()
            requestLayout()
        }
    }

    //create thread reload
    fun start() {
        arrayListBestPosition.clear()
        if (!playthread) {
            playthread = true
            thread = Thread {
                while (playthread) {
                    reload()
                    Thread.sleep(velocidad.toLong())
                }
            }
            thread.start()
        }else{
            playthread = false
            thread.start()
        }
    }

    //stop thread reload
    fun stop() {
        playthread = false
        //thread.interrupt()
    }

    fun invalidata(){
        invalidate()
        requestLayout()
    }


    class PointPos(var x: Float, var y: Float)



    //create oyente with interface
    interface deadPointListener{
        fun deadPoint(dead:Int)
    }

    fun setDeadPointListener(deadPointListener: deadPointListener){
        this.requestListener = deadPointListener
    }






}