package com.lupefit.feature.insights.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.Entry
import java.util.ArrayList

class TendenciasViewModel : ViewModel() {

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

    init {
        cargarDatosSimulados(_periodoSeleccionado.value!!)
    }

    fun setPeriodo(periodo: Periodo) {
        _periodoSeleccionado.value = periodo
        cargarDatosSimulados(periodo)
    }

    private fun cargarDatosSimulados(periodo: Periodo) {
        _cargando.value = true
        val calorias = ArrayList<Entry>()
        val peso = ArrayList<Entry>()
        val proteinas = ArrayList<Entry>()

        val dias = when (periodo) {
            Periodo.SEMANA -> 7
            Periodo.MES -> 30
            Periodo.TRIMESTRE -> 90
        }

        for (i in 0 until dias) {
            calorias.add(Entry(i.toFloat(), 2000f + (Math.random() * 500).toFloat()))
            if (i % when (periodo) {
                Periodo.SEMANA -> 1
                Periodo.MES -> 3
                Periodo.TRIMESTRE -> 10
            } == 0) {
                peso.add(Entry(i.toFloat(), 75f + (Math.random() * 2).toFloat()))
            }
            proteinas.add(Entry(i.toFloat(), 100f + (Math.random() * 50).toFloat()))
        }

        _datosCalorias.value = calorias
        _datosPeso.value = peso
        _datosProteinas.value = proteinas
        _cargando.value = false
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TendenciasViewModel::class.java)) {
                return TendenciasViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}