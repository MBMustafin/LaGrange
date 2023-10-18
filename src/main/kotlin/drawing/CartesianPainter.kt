package drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import drawing.convertation.Converter
import drawing.convertation.Plane
import kotlin.math.roundToInt

open class CartesianPainter : Painter {

    companion object{
        val PRIMARY_COLOR = Color.Black
        val ACCENT_COLOR1 = Color.Blue
        val ACCENT_COLOR2 = Color.Red
        val TRANSPARENT_COLOR = Color.Transparent
    }

    var plane: Plane? = null
    @OptIn(ExperimentalTextApi::class)
    var textMeasurer: TextMeasurer? = null

    override fun paint(scope: DrawScope){
        paintAxis(scope)
        paintXTics(scope)
    }

    fun paintAxis(scope: DrawScope){
        plane?.let { plane ->
            val x0 = Converter.xCrt2Scr(0.0, plane)
            val y0 = Converter.yCrt2Scr(0.0,plane)
            scope.apply {
                drawLine(Color.Black, Offset(0f, y0), Offset(size.width, y0), 1f)
                drawLine(Color.Black, Offset(x0, 0f), Offset(x0,size.height), 1f)
            }
        }
    }

    @OptIn(ExperimentalTextApi::class)
    fun paintXLabel(scope: DrawScope, value: Double){
        scope.apply {
            textMeasurer?.let {
                val text = it.measure(
                    value.toString(),
                    TextStyle(color = ACCENT_COLOR2, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                )
                plane?.let { plane ->
                    val y = Converter.yCrt2Scr(0.0, plane) + 12
                    val x = Converter.xCrt2Scr(value, plane) - text.size.width / 2
                    drawText(text, topLeft = Offset(x, y))
                }
            }
        }
    }

    fun paintXTics(scope: DrawScope){
        plane?.let {
            val step = 0.1
            var x = (it.xMin / step).roundToInt() * step
            val y0 = Converter.yCrt2Scr(0.0, it)
            while (x <= it.xMax) {
                var h = 4f
                var color = PRIMARY_COLOR
                val xPos = Converter.xCrt2Scr(x, it)
                scope.apply {
                    when{
                        (x * 10).roundToInt() == 0 -> color = TRANSPARENT_COLOR
                        (x * 10).roundToInt() % 10 == 0 -> {
                            h += 10f
                            color = ACCENT_COLOR2
                            paintXLabel(this, (x * 10).roundToInt() / 10.0)
                        }
                        (x * 10).roundToInt() % 5 == 0 -> { h += 4f; color = ACCENT_COLOR1 }
                    }
                    drawLine(color, Offset(xPos, y0 + h), Offset (xPos, y0-h), 1f)
                }
                x += step
            }
        }
    }
}