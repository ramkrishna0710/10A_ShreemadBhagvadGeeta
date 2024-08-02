package `in`.ram.gita.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import `in`.ram.gita.datasource.api.room.AppDataBase
import `in`.ram.gita.datasource.api.room.SavedChapters
import `in`.ram.gita.datasource.api.room.SavedVerses
import `in`.ram.gita.datasource.api.sp.SharedPreferenceManager
import `in`.ram.gita.model.Chapters
import `in`.ram.gita.model.Verses
import `in`.ram.gita.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class MainViewModel(application: Application) : AndroidViewModel(application){

    val savedChaptersDao = AppDataBase.getDatabaseInstance(application)?.savedChaptersDao()
    val savedVersesDao = AppDataBase.getDatabaseInstance(application)?.savedVersesDao()
    val sharedPreferenceManager = SharedPreferenceManager(application)

    val appRepository = AppRepository(savedChaptersDao!!, savedVersesDao!!, sharedPreferenceManager)

    fun getAllChapters() : Flow<List<Chapters.ChaptersItem>> = appRepository.getAllChapters()

    fun getVerses(chapterNumber : Int) : Flow<List<Verses.VersesItem>> = appRepository.getVerses(chapterNumber)

    fun getAParticularVerse(chapterNumber: Int,verseNumber: Int) : Flow<Verses.VersesItem> = appRepository.getAParticularVerse(chapterNumber,verseNumber)

    // saved chapters
    suspend fun insertChapters(savedChapters: SavedChapters) = appRepository.insertChapters(savedChapters)

    fun getChapters(): LiveData<List<SavedChapters>> = appRepository.getChapters()

    fun getAParticularChapter(chapterNumber : Int) : LiveData<SavedChapters> = appRepository.getAParticularChapter(chapterNumber)

    suspend fun deleteChapter(id : Int) = appRepository.deleteChapter(id)


    //saved verses

    suspend fun insertEnglishVerse(verseInEnglish : SavedVerses) = appRepository.insertEnglishVerse(verseInEnglish)
    fun getAllEnglishVerse() : LiveData<List<SavedVerses>> = appRepository.getAllEnglishVerse()
    fun getParticularVerse(chapterNumber: Int,verseNumber: Int) : LiveData<SavedVerses> = appRepository.getParticularVerse(chapterNumber,verseNumber)
    fun deleteAParticularVerse(chapterNumber: Int,verseNumber: Int) = appRepository.deleteAParticularVerse(chapterNumber,verseNumber)

    fun getAllSavedChaptersKeySP() : Set<String> = appRepository.getAllSavedChaptersKeySP()
    fun putSavedChaptersSP(key : String, value : Int) = appRepository.putSavedChaptersSP(key,value)
    fun deleteSavedChaptersSP(key: String) = appRepository.deleteSavedChaptersSP(key)

}