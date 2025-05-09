package lottie

import Lottie.CompatibleAnimationView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import contentScale.ContentScale
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.UIKit.UIViewContentMode


@OptIn(ExperimentalForeignApi::class)
@Composable
fun LottieAnimation(
    modifier: Modifier,
    composition: Any?,
    progress: () -> Float,
    backgroundColor: Color,
    contentScale: ContentScale,
    clipToCompositionBounds: Boolean
) {
    when (composition as? CompatibleAnimationView) {
        null -> {}
        else -> {

            val scaleType = when(contentScale){
                ContentScale.Fit -> UIViewContentMode.UIViewContentModeScaleAspectFit
                ContentScale.Crop -> UIViewContentMode.UIViewContentModeScaleAspectFill
                ContentScale.FillBounds -> UIViewContentMode.UIViewContentModeScaleToFill
                ContentScale.FitWidth -> UIViewContentMode.UIViewContentModeScaleToFill
                ContentScale.FitHeight -> UIViewContentMode.UIViewContentModeScaleToFill
            }

            androidx.compose.ui.viewinterop.UIKitView(
                factory = {
                    UIView().apply {
                        this.backgroundColor = backgroundColor.toUIColor()
                        this.tintColor = UIColor.clearColor
                        this.clipsToBounds = clipToCompositionBounds
                    }
                },
                modifier = modifier,
                update = { view ->

                    composition.translatesAutoresizingMaskIntoConstraints = false
                    composition.contentMode = scaleType
                    composition.clipsToBounds = clipToCompositionBounds

                    view.addSubview(composition)

                    val (compositionWidth, compositionHeight) = composition.intrinsicContentSize.useContents { width to height }
                    val constraints = when (contentScale) {
                        ContentScale.FitWidth -> listOf(
                            composition.widthAnchor.constraintEqualToAnchor(view.widthAnchor),
                            composition.heightAnchor.constraintEqualToAnchor(
                                composition.widthAnchor,
                                multiplier = compositionHeight / compositionWidth
                            )
                        )
                        ContentScale.FitHeight -> listOf(
                            composition.heightAnchor.constraintEqualToAnchor(view.heightAnchor),
                            composition.widthAnchor.constraintEqualToAnchor(
                                composition.heightAnchor,
                                multiplier = compositionWidth / compositionHeight
                            )
                        )
                        else -> listOf(
                            composition.widthAnchor.constraintEqualToAnchor(view.widthAnchor),
                            composition.heightAnchor.constraintEqualToAnchor(view.heightAnchor)
                        )
                    }

                    NSLayoutConstraint.activateConstraints(constraints)
                }
            )

        }
    }
}
