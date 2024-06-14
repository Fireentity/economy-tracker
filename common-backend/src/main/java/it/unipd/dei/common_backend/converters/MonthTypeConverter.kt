package it.unipd.dei.common_backend.converters

import androidx.room.TypeConverter
import java.time.Month

class MonthTypeConverter {
    @TypeConverter
    fun toMonth(value: Int): Month = Month.of(value)

    @TypeConverter
    fun fromMonth(value: Month) = value.ordinal
}