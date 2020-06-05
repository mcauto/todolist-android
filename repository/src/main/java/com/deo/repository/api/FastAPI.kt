package com.deo.repository.api

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

data class Todo(
    @SerializedName("seqno")
    val seqno: Int,
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

interface AuthService {
    @FormUrlEncoded
    @POST("/api/token")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("scope") scope: String = ""
    ): TokenResponse
}

interface TodoService {
    @GET("/api/v1/todos")
    suspend fun getAll(@Header("Authorization") auth: String): List<Todo>
}

suspend fun getTodoItems(
    baseUrl: String,
    username: String,
    password: String,
    scopes: String
): List<Todo> {
    // Dependency injection
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val todoService: TodoService = retrofit.create(TodoService::class.java)
    val authService: AuthService = retrofit.create(AuthService::class.java)
    return todoService.getAll(
        auth = "Bearer ${authService.getToken(
            username = username,
            password = password,
            scope = scopes
        ).accessToken}"
    )
}
