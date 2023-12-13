package com.kai.padhelper.data.repository

import androidx.lifecycle.LiveData
import com.kai.padhelper.data.model.PadCharacter
import com.kai.padhelper.data.model.TeamRecord

interface IRepository {
    suspend fun fetchHtmlContent(url: String)
    fun getPadCharacter(): PadCharacter
    fun getCharacterIconUrl(): String?
    suspend fun upsertPadCharacter(padCharacter: PadCharacter)
    suspend fun deletePadCharacter(padCharacter: PadCharacter)
    fun getSavedCharacter(): LiveData<List<PadCharacter>>
    suspend fun upsertTeamRecord(teamRecord: TeamRecord)
    suspend fun deleteTeamRecord(teamRecord: TeamRecord)
    fun getSavedTeamRecords(): LiveData<List<TeamRecord>>
    suspend fun getRecordById(id: String): TeamRecord
}