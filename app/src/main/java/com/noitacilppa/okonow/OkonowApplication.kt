package com.noitacilppa.okonow

import android.app.Application
import com.noitacilppa.okonow.data.OfflineTodoRepository
import com.noitacilppa.okonow.data.TodoDatabase
import com.noitacilppa.okonow.data.TodoRepository

class OkonowApplication : Application() {
    lateinit var todoRepository: TodoRepository

    override fun onCreate() {
        super.onCreate()
        val database = TodoDatabase.getDatabase(this)
        todoRepository = OfflineTodoRepository(database.todoDao())
    }
}
