package com.noitacilppa.okonow.ui.tasklist

import com.noitacilppa.okonow.data.TaskDetailed
import java.util.Calendar
import java.util.Date

fun groupTasksByDate(tasks: List<TaskDetailed>): Map<String, List<TaskDetailed>> {
    val grouped = mutableMapOf<String, MutableList<TaskDetailed>>(
        "Today" to mutableListOf(),
        "Tomorrow" to mutableListOf(),
        "Upcoming" to mutableListOf()
    )

    val todayCal = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    
    val tomorrowCal = (todayCal.clone() as Calendar).apply {
        add(Calendar.DAY_OF_YEAR, 1)
    }

    val dayAfterTomorrowCal = (todayCal.clone() as Calendar).apply {
        add(Calendar.DAY_OF_YEAR, 2)
    }

    // Sort tasks by startTime (ascending)
    val sortedTasks = tasks.sortedBy { it.task.startTime ?: Date(Long.MAX_VALUE) }

    sortedTasks.forEach { taskDetailed ->
        val taskDate = taskDetailed.task.endTime
        if (taskDate == null) {
            grouped["Upcoming"]?.add(taskDetailed)
        } else {
            if (taskDate.before(tomorrowCal.time)) {
                // If it's today or past, put in Today
                grouped["Today"]?.add(taskDetailed)
            } else if (taskDate.before(dayAfterTomorrowCal.time)) {
                grouped["Tomorrow"]?.add(taskDetailed)
            } else {
                grouped["Upcoming"]?.add(taskDetailed)
            }
        }
    }

    // Return only non-empty groups, but maintaining the order: Today, Tomorrow, Upcoming
    val result = linkedMapOf<String, List<TaskDetailed>>()
    listOf("Today", "Tomorrow", "Upcoming").forEach { key ->
        val list = grouped[key]
        if (!list.isNullOrEmpty()) {
            result[key] = list
        }
    }
    return result
}
