import DateTimeFormatterUtil.formatDateTime
import DateTimeFormatterUtil.getPattern
import kotlin.time.Duration.Companion.days
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.until
import kotlin.time.Duration.Companion.days

object DateTimeFormatter {

  private val ONE_DAY_IN_HOURS = 1.days.inWholeHours

  // 10:30 am if today
  // 11 pm if yesterday
  // 9 Nov if older
  fun formatRelativeDateTime(epochSeconds: Long, locale: String? = null, showTime: Boolean = true): String {
    val givenInstant = Instant.fromEpochSeconds(epochSeconds)

    val currentInstant = Clock.System.now()

    val timeDiff = givenInstant.until(currentInstant, DateTimeUnit.HOUR)

    val patternType = if (timeDiff < ONE_DAY_IN_HOURS) {
      // Only time for past 24 hours
      DateTimePattern.TIME
    } else {
      // Date and/or time for older than 24 hours
      if (showTime) DateTimePattern.DATE_TIME else DateTimePattern.DATE
    }

    val pattern = getPattern(patternType)
    return formatDateTime(epochSeconds, locale, pattern)
  }

  // Thu, 5 Sep 2024
  fun formatFullDateTime(epochSeconds: Long, locale: String? = null): String {
    val pattern = getPattern(DateTimePattern.DAY_DATE_YEAR)
    return formatDateTime(epochSeconds, locale, pattern)
  }

  // 10:30 am
  fun formatTime(epochSeconds: Long, locale: String? = null): String {
    val pattern = getPattern(DateTimePattern.TIME)
    return formatDateTime(epochSeconds, locale, pattern)
  }

  // 10 Nov
  fun formatDate(epochSeconds: Long, locale: String? = null): String {
    val pattern = getPattern(DateTimePattern.DATE)
    return formatDateTime(epochSeconds, locale, pattern)
  }

  // 10 Nov, 2026
  fun formatDateWithYear(epochSeconds: Long, locale: String? = null): String {
    val pattern = getPattern(DateTimePattern.DATE_YEAR)
    return formatDateTime(epochSeconds, locale, pattern)
  }
}