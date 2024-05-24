package local.entity

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GenreConvert {
    @TypeConverter
    fun fromStringArrayList(value: List<Int>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringArrayList(value: String): List<Int> {
        return try {
            Json.decodeFromString(value)
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}
