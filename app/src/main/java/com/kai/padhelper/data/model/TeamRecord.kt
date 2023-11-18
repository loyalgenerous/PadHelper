package com.kai.padhelper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kai.padhelper.util.Constants

@Entity(
    Constants.TABLE_NAME_TEAM_RECORD
)
data class TeamRecord(
    val badgeIconUrl: String? = null,
    val teamName: String? = null,
    val viceLeaderIconUrl: String? = null,
    val leaderUrl: String? = null,
    val member1Url: String? = null,
    val member2Url: String? = null,
    val member3Url: String? = null,
    val member4Url: String? = null,
    val recordText: String? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
