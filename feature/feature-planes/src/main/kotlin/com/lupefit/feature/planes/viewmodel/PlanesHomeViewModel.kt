package com.lupefit.feature.planes.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlanesHomeViewModel : ViewModel() {

    private val _semanaActual = MutableLiveData<Int>(1)
    val semanaActual: LiveData<Int> = _semanaActual

    fun setSemanaActual(semana: Int) {
        if (semana in 1..52) {
            _semanaActual.value = semana
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlanesHomeViewModel::class.java)) {
                return PlanesHomeViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}