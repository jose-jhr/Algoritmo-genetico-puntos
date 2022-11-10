package com.ingenieria.algoritmogenetico

import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Button
import java.util.Arrays.fill

class Meta(val ctx: Context, var mx: Float, var my: Float, var radioMeta: Int) {


    var ceroX = 0f
    var ceroY = 0f


    fun createMeta():Button{
        val view = Button(ctx).apply {
            background = ctx.getDrawable(R.drawable.circle_drawable)
            x = mx-radioMeta/2
            y = my-radioMeta/2
        }

        val viewGroup = ViewGroup.LayoutParams(radioMeta,radioMeta)
        view.layoutParams = viewGroup


        view.setOnTouchListener { v, event ->

            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    ceroX = event.x
                    ceroY = event.y

                }
                MotionEvent.ACTION_MOVE -> {
                    val x = event.x.toInt()
                    val y = event.y.toInt()

                    val dx = x - ceroX
                    val dy = y - ceroY

                    mx += dx
                    my += dy

                    view.x = mx
                    view.y = my
                }
            }
            true
        }
        //view convert circle

        return view

    }

}