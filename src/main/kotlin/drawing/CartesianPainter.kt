package drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

open class CartesianPainter {

    fun paint(scope: DrawScope){
        scope.apply{
            drawLine(Color.Black, Offset(0f, size.height/2), Offset(size.width, size.height/2), 3f)
        }
    }
}