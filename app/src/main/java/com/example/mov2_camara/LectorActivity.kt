package com.example.mov2_camara

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.integration.android.IntentIntegrator

class LectorActivity : AppCompatActivity() {

    private lateinit var codigo: EditText
    private lateinit var descripcion: EditText
    private lateinit var btnEscanear: Button
    private lateinit var btnCapturar: Button
    private lateinit var btnLimpiar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lector)

        //Asociar con componentes grafico
        codigo = findViewById(R.id.CodigoLeidoText)
        descripcion = findViewById(R.id.DescripcionText)
        btnEscanear = findViewById(R.id.btnescanear)
        btnCapturar = findViewById(R.id.btnCapturar)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        //Eventos
        btnEscanear.setOnClickListener { escanearCodigo() }
        btnCapturar.setOnClickListener {
            if(codigo.text.toString().isNotEmpty() && descripcion.text.toString().isNotEmpty()){
                Toast.makeText(this, "Datos capturados", Toast.LENGTH_SHORT).show()
                limpiar()
            } else{
                Toast.makeText(this, "Debe registrar datos", Toast.LENGTH_SHORT).show()
            }
        }
        btnLimpiar.setOnClickListener { limpiar() }


    }

    private fun limpiar() {
        codigo.text.clear()
        descripcion.text.clear()
    }

    private fun escanearCodigo() {
        val integrator = IntentIntegrator(this@LectorActivity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Lector de codigos")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_SHORT).show()
            } else {
                // Mostrar el código escaneado en el campo EditText
                codigo.setText(result.contents)
                Toast.makeText(this, "Código escaneado: ${result.contents}", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}