package com.lupefit.feature.planes.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.ArrayList

class PlanesComidaViewModel : ViewModel() {

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

    private val _semanaActual = MutableLiveData<Int>(1)
    val semanaActual: LiveData<Int> = _semanaActual

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _listaCompras = MutableLiveData<List<String>>(listOf())
    val listaCompras: LiveData<List<String>> = _listaCompras

    init {
        cargarPlanesSimulados()
    }

    fun setSemanaActual(semana: Int) {
        if (semana in 1..52) {
            _semanaActual.value = semana
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
        _semanas.value = nuevaLista
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
        _semanas.value = nuevaLista
        generarListaCompras()
    }

    private fun generarListaCompras() {
        val semana = _semanas.value?.firstOrNull { it.numeroSemana == _semanaActual.value }
        val compras = mutableSetOf<String>()
        semana?.dias?.forEach { dia ->
            if (dia.completado) {
                compras.addAll(dia.recetas)
            }
        }
        _listaCompras.value = ArrayList(compras)
    }

    private fun cargarPlanesSimulados() {
        val todasLasSemanas = ArrayList<SemanaPlan>()
        for (s in 1..52) {
            val dias = ArrayList<DiaPlan>()
            val nombresDias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
            for (d in 1..7) {
                val recetas = when (d) {
                    1 -> listOf("Pollo con arroz", "Ensalada mixta")
                    2 -> listOf("Pasta con atún", "Verduras al vapor")
                    3 -> listOf("Tacos de carne", "Guacamole")
                    4 -> listOf("Salmón a la plancha", "Puré de papa")
                    5 -> listOf("Hamburguesas caseras", "Papas fritas")
                    6 -> listOf("Asado", "Ensalada rusa")
                    7 -> listOf("Pizza casera", "Helado")
                    else -> listOf()
                }
                dias.add(DiaPlan(d, nombresDias[d-1], recetas))
            }
            todasLasSemanas.add(SemanaPlan(s, dias))
        }
        _semanas.value = todasLasSemanas
        generarListaCompras()
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlanesComidaViewModel::class.java)) {
                return PlanesComidaViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}