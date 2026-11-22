package com.lupefit.feature.insights.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PrediccionesViewModel : ViewModel() {

    private val _proyeccionPeso = MutableLiveData<String>("—")
    val proyeccionPeso: LiveData<String> = _proyeccionPeso

    private val _fechaObjetivo = MutableLiveData<String>("—")
    val fechaObjetivo: LiveData<String> = _fechaObjetivo

    private val _proyeccionCalorias = MutableLiveData<String>("—")
    val proyeccionCalorias: LiveData<String> = _proyeccionCalorias

    private val _tendenciaProteinas = MutableLiveData<String>("—")
    val tendenciaProteinas: LiveData<String> = _tendenciaProteinas

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _rachaActual = MutableLiveData<String>("0 días")
    val rachaActual: LiveData<String> = _rachaActual

    init {
        cargarPrediccionesSimuladas()
    }

    private fun cargarPrediccionesSimuladas() {
        _proyeccionPeso.value = "73.5 kg"
        _fechaObjetivo.value = "15/02/2027"
        _proyeccionCalorias.value = "2.150 kcal/día"
        _tendenciaProteinas.value = "En rango óptimo"
        _rachaActual.value = "12 días"
    }

    fun recargar() {
        _cargando.value = true
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            cargarPrediccionesSimuladas()
            _cargando.postValue(false)
        }, 500)
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PrediccionesViewModel::class.java)) {
                return PrediccionesViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}