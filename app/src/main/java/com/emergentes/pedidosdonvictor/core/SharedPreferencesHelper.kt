package com.emergentes.pedidosdonvictor.core

import android.content.Context
import android.content.SharedPreferences
import com.emergentes.pedidosdonvictor.ui.fragments.my_orders.models.Carrito
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.roundToInt

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("carrito_prefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val gson = Gson()

    fun saveCarritoList(carritoList: List<Carrito>) {
        val json = gson.toJson(carritoList)
        editor.putString("carrito_list", json)
        editor.apply()
    }

    fun getCarritoList(): List<Carrito> {
        val json = sharedPreferences.getString("carrito_list", null)
        if (json != null) {
            val type = object : TypeToken<List<Carrito>>() {}.type
            return gson.fromJson(json, type)
        }
        return emptyList()
    }

    fun addItem(carrito: Carrito) {
        val currentList = getCarritoList().toMutableList()
        currentList.add(carrito)
        saveCarritoList(currentList)
    }

    fun removeItem(position: Int) {
        val currentList = getCarritoList().toMutableList()
        if (position >= 0 && position < currentList.size) {
            currentList.removeAt(position)
            saveCarritoList(currentList)
        }
    }

    fun clearCart() {
        editor.remove("carrito_list")
        editor.apply()
    }

    fun getTotalSum(): Double {
        return getCarritoList().sumOf { it.total }.roundToInt().toDouble()
    }

}