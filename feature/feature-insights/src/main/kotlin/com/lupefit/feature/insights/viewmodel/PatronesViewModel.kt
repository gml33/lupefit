package com.lupefit.feature.insights.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.ArrayList

class PatronesViewModel : ViewModel() {

    data class PatronAlimentario(
        val nombre: String,
        val frecuencia: Int,
        val descripcion: String,
        val esPositivo: Boolean
    )

    private val _patrones = MutableLiveData<List<PatronAlimentario>>(listOf())
    val patrones: LiveData<List<PatronAlimentario>> = _patrones

    private val _patronPrincipal = MutableLiveData<String>("—")
    val patronPrincipal: LiveData<String> = _patronPrincipal

    private val _scoreConsistencia = MutableLiveData<Int>(0)
    val scoreConsistencia: LiveData<Int> = _scoreConsistencia

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> = _cargando

    init {
        cargarPatronesSimulados()
    }

    private fun cargarPatronesSimulados() {
        val lista = ArrayList<PatronAlimentario>()
        lista.add(PatronAlimentario("Desayuno consistente", 85, "Desayunas 6 de 7 días", true))
        lista.add(PatronAlimentario("Cena tardía", 60, "Cenas después de las 22:00", false))
        lista.add(PatronAlimentario("Hidratación matutina", 90, "Vaso de agua al despertar", true))
        lista.add(PatronAlimentario("Snack nocturno", 40, "Picoteo antes de dormir", false))
        lista.add(PatronAlimentario("Proteína en almuerzo", 95, "Siempre incluye proteína", true))
        lista.add(PatronAlimentario("Fines de semana flexibles", 70, "Más calorías Sáb/Dom", false))

        _patrones.value = lista
        _patronPrincipal.value = "Desayuno consistente (85%)"
        _scoreConsistencia.value = 72
    }

    fun recargar() {
        _cargando.value = true
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            cargarPatronesSimulados()
            _cargando.postValue(false)
        }, 500)
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PatronesViewModel::class.java)) {
                return PatronesViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}