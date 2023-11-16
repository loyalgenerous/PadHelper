package com.kai.padhelper.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.kai.padhelper.data.model.PadCharacter
import com.kai.padhelper.util.Constants.Companion.TABLE_NAME_PAD_CHARACTER

@Dao
interface PadSearchDao {
    @Upsert
    suspend fun upsertSearchResult(padCharacter: PadCharacter)

    @Delete
    suspend fun deleteResult(padCharacter: PadCharacter)

    @Query("SELECT * FROM $TABLE_NAME_PAD_CHARACTER")
    fun getSavedResult(): LiveData<List<PadCharacter>>
}