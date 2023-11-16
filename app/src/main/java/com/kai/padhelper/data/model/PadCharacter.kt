package com.kai.padhelper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kai.padhelper.util.Constants

@Entity(
    Constants.TABLE_NAME_PAD_CHARACTER
)
data class PadCharacter(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var name: String?,
    var iconUrl: String?,
    var typeUrls: MutableList<String>?,
    var awokenUrls: MutableList<String>?,
    var superAwokenUrls: MutableList<String>?,
    var skillCd: Pair<String?, String?>?,
    var characterId: String?
)
