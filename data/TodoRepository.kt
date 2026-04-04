package com.noitacilppa.okonow.data

import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAllItemsStream(): Flow<List<TodoItem>>
    suspend fun insertItem(item: TodoItem)
    suspend fun deleteItem(item: TodoItem)
    suspend fun updateItem(item: TodoItem)
}

class OfflineTodoRepository(private val todoDao: TodoDao) : TodoRepository {
    override fun getAllItemsStream(): Flow<List<TodoItem>> = todoDao.getAllItems()
    override suspend fun insertItem(item: TodoItem) = todoDao.insertItem(item)
    override suspend fun deleteItem(item: TodoItem) = todoDao.deleteItem(item)
    override suspend fun updateItem(item: TodoItem) = todoDao.updateItem(item)
}
