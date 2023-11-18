package com.kai.padhelper.data.repository

import com.kai.padhelper.data.remote.HtmlFetcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class PadSearchRepositoryTest {

    private lateinit var mockHtmlFetcher: HtmlFetcher
    private lateinit var mockDocument: Document
    private lateinit var padSearchRepository: Repository

    @Before
    fun setUp() {
        mockHtmlFetcher = mock(HtmlFetcher::class.java)
        mockDocument = mock(Document::class.java)
        padSearchRepository = Repository(mockHtmlFetcher)
    }

    @Test
    fun `test getCharacterName returns correct name`() = runTest {
        val mockElement = mock(Element::class.java)
        val mockElements = mock(Elements::class.java)

        `when`(mockHtmlFetcher.fetchHtml(anyString())).thenReturn(mockDocument)
        `when`(mockDocument.select("h1.mb-0.monster-name")).thenReturn(mockElements)
        `when`(mockElements.first()).thenReturn(mockElement)
        `when`(mockElement.text()).thenReturn("name")

        padSearchRepository.fetchHtmlContent("some_url")
        val characterName = padSearchRepository.getCharacterName()
        assertEquals("name", characterName)
    }


}