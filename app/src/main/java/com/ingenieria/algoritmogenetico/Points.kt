package com.ingenieria.algoritmogenetico

import android.graphics.Paint
import kotlin.math.cos
import kotlin.math.sin

class Point(var x: Float, var y: Float, val paint: Paint, var dead: Boolean) {
    fun move() {
        val newPos = newPositionPoint()
        x += newPos.x
        y += newPos.y
    }

    private fun newPositionPoint() : Lienzo.PointPos {
        val angle = (0..360).random()
        //genera un radio al azar
        val radius = (0..80).random()
        //calcula la posicion x y y
        val x = (radius * cos(angle.toDouble())).toFloat()
        val y = (radius * sin(angle.toDouble())).toFloat()
        return Lienzo.PointPos(x, y)
    }

}