package com.deo.todolist

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.deo.repository.api.Todo
import com.deo.repository.api.getTodoItems
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var todoItems: List<Todo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            if (::todoItems.isInitialized) {
                textView.text = todoItems.toString()
            } else {
                textView.text = "todo Items를 불러오지 못했습니다"
            }
        }
        GlobalScope.launch(Dispatchers.IO) {
            try {
                todoItems = getTodoItems(
                    baseUrl = "http://10.0.2.2:5000/",
                    username = "tester",
                    password = "imdeo",
                    scopes = "TODOS/GET"
                )
            } catch (exception: retrofit2.HttpException) {
                Log.d("MainActivity", "onCreate: ${exception.message}")
            }
        }
    }
}
