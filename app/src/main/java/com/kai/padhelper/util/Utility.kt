package com.kai.padhelper.util

class Utility {
    companion object {
        fun isValidInt(str: String): Boolean {
            return try {
                if (str.matches(Regex("\\d+"))) {
                    str.toInt()
                    true
                } else {
                    false
                }
            } catch (e: NumberFormatException) {
                false
            }
        }

        fun extractCharacterIdFromIconUrl(url: String): String? {
            val pattern = Regex("monster/(\\d+)\\.webp")
            return pattern.find(url)?.groupValues?.get(1)
        }
    }
}