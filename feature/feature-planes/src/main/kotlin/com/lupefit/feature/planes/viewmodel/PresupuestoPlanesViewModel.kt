package com.lupefit.feature.planes.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.ArrayList
import com.google.gson.reflect.TypeToken

class PresupuestoPlanesViewModel private constructor(private val storage: PlanesStorage) : ViewModel() {

    data class ItemPresupuesto(
        val nombre: String,
        val costoEstimado: Double,
        val cantidad: Int,
        val unidad: String,
        val tienePrecio: Boolean = true
    )

    private val _items = MutableLiveData<List<ItemPresupuesto>>(listOf())
    val items: LiveData<List<ItemPresupuesto>> = _items

    private val _metaSemanal = MutableLiveData<Double>(50000.0)
    val metaSemanal: LiveData<Double> = _metaSemanal

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _alertaPresupuesto = MutableLiveData<Boolean>(false)
    val alertaPresupuesto: LiveData<Boolean> = _alertaPresupuesto

    private val _itemsSinPrecio = MutableLiveData<Int>(0)
    val itemsSinPrecio: LiveData<Int> = _itemsSinPrecio

    private val _totalEstimado = MutableLiveData<Double>(0.0)
    val totalEstimado: LiveData<Double> = _totalEstimado

    private val _porcentajeMeta = MutableLiveData<Int>(0)
    val porcentajeMeta: LiveData<Int> = _porcentajeMeta

    init {
        _items.value = storage.read("presupuesto_items", object : TypeToken<List<ItemPresupuesto>>() {}.type,
            emptyList<ItemPresupuesto>())
        _metaSemanal.value = storage.read("presupuesto_meta", Double::class.javaObjectType, 50000.0)
        evaluarAlerta()
    }

    fun setMetaSemanal(meta: Double) {
        if (!meta.isFinite() || meta <= 0) return
        storage.write("presupuesto_meta", meta)
        _metaSemanal.value = meta
        evaluarAlerta()
    }

    fun agregarItem(item: ItemPresupuesto) {
        val lista = ArrayList<ItemPresupuesto>(_items.value ?: listOf())
        lista.add(item)
        saveItems(lista)
        evaluarAlerta()
    }

    fun eliminarItem(index: Int) {
        val lista = ArrayList<ItemPresupuesto>(_items.value ?: listOf())
        if (index in lista.indices) {
            lista.removeAt(index)
            saveItems(lista)
            evaluarAlerta()
        }
    }

    fun actualizarCosto(index: Int, nuevoCosto: Double) {
        val lista = ArrayList<ItemPresupuesto>(_items.value ?: listOf())
        if (index in lista.indices) {
            val item = lista[index]
            lista[index] = item.copy(costoEstimado = nuevoCosto, tienePrecio = true)
            saveItems(lista)
            evaluarAlerta()
        }
    }

    private fun evaluarAlerta() {
        val currentItems = _items.value ?: listOf()
        val total = currentItems.sumOf { it.costoEstimado * it.cantidad }
        val meta = _metaSemanal.value ?: 50000.0
        _totalEstimado.value = total
        _alertaPresupuesto.value = total > meta * 0.8
        _itemsSinPrecio.value = currentItems.count { !it.tienePrecio }
        _porcentajeMeta.value = if (meta > 0) (total / meta * 100).toInt() else 0
    }

    private fun saveItems(items: List<ItemPresupuesto>) {
        storage.write("presupuesto_items", items)
        _items.value = items
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PresupuestoPlanesViewModel::class.java)) {
                return PresupuestoPlanesViewModel(PlanesStorage(context)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}