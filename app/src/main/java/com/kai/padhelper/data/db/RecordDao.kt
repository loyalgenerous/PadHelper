package com.kai.padhelper.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.kai.padhelper.data.model.TeamRecord
import com.kai.padhelper.util.Constants

@Dao
interface RecordDao {
    @Upsert
    suspend fun upsertTeamRecord(teamRecord: TeamRecord)

    @Delete
    suspend fun deleteResult(teamRecord: TeamRecord)

    @Query("SELECT * FROM ${Constants.TABLE_NAME_TEAM_RECORD}")
    fun getSavedRecords(): LiveData<List<TeamRecord>>

    @Query("SELECT * FROM ${Constants.TABLE_NAME_TEAM_RECORD} WHERE id = :id")
    suspend fun getRecordById(id: String): TeamRecord
}