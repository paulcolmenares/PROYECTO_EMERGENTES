package com.emergentes.pedidosdonvictor.ui.fragments.my_orders.models

data class Carrito(
    val nombre: String,
    val imagen: String,
    val precio: Double,
    val cantidad: Int,
    val total: Double
)
