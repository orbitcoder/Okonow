package com.noitacilppa.okonow.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Data class to represent a Task with all its related data.
 * Used for production-ready "details" views.
 */
data class TaskDetailed(
    @Embedded val task: Task,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val subtasks: List<Subtask>,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val attachments: List<Attachment>,
    
    @Relation(
        associateBy = Junction(
            value = TaskTagCrossRef::class,
            parentColumn = "taskId",
            entityColumn = "tagId"
        ),
        parentColumn = "id",
        entityColumn = "id"
    )
    val tags: List<Tag>
) {
    val completionPercentage: Float
        get() {
            if (subtasks.isEmpty()) return if (task.isCompleted) 1f else 0f
            val completedCount = subtasks.count { it.isCompleted }
            return completedCount.toFloat() / subtasks.size
        }
}
