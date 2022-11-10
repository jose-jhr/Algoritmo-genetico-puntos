package com.ingenieria.algoritmogenetico

import android.app.ActionBar
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var lienzo:Lienzo
    val points = ArrayList<Point>()
    var poblacion = 100

    var withDisplay = 0
    var heightDisplay = 0

    var displayFocus = false
    var play = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lienzo = Lienzo(this)
        linearPaint.addView(lienzo)

        fab_play.setOnClickListener {
            if (displayFocus && !play){
                Toast.makeText(this, "Init algorithm", Toast.LENGTH_SHORT).show()
                lienzo.start()
                fab_play.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_circle_filled_24))
                fab_play.labelText = "Pause"
                play = true
            }else{
                if(play) {
                    Toast.makeText(this, "Pause algorithm", Toast.LENGTH_SHORT).show()
                    lienzo.stop()
                    fab_play.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_circle_24))
                    fab_play.labelText = "Play"
                    play = false
                }
            }
        }

        fab_ajustes.setOnClickListener {
            floatingButton.hideMenu(true)
            alertFix()
        }


    }

    /**
     * alert fix settings
     */
    private fun alertFix() {
        val alert = android.app.AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.alert_fix_config, null)
        alert.setView(view)
        val dialog = alert.create()

        val btn_iniciar = view.findViewById<Button>(R.id.btn_iniciar)
        val edt_velocidad = view.findViewById<EditText>(R.id.edt_velocidad)
        val edt_poblacion = view.findViewById<EditText>(R.id.edt_poblacion)
        val edt_radio = view.findViewById<EditText>(R.id.edt_radio)
        val edt_obstaculos = view.findViewById<EditText>(R.id.edt_obstaculos)

        //default values
        edt_velocidad.setText(lienzo.velocidad.toString())
        edt_poblacion.setText(poblacion.toString())
        edt_radio.setText(lienzo.radio.toString())
        edt_obstaculos.setText(lienzo.obstaculos.toString())

        btn_iniciar.setOnClickListener {
            //no empty
            if(edt_velocidad.text.toString().isNotEmpty() && edt_poblacion.text.toString().isNotEmpty() && edt_radio.text.toString().isNotEmpty() && edt_obstaculos.text.toString().isNotEmpty()){
                lienzo.velocidad = edt_velocidad.text.toString().toFloat()
                poblacion = edt_poblacion.text.toString().toInt()
                lienzo.radio = edt_radio.text.toString().toFloat()
                lienzo.obstaculos = edt_obstaculos.text.toString().toInt()
                dialog.dismiss()
                addPoints(poblacion)
                //floatingButton.showMenu(true)
                floatingButton.showMenu(true)
            }else{
                Toast.makeText(this, "Complete todos los datos", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()

        //dimension dialog height wrap content
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height =  LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus){
            displayFocus = true
            withDisplay = linearPaint.width
            heightDisplay = linearPaint.height
            addPoints(poblacion)
            lienzo.setDisplay(withDisplay,heightDisplay)
        }
    }

    private fun addPoints(poblation: Int) {
        points.clear()
        //paint points
        val paintPoints =  Paint().apply {
            color = Color.BLUE
            strokeWidth = 5f
            style = Paint.Style.FILL_AND_STROKE
        }
        //init points
        for ( i in 0 until poblation){
            val point = Point((withDisplay/2).toFloat(),(heightDisplay-35).toFloat(),paintPoints,false)
            points.add(point)
        }
        lienzo.clearPoints()
        lienzo.initPoints(points)
    }


}