package com.lupefit.feature.insights.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.ArrayList
import java.util.Calendar
import com.lupefit.core.domain.model.DiarioEntry
import com.lupefit.core.domain.provider.DiarioRepositoryProvider
import com.lupefit.core.domain.repository.DiarioRepository

class SemanalViewModel(private val repository: DiarioRepository) : ViewModel() {

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

    private val entriesSource = repository.getEntriesForDateRange(dateKey(-6), dateKey(0)).also {
        it.observeForever { entries -> cargarDatosReales(entries.orEmpty()) }
    }

    fun recargar() {
        _cargando.value = true
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            cargarDatosReales(entriesSource.value.orEmpty())
            _cargando.postValue(false)
        }, 500)
    }

    fun setPeriodo(periodo: String) {
        _periodo.value = periodo
    }

    private fun cargarDatosReales(entries: List<DiarioEntry>) {
        val kcal = entries.sumOf { it.calorias }
        val protein = entries.sumOf { it.proteinas }
        val carbs = entries.sumOf { it.carbohidratos }
        val fat = entries.sumOf { it.grasas }
        _resumenCalorias.value = if (entries.isEmpty()) "—" else String.format("%.0f kcal", kcal.toDouble())
        _resumenProteinas.value = if (entries.isEmpty()) "—" else String.format("%.1f g", protein)
        _resumenCarbohidratos.value = if (entries.isEmpty()) "—" else String.format("%.1f g", carbs)
        _resumenGrasas.value = if (entries.isEmpty()) "—" else String.format("%.1f g", fat)
        _resumenAgua.value = "—"
        _resumenEjercicio.value = "—"

        val lista = ArrayList<MacroResumen>()
        lista.add(MacroResumen("Calorías", _resumenCalorias.value ?: "—", 0, "Registros del diario"))
        lista.add(MacroResumen("Proteínas", _resumenProteinas.value ?: "—", 0, "Registros del diario"))
        lista.add(MacroResumen("Carbohidratos", _resumenCarbohidratos.value ?: "—", 0, "Registros del diario"))
        lista.add(MacroResumen("Grasas", _resumenGrasas.value ?: "—", 0, "Registros del diario"))
        _macroResumenes.value = lista
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SemanalViewModel::class.java)) {
                val provider = context.applicationContext as DiarioRepositoryProvider
                return SemanalViewModel(provider.getDiarioRepository()) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private fun dateKey(offset: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, offset)
        return calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)
    }
}
