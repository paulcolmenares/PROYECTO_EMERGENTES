package com.emergentes.pedidosdonvictor.ui.fragments.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emergentes.pedidosdonvictor.databinding.FragmentHomeBinding
import com.emergentes.pedidosdonvictor.ui.fragments.home.adapter.ProductoAdapter
import com.emergentes.pedidosdonvictor.ui.fragments.home.models.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _bindind: FragmentHomeBinding? = null
    private val binding get() = _bindind!!
    private lateinit var listaProductos: ArrayList<Producto>
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()


    }

    private fun initUI() {

        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val recyclerView = binding.rv
        listaProductos = arrayListOf()

        // setup
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ProductoAdapter(listaProductos)
        recyclerView.adapter = adapter

        // Cargar informacion
        db.collection("productos")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.i("firestore", error.message.toString())
                } else if (value != null && !value.isEmpty) {
                    for (dc: DocumentChange in value.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            listaProductos.add(dc.document.toObject(Producto::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

        // Cargar info usuario
        binding.tvUserName.text = user!!.displayName
        Glide.with(requireContext()).load(user.photoUrl).into(binding.ivPerfil)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindind = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

}

