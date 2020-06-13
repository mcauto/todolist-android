package com.deo.todolist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deo.repository.api.Todo
import com.deo.repository.api.TodoUsecase
import com.deo.repository.api.TokenSecurityScope
import com.deo.todolist.Application.Companion.todoAPI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {
    val todoItems = MutableLiveData<List<Todo>>()

    fun fetchTodoItems(
        todoUsecase: TodoUsecase,
        username: String,
        password: String,
        scopes: List<TokenSecurityScope>
    ) {
        viewModelScope.launch {
            todoItems.value = todoUsecase.getTodoItems(
                username = username,
                password = password,
                scopes = scopes
            )
        }
    }
}

class MainActivity : AppCompatActivity() {
    private val viewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.todoItems.observe(this, Observer { textView.text = it.toString() })
        button.setOnClickListener {
            viewModel.fetchTodoItems(
                todoUsecase = todoAPI.todoUsecase,
                username = username.text.toString(),
                password = password.text.toString(),
                scopes = listOf(TokenSecurityScope.FETCH)
            )
            textView.text = "불러오는 중"
        }
    }
}
