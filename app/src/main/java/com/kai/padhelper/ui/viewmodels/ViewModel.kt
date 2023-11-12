package com.kai.padhelper.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.kai.padhelper.data.model.PadSearchModel
import com.kai.padhelper.data.repository.IPadSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val mPadSearchRepository: IPadSearchRepository
): ViewModel() {

    private val mPadSearchList = mutableListOf<PadSearchModel>()
    private val mPadSearchLiveData = MutableLiveData<List<PadSearchModel>>()
    val padSearchListLiveData: LiveData<List<PadSearchModel>>
        get() = mPadSearchLiveData

    fun fetchData(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mPadSearchRepository.fetchHtmlContent(url)
                val name = mPadSearchRepository.getCharacterName()
                val iconUrl = mPadSearchRepository.getCharacterIconUrl()
                val typeUrls = mPadSearchRepository.getTypeIconUrls()
                val awokenAndKillUrls = mPadSearchRepository.getAwokenAndKillerIconUrls()
                val skillCd = mPadSearchRepository.getSkillCd()
                val characterId = url.substringAfterLast("/")

                mPadSearchList.add(
                    0, PadSearchModel(
                        name, iconUrl, typeUrls,
                        awokenAndKillUrls["awoken"], awokenAndKillUrls["superAwoken"],
                        skillCd, characterId
                    )
                )
                mPadSearchLiveData.postValue(mPadSearchList.toList())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeItem(position: Int) {
        mPadSearchList.removeAt(position)
        mPadSearchLiveData.postValue(mPadSearchList.toList())
    }
}