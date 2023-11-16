package com.kai.padhelper.data.repository

import androidx.lifecycle.LiveData
import com.kai.padhelper.data.db.AppDataBase
import com.kai.padhelper.data.model.PadCharacter
import com.kai.padhelper.data.remote.HtmlFetcher
import com.kai.padhelper.util.HtmlParser
import org.jsoup.nodes.Document
import java.lang.Exception
import javax.inject.Inject

class PadSearchRepository @Inject constructor(
    private val htmlFetcher: HtmlFetcher,
    private val db: AppDataBase
) : IPadSearchRepository {
    private lateinit var htmlContent: Document

    override suspend fun fetchHtmlContent(url: String) {
        try {
            htmlContent = htmlFetcher.fetchHtml(url)
        } catch (e: Exception) {
            println("failed when fetchHtmlContent" + e.printStackTrace())
        }
    }

    override fun getPadCharacter(): PadCharacter {
        return HtmlParser.getPadCharacter(htmlContent)
    }

    override suspend fun upsertPadCharacter(padCharacter: PadCharacter) =
        db.getPadCharacterDao().upsertSearchResult(padCharacter)

    override suspend fun deletePadCharacter(padCharacter: PadCharacter) =
        db.getPadCharacterDao().deleteResult(padCharacter)

    override fun getSavedCharacter(): LiveData<List<PadCharacter>> =
        db.getPadCharacterDao().getSavedResult()
}