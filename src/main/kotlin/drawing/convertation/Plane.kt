package drawing.convertation

data class Plane(
    var xMin: Double,
    var xMax: Double,
    var yMin: Double,
    var yMax: Double,
    var width: Float,
    var height: Float,
) {
    val xDen: Double
        get() = width/(xMax-xMin)
    val yDen: Double
        get() = height/(yMax-yMin)

}