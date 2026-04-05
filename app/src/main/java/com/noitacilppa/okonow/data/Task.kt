package com.noitacilppa.okonow.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["slug"], unique = true),
        Index(value = ["isCompleted", "endTime"])
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val title: String,
    val details: String, // Supports HTML
    val attachmentUri: String? = null, // URI of the attached file
    val contentFormat: ContentFormat = ContentFormat.PARAGRAPH,
    val priority: Priority = Priority.MEDIUM,
    val startTime: Date? = null,
    val endTime: Date? = null,
    val reminderTime: Date? = null, // Time for local notification
    val isCompleted: Boolean = false,
    val completedAt: Date? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val isDeleted: Boolean = false, // Soft delete
    val slug: String = UUID.randomUUID().toString() // For deep linking or clean URLs
) {
    val isOverdue: Boolean
        get() = !isCompleted && endTime != null && endTime.before(Date())
}
