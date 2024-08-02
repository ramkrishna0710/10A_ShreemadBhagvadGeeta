package `in`.ram.gita.datasource.api.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.ram.gita.model.Verses

class AppTypeConverters {

    @TypeConverter
    fun fromListToString(list: List<String>) : String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToList(string: String): List<String> {
        return Gson().fromJson(string,object : TypeToken<List<String>>(){}.type)
    }

    @TypeConverter
    fun fromTranslationToString(list: List<Verses.VersesItem.Translation>) : String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToTranslation(string: String): List<Verses.VersesItem.Translation> {
        return Gson().fromJson(string,object : TypeToken<List<Verses.VersesItem.Translation>>(){}.type)
    }

@TypeConverter
    fun fromCommentaryToString(list: List<Verses.VersesItem.Commentary>) : String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToCommentary(string: String): List<Verses.VersesItem.Commentary> {
        return Gson().fromJson(string,object : TypeToken<List<Verses.VersesItem.Commentary>>(){}.type)
    }


}