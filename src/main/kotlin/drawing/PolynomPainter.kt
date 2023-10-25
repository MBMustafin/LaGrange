package drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import drawing.convertation.Converter
import drawing.convertation.Plane
import ru.smak.math.polynomials.Polynomial

class PolynomPainter(val p: Polynomial) : Painter {
    companion object{
        val PRIMARY_COLOR = Color.Black
    }

    var plane: Plane? = null

    @OptIn(ExperimentalStdlibApi::class)
    override fun paint(scope: DrawScope) {
        plane?.let {
            for (i in 0 ..< it.width.toInt()){
                val x1 = Converter.xScr2Crt(i.toFloat(), it)
                val x2 = Converter.xScr2Crt((i+1).toFloat(), it)
                val y1 = p(x1)
                val y2 = p(x2)
                scope.drawLine(
                    PRIMARY_COLOR,
                    Offset(i.toFloat(), Converter.yCrt2Scr(y1, it)),
                    Offset((i+1).toFloat(), Converter.yCrt2Scr(y2, it))
                )

            }
        }

    }

}