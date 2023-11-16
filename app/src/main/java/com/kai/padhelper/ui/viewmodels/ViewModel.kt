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

    /*private val mPadSearchList = mutableListOf<PadCharacter>()
    private val mPadSearchLiveData = MutableLiveData<List<PadCharacter>>()
    val padSearchListLiveData: LiveData<List<PadCharacter>>
        get() = mPadSearchLiveData*/

    fun fetchData(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mPadSearchRepository.fetchHtmlContent(url)
                val padCharacter = mPadSearchRepository.getPadCharacter()
                saveCharacterSearchResult(padCharacter)
                /*val name = mPadSearchRepository.getCharacterName()
                val iconUrl = mPadSearchRepository.getCharacterIconUrl()
                val typeUrls = mPadSearchRepository.getTypeIconUrls()
                val awokenAndKillUrls = mPadSearchRepository.getAwokenAndKillerIconUrls()
                val skillCd = mPadSearchRepository.getSkillCd()
                val characterId = url.substringAfterLast("/")

                mPadSearchList.add(
                    0, PadCharacter(null,
                        name, iconUrl, typeUrls,
                        awokenAndKillUrls["awoken"], awokenAndKillUrls["superAwoken"],
                        skillCd, characterId
                    )
                )
                mPadSearchLiveData.postValue(mPadSearchList.toList())*/
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /*fun removeItem(position: Int) {
        mPadSearchList.removeAt(position)
        mPadSearchLiveData.postValue(mPadSearchList.toList())
    }*/

    fun saveCharacterSearchResult(padCharacter: PadCharacter) = viewModelScope.launch {
        mPadSearchRepository.upsertPadCharacter(padCharacter)
    }

    fun deleteCharacterSearchResult(padCharacter: PadCharacter) = viewModelScope.launch {
        mPadSearchRepository.deletePadCharacter(padCharacter)
    }

    fun getSavedSearchResult() = mPadSearchRepository.getSavedCharacter()
}