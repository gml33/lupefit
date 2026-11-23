package com.lupefit.feature.insights.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.Entry
import com.lupefit.core.domain.model.DiarioEntry
import com.lupefit.core.domain.provider.DiarioRepositoryProvider
import com.lupefit.core.domain.repository.DiarioRepository
import java.util.Calendar
import java.util.ArrayList

class TendenciasViewModel(private val repository: DiarioRepository) : ViewModel() {

    enum class Periodo(val label: String) {
        SEMANA("7 días"),
        MES("30 días"),
        TRIMESTRE("90 días")
    }

    private val _periodoSeleccionado = MutableLiveData<Periodo>(Periodo.SEMANA)
    val periodoSeleccionado: LiveData<Periodo> = _periodoSeleccionado

    private val _datosCalorias = MutableLiveData<List<Entry>>(listOf())
    val datosCalorias: LiveData<List<Entry>> = _datosCalorias

    private val _datosPeso = MutableLiveData<List<Entry>>(listOf())
    val datosPeso: LiveData<List<Entry>> = _datosPeso

    private val _datosProteinas = MutableLiveData<List<Entry>>(listOf())
    val datosProteinas: LiveData<List<Entry>> = _datosProteinas

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> = _cargando

    private val entriesSource = repository.getEntriesForDateRange(dateKey(-90), dateKey(0)).also {
        it.observeForever { entries -> cargarDatosReales(entries.orEmpty(), _periodoSeleccionado.value!!) }
    }

    fun setPeriodo(periodo: Periodo) {
        _periodoSeleccionado.value = periodo
        val entries = entriesSource.value.orEmpty()
        cargarDatosReales(entries, periodo)
    }

    private fun cargarDatosReales(entries: List<DiarioEntry>, periodo: Periodo) {
        _cargando.value = true
        val calorias = ArrayList<Entry>()
        val proteinas = ArrayList<Entry>()

        val dias = when (periodo) {
            Periodo.SEMANA -> 7
            Periodo.MES -> 30
            Periodo.TRIMESTRE -> 90
        }

        val grouped = entries.groupBy { it.dateKey }
        for (i in 0 until dias) {
            val day = dateKey(-(dias - 1 - i))
            val dayEntries = grouped[day].orEmpty()
            calorias.add(Entry(i.toFloat(), dayEntries.sumOf { it.calorias }.toFloat()))
            proteinas.add(Entry(i.toFloat(), dayEntries.sumOf { it.proteinas }.toFloat()))
        }

        _datosCalorias.value = calorias
        _datosPeso.value = emptyList()
        _datosProteinas.value = proteinas
        _cargando.value = false
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TendenciasViewModel::class.java)) {
                val provider = context.applicationContext as DiarioRepositoryProvider
                return TendenciasViewModel(provider.getDiarioRepository()) as T
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
