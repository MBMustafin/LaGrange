package ru.smak.math.polynomials

import ru.smak.math.eq

/**
 * Класс для формирования интерполяционного полинома Ньютона
 * @author Sergey Makletsov
 * @constructor Позволяет сформировать полином Ньютона по набору точек, представленных в виде словаря
 * @param points Словарь, содержащий абсциссы и ординаты точек, по которым строится интерполяционный полином Ньютона
 */
class Newton() : Polynomial() {

    private val fPoly = PolyProduct()//Polynomial(1.0)
    private val _points = mutableMapOf<Double, Double>()
    var created = false
        private set

    val points
        get() = _points.toMap()

    constructor(points: Map<Double, Double>) : this (){
        _points.putAll(points)
        createNPoly()
        created = true
    }

    /**
     * Построение полинома Ньютона по набору точек
     */
//    private fun createNPoly() {
//        val res = Polynomial()
//        _points.keys.forEachIndexed { index, x ->
//            res += fPoly * getDivDiff(index)
//            fPoly *= Polynomial(-x, 1.0)
//        }
//        coef = res.coeffs.toMutableMap()
//    }
    private fun createNPoly() {
        val res = Polynomial()
        _points.keys.forEachIndexed { index, x ->
            res += (fPoly * getDivDiff(index)).toPolynomial()
            fPoly *= Polynomial(-x, 1.0)
        }
        coef = res.coeffs.toMutableMap()
    }

    /**
     * Получает следующее значение разделенной разности относительного
     * текущего состояния построенного полинома
     */
    private fun getDivDiff(k: Int) = _points.keys.toList().slice(0..k).let{ xs ->
        val ys = List(xs.size){ _points[xs[it]]!! }
        xs.foldIndexed(0.0) { i, sAcc, x ->
            sAcc + 1.0 / xs.foldIndexed(1.0) { ind, pAcc, p -> pAcc * if (ind != i) (x - p) else (1.0 / ys[i]) }
        }
    }

    /**
     * Добавление новой точки, через которую должен "проходить" полином
     * @param point Точка, представленная парой значений
     */
    fun addPoint(point: Pair<Double, Double>){
        addPoint(point.first, point.second)
    }

    /**
     * Добавление новой точки, через которую должен "проходить" полином
     * @param pointX абсцисса точки
     * @param pointY ордината точки
     */
    fun addPoint(pointX: Double, pointY: Double){
        val res = Polynomial(coef)
        if (_points.keys.find { pointX eq it } == null) {
            _points.put(pointX, pointY)
            res += (fPoly * getDivDiff(_points.size - 1)).toPolynomial()
            fPoly *= Polynomial(-pointX, 1.0)
        }
        coef = res.coeffs.toMutableMap()
        created = true
    }

    /**
     * Класс интерполяционного полинома Ньютона не позволяет
     * изменять значение коэффициентов, поскольку в этом случае полином
     * перестанет быть интерполяционным.
     */
    override fun set(i: Int, v: Double){
        throw InterpolationPolynomModificationException()
    }
}

class InterpolationPolynomModificationException : Throwable(
    message = "Попытка произвести изменения, которые не могут выполнены для интерполяционного полинома"
)