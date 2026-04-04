package com.noitacilppa.okonow.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    // --- User Operations ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Long): Flow<User?>

    // --- Task Operations ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("UPDATE tasks SET isDeleted = 1, updatedAt = :timestamp WHERE id = :taskId")
    suspend fun softDeleteTask(taskId: Long, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM tasks WHERE userId = :userId AND isDeleted = 0 ORDER BY createdAt DESC")
    fun getTasksForUser(userId: Long): Flow<List<Task>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId AND isDeleted = 0")
    fun getTaskDetailed(taskId: Long): Flow<TaskDetailed?>

    @Transaction
    @Query("SELECT * FROM tasks WHERE userId = :userId AND isDeleted = 0 ORDER BY priority DESC, endTime ASC")
    fun getTasksDetailedForUser(userId: Long): Flow<List<TaskDetailed>>

    // --- Subtask Operations ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtasks(subtasks: List<Subtask>)

    @Query("DELETE FROM subtasks WHERE taskId = :taskId")
    suspend fun deleteSubtasksForTask(taskId: Long)

    // --- Tag Operations ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: Tag): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTaskTagCrossRef(crossRef: TaskTagCrossRef)

    @Transaction
    suspend fun updateTaskTags(taskId: Long, tags: List<Tag>) {
        deleteTagsForTask(taskId)
        tags.forEach { tag ->
            val tagId = insertTag(tag)
            val finalTagId = if (tagId == -1L) getTagByName(tag.name)?.id ?: return@forEach else tagId
            insertTaskTagCrossRef(TaskTagCrossRef(taskId, finalTagId))
        }
    }

    @Query("DELETE FROM task_tag_cross_ref WHERE taskId = :taskId")
    suspend fun deleteTagsForTask(taskId: Long)

    @Query("SELECT * FROM tags WHERE name = :name")
    suspend fun getTagByName(name: String): Tag?

    // --- Attachment Operations ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachment(attachment: Attachment)

    @Query("DELETE FROM attachments WHERE taskId = :taskId")
    suspend fun deleteAttachmentsForTask(taskId: Long)

    // --- TodoItem Operations ---
    @Query("SELECT * from todo_items ORDER BY title ASC")
    fun getAllItems(): Flow<List<TodoItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: TodoItem)

    @Update
    suspend fun updateItem(item: TodoItem)

    @Delete
    suspend fun deleteItem(item: TodoItem)
}
