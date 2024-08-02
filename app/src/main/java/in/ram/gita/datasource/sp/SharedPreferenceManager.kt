package `in`.ram.gita.datasource.api.sp

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(val context: Context) {

    val sharedPreferences : SharedPreferences by lazy {
        context.getSharedPreferences("saved_chapters",Context.MODE_PRIVATE)
    }

    fun getAllSavedChaptersKeySP() : Set<String>{
        return sharedPreferences.all.keys
    }

    fun putSavedChaptersSP(key : String, value : Int) {
        sharedPreferences.edit().putInt(key,value).apply()
    }

    fun deleteSavedChaptersSP(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}