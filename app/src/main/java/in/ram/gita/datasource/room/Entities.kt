package `in`.ram.gita.datasource.api.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import `in`.ram.gita.model.Verses


@Entity(tableName = "SavedChapters")
    data class SavedChapters(
        @SerializedName("chapter_number")
        val chapterNumber: Int?,
        @SerializedName("chapter_summary")
        val chapterSummary: String?,
        @SerializedName("chapter_summary_hindi")
        val chapterSummaryHindi: String?,
        @SerializedName("id")
        @PrimaryKey
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("name_meaning")
        val nameMeaning: String?,
        @SerializedName("name_translated")
        val nameTranslated: String?,
        @SerializedName("name_transliterated")
        val nameTransliterated: String?,
        @SerializedName("slug")
        val slug: String?,
        @SerializedName("verses_count")
        val versesCount: Int?,

        val verses : List<String>
    )

@Entity(tableName = "SavedVerses")
data class SavedVerses(
    @SerializedName("chapter_number")
    val chapterNumber: Int?,
    @SerializedName("commentaries")
    val commentaries: List<Verses.VersesItem.Commentary?>?,
    @SerializedName("id")
    @PrimaryKey
    val id: Int?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("translations")
    val translations: List<Verses.VersesItem.Translation?>?,
    @SerializedName("transliteration")
    val transliteration: String?,
    @SerializedName("verse_number")
    val verseNumber: Int?,
    @SerializedName("word_meanings")
    val wordMeanings: String?
)