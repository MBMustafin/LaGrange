package drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import drawing.convertation.Converter
import drawing.convertation.Plane

open class CartesianPainter : Painter {

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

    fun paintTics(scope: DrawScope){}
}