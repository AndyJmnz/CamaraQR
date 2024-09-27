package com.example.mov2_camara

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FotoActivity : AppCompatActivity() {

    private lateinit var foto: ImageView
    private lateinit var btnTomar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foto)

        foto = findViewById(R.id.imgFoto)
        btnTomar = findViewById(R.id.btnTomar)

        btnTomar.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            responseLauncher.launch(intent)
        }
    }

    private val responseLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            Toast.makeText(this, "Fotografía tomada!!", Toast.LENGTH_SHORT).show()
            val extras = activityResult.data?.extras
            val imgBitmap = extras?.get("data") as Bitmap?
            foto.setImageBitmap(imgBitmap)
        }
    }
}