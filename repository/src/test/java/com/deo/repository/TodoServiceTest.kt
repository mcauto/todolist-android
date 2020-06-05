package com.deo.repository

import com.deo.repository.api.AuthService
import com.deo.repository.api.Todo
import com.deo.repository.api.TodoService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodoServiceTest {
    // https://github.com/mcauto/todo-list-fastapi
    private val host: String = "http://localhost:5000/"

    // apply dependency injection by dagger (retrofit, services)
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val todoService: TodoService = retrofit.create(TodoService::class.java)
    private val authService: AuthService = retrofit.create(AuthService::class.java)

    private lateinit var todoItems: List<Todo>

    @Test
    fun getAll() {
        GlobalScope.launch {
            // authentication header
            try {
                todoItems = todoService.getAll(
                    auth = "Bearer ${authService.getToken(
                        username = "tester",
                        password = "imdeo",
                        scope = "TODOS/GET"
                    ).accessToken}"
                )
                print(todoItems)
            } catch (exception: retrofit2.HttpException) {
                print(exception.message())
            }
        }
//        sleep(10000L)
    }
}
