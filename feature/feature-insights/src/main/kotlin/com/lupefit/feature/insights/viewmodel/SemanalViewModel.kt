package com.lupefit.feature.insights.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.ArrayList

class SemanalViewModel : ViewModel() {

    data class MacroResumen(
        val nombre: String,
        val valor: String,
        val progreso: Int,
        val porcentaje: String
    )

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _periodo = MutableLiveData<String>("Esta semana")
    val periodo: LiveData<String> = _periodo

    private val _resumenCalorias = MutableLiveData<String>("—")
    val resumenCalorias: LiveData<String> = _resumenCalorias

    private val _resumenProteinas = MutableLiveData<String>("—")
    val resumenProteinas: LiveData<String> = _resumenProteinas

    private val _resumenCarbohidratos = MutableLiveData<String>("—")
    val resumenCarbohidratos: LiveData<String> = _resumenCarbohidratos

    private val _resumenGrasas = MutableLiveData<String>("—")
    val resumenGrasas: LiveData<String> = _resumenGrasas

    private val _resumenAgua = MutableLiveData<String>("—")
    val resumenAgua: LiveData<String> = _resumenAgua

    private val _resumenEjercicio = MutableLiveData<String>("—")
    val resumenEjercicio: LiveData<String> = _resumenEjercicio

    private val _macroResumenes = MutableLiveData<List<MacroResumen>>(listOf())
    val macroResumenes: LiveData<List<MacroResumen>> = _macroResumenes

    init {
        cargarDatosSimulados()
    }

    fun recargar() {
        _cargando.value = true
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            cargarDatosSimulados()
            _cargando.postValue(false)
        }, 500)
    }

    fun setPeriodo(periodo: String) {
        _periodo.value = periodo
    }

    private fun cargarDatosSimulados() {
        _resumenCalorias.value = "14.200 kcal"
        _resumenProteinas.value = "120 g"
        _resumenCarbohidratos.value = "180 g"
        _resumenGrasas.value = "55 g"
        _resumenAgua.value = "14.000 ml"
        _resumenEjercicio.value = "5 sesiones"

        val lista = ArrayList<MacroResumen>()
        lista.add(MacroResumen("Calorías", "14.200 kcal", 85, "85% de la meta semanal"))
        lista.add(MacroResumen("Proteínas", "120 g", 92, "92% de la meta semanal"))
        lista.add(MacroResumen("Carbohidratos", "180 g", 78, "78% de la meta semanal"))
        lista.add(MacroResumen("Grasas", "55 g", 88, "88% de la meta semanal"))
        lista.add(MacroResumen("Agua", "14.000 ml", 100, "Meta cumplida"))
        lista.add(MacroResumen("Ejercicio", "5 sesiones", 71, "5 de 7 días"))
        _macroResumenes.value = lista
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SemanalViewModel::class.java)) {
                return SemanalViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}