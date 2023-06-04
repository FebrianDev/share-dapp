package com.febrian.sharedapps.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Instant
import java.util.*

object DateUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTime(date: Date): String {

        try {
            val instant1 = date.toInstant()

            val instant2 = Instant.now()

            val diff: Duration = Duration.between(instant1, instant2)
            val getTime = diff.toMinutes()
            var finalTime = getTime.toString() + "m\nago"
            if (getTime >= 60) {
                finalTime = "${getTime / 60}h\nago"
            }
            if (getTime >= 1440) {
                finalTime = "${getTime / 1440}d\nago"
            }

            return finalTime
        } catch (e: Exception) {
            return "0"
        }
    }
}