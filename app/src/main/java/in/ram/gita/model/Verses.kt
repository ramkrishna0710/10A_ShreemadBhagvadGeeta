package `in`.ram.gita.model


import com.google.gson.annotations.SerializedName

class Verses : ArrayList<Verses.VersesItem>(){
    data class VersesItem(
        @SerializedName("chapter_number")
        val chapterNumber: Int?,
        @SerializedName("commentaries")
        val commentaries: List<Commentary?>?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("slug")
        val slug: String?,
        @SerializedName("text")
        val text: String?,
        @SerializedName("translations")
        val translations: List<Translation?>?,
        @SerializedName("transliteration")
        val transliteration: String?,
        @SerializedName("verse_number")
        val verseNumber: Int?,
        @SerializedName("word_meanings")
        val wordMeanings: String?
    ) {
        data class Commentary(
            @SerializedName("author_name")
            val authorName: String?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("language")
            val language: String?
        )
    
        data class Translation(
            @SerializedName("author_name")
            val authorName: String?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("language")
            val language: String?
        )
    }
}