package com.ftrujillo.moviedbsample.data.repository

import app.cash.turbine.test
import com.ftrujillo.moviedbsample.core.utils.AppDispatchers
import com.ftrujillo.moviedbsample.core.utils.RemoteRequestWrapper
import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
import com.ftrujillo.moviedbsample.data.data_source.remote.MoviesRemoteSource
import com.ftrujillo.moviedbsample.data.storage.MoviesDao
import com.ftrujillo.moviedbsample.domain.datamodel.Movie
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class MoviesRepositoryImplTest {


    @MockK
    private lateinit var remoteSource: MoviesRemoteSource

    @MockK
    private lateinit var moviesDao: MoviesDao

    private lateinit var moviesRepository: MoviesRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        moviesRepository = MoviesRepositoryImpl(remoteSource, AppDispatchers(), moviesDao)

    }

    @Test
    fun `test get popular movies emit success`() {

        runBlocking {
            coEvery {
                remoteSource.getPopularMoviesByPage(
                    any(),
                    any()
                )
            } returns (RemoteRequestWrapper.Success(createMovieList()))
            coEvery {
                moviesDao.getMovies()
            } returns (createMovieList())
            coEvery { moviesDao.upsertMovies(any()) } returns Unit

            moviesRepository.getPopularMovies(true).test {
                assert(awaitItem() is RequestDataWrapper.Loading)
                val successEmission = awaitItem()
                assert(successEmission is RequestDataWrapper.Success)
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    private fun createMovieList(): List<Movie> {
        val movieList = mutableListOf<Movie>()
        movieList.add(
            Movie(
                "bck",
                1,
                "sdsdsd",
                32.5,
                "ssd",
                "sds"
            )
        )
        return movieList
    }

    @Test
    fun `test get popular movies correct success data conversion`() {
        runBlocking {
            val movieList = MutableList(10) { mockk<Movie>() }
            coEvery {
                remoteSource.getPopularMoviesByPage(
                    any(),
                    any()
                )
            } returns (RemoteRequestWrapper.Success(movieList))

            moviesRepository.getPopularMovies(true).test {
                assert(awaitItem() is RequestDataWrapper.Loading)
                val successEmission = awaitItem()
                assert(successEmission is RequestDataWrapper.Success)
//                assertEquals(movieList.size, successEmission.result?.size)
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `test get popular movies emit loading`() {
        runBlocking {
            val movieList = MutableList(10) { mockk<Movie>() }
            coEvery {
                remoteSource.getPopularMoviesByPage(
                    any(),
                    any()
                )
            } returns (RemoteRequestWrapper.Success(movieList))

            moviesRepository.getPopularMovies(true).test {
                val emission = awaitItem()
                assert(emission is RequestDataWrapper.Loading)
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `test get popular movies emit error`() {
        runBlocking {
            coEvery {
                remoteSource.getPopularMoviesByPage(
                    any(),
                    any()
                )
            } returns (RemoteRequestWrapper.Error(""))

            moviesRepository.getPopularMovies(true).test {
                awaitItem()
                val emission = awaitItem()
                assert(emission is RequestDataWrapper.Error)
                cancelAndConsumeRemainingEvents()
            }
        }
    }
}