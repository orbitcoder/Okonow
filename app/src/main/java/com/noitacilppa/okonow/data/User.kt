package com.noitacilppa.okonow.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "users"
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val profileImageUri: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
