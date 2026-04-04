package com.noitacilppa.okonow.data

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromPriority(priority: Priority): Int {
        return priority.value
    }

    @TypeConverter
    fun toPriority(value: Int): Priority {
        return Priority.entries.find { it.value == value } ?: Priority.MEDIUM
    }

    @TypeConverter
    fun fromContentFormat(format: ContentFormat): String {
        return format.name
    }

    @TypeConverter
    fun toContentFormat(value: String): ContentFormat {
        return try {
            ContentFormat.valueOf(value)
        } catch (e: Exception) {
            ContentFormat.PARAGRAPH
        }
    }
}
