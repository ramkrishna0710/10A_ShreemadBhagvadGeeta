package `in`.ram.gita.datasource.api.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedChaptersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(savedChapters: SavedChapters)

    @Query("SELECT * FROM SavedChapters")
    fun getChapters(): LiveData<List<SavedChapters>>

    @Query("DELETE FROM SavedChapters WHERE id = :id")
    suspend fun deleteChapter(id : Int)

    @Query("SELECT * FROM SavedChapters WHERE chapterNumber = :chapterNumber")
    fun getAParticularChapter(chapterNumber : Int) : LiveData<SavedChapters>
}

@Dao
interface SavedVersesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnglishVerse(verseInEnglish : SavedVerses)

    @Query("SELECT * FROM SavedVerses")
    fun getAllEnglishVerse() : LiveData<List<SavedVerses>>

    @Query("SELECT * FROM SavedVerses WHERE chapterNumber = :chapterNumber AND verseNumber = :verseNumber")
    fun getParticularVerse(chapterNumber: Int,verseNumber: Int) : LiveData<SavedVerses>

    @Query("SELECT * FROM SavedVerses WHERE chapterNumber = :chapterNumber AND verseNumber = :verseNumber")
    fun deleteAParticularVerse(chapterNumber: Int,verseNumber: Int): LiveData<SavedVerses>

}