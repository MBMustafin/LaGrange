package drawing

import androidx.compose.foundation.pager.PageSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import drawing.convertation.Converter
import drawing.convertation.Plane
import kotlin.math.roundToInt

open class CartesianPainter : Painter {

    companion object{
        val PRIMARY_COLOR = Color.Black
        val ACCENT_COLOR1 = Color.Blue
        val ACCENT_COLOR2 = Color.Red
    }

    var plane: Plane? = null

    override fun paint(scope: DrawScope){
        paintAxis(scope)
        paintTics(scope)
        paintLabels(scope)
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

    fun paintLabels(scope: DrawScope){}

    fun paintTics(scope: DrawScope){
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
                        (x*10).roundToInt() % 10 == 0 -> { h += 10f; color = ACCENT_COLOR2 }
                        (x*10).roundToInt() % 5 == 0 -> { h += 4f; color = ACCENT_COLOR1 }

                    }
                    drawLine(color, Offset(xPos, y0 + h), Offset (xPos, y0-h), 1f)
                }
                x += step
            }
        }
    }
}