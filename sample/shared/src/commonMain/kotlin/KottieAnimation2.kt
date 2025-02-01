import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kottieComposition.KottieCompositionAnimation
import kottieComposition.bounds
import contentScale.ContentScale as KottieContentScale


@Composable
fun KottieAnimation2(
  modifier: Modifier = Modifier,
  composition: Any?,
  progress: () -> Float,
  backgroundColor: Color = Color.Transparent,
  contentScale: ContentScale = ContentScale.Fit,
  contentAlign: Alignment = Alignment.Center,
  clipToCompositionBounds: Boolean = true,
) {
  var offsetX by remember { mutableFloatStateOf(0f) }
  var offsetY by remember { mutableFloatStateOf(0f) }

  val density = LocalDensity.current.density

  val containerSizeVal = remember { mutableStateListOf(-1f, -1f) }
  val containerSizeDp = remember {
    derivedStateOf {
      if (containerSizeVal[0] == -1f && containerSizeVal[1] == -1f) {
        listOf(Dp.Unspecified, Dp.Unspecified)
      } else {
          listOf(
            (containerSizeVal[0] / density).dp,
            (containerSizeVal[1] / density).dp
          )
      }
    }
  }

  val bounds = (composition as? KottieCompositionAnimation)?.let { bounds(it) } ?: Rect.Zero
  Box(
    modifier = Modifier
      .onSizeChanged { size ->
        val containerSize = Size(size.width.toFloat(), size.height.toFloat())
        val aspectRatio = bounds.takeIf { it.size != Size.Zero }?.let { it.width / it.height } ?: 1f

        val compositionWidth = containerSize.width
        val compositionHeight = compositionWidth / aspectRatio

        val compositionSize = Size(compositionWidth, compositionHeight)

        val scale = calculateScale(containerSize, compositionSize, contentScale)

        containerSizeVal[0] = compositionWidth * scale
        containerSizeVal[1] = compositionHeight * scale

        val (offsetXCalc, offsetYCalc) = calculateOffset(
          containerSize,
          compositionSize,
          contentScale = contentScale,
          contentAlign = contentAlign,
        )

        offsetX = offsetXCalc
        offsetY = offsetYCalc
      }
    //  .height(containerSizeDp.value[1])
    ,
  ) {
    KottieAnimation(
      modifier = modifier
        .offset(offsetX.toDp(), offsetY.toDp()),
      composition = composition,
      progress = progress,
      backgroundColor = backgroundColor,
      contentScale = mapScale(contentScale),
      clipToCompositionBounds = clipToCompositionBounds,
    )
  }
}

private fun calculateScale(
  containerSize: Size,
  contentSize: Size,
  contentScale: ContentScale,
): Float {
  val containerWidth = containerSize.width
  val containerHeight = containerSize.height

  val contentWidth = contentSize.width
  val contentHeight = contentSize.height

  return when (contentScale) {
    ContentScale.Fit -> minOf(
      containerWidth / contentWidth,
      containerHeight / contentHeight,
    )

    ContentScale.Crop -> maxOf(
      containerWidth / contentWidth,
      containerHeight / contentHeight,
    )

    ContentScale.FillBounds -> 1f

    ContentScale.FillWidth -> containerWidth / contentWidth

    ContentScale.FillHeight -> containerHeight / contentHeight

    else -> 1f // Default scale
  }
}

private fun calculateOffset(
  containerSize: Size,
  contentSize: Size,
  contentScale: ContentScale,
  contentAlign: Alignment,
): Pair<Float, Float> {
  val scale = calculateScale(
    containerSize,
    contentSize,
    contentScale = contentScale,
  )

  val scaledWidth = contentSize.width * scale
  val scaledHeight = contentSize.height * scale

  val containerWidth = containerSize.width
  val containerHeight = containerSize.height

  val offsetX = when (contentAlign) {
    Alignment.Start -> -(containerWidth - scaledWidth) / 2
    Alignment.CenterHorizontally -> 0f
    Alignment.End -> (containerWidth - scaledWidth) / 2
    else -> 0f
  }

  val offsetY = when (contentAlign) {
    Alignment.Top, Alignment.TopCenter -> -(containerHeight - scaledHeight) / 2
    Alignment.CenterVertically -> 0f
    Alignment.Bottom, Alignment.BottomCenter -> (containerHeight - scaledHeight) / 2
    else -> 0f
  }

  return Pair(offsetX, offsetY)
}

private fun mapScale(contentScale: ContentScale): KottieContentScale {
  return when (contentScale) {
    ContentScale.Fit -> KottieContentScale.Fit
    ContentScale.Crop -> KottieContentScale.Crop
    ContentScale.FillBounds -> KottieContentScale.FillBounds
    ContentScale.FillWidth -> KottieContentScale.FitWidth
    ContentScale.FillHeight -> KottieContentScale.FitHeight
    else -> throw IllegalArgumentException("Scale type not supported: $contentScale")
  }
}

@Composable
fun Float.toDp(): Dp {
  val density = LocalDensity.current.density
  return (this / density).dp
}
