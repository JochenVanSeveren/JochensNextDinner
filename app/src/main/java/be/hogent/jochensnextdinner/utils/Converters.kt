package be.hogent.jochensnextdinner.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * This class provides methods to convert certain complex types that are not directly supported by Room
 * into types that Room can persist in the database.
 */
class Converters {
    /**
     * Converts a JSON string into a List of Strings.
     *
     * @param value The JSON string.
     * @return The List of Strings.
     */
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    /**
     * Converts a List of Strings into a JSON string.
     *
     * @param list The List of Strings.
     * @return The JSON string.
     */
    @TypeConverter
    fun fromList(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}