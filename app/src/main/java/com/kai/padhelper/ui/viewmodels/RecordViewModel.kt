package com.kai.padhelper.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kai.padhelper.data.model.TeamRecord
import com.kai.padhelper.data.model.TeamRole
import com.kai.padhelper.data.repository.IRepository
import com.kai.padhelper.util.Constants.Companion.URL_PAD_INDEX_BASE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    fun queryCharacterId(teamRecord: TeamRecord, id: String, role: TeamRole) = viewModelScope.launch {
        repository.fetchHtmlContent(URL_PAD_INDEX_BASE + id)
        val updateRecord: TeamRecord = when(role) {
            TeamRole.LEADER -> teamRecord.copy(leaderUrl = repository.getCharacterIconUrl())
            TeamRole.MEMBER1 -> teamRecord.copy(member1Url = repository.getCharacterIconUrl())
            TeamRole.MEMBER2 -> teamRecord.copy(member2Url = repository.getCharacterIconUrl())
            TeamRole.MEMBER3 -> teamRecord.copy(member3Url = repository.getCharacterIconUrl())
            TeamRole.MEMBER4 -> teamRecord.copy(member4Url = repository.getCharacterIconUrl())
            TeamRole.VICE_CAPTAIN -> teamRecord.copy(viceLeaderIconUrl = repository.getCharacterIconUrl())
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