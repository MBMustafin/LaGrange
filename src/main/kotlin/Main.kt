import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import drawing.CartesianPainter
import drawing.convertation.Plane
import java.awt.Dimension
import kotlin.random.Random

object Params{
    val _color
        get() = mutableStateOf(Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()))

    val color: Color
        get() = _color.value
}

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    val p = CartesianPainter()
    p.plane = Plane(-5.0,10.0,-10.0,4.0, 0f,0f )
    MaterialTheme {
        Box(Modifier.background(Color.Blue).fillMaxSize()) {
            Column(
                Modifier.padding(2.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Canvas(Modifier.fillMaxWidth()
                    .weight(1f)
                    .background(Color.White)
                ) {
                    p.plane?.width = size.width
                    p.plane?.height = size.height
                    p.paint(this)
                }
                Column(Modifier.padding(top = 2.dp).fillMaxWidth().background(Color(1f, 1f, 1f, 0.9f))) {
                    Button(onClick = {
                    }, Modifier.align(Alignment.CenterHorizontally)) {
                        Text(text)
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(
        title = "Интерполяция функций",
        onCloseRequest = ::exitApplication,
        state = WindowState(width = 600.dp, height = 500.dp)
    ) {
        window.size = Dimension(1000, 200)
        App()
    }
}
