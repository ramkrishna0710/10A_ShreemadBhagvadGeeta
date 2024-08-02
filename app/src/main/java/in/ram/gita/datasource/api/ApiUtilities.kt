package `in`.ram.gita.datasource.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {

    val headers = mapOf<String,String>(
        "Accept" to "Application/json",
        "X-RapidAPI-Key" to "c29edf8d66msh0d6365cf9ace13bp19dad5jsn52ad17c92f78",
        "X-RapidAPI-Host" to "bhagavad-gita3.p.rapidapi.com"
    )

    val client = OkHttpClient.Builder().apply {
        addInterceptor{chain ->

            val newRequest = chain.request().newBuilder().apply {
                headers.forEach { key, value -> addHeader(key,value) }
            }.build()

            chain.proceed(newRequest)
        }
    }.build()

    val api : ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://bhagavad-gita3.p.rapidapi.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}