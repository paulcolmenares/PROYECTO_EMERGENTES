package com.emergentes.pedidosdonvictor.ui.fragments.my_orders.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emergentes.pedidosdonvictor.R
import com.emergentes.pedidosdonvictor.core.SharedPreferencesHelper
import com.emergentes.pedidosdonvictor.ui.fragments.my_orders.models.Carrito

class CarritoAdapter(var carritoList: List<Carrito>, private val itemRemovedListener: OnCarritoItemRemovedListener) :
    RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarritoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CarritoViewHolder(layoutInflater.inflate(R.layout.card_carrito, parent, false))
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = carritoList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = carritoList.size

    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var sharedPreferencesHelper: SharedPreferencesHelper =
            SharedPreferencesHelper(itemView.context)

        var imageUrl = itemView.findViewById<ImageView>(R.id.ivCardCarrito)
        var titulo = itemView.findViewById<TextView>(R.id.tvTituloCardCarrito)
        var precio = itemView.findViewById<TextView>(R.id.tvPrecioCardCarrito)
        var contador = itemView.findViewById<TextView>(R.id.tvContadorCarrito)
        var totalPrecio = itemView.findViewById<TextView>(R.id.tvPrecioTotalCardCarrito)
        var btnDelete = itemView.findViewById<ImageView>(R.id.btnDeleteCarrito)

        fun render(carritoModel: Carrito) {
            titulo.text = carritoModel.nombre
            precio.text = "P/U: ${carritoModel.precio} Bs."
            contador.text = "C: ${carritoModel.cantidad}"
            totalPrecio.text = "${carritoModel.total} Bs."
            Glide.with(imageUrl.context).load(carritoModel.imagen).into(imageUrl)

            btnDelete.setOnClickListener {
                // Borrar del recycler
                carritoList = carritoList.toMutableList().apply {
                    removeAt(position)
                }
                // Borrar del carrito
                sharedPreferencesHelper.removeItem(position)
                notifyItemRemoved(position)
                notifyItemChanged(position, carritoList.size)
                // Actualizar
                itemRemovedListener.onItemRemoved()
            }

        }

    }

}