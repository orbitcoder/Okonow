package com.noitacilppa.okonow.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface TodoRepository {
    fun getAllItemsStream(): Flow<List<TodoItem>>
    suspend fun insertItem(item: TodoItem)
    suspend fun deleteItem(item: TodoItem)
    suspend fun updateItem(item: TodoItem)

    // Task operations
    fun getTasksDetailedStream(userId: Long): Flow<List<TaskDetailed>>
    suspend fun insertTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)

    // Subtask operations
    suspend fun insertSubtasks(subtasks: List<Subtask>)

    // Tag operations
    suspend fun updateTaskTags(taskId: Long, tags: List<Tag>)

    // User operations
    suspend fun insertUser(user: User): Long
    fun getUserStream(userId: Long): Flow<User?>

    // Global operations
    suspend fun clearAllData()
}

class OfflineTodoRepository(private val database: TodoDatabase) : TodoRepository {
    private val todoDao = database.todoDao()

    override fun getAllItemsStream(): Flow<List<TodoItem>> = todoDao.getAllItems()
    override suspend fun insertItem(item: TodoItem) = todoDao.insertItem(item)
    override suspend fun deleteItem(item: TodoItem) = todoDao.deleteItem(item)
    override suspend fun updateItem(item: TodoItem) = todoDao.updateItem(item)

    override fun getTasksDetailedStream(userId: Long): Flow<List<TaskDetailed>> = 
        todoDao.getTasksDetailedForUser(userId)
    
    override suspend fun insertTask(task: Task): Long = todoDao.insertTask(task)
    override suspend fun updateTask(task: Task) = todoDao.updateTask(task)
    override suspend fun deleteTask(task: Task) = todoDao.deleteTask(task)

    override suspend fun insertSubtasks(subtasks: List<Subtask>) = todoDao.insertSubtasks(subtasks)

    override suspend fun updateTaskTags(taskId: Long, tags: List<Tag>) = todoDao.updateTaskTags(taskId, tags)

    override suspend fun insertUser(user: User): Long = todoDao.insertUser(user)
    override fun getUserStream(userId: Long): Flow<User?> = todoDao.getUserById(userId)

    override suspend fun clearAllData() {
        withContext(Dispatchers.IO) {
            database.clearAllTables()
        }
    }
}
