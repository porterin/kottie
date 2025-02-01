@JsFun("""
function formatDateTimeJs(epochSeconds, locale, pattern) {
    const opts = JSON.parse(pattern);
    const date = new Date(epochSeconds * 1000);
    return new Intl.DateTimeFormat(locale, opts).format(date);
}
""")

external fun formatDateTimeJs(
    epochSeconds: Double,
    locale: String,
    pattern: String
): String