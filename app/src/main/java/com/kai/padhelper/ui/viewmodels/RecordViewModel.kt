package com.kai.padhelper.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kai.padhelper.data.model.TeamRecord
import com.kai.padhelper.data.repository.IRepository
import com.kai.padhelper.util.Constants.Companion.URL_PAD_INDEX_BASE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    fun queryCharacterId(teamRecord: TeamRecord, columnName: String, id: String) = viewModelScope.launch {
        repository.fetchHtmlContent(URL_PAD_INDEX_BASE + id)
        val updateRecord: TeamRecord = when(columnName) {
            "imgLeader" -> teamRecord.copy(leaderUrl = repository.getCharacterIconUrl())
            "imgMember1" -> teamRecord.copy(member1Url = repository.getCharacterIconUrl())
            "imgMember2" -> teamRecord.copy(member2Url = repository.getCharacterIconUrl())
            "imgMember3" -> teamRecord.copy(member3Url = repository.getCharacterIconUrl())
            "imgMember4" -> teamRecord.copy(member4Url = repository.getCharacterIconUrl())
            "imgViceCaptain" -> teamRecord.copy(viceLeaderIconUrl = repository.getCharacterIconUrl())
            else -> teamRecord.copy()
        }
        repository.upsertTeamRecord(updateRecord)
    }

    fun saveTeamRecord(teamRecord: TeamRecord) = viewModelScope.launch {
        repository.upsertTeamRecord(teamRecord)
    }

    fun deleteTeamRecord(teamRecord: TeamRecord) = viewModelScope.launch {
        repository.deleteTeamRecord(teamRecord)
    }

    fun getSavedRecords() = repository.getSavedTeamRecords()
}