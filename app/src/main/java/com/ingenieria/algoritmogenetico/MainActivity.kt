package com.ingenieria.algoritmogenetico

import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var lienzo:Lienzo
    val points = ArrayList<Point>()
    var poblacion = 50

    var withDisplay = 0
    var heightDisplay = 0

    var displayFocus = false
    var play = false

    var obstaculosCon = 0

    var radioMeta = 100

    //array de obstaculos
    var obstaculos = ArrayList<Obstaculos>()
    var firstFocus = false

    lateinit var meta: Meta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //blue color bar notification
        window.statusBarColor = Color.parseColor("#000000")

        lienzo = Lienzo(this)
        linearPaint.addView(lienzo)

        fab_play.setOnClickListener {
            if (displayFocus && !play){
                lienzo.initObstaculos(obstaculos)
                Toast.makeText(this, "Init algorithm", Toast.LENGTH_SHORT).show()
                lienzo.start()
                fab_play.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_circle_filled_24))
                fab_play.labelText = "Pause"
                lienzo.initMetaPos(MetaPos(meta.mx, meta.my,meta.radioMeta.toFloat()))
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

        fab_reload.setOnClickListener {
            reload_app()
        }

        //listener deads
        lienzo.setDeadPointListener(object: Lienzo.deadPointListener{

            override fun deadPoint(dead: Int) {
               txt_muertos.text = "Muertos: $dead"
            }
        })

    }

    /**
     * reload app
     */
    private fun reload_app() {
        fab_play.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_circle_24))
        fab_play.labelText = "Play"
        play = false
        lienzo.stop()
        addPoints(poblacion)
        //addObstaculos()
        //addObstaculosExist()
        lienzo.invalidate()
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
            floatingButton.showMenu(true)
            //no empty
            if(edt_velocidad.text.toString().isNotEmpty() && edt_poblacion.text.toString().isNotEmpty() && edt_radio.text.toString().isNotEmpty() && edt_obstaculos.text.toString().isNotEmpty()){
                lienzo.velocidad = edt_velocidad.text.toString().toFloat()
                poblacion = edt_poblacion.text.toString().toInt()
                lienzo.radio = edt_radio.text.toString().toFloat()
                lienzo.obstaculos = edt_obstaculos.text.toString().toInt()

                addPoints(poblacion)
                //floatingButton.showMenu(true)
                //addObstaculos
                addObstaculos()
                addObstaculosView()
                lienzo.initObstaculos(obstaculos)
                addMeta()
                dialog.dismiss()
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

    private fun addObstaculosView() {
        linearPaint.removeAllViews()
        linearPaint.addView(lienzo)
        for (i in 0 until lienzo.obstaculos){
            obstaculos.add(Obstaculos(this, 100,100, 350, 100))
            val button = obstaculos[i].crearObstaculo()
            linearPaint.addView(button)
        }
    }

    /**
     * add obstaculos to array
     */
    private fun addObstaculos() {
        obstaculos.clear()
        for (i in 0 until lienzo.obstaculos){
            obstaculos.add(Obstaculos(this@MainActivity,
                100, 100, 300, 100))
            linearPaint.addView(obstaculos[i].crearObstaculo())
        }
    }

    /**
     * add obstaculos exist
     */
    private fun addObstaculosExist() {
        for (i in 0 until lienzo.obstaculos) {
            linearPaint.addView(obstaculos[i].crearObstaculo())
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus && !firstFocus){
            Toast.makeText(this, "focus", Toast.LENGTH_SHORT).show()
            firstFocus = true
            displayFocus = true
            withDisplay = linearPaint.width
            heightDisplay = linearPaint.height
            addPoints(poblacion)
            lienzo.setDisplay(withDisplay,heightDisplay)
            addObstaculos()
            meta = Meta(this, (withDisplay/2).toFloat(), radioMeta.toFloat(), radioMeta)
            addMeta()


            //addObstaculosView()
        }
    }


    /**
     * add meta
     */
    private fun addMeta() {
        linearPaint.addView(meta.createMeta())
    }

    /**
     * add point in linzo
     */
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

        lienzo.initPoints(points)
    }


}