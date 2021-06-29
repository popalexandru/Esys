package com.example.leagueapp.networking.model.Utils

import com.example.leagueapp.networking.model.models.ParticipantIdentity
import timber.log.Timber
import java.util.concurrent.TimeUnit

object Utility {
    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var milliseconds = ms

        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        if (!includeMillis) {
            if (hours > 0) {
                return "${if (hours < 10) "0" else ""}$hours:" +
                        "${if (minutes < 10) "0" else ""}$minutes:" +
                        "${if (seconds < 10) "0" else ""}$seconds:"
            }

            return "${if (minutes < 10) "0" else ""}$minutes:" +
                    "${if (seconds < 10) "0" else ""}$seconds"
        }

        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 100

        Timber.d("Hours: $hours")
        if (hours > 0) {
            return "${if (hours < 10) "0" else ""}$hours:" +
                    "${if (minutes < 10) "0" else ""}$minutes:" +
                    "${if (seconds < 10) "0" else ""}$seconds:" +
                    "${if (milliseconds < 10) "0" else ""}$milliseconds"
        }

        return "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds:" +
                "${if (milliseconds < 10) "0" else ""}$milliseconds"
    }

    fun getFormattedTime(timestamp : Long) : String {
        var difference = System.currentTimeMillis() - timestamp

        val days = TimeUnit.MILLISECONDS.toDays(difference)
        difference -= TimeUnit.DAYS.toMillis(days)

        val hours = TimeUnit.MILLISECONDS.toHours(difference)
        difference -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(difference)
        difference -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(difference)
        difference -= TimeUnit.SECONDS.toMillis(seconds)

        return when{
            days > 1 -> "Last time fetched: $days days ago"
            hours > 1 -> "Last time fetched: $hours hours ago"
            minutes > 1 -> "Last time fetched: $minutes minutes ago"
            seconds > 2 -> "Last time fetched: $seconds seconds ago"
            difference > 5 -> "Fetched moments ago"
            else -> "N/A"
        }
    }

    fun getTimeAgo(timestamp : Long) : String {
        var difference = System.currentTimeMillis() - timestamp

        val days = TimeUnit.MILLISECONDS.toDays(difference)
        difference -= TimeUnit.DAYS.toMillis(days)

        val hours = TimeUnit.MILLISECONDS.toHours(difference)
        difference -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(difference)
        difference -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(difference)
        difference -= TimeUnit.SECONDS.toMillis(seconds)

        return when{
            days > 1 -> "$days days ago"
            hours > 1 -> "$hours hours ago"
            minutes > 1 -> "$minutes minutes ago"
            seconds > 2 -> "$seconds seconds ago"
            difference > 5 -> "moments ago"
            else -> "N/A"
        }
    }
}