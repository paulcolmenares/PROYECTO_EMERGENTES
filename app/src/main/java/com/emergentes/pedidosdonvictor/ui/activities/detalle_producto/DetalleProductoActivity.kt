package com.emergentes.pedidosdonvictor.ui.activities.detalle_producto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.emergentes.pedidosdonvictor.core.SharedPreferencesHelper
import com.emergentes.pedidosdonvictor.databinding.ActivityDetalleProductoBinding
import com.emergentes.pedidosdonvictor.ui.fragments.my_orders.models.Carrito

class DetalleProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleProductoBinding
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var menosTv: TextView
    private lateinit var masTv: TextView
    private lateinit var contadorTv: TextView
    private lateinit var totalTv: TextView
    private var contador = 0
    private var precioGlobal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        menosTv = binding.tvMenosDetalle
        masTv = binding.tvMasDetalle
        contadorTv = binding.tvContadorDetalle
        totalTv = binding.tvTotalDetalle

        val id = intent.getStringExtra("id")
        val nombre = intent.getStringExtra("nombre")
        val precio = intent.getStringExtra("precio")
        val descripcion = intent.getStringExtra("descripcion")
        val image = intent.getStringExtra("imagen")


        precioGlobal = precio!!.toDouble()
        binding.tvNombreDetalle.text = nombre
        binding.tvPrecioDetalle.text = precio + " Bs."
        binding.tvDescripcionDetalle.text = descripcion
        Glide.with(this).load(image).into(binding.ivDetalle)

        carrito()

        binding.btnAddCar.setOnClickListener {
            if (contador != 0){
                val newItem = Carrito(
                    nombre = nombre!!,
                    imagen = image!!,
                    precio = precio.toDouble(),
                    cantidad = contador,
                    total = precio.toDouble() * contador
                )
                sharedPreferencesHelper.addItem(newItem)
                finish()
            } else{
                Toast.makeText(this, "Debes agregar algo por lo menos...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBackDetalle.setOnClickListener {
            finish()
        }

    }

    private fun carrito() {
        masTv.setOnClickListener {
            contador++
            contadorTv.text = contador.toString()
            totalTv.text = "${contador * precioGlobal} Bs."
        }
        menosTv.setOnClickListener {
            if (contador > 0){
                contador--
            }
            contadorTv.text = contador.toString()
            totalTv.text = "${contador * precioGlobal} Bs."
        }
    }
}



