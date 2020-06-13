package com.deo.repository

import com.deo.repository.api.AuthService
import com.deo.repository.api.Todo
import com.deo.repository.api.TodoService
import com.deo.repository.api.TodoUsecase
import com.deo.repository.api.TokenSecurityScope
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class TodoServiceTest {
//     https://github.com/mcauto/todo-list-fastapi
//     (android) http://10.0.2.2:5000/
//     (native) http://localhost:5000/
//     private val host: String = "http://localhost:5000/"

    @MockK
    lateinit var authService: AuthService

    @MockK
    lateinit var todoService: TodoService

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun getTodoItems() {
        var todoItems: List<Todo> = listOf()
        val username = "tester"
        val password = "${List(10) { Random.nextInt(0, 100) }}"
        val usecase = TodoUsecase(authService, todoService)
        val scopes: List<TokenSecurityScope> =
            listOf(TokenSecurityScope.FETCH, TokenSecurityScope.CREATE)
        every {
            runBlocking {
                todoItems = usecase.getTodoItems(
                    username = username,
                    password = password,
                    scopes = scopes
                )
            }
        } answers {
            print(todoItems)
        }
    }
}
