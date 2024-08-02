package `in`.ram.gita.model


import com.google.gson.annotations.SerializedName

class Chapters : ArrayList<Chapters.ChaptersItem>(){
    data class ChaptersItem(
        @SerializedName("chapter_number")
        val chapterNumber: Int?,
        @SerializedName("chapter_summary")
        val chapterSummary: String?,
        @SerializedName("chapter_summary_hindi")
        val chapterSummaryHindi: String?,
        @SerializedName("id")
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
        val versesCount: Int?
    )
}