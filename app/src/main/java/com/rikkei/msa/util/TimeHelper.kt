package com.rikkei.msa.util

import java.util.concurrent.TimeUnit

object TimeHelper {
    fun covertMillisecondToMinute(millisecond: Long): String {
        return String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millisecond),
            TimeUnit.MILLISECONDS.toSeconds(millisecond)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisecond))
        )
    }
}
