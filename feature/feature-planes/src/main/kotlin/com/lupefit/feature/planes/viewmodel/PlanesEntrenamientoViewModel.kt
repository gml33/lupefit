package com.lupefit.feature.planes.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.ArrayList
import com.google.gson.reflect.TypeToken

class PlanesEntrenamientoViewModel private constructor(private val storage: PlanesStorage) : ViewModel() {

    data class DiaEntrenamiento(
        val dia: Int,
        val nombre: String,
        val rutinasAsignadas: List<String> = listOf(),
        val completado: Boolean = false
    )

    data class SemanaEntrenamiento(
        val numeroSemana: Int,
        val dias: List<DiaEntrenamiento> = listOf()
    )

    private val _semanas = MutableLiveData<List<SemanaEntrenamiento>>(listOf())
    val semanas: LiveData<List<SemanaEntrenamiento>> = _semanas

    private val _semanaActual = MutableLiveData(
        storage.read("entrenamientos_semana", Int::class.javaObjectType, 1).coerceIn(1, 52))
    val semanaActual: LiveData<Int> = _semanaActual

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _progresoSemanal = MutableLiveData<Int>(0)
    val progresoSemanal: LiveData<Int> = _progresoSemanal

    init {
        _semanas.value = storage.read("entrenamientos", object : TypeToken<List<SemanaEntrenamiento>>() {}.type, emptyList<SemanaEntrenamiento>())
        calcularProgreso()
    }

    fun setSemanaActual(semana: Int) {
        if (semana in 1..52) {
            storage.write("entrenamientos_semana", semana)
            _semanaActual.value = semana
            calcularProgreso()
        }
    }

    fun toggleDiaCompletado(numeroSemana: Int, dia: Int) {
        _cargando.value = true
        val listaActual = _semanas.value ?: ArrayList<SemanaEntrenamiento>()
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
        calcularProgreso()
        _cargando.value = false
    }

    fun asignarRutinaADia(numeroSemana: Int, dia: Int, rutina: String) {
        val listaActual = _semanas.value ?: ArrayList<SemanaEntrenamiento>()
        val nuevaLista = listaActual.map { semana ->
            if (semana.numeroSemana == numeroSemana) {
                val nuevosDias = semana.dias.map { d ->
                    if (d.dia == dia) {
                        val nuevasRutinas = ArrayList<String>(d.rutinasAsignadas)
                        nuevasRutinas.add(rutina)
                        d.copy(rutinasAsignadas = nuevasRutinas)
                    } else d
                }
                semana.copy(dias = nuevosDias)
            } else semana
        }
        save(nuevaLista)
    }

    private fun calcularProgreso() {
        val semana = _semanas.value?.firstOrNull { it.numeroSemana == _semanaActual.value }
        if (semana != null) {
            val diasPlanificados = semana.dias.filter { it.rutinasAsignadas.isNotEmpty() }
            val total = diasPlanificados.size
            val completados = diasPlanificados.count { it.completado }
            _progresoSemanal.value = if (total > 0) (completados * 100 / total) else 0
        } else {
            _progresoSemanal.value = 0
        }
    }

    fun guardarSemana(semana: Int, nombres: List<String>, entradas: List<List<String>>) {
        require(semana in 1..52 && nombres.size == 7 && entradas.size == 7)
        val anterior = _semanas.value.orEmpty().find { it.numeroSemana == semana }
        val dias = nombres.mapIndexed { index, nombre ->
            val elementos = entradas[index]
            val previo = anterior?.dias?.getOrNull(index)
            DiaEntrenamiento(index + 1, nombre, elementos, previo?.let { it.rutinasAsignadas == elementos && it.completado } == true)
        }
        save((_semanas.value.orEmpty().filterNot { it.numeroSemana == semana } + SemanaEntrenamiento(semana, dias))
            .sortedBy { it.numeroSemana })
        setSemanaActual(semana)
    }

    private fun save(semanas: List<SemanaEntrenamiento>) {
        storage.write("entrenamientos", semanas)
        _semanas.value = semanas
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlanesEntrenamientoViewModel::class.java)) {
                return PlanesEntrenamientoViewModel(PlanesStorage(context)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}