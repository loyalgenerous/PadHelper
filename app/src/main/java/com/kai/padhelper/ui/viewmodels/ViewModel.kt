package com.kai.padhelper.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.kai.padhelper.data.model.PadCharacter
import com.kai.padhelper.data.repository.IPadSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val mPadSearchRepository: IPadSearchRepository
): ViewModel() {

    fun fetchData(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mPadSearchRepository.fetchHtmlContent(url)
                val padCharacter = mPadSearchRepository.getPadCharacter()
                saveCharacterSearchResult(padCharacter)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveCharacterSearchResult(padCharacter: PadCharacter) = viewModelScope.launch {
        mPadSearchRepository.upsertPadCharacter(padCharacter)
    }

    fun deleteCharacterSearchResult(padCharacter: PadCharacter) = viewModelScope.launch {
        mPadSearchRepository.deletePadCharacter(padCharacter)
    }

    fun getSavedSearchResult() = mPadSearchRepository.getSavedCharacter()
}