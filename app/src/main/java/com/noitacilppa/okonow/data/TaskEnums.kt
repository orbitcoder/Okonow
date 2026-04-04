package com.noitacilppa.okonow.data

enum class Priority(val value: Int) {
    LOW(1), MEDIUM(2), HIGH(3), URGENT(4), CRITICAL(5)
}

enum class ContentFormat {
    PARAGRAPH, LIST
}
