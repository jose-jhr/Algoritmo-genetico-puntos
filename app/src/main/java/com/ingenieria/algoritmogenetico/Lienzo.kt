package com.ingenieria.algoritmogenetico

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
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
    var obstaculos = 2

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        movePoints(canvas)
    }

    /**
     * funcion recibe points
     */
    fun initPoints(points: ArrayList<Point>){
        this.points.addAll(points)
    }

    /**
     * clear points
     */
    fun clearPoints(){
        this.points.clear()
    }


    private fun movePoints(canvas: Canvas) {
        if (points.size > 0) {
            for ((count,point) in points.withIndex()) {
                if (!point.dead){
                    if (point.x < withDisplay.toFloat() && point.y < heightDisplay.toFloat() && point.x > 0 && point.y > 0) {
                        canvas.drawCircle(point.x, point.y, radio, point.paint)

                        val newPos = newPositionPoint()
                        //asigna la nueva posicion
                        this.points[count].x += newPos.x
                        this.points[count].y += newPos.y

                    } else {
                        if(withDisplay > 0 && heightDisplay > 0){
                            this.points[count].dead = true
                        }
                    }
                }else{
                    canvas.drawCircle(point.x, point.y, radio, point.paint)
                }
            }
        }
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



    class PointPos(var x: Float, var y: Float)



}