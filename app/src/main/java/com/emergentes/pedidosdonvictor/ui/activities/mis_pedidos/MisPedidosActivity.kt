package com.emergentes.pedidosdonvictor.ui.activities.mis_pedidos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.emergentes.pedidosdonvictor.databinding.ActivityMisPedidosBinding
import com.emergentes.pedidosdonvictor.ui.fragments.home.adapter.MisPedidosAdapter
import com.emergentes.pedidosdonvictor.ui.fragments.my_orders.models.Carrito
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class MisPedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMisPedidosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        val listaCarrito = ArrayList<Carrito>()
        val adapter = MisPedidosAdapter(listaCarrito)

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter

        db.collection("usuarios")
            .document(user?.email!!).collection("pedidos")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.i("firestore", error.message.toString())
                } else if (value != null && !value.isEmpty) {
                    for (dc: DocumentChange in value.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val nombre = dc.document.data["nombre"]?.toString()
                            val cantidadStr = dc.document.data["cantidad"]?.toString()
                            val imagen = dc.document.data["imagen"]?.toString()
                            val precioStr = dc.document.data["precio"]?.toString()
                            val totalStr = dc.document.data["total"]?.toString()

                            if (nombre != null && cantidadStr != null && imagen != null && precioStr != null && totalStr != null) {
                                try {
                                    val cantidad = cantidadStr.toInt()
                                    val precio = precioStr.toDouble()
                                    val total = totalStr.toDouble()

                                    val c = Carrito(
                                        nombre,
                                        imagen,
                                        precio,
                                        cantidad,
                                        total
                                    )
                                    listaCarrito.add(c)
                                } catch (e: NumberFormatException) {
                                    Log.e("firestore", "error parseo", e)
                                }
                            } else {
                                Log.w("firestore", "faltan campos: ${dc.document.id}")
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                    Log.i("firestore", listaCarrito.toString())
                }
            }


        binding.btnBackDetalle.setOnClickListener {
            finish()
        }

    }
}