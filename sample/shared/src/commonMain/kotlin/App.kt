import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kottie.sample.shared.generated.resources.Res
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import utils.KottieConstants

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(
    modifier: Modifier = Modifier,
    str: String
) {

    var animation by remember { mutableStateOf("") }
    var animation2 by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        animation = Res.readBytes("files/Header_with_location.json").decodeToString()
    }

    LaunchedEffect(Unit){
        animation2 = Res.readBytes("files/body_with_location.json").decodeToString()
    }

    val composition = rememberKottieComposition(
        spec = KottieCompositionSpec.File(animation)
    )

    val composition2 = rememberKottieComposition(
        spec = KottieCompositionSpec.File(animation2)
    )

    val animationState by animateKottieCompositionAsState(
        composition = composition,
        iterations = KottieConstants.IterateForever,
        reverseOnRepeat = true
    )

    val animationState2 by animateKottieCompositionAsState(
        composition = composition2,
        iterations = KottieConstants.IterateForever,
        reverseOnRepeat = true
    )

    MaterialTheme {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Red.copy(alpha = 0.0f)),
        ) {
            println("Date: $str")
//            Text("Date: $str")
            KottieAnimation2(
                composition = composition,
                progress = { animationState.progress },
                modifier = modifier
                    .fillMaxWidth(),
                contentAlign = Alignment.TopCenter,
                contentScale = ContentScale.FillWidth
            )
            KottieAnimation2(
                composition = composition2,
                progress = { animationState2.progress },
                modifier = modifier
                    .fillMaxWidth()
//                    .height(700.dp)
                    .background(Color.Blue.copy(alpha = 0.0f)),
                contentAlign = Alignment.TopCenter,
                contentScale = ContentScale.FillWidth
            )

        }
    }


}



