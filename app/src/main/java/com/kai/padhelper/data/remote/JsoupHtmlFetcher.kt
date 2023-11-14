package com.kai.padhelper.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JsoupHtmlFetcher : HtmlFetcher {
    override suspend fun fetchHtml(url: String): Document {
        return withContext(Dispatchers.IO) {
            Jsoup.connect(url).get()
        }
    }
}