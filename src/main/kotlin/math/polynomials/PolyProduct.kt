package ru.smak.math.polynomials

/**
 * Класс для работы с полиномами, представляющими собой произведение других полиномов
 */
class PolyProduct() {
    private val polys = mutableListOf<Polynomial>()
    private var coeff = 1.0

    constructor(pp: PolyProduct) : this(){
        polys.addAll(pp.polys)
        coeff = pp.coeff
    }

    operator fun timesAssign(poly: Polynomial){
        polys.add(poly)
    }

    operator fun timesAssign(v: Double){
        coeff *= v
    }

    operator fun times(poly: Polynomial) = PolyProduct(this).also {
        it *= poly
    }

    operator fun times(v: Double) = PolyProduct(this).also{
        it *= v
    }

    fun toPolynomial() = polys.fold(Polynomial(coeff)){ res, p -> res * p}

    override fun toString(): String = polys.joinToString(
        separator = ")(",
        prefix = "(",
        postfix = ")"
    ){
        it.toString()
    }
}