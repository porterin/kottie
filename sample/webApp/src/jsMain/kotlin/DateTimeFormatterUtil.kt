import kotlinx.datetime.*

actual object DateTimeFormatterUtil {
  actual fun formatDateTime(
    epochSeconds: Long,
    locale: String?,
    pattern: String,
    useWesternNumerals: Boolean,
  ): String {
    // Parse pattern into a JavaScript object
    val options = js("JSON").parse<dynamic>(pattern)
//    val options = js("eval")("(${pattern})")

    val date = js("new Date(epochSeconds * 1000)")

    // Resolve locale, adding -u-nu-latn if Western numerals are required
    val resolvedLocale = if (useWesternNumerals) {
      "${locale ?: "en-US"}-u-nu-latn"
    } else {
      // en-US as fallback locale
      locale ?: "en-US"
    }

    // Use Intl.DateTimeFormat with the resolved locale and options
    val formatter = js("new Intl.DateTimeFormat")(resolvedLocale, options)

    return formatter.format(date) as String
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