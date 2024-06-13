package it.unipd.dei.common_backend.converters

import androidx.room.TypeConverter
import java.time.Month

class MonthTypeConverter {
    @TypeConverter
    fun toHealth(value: Int) = Month.entries[value]

    @TypeConverter
    fun fromHealth(value: Month) = value.ordinal
}