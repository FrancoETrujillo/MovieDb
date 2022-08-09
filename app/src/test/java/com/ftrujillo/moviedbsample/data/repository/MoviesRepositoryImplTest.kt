package com.ftrujillo.moviedbsample.data.repository

import app.cash.turbine.test
import com.ftrujillo.moviedbsample.core.utils.AppDispatchers
import com.ftrujillo.moviedbsample.core.utils.RemoteRequestWrapper
import com.ftrujillo.moviedbsample.core.utils.RequestDataWrapper
import com.ftrujillo.moviedbsample.data.data_source.remote.MoviesRemoteSource
import com.ftrujillo.moviedbsample.domain.datamodel.Movie
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import kotlin.test.assertEquals

class MoviesRepositoryImplTest {


    @MockK
    private lateinit var remoteSource: MoviesRemoteSource
    private lateinit var moviesRepository: MoviesRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        moviesRepository = MoviesRepositoryImpl(remoteSource, AppDispatchers())
    }

    @Test
    fun `test get popular movies emit success`() {
        runBlocking {
            coEvery {
                remoteSource.getPopularMoviesByPage(
                    any(),
                    any()
                )
            } returns (RemoteRequestWrapper.Success(any()))

            moviesRepository.getPopularMovies().test {
                assert(awaitItem() is RequestDataWrapper.Loading)
                val successEmission = awaitItem()
                assert(successEmission is RequestDataWrapper.Success)
                cancelAndConsumeRemainingEvents()
            }
        }
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

            moviesRepository.getPopularMovies().test {
                assert(awaitItem() is RequestDataWrapper.Loading)
                val successEmission = awaitItem()
                assert(successEmission is RequestDataWrapper.Success)
                assertEquals(movieList.size, successEmission.result?.size)
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

            moviesRepository.getPopularMovies().test {
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

            moviesRepository.getPopularMovies().test {
                awaitItem()
                val emission = awaitItem()
                assert(emission is RequestDataWrapper.Error)
                cancelAndConsumeRemainingEvents()
            }
        }
    }
}