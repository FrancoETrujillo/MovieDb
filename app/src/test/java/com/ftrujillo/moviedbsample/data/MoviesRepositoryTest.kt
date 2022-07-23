package com.ftrujillo.moviedbsample.data

import com.ftrujillo.moviedbsample.ResourceHelper
import com.ftrujillo.moviedbsample.data.data_source.remote.MovieDbApi
import com.ftrujillo.moviedbsample.data.repository.MoviesRepositoryImpl
import com.ftrujillo.moviedbsample.di.provideOkHttpClient
import com.ftrujillo.moviedbsample.core.utils.NetworkInfoProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.HttpUrl.Companion.toHttpUrl
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
import org.koin.test.get
import org.mockito.MockitoAnnotations.openMocks
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@ExperimentalCoroutinesApi
class MoviesRepositoryTest : KoinTest {

    lateinit var api: MovieDbApi

    lateinit var moviesRepository: MoviesRepositoryImpl

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        // Your KoinApplication instance here
        modules(module {
            single { NetworkInfoProvider(get()) }
            single<MovieDbApi> {
                Retrofit.Builder()
                    .baseUrl("http://localhost:8080/".toHttpUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient())
                    .build()
                    .create(MovieDbApi::class.java)
            }
            single { MoviesRepositoryImpl(get()) }

        })

    }

    @Before
    fun setUp() {
        openMocks(this)
        moviesRepository = get()

    }

    @After
    fun tearDown() {

    }

    @Test
    fun getPopularMovies() {
        println("Franco test starts")

        //test json load


        val server = MockWebServer()


        val response = ResourceHelper.loadString("movies_api_response_body.json").let {
            MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setResponseCode(200)
                .setBody(it)
        }


        println("Franco response gen: ${response.getBody().toString()}")
//            response?.let { server.enqueue(it) }
        server.dispatcher = successDispatcher()
        server.start(8080)
        println("Franco server started at ${server.hostName}")
        runTest {

//            moviesRepository.refresh()

//
//            moviesRepository.popularMovies.test{
//                moviesRepository.refresh()
//                assertNotNull(awaitItem())
//            }
            println("Franco refreshing")

            println("Franco collect")
        }

        server.shutdown()

    }


    fun successDispatcher(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    else -> MockResponse().setResponseCode(200)
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