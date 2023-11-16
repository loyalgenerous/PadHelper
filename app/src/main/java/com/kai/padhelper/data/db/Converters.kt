package com.kai.padhelper.data.db

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(value: MutableList<String>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(value: String?): MutableList<String>? {
        return value?.split(",")?.toMutableList()
    }

    @TypeConverter
    fun fromPair(pair: Pair<String?, String?>?): String? {
        return pair?.let { "${it.first}|${it.second}" }
    }

    @TypeConverter
    fun toPair(value: String?): Pair<String?, String?>? {
        return value?.split("|")?.let { Pair(it.getOrNull(0), it.getOrNull(1)) }
    }
}