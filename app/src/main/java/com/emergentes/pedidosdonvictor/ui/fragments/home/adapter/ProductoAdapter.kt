package com.emergentes.pedidosdonvictor.ui.fragments.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emergentes.pedidosdonvictor.R
import com.emergentes.pedidosdonvictor.ui.activities.detalle_producto.DetalleProductoActivity
import com.emergentes.pedidosdonvictor.ui.fragments.home.models.Producto

class ProductoAdapter(private val productosList: ArrayList<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ProductosViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductosViewHolder(layoutInflater.inflate(R.layout.card_producto, parent, false))
    }

    override fun onBindViewHolder(holder: ProductosViewHolder, position: Int) {
        val item = productosList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = productosList.size

    inner class ProductosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageUrl = itemView.findViewById<ImageView>(R.id.ivCard)
        var id = itemView.findViewById<TextView>(R.id.tvIdCard)
        var titulo = itemView.findViewById<TextView>(R.id.tvTituloCard)
        var estado = itemView.findViewById<TextView>(R.id.tvEstadoCard)
        var precio = itemView.findViewById<TextView>(R.id.tvPrecioCard)
        var descripcion = itemView.findViewById<TextView>(R.id.tvDescripcionCard)

        fun render(productosModel: Producto) {
            id.text = productosModel.id
            titulo.text = productosModel.nombre
            estado.text = productosModel.estado
            precio.text = "${productosModel.precio} Bs."
            descripcion.text = productosModel.descripcion
            Glide.with(imageUrl.context).load(productosModel.imagen).into(imageUrl)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetalleProductoActivity::class.java)
                intent.putExtra("id", productosModel.id)
                intent.putExtra("nombre", productosModel.nombre)
                intent.putExtra("precio", productosModel.precio)
                intent.putExtra("descripcion", productosModel.descripcion)
                intent.putExtra("imagen", productosModel.imagen)
                itemView.context.startActivity(intent)
            }
        }
    }

}