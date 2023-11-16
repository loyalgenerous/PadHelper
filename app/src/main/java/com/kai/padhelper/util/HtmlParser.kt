package com.kai.padhelper.util

import com.kai.padhelper.data.model.PadCharacter
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.lang.Exception

object HtmlParser {
    private lateinit var htmlContent: Document

    fun getPadCharacter(content: Document): PadCharacter {
        htmlContent = content
        return PadCharacter(
            getCharacterName(),
            getCharacterIconUrl(),
            getTypeIconUrls(),
            getAwokenAndKillerIconUrls()["awoken"],
            getAwokenAndKillerIconUrls()["superAwoken"],
            getSkillCd(),
            getCharacterId()
        )
    }

    private fun selectElement(cssSelector: String): Elements? {
        return try {
            htmlContent.select(cssSelector)
        } catch (e: Exception) {
            println("Failed when selecting element: ${e.printStackTrace()}")
            null
        }
    }

    private fun getCharacterName(): String? {
        val elements = selectElement("h1.mb-0.monster-name")
        return elements?.first()?.text()
    }

    private fun getCharacterIconUrl(): String? {
        val elements = selectElement("img.mr-2.monster-icon")
        return elements?.first()?.attr("src")
    }

    private fun getTypeIconUrls(): MutableList<String>? {
        val elements = selectElement("img.type-icon")
        return elements?.eachAttr("src")
    }

    private fun getAwokenAndKillerIconUrls(): MutableMap<String, MutableList<String>> {
        val res = mutableMapOf<String, MutableList<String>>()
        val awakenings = mutableListOf<String>()
        val superAwakenings = mutableListOf<String>()
        val latentKillers = mutableListOf<String>()

        // 提取覺醒的圖片URL
        val awakeningDiv = selectElement("small:containsOwn(覚醒スキル) ~ div")
        awakeningDiv?.first()?.select("img")?.forEach { img ->
            img.attr("src").let { awakenings.add(it) }
        }

        // 提取超覺醒的圖片URL
        val superAwakeningDiv = selectElement("small:containsOwn(超覚醒スキル) ~ div")
        superAwakeningDiv?.first()?.select("img")?.forEach { img ->
            img.attr("src").let { superAwakenings.add(it) }
        }

        // 提取可裝潛在killer的圖片URL
        val latentKillersDiv = selectElement("small:containsOwn(付けられる潛在キラー) ~ div")
        latentKillersDiv?.first()?.select("img")?.forEach { img ->
            img.attr("src").let { latentKillers.add(it) }
        }

        res["awoken"] = awakenings
        res["superAwoken"] = superAwakenings
        res["killer"] = latentKillers
        return res
    }

    private fun getSkillCd(): Pair<String?, String?> {
        // 提取 LV.1 CD 的數值
        val lv1CdSpan = selectElement("span.badge-steelblue")
        val lv1Cd = lv1CdSpan?.first()?.ownText()?.split(" ")?.get(2)

        // 提取 LV.MAX CD 的數值
        val lvMaxCdSpan = selectElement("span.badge-danger")
        val lvMaxCd = lvMaxCdSpan?.first()?.ownText()?.split(" ")?.get(2)

        // 打印結果
        println("LV.1 CD: $lv1Cd")
        println("LV.MAX CD: $lvMaxCd")
        return Pair(lv1Cd, lvMaxCd)
    }

    private fun getCharacterId(): String? {
        val elements = selectElement("input[name=monster_id]")
        return elements?.first()?.attr("value")
    }
}