package com.ftrujillo.moviedbsample.data.storage

import androidx.room.TypeConverter
import com.ftrujillo.moviedbsample.data.data_source.dto.movie_details.Genre
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class DbTypeConverters {
    @TypeConverter
    fun toGenreList(genreJson: String): List<Genre> {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Genre::class.java)
        val adapter = moshi.adapter<List<Genre>>(type)
        return adapter.fromJson(genreJson) ?: listOf()
    }

    @TypeConverter
    fun fromGenreList(genreList: List<Genre>): String {
        val moshi: Moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Genre::class.java)
        val adapter = moshi.adapter<List<Genre>>(type)
        return adapter.toJson(genreList)
    }
}