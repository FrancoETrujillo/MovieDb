package com.ftrujillo.moviedbsample.data.data_source.remote

import com.ftrujillo.moviedbsample.ResourceHelper
import com.ftrujillo.moviedbsample.core.utils.RemoteRequestWrapper
import com.ftrujillo.moviedbsample.di.createMovieApi
import com.ftrujillo.moviedbsample.di.createOkHttpClient
import com.ftrujillo.moviedbsample.domain.datamodel.Movie
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class MovieDbApiSourceTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        // Your KoinApplication instance here
        modules(module {
            single {
                createMovieApi("http://localhost:8080/", createOkHttpClient())
            }
            single { MovieDbApiSource(get()) }
        })
    }

    private val movieDbApiSource by inject<MovieDbApiSource>()
    private lateinit var mockedServer:MockWebServer

    @Before
    fun setUp() {
        //test json load
        mockedServer = MockWebServer()
        mockedServer.start(8080)
    }

    @After
    fun tearDown() {
        mockedServer.shutdown()
    }

    @Test
    fun `get popular movies returns error`() {
        mockedServer.dispatcher = errorDispatcher()

        runBlocking {
            val popularMovies = movieDbApiSource.getPopularMoviesByPage("en", 1)
            assert(popularMovies is RemoteRequestWrapper.Error)
        }
    }

    @Test
    fun `get popular movies returns success`() {
        mockedServer.dispatcher = successDispatcher()

        runBlocking {
            val popularMovies = movieDbApiSource.getPopularMoviesByPage("en", 1)
            assertThat(popularMovies is RemoteRequestWrapper.Success).isTrue()
        }
    }

    @Test
    fun `get popular movies returns list of movies`() {
        mockedServer.dispatcher = successDispatcher()

        runBlocking {
            val popularMovies = movieDbApiSource.getPopularMoviesByPage("en", 1)
            assertThat(popularMovies).isInstanceOf(RemoteRequestWrapper.Success::class.java)
            popularMovies as RemoteRequestWrapper.Success
            assertThat(popularMovies.result).isNotEmpty()
        }
    }

    fun successDispatcher(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    else -> MockResponse().setResponseCode(200)
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("Cache-Control", "no-cache")
                        .setBody(ResourceHelper.loadString("movies_api_response_body.json"))
                }
            }
        }
    }

    fun errorDispatcher(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    else -> MockResponse().setResponseCode(404).setBody("{error}")
                }
            }
        }
    }
}