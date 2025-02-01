expect object DateTimeFormatterUtil {
  fun formatDateTime(
    epochSeconds: Long,
    locale: String?,
    pattern: String,
    useWesternNumerals: Boolean = true,
  ): String

  fun getPattern(patternEnum: DateTimePattern): String
}
