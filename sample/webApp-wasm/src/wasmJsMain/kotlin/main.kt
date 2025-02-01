import DateTimeFormatterUtil.formatDateTime
import DateTimeFormatterUtil.getPattern
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val epochSeconds = 1672531200L
    val locale = "en-US"
    val pattern = getPattern(DateTimePattern.DATE_TIME)
    val formattedDateTime = formatDateTime(
        epochSeconds = epochSeconds,
        locale = locale,
        pattern = pattern,
        useWesternNumerals = true
    )

    println("Formatted date: $formattedDateTime") // Outputs the formatted date/time
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        MainView(formattedDateTime)
    }
}