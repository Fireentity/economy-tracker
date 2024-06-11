package it.unipd.dei.common_backend.utils

import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

object TimeUtils {

    fun timestampNow(): Timestamp {
        return Timestamp(System.currentTimeMillis())
    }

    fun zonedDateTimeNow(): ZonedDateTime {
        return ZonedDateTime.now()
    }

    fun zonedDateTimeToMillis(zonedDateTime: ZonedDateTime): Long {
        return zonedDateTime.toInstant().toEpochMilli()
    }

    fun zonedDateTimeFromMillis(millis: Long): ZonedDateTime {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
    }

    fun zonedDateTimeFromTimestamp(timestamp: Timestamp): ZonedDateTime {
        return ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault())
    }

    fun secondsBetween(
        zonedDateTime1Inclusive: ZonedDateTime,
        zonedDateTime2Exclusive: ZonedDateTime
    ): Long {
        return ChronoUnit.SECONDS.between(zonedDateTime1Inclusive, zonedDateTime2Exclusive)
    }

    fun secondsFromNow(zonedDateTime2Exclusive: ZonedDateTime): Long {
        return ChronoUnit.SECONDS.between(ZonedDateTime.now(), zonedDateTime2Exclusive)
    }
}