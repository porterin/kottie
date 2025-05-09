package lottie

import androidx.compose.ui.graphics.Color
import platform.UIKit.UIColor

fun Color.toUIColor(): UIColor {
    val red = (this.red * 255) / 255.0
    val green = (this.green * 255) / 255.0
    val blue = (this.blue * 255) / 255.0
    val alpha = (this.alpha * 255) / 255.0
    return UIColor(red = red, green = green, blue = blue, alpha = alpha)
}