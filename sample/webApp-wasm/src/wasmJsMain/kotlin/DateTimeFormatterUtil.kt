import kotlinx.datetime.*

actual object DateTimeFormatterUtil {
  actual fun formatDateTime(
    epochSeconds: Long,
    locale: String?,
    pattern: String,
    useWesternNumerals: Boolean,
  ): String {
    // Do any Kotlin logic you want here; just don't call js(...) again.
    val defaultLocale = "en-US"
    val resolvedLocale = if (useWesternNumerals) {
      "${locale ?: defaultLocale}-u-nu-latn"
    } else {
      locale ?: defaultLocale
    }

    // Delegate the final formatting to our single-expression js function
    return formatDateTimeJs(epochSeconds.toDouble(), resolvedLocale, pattern)
  }

  actual fun getPattern(patternEnum: DateTimePattern): String {
    return when (patternEnum) {
      DateTimePattern.TIME -> """
    {
        "hour": "numeric",
        "minute": "numeric",
        "hour12": true
    }
      """.trimIndent()

      DateTimePattern.DATE -> """
    {
        "day": "numeric",
        "month": "short"
    }
      """.trimIndent()

      DateTimePattern.DATE_TIME -> """
    {
        "day": "numeric",
        "month": "short",
        "hour": "numeric",
        "minute": "numeric",
        "hour12": true
    }
      """.trimIndent()

      DateTimePattern.DATE_YEAR -> """
    {
        "day": "numeric",
        "month": "short",
        "year": "numeric"
    }
      """.trimIndent()

      DateTimePattern.DAY_DATE_YEAR -> """
    {
        "weekday": "long",
        "day": "numeric",
        "month": "short",
        "year": "numeric"
    }
      """.trimIndent()

      DateTimePattern.DATE_YEAR_TIME -> """
    {
        "day": "numeric",
        "month": "short",
        "year": "numeric",
        "hour": "numeric",
        "minute": "numeric",
        "hour12": true
        }
      """.trimIndent()

      DateTimePattern.DAY_DATE_YEAR_TIME -> """
    {
        "weekday": "long",
        "day": "numeric",
        "month": "short",
        "year": "numeric",
        "hour": "numeric",
        "minute": "numeric",
        "hour12": true
    }
      """.trimIndent()
    }
  }
}

external interface JsDateTimeOptions {
  var day: String?
  var month: String?
  var year: String?
  var hour: String?
  var minute: String?
  var hour12: Boolean?
  var weekday: String?
}

external class IntlDateTimeFormat(locale: String, options: JsDateTimeOptions) {
  fun format(date: JsDate): String
}

external class JsDate(epochMilliseconds: Double)

fun parseJsonToMap(jsonString: String): Map<String, Any> {
  return jsonString.trimIndent()
    .removeSurrounding("{", "}")
    .split(",")
    .map { entry ->
      val (key, value) = entry.split(":").map { it.trim().removeSurrounding("\"") }
      key to value.toKotlinType()
    }.toMap()
}

fun String.toKotlinType(): Any {
  return when {
    this == "true" -> true
    this == "false" -> false
    this.toDoubleOrNull() != null -> this.toDouble()
    else -> this
  }
}
