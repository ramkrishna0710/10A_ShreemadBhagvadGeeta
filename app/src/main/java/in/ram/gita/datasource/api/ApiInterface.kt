package `in`.ram.gita.datasource.api

import `in`.ram.gita.model.Chapters
import `in`.ram.gita.model.Verses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiInterface {

    @GET("/v2/chapters/")
    fun getAllChapters() : Call<List<Chapters.ChaptersItem>>

    @GET("v2/chapters/{chapterNumber}/verses/")
    fun getVerses(@Path("chapterNumber") chapterNumber: Int) : Call<List<Verses.VersesItem>>

    @GET("v2/chapters/{chapterNumber}/verses/{verseNumber}/")
    fun getAParticularVerse(
        @Path("chapterNumber") chapterNumber: Int,
        @Path("verseNumber") verseNumber: Int,
    ): Call<Verses.VersesItem>
}