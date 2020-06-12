package com.deo.todolist

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.deo.repository.api.APIConfig
import com.deo.repository.api.HttpClientConfig
import com.deo.repository.api.TodoAPI

open class Application : MultiDexApplication() {
    companion object {
        var todoAPI: TodoAPI = TodoAPI(
            APIConfig(host = "10.0.2.2", port = 5000),
            HttpClientConfig(
                poolSize = 5,
                poolAliveTimeout = 5,
                connTimeout = 5,
                readTimeout = 5,
                writeTimeout = 5
            )
        )
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}
