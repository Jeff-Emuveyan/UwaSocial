package com.bellogate_caliphate.uwasocial.core.utils

import java.util.concurrent.TimeUnit

object TimeUtils {

    fun formatRelativeTime(postId: Long): String {
        val minutesAgo = calculateMinutesOffset(postId)
        return formatTimeSpan(minutesAgo)
    }

    private fun calculateMinutesOffset(postId: Long): Long {
        return ((postId * 7) % 180) + 2
    }

    private fun formatTimeSpan(minutes: Long): String {
        return when {
            minutes < 60 -> "$minutes min ago"
            minutes < 120 -> "1 hr ago"
            else -> "${minutes / 60} hrs ago"
        }
    }
}
