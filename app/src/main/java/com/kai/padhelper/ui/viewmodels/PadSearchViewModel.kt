package com.kai.padhelper.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kai.padhelper.data.repository.PadSearchRepository
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.kai.padhelper.data.model.PadSearchModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class PadSearchViewModel @Inject constructor(
        private val mPadSearchRepository: PadSearchRepository): ViewModel() {

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

                mPadSearchList.add(0, PadSearchModel(name, iconUrl, typeUrls,
                    awokenAndKillUrls["awoken"], awokenAndKillUrls["superAwoken"], skillCd))
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