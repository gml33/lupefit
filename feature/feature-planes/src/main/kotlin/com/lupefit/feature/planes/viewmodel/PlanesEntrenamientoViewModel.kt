package com.lupefit.feature.planes.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.ArrayList

class PlanesEntrenamientoViewModel : ViewModel() {

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

    private val _semanaActual = MutableLiveData<Int>(1)
    val semanaActual: LiveData<Int> = _semanaActual

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _progresoSemanal = MutableLiveData<Int>(0)
    val progresoSemanal: LiveData<Int> = _progresoSemanal

    init {
        cargarPlanesSimulados()
    }

    fun setSemanaActual(semana: Int) {
        if (semana in 1..52) {
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
        _semanas.value = nuevaLista
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
        _semanas.value = nuevaLista
    }

    private fun calcularProgreso() {
        val semana = _semanas.value?.firstOrNull { it.numeroSemana == _semanaActual.value }
        if (semana != null) {
            val total = semana.dias.size
            val completados = semana.dias.count { it.completado }
            _progresoSemanal.value = if (total > 0) (completados * 100 / total) else 0
        } else {
            _progresoSemanal.value = 0
        }
    }

    private fun cargarPlanesSimulados() {
        val todasLasSemanas = ArrayList<SemanaEntrenamiento>()
        for (s in 1..52) {
            val dias = ArrayList<DiaEntrenamiento>()
            val nombresDias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
            val rutinasPorDia = mapOf(
                1 to listOf("Push/Pull/Legs - Push", "Core 15min"),
                2 to listOf("Upper Body", "Cardio 20min"),
                3 to listOf("Lower Body", "Movilidad"),
                4 to listOf("Push/Pull/Legs - Pull", "Core 15min"),
                5 to listOf("Full Body", "HIIT 20min"),
                6 to listOf("Push/Pull/Legs - Legs", "Estiramiento"),
                7 to listOf("Descanso activo", "Yoga/Caminata")
            )
            for (d in 1..7) {
                dias.add(DiaEntrenamiento(d, nombresDias[d-1], rutinasPorDia[d] ?: listOf()))
            }
            todasLasSemanas.add(SemanaEntrenamiento(s, dias))
        }
        _semanas.value = todasLasSemanas
        calcularProgreso()
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlanesEntrenamientoViewModel::class.java)) {
                return PlanesEntrenamientoViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}