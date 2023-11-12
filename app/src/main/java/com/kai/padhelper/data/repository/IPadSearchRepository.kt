package com.kai.padhelper.data.repository

interface IPadSearchRepository {
    suspend fun fetchHtmlContent(url: String)
    fun getCharacterName(): String?
    fun getCharacterIconUrl(): String?
    fun getTypeIconUrls(): MutableList<String>?
    fun getAwokenAndKillerIconUrls(): MutableMap<String, MutableList<String>>
    fun getSkillCd(): Pair<String?, String?>
}