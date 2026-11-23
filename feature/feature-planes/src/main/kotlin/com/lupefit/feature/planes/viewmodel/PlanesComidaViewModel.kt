package com.lupefit.feature.planes.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.ArrayList
import com.google.gson.reflect.TypeToken

class PlanesComidaViewModel private constructor(private val storage: PlanesStorage) : ViewModel() {

    data class DiaPlan(
        val dia: Int,
        val nombre: String,
        val recetas: List<String> = listOf(),
        val completado: Boolean = false
    )

    data class SemanaPlan(
        val numeroSemana: Int,
        val dias: List<DiaPlan> = listOf()
    )

    private val _semanas = MutableLiveData<List<SemanaPlan>>(listOf())
    val semanas: LiveData<List<SemanaPlan>> = _semanas

    private val _semanaActual = MutableLiveData(
        storage.read("comidas_semana", Int::class.javaObjectType, 1).coerceIn(1, 52))
    val semanaActual: LiveData<Int> = _semanaActual

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _listaCompras = MutableLiveData<List<String>>(listOf())
    val listaCompras: LiveData<List<String>> = _listaCompras

    init {
        _semanas.value = storage.read("comidas", object : TypeToken<List<SemanaPlan>>() {}.type, emptyList<SemanaPlan>())
        generarListaCompras()
    }

    fun setSemanaActual(semana: Int) {
        if (semana in 1..52) {
            storage.write("comidas_semana", semana)
            _semanaActual.value = semana
            generarListaCompras()
        }
    }

    fun toggleDiaCompletado(numeroSemana: Int, dia: Int) {
        _cargando.value = true
        val listaActual = _semanas.value ?: ArrayList<SemanaPlan>()
        val nuevaLista = listaActual.map { semana ->
            if (semana.numeroSemana == numeroSemana) {
                val nuevosDias = semana.dias.map { d ->
                    if (d.dia == dia) d.copy(completado = !d.completado) else d
                }
                semana.copy(dias = nuevosDias)
            } else {
                semana
            }
        }
        save(nuevaLista)
        generarListaCompras()
        _cargando.value = false
    }

    fun agregarRecetaADia(numeroSemana: Int, dia: Int, receta: String) {
        val listaActual = _semanas.value ?: ArrayList<SemanaPlan>()
        val nuevaLista = listaActual.map { semana ->
            if (semana.numeroSemana == numeroSemana) {
                val nuevosDias = semana.dias.map { d ->
                    if (d.dia == dia) {
                        val nuevasRecetas = ArrayList<String>(d.recetas)
                        nuevasRecetas.add(receta)
                        d.copy(recetas = nuevasRecetas)
                    } else d
                }
                semana.copy(dias = nuevosDias)
            } else semana
        }
        save(nuevaLista)
        generarListaCompras()
    }

    private fun generarListaCompras() {
        val semana = _semanas.value?.firstOrNull { it.numeroSemana == _semanaActual.value }
        val compras = mutableListOf<String>()
        semana?.dias?.forEach { dia ->
            compras.addAll(dia.recetas)
        }
        _listaCompras.value = ArrayList(compras)
    }

    fun guardarSemana(semana: Int, nombres: List<String>, entradas: List<List<String>>) {
        require(semana in 1..52 && nombres.size == 7 && entradas.size == 7)
        val anterior = _semanas.value.orEmpty().find { it.numeroSemana == semana }
        val dias = nombres.mapIndexed { index, nombre ->
            val elementos = entradas[index]
            val previo = anterior?.dias?.getOrNull(index)
            DiaPlan(index + 1, nombre, elementos, previo?.let { it.recetas == elementos && it.completado } == true)
        }
        save((_semanas.value.orEmpty().filterNot { it.numeroSemana == semana } + SemanaPlan(semana, dias))
            .sortedBy { it.numeroSemana })
        setSemanaActual(semana)
    }

    private fun save(semanas: List<SemanaPlan>) {
        storage.write("comidas", semanas)
        _semanas.value = semanas
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlanesComidaViewModel::class.java)) {
                return PlanesComidaViewModel(PlanesStorage(context)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}