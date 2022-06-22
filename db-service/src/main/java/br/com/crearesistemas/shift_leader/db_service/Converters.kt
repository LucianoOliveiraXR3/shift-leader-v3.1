package br.com.crearesistemas.shift_leader.db_service

import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Converters {

    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    /**
     * String to java.time.OffsetDateTime
     */
    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    /**
     * java.time.OffsetDateTime to String
     */
    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }

//    /**
//     * Long to java.util.Date
//     */
//    @TypeConverter
//    fun toDate(value: Long?): Date? {
//        return value?.let { Date(it) }
//    }
//
//    /**
//     * java.util.Date to Long
//     */
   @TypeConverter
   fun fromDate(date: Date?): Long? {
        return date?.time
    }
}