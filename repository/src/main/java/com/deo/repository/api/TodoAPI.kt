package com.deo.repository.api

import com.google.gson.annotations.SerializedName
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

data class Todo(
    @SerializedName("seqno")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("is_done")
    val isDone: Boolean
)

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String
)

enum class TokenSecurityScope(val scope: String) {
    CREATE("TODOS/POST"),
    FETCH("TODOS/GET"),
    UPDATE("TODOS/PATCH"),
    DELETE("TODOS/DELETE");

    fun scope() = scope
}

interface AuthService {
    @FormUrlEncoded
    @POST("/api/token")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("scope") scope: String = "${TokenSecurityScope.FETCH}"
    ): TokenResponse
}

interface TodoService {
    @GET("/api/v1/todos")
    suspend fun getAll(@Header("Authorization") auth: String): List<Todo>
}

@Module
class TodoModule constructor(
    val retrofit: Retrofit
) {

    @Singleton
    @Provides
    fun provideTodoService(): TodoService {
        return retrofit.create(TodoService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthService(): AuthService {
        return retrofit.create(AuthService::class.java)
    }
}

@Singleton
@Component(modules = [TodoModule::class])
interface TodoComponent {
    fun inject(api: TodoAPI)
    fun authService(): AuthService
    fun todoService(): TodoService
}

class TodoUsecase @Inject constructor(
    private val authService: AuthService,
    private val todoService: TodoService
) {
    suspend fun getTodoItems(
        username: String,
        password: String,
        scopes: List<TokenSecurityScope>
    ): List<Todo> {
        var todoItems: List<Todo> = listOf()
        try {
            todoItems = todoService.getAll(
                auth = "Bearer ${authService.getToken(
                    username = username,
                    password = password,
                    scope = scopes.joinToString(separator = " ") { it.scope() }
                ).accessToken}"
            )
        } catch (exception: retrofit2.HttpException) {
            print(exception.message)
        } catch (timeout: java.net.SocketTimeoutException) {
            print(timeout)
        }
        return todoItems
    }
}

data class APIConfig(
    val host: String,
    val port: Int
)

data class HttpClientConfig(
    val poolSize: Int,
    val poolAliveTimeout: Long,
    val connTimeout: Long,
    val readTimeout: Long,
    val writeTimeout: Long
)

class TodoAPI
constructor(
    apiConfig: APIConfig,
    httpClientConfig: HttpClientConfig
) {
    init {
        val client = OkHttpClient.Builder()
            .connectionPool(
                ConnectionPool(
                    httpClientConfig.poolSize,
                    httpClientConfig.poolAliveTimeout,
                    TimeUnit.MILLISECONDS
                )
            )
            .connectTimeout(httpClientConfig.connTimeout, TimeUnit.SECONDS)
            .readTimeout(httpClientConfig.readTimeout, TimeUnit.SECONDS)
            .writeTimeout(httpClientConfig.writeTimeout, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://${apiConfig.host}:${apiConfig.port}/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val todoModule = TodoModule(retrofit)
        DaggerTodoComponent.builder()
            .todoModule(todoModule)
            .build()
            .inject(this)
    }

    @Inject
    lateinit var todoUsecase: TodoUsecase
}

/*
fun main() {
    val apiConfig = APIConfig(host = "localhost", port = 5000)
    val httpClientConfig = HttpClientConfig(
        poolSize = 5,
        poolAliveTimeout = 5,
        connTimeout = 5,
        readTimeout = 5,
        writeTimeout = 5
    )
    val api = TodoAPI(apiConfig, httpClientConfig)
    var todoItems: List<Todo>
    runBlocking {
        todoItems = api.todoUsecase.getTodoItems(
            username = "tester",
            password = "imdeo",
            scopes = listOf(TokenSecurityScope.FETCH)
        )
    }
}
*/
