package com.kai.padhelper.data.model

data class PadSearchModel(
    var name: String?,
    var iconUrl: String?,
    var typeUrls: MutableList<String>?,
    var awokenUrls: MutableList<String>?,
    var superAwokenUrls: MutableList<String>?,
    var skillCd: Pair<String?, String?>?
)
