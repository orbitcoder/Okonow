package com.noitacilppa.okonow.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noitacilppa.okonow.data.Task
import com.noitacilppa.okonow.data.TaskDetailed
import com.noitacilppa.okonow.data.TodoItem
import com.noitacilppa.okonow.data.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TodoUiState(
    val itemList: List<TodoItem> = listOf(),
    val tasks: List<TaskDetailed> = listOf()
)

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {

    // For now, we use a hardcoded userId 1. In a real app, this would come from a Session/User manager.
    private val currentUserId = 1L

    val uiState: StateFlow<TodoUiState> =
        todoRepository.getTasksDetailedStream(currentUserId).map { TodoUiState(tasks = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = TodoUiState()
            )

    fun addTask(title: String, description: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            todoRepository.insertTask(
                Task(
                    userId = currentUserId,
                    title = title,
                    details = description
                )
            )
        }
    }

    fun addItem(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            todoRepository.insertItem(TodoItem(title = title))
        }
    }

    fun toggleDone(item: TodoItem) {
        viewModelScope.launch {
            todoRepository.updateItem(item.copy(isDone = !item.isDone))
        }
    }

    fun toggleTaskDone(task: Task) {
        viewModelScope.launch {
            todoRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteItem(item: TodoItem) {
        viewModelScope.launch {
            todoRepository.deleteItem(item)
        }
    }
}
