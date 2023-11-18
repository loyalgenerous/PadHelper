package com.kai.padhelper.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.kai.padhelper.data.model.PadCharacter
import com.kai.padhelper.data.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: IRepository
): ViewModel() {

    fun fetchData(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.fetchHtmlContent(url)
                val padCharacter = repository.getPadCharacter()
                saveCharacterSearchResult(padCharacter)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveCharacterSearchResult(padCharacter: PadCharacter) = viewModelScope.launch {
        repository.upsertPadCharacter(padCharacter)
    }

    fun deleteCharacterSearchResult(padCharacter: PadCharacter) = viewModelScope.launch {
        repository.deletePadCharacter(padCharacter)
    }

    fun getSavedSearchResult() = repository.getSavedCharacter()
}