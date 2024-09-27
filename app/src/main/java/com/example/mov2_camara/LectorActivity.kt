package com.example.mov2_camara

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.zxing.integration.android.IntentIntegrator

class LectorActivity : AppCompatActivity() {

    private lateinit var codigo: EditText
    private lateinit var descripcion: EditText
    private lateinit var fecha: EditText
    private lateinit var precio: EditText
    private lateinit var btnEscanear: Button
    private lateinit var btnBuscar : Button
    private lateinit var btnCapturar: Button
    private lateinit var btnLimpiar: Button

    private val registros = ArrayList<Registro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lector)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        codigo = findViewById(R.id.CodigoLeido)
        descripcion = findViewById(R.id.Descripcion)
        fecha = findViewById(R.id.Fecha)
        precio = findViewById(R.id.Precio)
        btnEscanear = findViewById(R.id.btnescanear)
        btnBuscar = findViewById(R.id.btnBuscar)
        btnCapturar = findViewById(R.id.btnCapturar)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        btnEscanear.setOnClickListener { escanearCodigo() }
        btnCapturar.setOnClickListener { capturarDatos() }
        btnBuscar.setOnClickListener {  buscarCodigos() }
        btnLimpiar.setOnClickListener { limpiar() }
    }

    private fun buscarCodigos() {
        val codigoBuscado = codigo.text.toString()

        if (codigoBuscado.isNotEmpty()) {
            val registroEncontrado = registros.find { it.codigo == codigoBuscado }

            if (registroEncontrado != null) {
                descripcion.setText(registroEncontrado.descripcion)
                fecha.setText(registroEncontrado.fecha)
                precio.setText(registroEncontrado.precio.toString())
                Toast.makeText(this, "Registro encontrado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registro no encontrado", Toast.LENGTH_SHORT).show()
                limpiar()
            }
        } else {
            Toast.makeText(this, "Debe ingresar un código para buscar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun capturarDatos() {
        if (codigo.text.toString().isNotEmpty() && descripcion.text.toString().isNotEmpty() &&
            fecha.text.toString().isNotEmpty() && precio.text.toString().isNotEmpty()) {

            val registro = Registro(
                codigo = codigo.text.toString(),
                descripcion = descripcion.text.toString(),
                fecha = fecha.text.toString(),
                precio = precio.text.toString().toDouble()
            )

            if (registros.size < 10) {
                registros.add(registro)
                Toast.makeText(this, "Datos capturados", Toast.LENGTH_SHORT).show()
                limpiar()
            } else {
                Toast.makeText(this, "Arreglo lleno, no se pueden agregar más registros", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Debe registrar todos los datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiar() {
        codigo.text.clear()
        descripcion.text.clear()
        fecha.text.clear()
        precio.text.clear()
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
                codigo.setText(result.contents)
                Toast.makeText(this, "Código escaneado: ${result.contents}", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    data class Registro(
        val codigo: String,
        val descripcion: String,
        val fecha: String,
        val precio: Double
    )
}