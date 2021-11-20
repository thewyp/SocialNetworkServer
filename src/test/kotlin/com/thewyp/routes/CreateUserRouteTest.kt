package com.thewyp.routes

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.thewyp.data.models.User
import com.thewyp.data.requests.CreateAccountRequest
import com.thewyp.data.responses.BasicApiResponse
import com.thewyp.di.testModule
import com.thewyp.plugins.configureSerialization
import com.thewyp.data.repository.user.FakeUserRepository
import com.thewyp.data.requests.LoginRequest
import com.thewyp.util.ApiResponseMessages
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import io.netty.handler.codec.http.HttpHeaders.addHeader
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


internal class CreateUserRouteTest : KoinTest {

    private val userRepository by inject<FakeUserRepository>()

    private val gson = Gson()

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(testModule)
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Create user, no body attached, responds with BadRequest`() {
        withTestApplication(
            moduleFunction = {
                install(Routing) {
                    createUserRoute(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/create"
            )

            assertThat(request.response.status()).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `Create user, user already exists, responds with unsuccessful`() = runBlocking {
        createFakeUser()
        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    createUserRoute(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/create"
            ) {
                addHeader("Content-Type", "application/json")
                val request = CreateAccountRequest(
                    email = "test@test.com",
                    username = "asdf",
                    password = "asdf"
                )
                setBody(gson.toJson(request))
            }

            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )
            assertThat(response.successful).isFalse()
            assertThat(response.message).isEqualTo(ApiResponseMessages.USER_ALREADY_EXISTS)
        }
    }

    private suspend fun createFakeUser() {
        val user = User(
            email = "test@test.com",
            username = "test",
            password = "test",
            profileImageUrl = "",
            bio = "",
            gitHubUrl = null,
            instagramUrl = null,
            linkedInUrl = null
        )
        userRepository.createUser(user)
    }

    @Test
    fun `Create user, email is empty, responds with unsuccessful`() = runBlocking {
        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    createUserRoute(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/create"
            ) {
                addHeader("Content-Type", "application/json")
                val request = CreateAccountRequest(
                    email = "",
                    username = "",
                    password = ""
                )
                setBody(gson.toJson(request))
            }

            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )
            assertThat(response.successful).isFalse()
            assertThat(response.message).isEqualTo(ApiResponseMessages.FIELDS_BLANK)
        }
    }

    @Test
    fun `Create user, valid data, responds with successful`() {
        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    createUserRoute(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/create"
            ) {
                addHeader("Content-Type", "application/json")
                val request = CreateAccountRequest(
                    email = "test@test.com",
                    username = "test",
                    password = "test"
                )
                setBody(gson.toJson(request))
            }

            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )
            assertThat(response.successful).isTrue()

            runBlocking {
                val isUserInDb = userRepository.getUserByEmail("test@test.com") != null
                assertThat(isUserInDb).isTrue()
            }
        }
    }

    @Test
    fun `Login user, no body attached, responds with BadRequest`() = runBlocking {
        createFakeUser()
        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    loginUser(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/login"
            )
            assertThat(request.response.status()).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `Login user, email is empty, responds with unsuccessful`() = runBlocking {
        createFakeUser()
        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    loginUser(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/login"
            ) {
                addHeader("Content-Type", "application/json")
                val request = LoginRequest(
                    email = "",
                    password = ""
                )
                setBody(gson.toJson(request))
            }

            assertThat(request.response.status()).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `Login user, invalid password, responds with unsuccessful`() = runBlocking {
        createFakeUser()
        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    loginUser(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/login"
            ) {
                addHeader("Content-Type", "application/json")
                val request = LoginRequest(
                    email = "test@test.com",
                    password = "text"
                )
                setBody(gson.toJson(request))
            }

            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )
            assertThat(response.successful).isFalse()
            assertThat(response.message).isEqualTo(ApiResponseMessages.INVALID_CREDENTIALS)
        }
    }

    @Test
    fun `Login user, valid data, responds with successful`() = runBlocking {
        createFakeUser()
        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    loginUser(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/login"
            ) {
                addHeader("Content-Type", "application/json")
                val request = LoginRequest(
                    email = "test@test.com",
                    password = "test"
                )
                setBody(gson.toJson(request))
            }

            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )
            assertThat(response.successful).isTrue()
        }
    }
}