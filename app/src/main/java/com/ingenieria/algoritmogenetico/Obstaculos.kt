package com.ingenieria.algoritmogenetico

import android.content.Context
import android.graphics.Color.red
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlin.math.abs

class Obstaculos(val ctx:Context,var x_pos: Int, var y_pos: Int,
                 var ancho_obs: Int, var alto_obs: Int) {

    lateinit var view: Button

    var ceroX = 0
    var ceroY = 0


    fun crearObstaculo():Button{
        view = Button(ctx).apply {
            x = x_pos.toFloat()
            y = y_pos.toFloat()
            background = ctx.getDrawable(R.color.red)

        }
        val viewgGroup = ViewGroup.LayoutParams(ancho_obs,alto_obs)
        view.layoutParams = viewgGroup



        view.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    ceroX = event.x.toInt()
                    ceroY = event.y.toInt()
                }
                MotionEvent.ACTION_MOVE -> {

                    val x = event.x.toInt()
                    val y = event.y.toInt()

                    val dx = x - ceroX
                    val dy = y - ceroY

                    x_pos += dx
                    y_pos += dy
                    view.x = x_pos.toFloat()
                    view.y = y_pos.toFloat()
                }
            }

            //expandible view two touch
            if(event.pointerCount == 2){
                //get the distance between the two points
                val x = abs(event.getX(0) - event.getX(1))
                val y = abs(event.getY(0) - event.getY(1))
                ancho_obs = x.toInt()
                alto_obs = y.toInt()
                view.layoutParams.width = ancho_obs
                view.layoutParams.height = alto_obs
            }
            view.requestLayout()
            true
        }

        return view
    }

}