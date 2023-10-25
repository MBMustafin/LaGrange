package ru.smak.math.polynomials

class Lagrange(dots: MutableMap<Double, Double>) : Polynomial(){

    private val dots = dots.toMutableMap()
    init{
        val p = Polynomial()
        dots.forEach {
            val r = fundamental(it.key)
            if (r != null) p += r * it.value
            else return@forEach
        }
        coef = p.coeffs.toMutableMap()
    }

    /**
     * Вычисление фундаментальных полиномов Лагранжа
     */
    private fun fundamental(key: Double): Polynomial? {
        val p = Polynomial(1.0)
        dots.forEach {
            if (it.key.compareTo(key)!=0){
                val m = Polynomial(-it.key, 1.0) /
                        (key - it.key)
                if (m != null){
                    p *= m
                } else return@fundamental null
            }
        }
        return p
    }

}