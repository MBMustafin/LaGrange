import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import drawing.CartesianPainter
import drawing.convertation.Plane
import java.awt.Dimension
import kotlin.math.roundToInt
import kotlin.random.Random

object Params{
    val _color
        get() = mutableStateOf(Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()))

    val color: Color
        get() = _color.value
}

@OptIn(ExperimentalTextApi::class)
@Composable
@Preview
fun App() {
    val p = CartesianPainter()
    p.plane = Plane(-5.0,10.0,-10.0,4.0, 0f,0f )
    var xMin by mutableStateOf(p.plane?.xMin.toString())
    var xMax by mutableStateOf(p.plane?.xMax.toString())
    var yMin by mutableStateOf(p.plane?.yMin.toString())
    var yMax by mutableStateOf(p.plane?.yMax.toString())
    p.textMeasurer = rememberTextMeasurer()
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
                Column(Modifier.padding(top = 2.dp).fillMaxWidth()
                    .background(Color(1f, 1f, 1f, 0.9f))
                ) {
                    Row(Modifier.fillMaxWidth().padding(4.dp), verticalAlignment = Alignment.CenterVertically){
                        Text("xMin: ")
                        OutlinedTextField(
                            value = xMin,
                            onValueChange = {
                                xMin = it.filter { it in '0'..'9' || it in arrayOf('-', '+', '.') }
                                with (it.toDoubleOrNull() ?: (p.plane?.xMin ?: 0.0)){
                                    p.plane?.xMin = ((this*10).roundToInt()/10.0).coerceIn(-100.0,(p.plane?.xMax ?: 0.0) - 0.1)
                                }
                            },
                            Modifier.padding(end = 32.dp).weight(2f).background(Color.White).onFocusChanged {
                                if (!it.isFocused) xMin = p.plane?.xMin.toString()
                            },
                            singleLine = true,
                        )
                        Text("xMax: ")
                        OutlinedTextField(
                            value = xMax,
                            onValueChange = {
                                xMax = it.filter { it in '0'..'9' || it in arrayOf('-', '+', '.') }
                                with (it.toDoubleOrNull() ?: (p.plane?.xMax ?: 0.0)) {
                                    p.plane?.xMax = (this*10).roundToInt()/10.0

                                }
                            },
                            Modifier.weight(2f).background(Color.White),
                            singleLine = true,
                        )
                    }
                    Row(Modifier.fillMaxWidth().padding(4.dp), verticalAlignment = Alignment.CenterVertically){
                        Text("yMin: ")
                        OutlinedTextField(
                            value = yMin,
                            onValueChange = {},
                            Modifier.padding(end = 32.dp).weight(2f).background(Color.White),
                            singleLine = true,
                        )
                        Text("yMax: ")
                        OutlinedTextField(
                            value = yMax,
                            onValueChange = {},
                            Modifier.weight(2f).background(Color.White),
                            singleLine = true,
                        )
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
        state = WindowState(width = 600.dp, height = 670.dp)
    ) {
        App()
    }
}
