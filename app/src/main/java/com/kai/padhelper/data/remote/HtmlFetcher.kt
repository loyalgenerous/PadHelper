package com.kai.padhelper.data.remote

import org.jsoup.nodes.Document

interface HtmlFetcher {
    suspend fun fetchHtml(url: String): Document
}