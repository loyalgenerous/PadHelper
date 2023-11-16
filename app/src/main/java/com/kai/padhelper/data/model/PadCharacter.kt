package com.kai.padhelper.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kai.padhelper.util.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    Constants.TABLE_NAME_PAD_CHARACTER
)
data class PadCharacter(
    var name: String?,
    var iconUrl: String?,
    var typeUrls: MutableList<String>?,
    var awokenUrls: MutableList<String>?,
    var superAwokenUrls: MutableList<String>?,
    var skillCd: Pair<String?, String?>?,
    var characterId: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable
