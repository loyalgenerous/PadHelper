package com.kai.padhelper.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kai.padhelper.data.repository.PadSearchRepository
import javax.inject.Inject

class PadSearchViewModelFactory @Inject constructor(private val repository: PadSearchRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PadSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PadSearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}