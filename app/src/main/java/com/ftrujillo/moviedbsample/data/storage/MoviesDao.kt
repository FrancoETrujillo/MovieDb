package com.ftrujillo.moviedbsample.data.storage

import androidx.room.*
import com.ftrujillo.moviedbsample.domain.datamodel.Movie
import com.ftrujillo.moviedbsample.domain.datamodel.MovieDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovies(movies:List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMoviesDetails(movies:List<MovieDetails>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMoviesDetails(movies:MovieDetails)

    @Query("SELECT COUNT(*) FROM movie_table")
    suspend fun getSavedMoviesCount():Int

    @Query("SELECT * FROM movie_table")
    fun getMoviesFlow(): Flow<List<Movie>>

    @Query("SELECT * FROM movie_table")
    suspend fun getMovies(): List<Movie>

    @Transaction
    @Query("SELECT * FROM movie_details WHERE id = :id")
    suspend fun getMovieDetail(id:Int): MovieDetails?

    @Query("DELETE FROM movie_table")
    suspend fun clearSavedMovies()
}