package com.kai.padhelper.data.repository

import androidx.lifecycle.LiveData
import com.kai.padhelper.data.model.PadCharacter

interface IPadSearchRepository {
    suspend fun fetchHtmlContent(url: String)
    fun getPadCharacter(): PadCharacter
    suspend fun upsertPadCharacter(padCharacter: PadCharacter)
    suspend fun deletePadCharacter(padCharacter: PadCharacter)
    fun getSavedCharacter(): LiveData<List<PadCharacter>>
}