package `in`.ram.gita.repository

import androidx.lifecycle.LiveData
import `in`.ram.gita.datasource.api.ApiUtilities
import `in`.ram.gita.datasource.api.room.SavedChapters
import `in`.ram.gita.datasource.api.room.SavedChaptersDao
import `in`.ram.gita.datasource.api.room.SavedVerses
import `in`.ram.gita.datasource.api.room.SavedVersesDao
import `in`.ram.gita.datasource.api.sp.SharedPreferenceManager
import `in`.ram.gita.model.Chapters
import `in`.ram.gita.model.Verses
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository (val savedChaptersDao: SavedChaptersDao, val savedVersesDao: SavedVersesDao , val sharedPreferenceManager: SharedPreferenceManager) {

    fun getAllChapters() : Flow<List<Chapters.ChaptersItem>> = callbackFlow {
        val callback = object : Callback<List<Chapters.ChaptersItem>>{
            override fun onResponse(
                call: Call<List<Chapters.ChaptersItem>>,
                response: Response<List<Chapters.ChaptersItem>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<List<Chapters.ChaptersItem>>, t: Throwable) {
                close(t)
            }
        }

        ApiUtilities.api.getAllChapters().enqueue(callback)

        awaitClose {}
    }

    fun getVerses(chapterNumber : Int) : Flow<List<Verses.VersesItem>> = callbackFlow {
        val callback = object : Callback<List<Verses.VersesItem>> {
            override fun onResponse(
                call: Call<List<Verses.VersesItem>>,
                response: Response<List<Verses.VersesItem>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<List<Verses.VersesItem>>, t: Throwable) {
                close(t)
            }
        }
        ApiUtilities.api.getVerses(chapterNumber).enqueue(callback)
        awaitClose{}
    }

    fun getAParticularVerse(chapterNumber: Int,verseNumber: Int) : Flow<Verses.VersesItem> = callbackFlow {
        val callback = object : Callback<Verses.VersesItem>{
            override fun onResponse(
                call: Call<Verses.VersesItem>,
                response: Response<Verses.VersesItem>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<Verses.VersesItem>, t: Throwable) {
                close(t)
            }
        }
        ApiUtilities.api.getAParticularVerse(chapterNumber,verseNumber).enqueue(callback)

        awaitClose {  }
    }

    // saved chapters
    suspend fun insertChapters(savedChapters: SavedChapters) = savedChaptersDao.insertChapters(savedChapters)

    fun getChapters(): LiveData<List<SavedChapters>> = savedChaptersDao.getChapters()

    fun getAParticularChapter(chapterNumber : Int) : LiveData<SavedChapters> = savedChaptersDao.getAParticularChapter(chapterNumber)

    suspend fun deleteChapter(id : Int) = savedChaptersDao.deleteChapter(id)

    // saved verses

    suspend fun insertEnglishVerse(verseInEnglish : SavedVerses) = savedVersesDao.insertEnglishVerse(verseInEnglish)
    fun getAllEnglishVerse() : LiveData<List<SavedVerses>> = savedVersesDao.getAllEnglishVerse()
    fun getParticularVerse(chapterNumber: Int,verseNumber: Int) : LiveData<SavedVerses> = savedVersesDao.getParticularVerse(chapterNumber,verseNumber)
    fun deleteAParticularVerse(chapterNumber: Int,verseNumber: Int) = savedVersesDao.deleteAParticularVerse(chapterNumber,verseNumber)

    // saved chapters in sp

    fun getAllSavedChaptersKeySP() : Set<String> = sharedPreferenceManager.getAllSavedChaptersKeySP()
    fun putSavedChaptersSP(key : String, value : Int) = sharedPreferenceManager.putSavedChaptersSP(key,value)
    fun deleteSavedChaptersSP(key: String) = sharedPreferenceManager.deleteSavedChaptersSP(key)

}