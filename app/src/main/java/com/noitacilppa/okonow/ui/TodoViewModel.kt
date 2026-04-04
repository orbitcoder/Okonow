package com.noitacilppa.okonow.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noitacilppa.okonow.data.TodoItem
import com.noitacilppa.okonow.data.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TodoUiState(
    val itemList: List<TodoItem> = listOf()
)

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {

    val uiState: StateFlow<TodoUiState> =
        todoRepository.getAllItemsStream().map { TodoUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = TodoUiState()
            )

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

    fun deleteItem(item: TodoItem) {
        viewModelScope.launch {
            todoRepository.deleteItem(item)
        }
    }
}
