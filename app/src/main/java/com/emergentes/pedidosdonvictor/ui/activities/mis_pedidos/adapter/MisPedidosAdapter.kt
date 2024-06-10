package com.emergentes.pedidosdonvictor.ui.fragments.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emergentes.pedidosdonvictor.R
import com.emergentes.pedidosdonvictor.ui.fragments.my_orders.models.Carrito

class MisPedidosAdapter(var carritoList: List<Carrito>) :
    RecyclerView.Adapter<MisPedidosAdapter.CarritoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarritoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CarritoViewHolder(layoutInflater.inflate(R.layout.card_carrito_pedidos, parent, false))
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = carritoList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = carritoList.size

    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageUrl = itemView.findViewById<ImageView>(R.id.ivCardCarritoPedido)
        var titulo = itemView.findViewById<TextView>(R.id.tvTituloCardCarritoPedido)
        var contador = itemView.findViewById<TextView>(R.id.tvContadorCarritoPedido)
        var totalPrecio = itemView.findViewById<TextView>(R.id.tvPrecioTotalCardCarritoPedido)

        fun render(carritoModel: Carrito) {
            titulo.text = carritoModel.nombre
            contador.text = "C: ${carritoModel.cantidad}"
            totalPrecio.text = "${carritoModel.total} Bs."
            Glide.with(imageUrl.context).load(carritoModel.imagen).into(imageUrl)
        }

    }

}