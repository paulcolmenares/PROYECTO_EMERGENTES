package com.emergentes.pedidosdonvictor.ui.fragments.my_orders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emergentes.pedidosdonvictor.core.SharedPreferencesHelper
import com.emergentes.pedidosdonvictor.databinding.FragmentMyOrdersBinding
import com.emergentes.pedidosdonvictor.ui.fragments.my_orders.adapter.CarritoAdapter
import com.emergentes.pedidosdonvictor.ui.fragments.my_orders.adapter.OnCarritoItemRemovedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyOrdersFragment : Fragment(), OnCarritoItemRemovedListener {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private var _bindind: FragmentMyOrdersBinding? = null
    private val binding get() = _bindind!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarritoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {

        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        val carritoList = sharedPreferencesHelper.getCarritoList()

        // Setup
        recyclerView = binding.rvCarrito
        recyclerView.layoutManager = LinearLayoutManager(context)

        if(carritoList.isEmpty()){
            recyclerView.visibility = View.INVISIBLE
            binding.container.visibility= View.INVISIBLE
            binding.llVacio.visibility = View.VISIBLE

            recyclerView.adapter = null
        } else{
            recyclerView.visibility = View.VISIBLE
            binding.container.visibility= View.VISIBLE
            binding.llVacio.visibility = View.INVISIBLE

            adapter = CarritoAdapter(carritoList, this)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }


        // Mostrar resultados
        updatePrecio()

        // Pagar
        pagar()

    }

    override fun onItemRemoved() {
        updatePrecio()
    }

    private fun updatePrecio() {
        binding.tvSubTotalCarrito.text = "${sharedPreferencesHelper.getTotalSum()} Bs."
        binding.tvTotalCarrito.text = "${(sharedPreferencesHelper.getTotalSum() + 5)} Bs."
    }

    private fun pagar() {
        binding.btnPagarCarrito.setOnClickListener {
            val direccion = binding.etDireccion.text.toString()
            if (direccion.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Debe ingresar una dirección",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val db = FirebaseFirestore.getInstance()
                val user = FirebaseAuth.getInstance().currentUser
                val carritoList = sharedPreferencesHelper.getCarritoList()

                for (c in carritoList){
                    c.direccion = direccion
                    db.collection("usuarios").document(user?.email!!).collection("pedidos")
                    .add(c)
                    .addOnSuccessListener { documentReference ->
                        Log.w("Firestore", "Documentos subidos")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error adding document", e)
                    }
                }
                // Borrar datos guardados y actualizar
                Toast.makeText(
                    requireContext(),
                    "Pago realizado existosamente...",
                    Toast.LENGTH_SHORT
                ).show()
                binding.etDireccion.text?.clear()
                sharedPreferencesHelper.clearCart()
                val carritoList1 = sharedPreferencesHelper.getCarritoList()
                adapter = CarritoAdapter(carritoList1, this)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
                binding.tvSubTotalCarrito.text = "${sharedPreferencesHelper.getTotalSum()} Bs."
                binding.tvTotalCarrito.text = "${(sharedPreferencesHelper.getTotalSum() + 5)} Bs."

                // mostrar las vistas
                binding.container.visibility = View.INVISIBLE
                binding.llVacio.visibility = View.VISIBLE
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindind = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

}