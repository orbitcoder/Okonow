package com.noitacilppa.okonow.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Task::class,
        Subtask::class,
        Tag::class,
        TaskTagCrossRef::class,
        Attachment::class,
        TodoItem::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var Instance: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TodoDatabase::class.java, "todo_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(TodoDatabaseCallback())
                    .build()
                    .also { Instance = it }
            }
        }
    }

    private class TodoDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Instance?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    prepopulateTags(database.todoDao())
                }
            }
        }

        private suspend fun prepopulateTags(todoDao: TodoDao) {
            val defaultTags = listOf(
                Tag(name = "Work", colorHex = "#9D50FF"),
                Tag(name = "Personal", colorHex = "#50EBD5"),
                Tag(name = "Health", colorHex = "#FF507A"),
                Tag(name = "Finance", colorHex = "#FFD150"),
                Tag(name = "Urgent", colorHex = "#FF3B3B")
            )
            defaultTags.forEach { todoDao.insertTag(it) }
        }
    }
}
