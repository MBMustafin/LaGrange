package ru.smak.math.polynomials

import ru.smak.math.eq
import ru.smak.math.neq
import java.util.*
import kotlin.collections.Collection
import kotlin.collections.LinkedHashMap
import kotlin.math.*

/**
 * Класс, предоставляющий возможность работы с полиномами
 * @author Sergey Makletsov, 2022
 * @constructor Позволяет создать класс по набору коэффициентов, расположенных в коллекции. При этом индексы элементов коллекции должны соответствовать степени монома
 * @param coeffs коллекция коэффициентов
 */
open class Polynomial(coeffs: Map<Int, Double>?) {
    protected var coef = mutableMapOf<Int, Double>()

    val coeffs: Map<Int, Double>
        get() = coef.toMap()

    /**
     * Позволяет создать класс по набору коэффициентов,
     * перечисленных непосредственно в параметре конструктора
     * Порядковый номер коэффициента (при подсчете с 0) соответствует
     * степени монома, при которой будет расположен данный коэффициент
     */
    constructor(vararg coeffs: Double) : this(
        LinkedHashMap<Int, Double>(coeffs.size).also{
            coeffs.forEachIndexed{ i, v -> if (v neq 0.0) it[i] = v }
        }
    )

    private constructor() : this(null)

    /**
     * Конструктор предназначен для создания полинома по словарю, содержащему степени в качестве ключей и коэффициенты при степенях в значениях
     * @param coeffs словарь, содержащий коэффициенты полинома. Ключи словаря содержат степени, значения - коэффициенты при степенях
     */
    constructor(coeffs: Collection<Double>) : this(
        LinkedHashMap<Int, Double>(coeffs.size).also{
            coeffs.forEachIndexed{ i, v -> if (v neq 0.0) it[i] = v }
        }
    )

    /**
     * Конструктор копирования
     * @param other Поплином, копию которого требуется создать
     */
    constructor(other: Polynomial) : this(other.coef)

    init {
        coef = repairCoeffs(coeffs ?: mapOf())
    }

    /**
     * Вспомогательная функция, позволяющая избавиться от нулевых коэффициентов, которые не требуют хранения, а также
     * удалить из словаря коэффициенты при отрицательных степенях
     * @throws IllegalCoefficientException Выбрасывается при значении коэффициента полинома равного бесконечности или NaN
     */
    private fun repairCoeffs(coeffs: Map<Int, Double>) : MutableMap<Int, Double> {
        /*if (
            coeffs.values.contains(Double.POSITIVE_INFINITY) ||
            coeffs.values.contains(Double.NEGATIVE_INFINITY) ||
            coeffs.values.contains(Double.NaN)
        ) throw IllegalCoefficientException()*/
        return LinkedHashMap(coeffs).filterNot { (i, v) -> i < 0 || v.eq(0.0) }.toMutableMap()
    }

    /**
     * Проверка двух полиномов на равенство.
     * @param other Полином, с которым выполняется сравнение данного
     */
    override operator fun equals(other: Any?) =
        other is Polynomial &&
                other.coef.keys == coef.keys &&
                other.coef.values.sorted() == coef.values.sorted()

    /**
     * Получение хэш-кода для данного полинома с учётом коэффициентов при степенях
     */
    override fun hashCode() =
        Objects.hash(*coef.keys.toTypedArray(), *coef.keys.toTypedArray())

    /**
     * Свойство, позволяющее узнать степень текущего полинома.
     */
    val degree: Int
        get() = coef.keys.max()

    /**
     * Преобразование полинома в строковую форму
     * @return строка, содержащая представление полинома
     */
    override fun toString() = if (coef.keys.isEmpty()) "0" else coef.keys.sorted().reversed()
        .joinToString(separator = ",") {
            (round((coef[it]!!) / (2.0 * Math.ulp(coef[it]!!))) * 2.0 * Math.ulp(coef[it]!!)
                    ).toString()+"x^"+it
        }.replace(",-"," - ")
        .replace(",", " + ")
        .replace(Regex("x\\^1\\b"), "x")
        .replace("x^0", "")
        .replace("1.0x", "x")
        .replace(Regex("\\.0(?=(\\D|$))"), "")

    /**
     * Оператор сложения вычисляет сумму двух полиномов
     * @param other полином, с которым выполняется сложение
     * @return Новый полином, являющийся суммой текущего полинома, и полинома, переданного в качестве параметра
     */
    operator fun plus(other: Polynomial) =
        Polynomial(
            LinkedHashMap(coef).also {
                other.coef.forEach { (k, v) -> it[k] = v + (it[k] ?: 0.0) }
            }
        )

    /**
     * Оператор сложения с присвоением выполняет сложение двух полиномов.
     * Результат сложения сохраняется в первом полиноме
     * @param other полином, с которым выполняется сложение
     */
    operator fun plusAssign(other: Polynomial) =
        LinkedHashMap(coef).let {
            other.coef.forEach { (k, v) -> it[k] = v + (it[k] ?: 0.0) }
            coef = repairCoeffs(it)
        }

    /**
     * Оператор умножения реализует умножение полинома на число
     * @param v вещественное число, на которое умножается текущий полином
     * @return Новый полином, полученный после умножения исходного полинома на вещественное число
     */
    operator fun times(v: Double) =
        Polynomial(
            LinkedHashMap(coef).also{ it.forEach {
                    me -> it[me.key] = me.value * v
            } } )

    /**
     * Оператор умножения с присвоением выполняет умножение полинома на число
     * Результат при этом сохраняется в текущем экземпляре полинома.
     * @param v Число, на которое требуется умножить полином
     */
    operator fun timesAssign(v: Double) =
        LinkedHashMap(coef).let {
            it.forEach { me -> it[me.key] = it[me.key]!! * v }
            coef = repairCoeffs(it)
        }

    /**
     * Оператор унарного минуса вычисляет произведение данного полинома на -1.0
     * @return Результат умножения полинома на -1.0
     */
    operator fun unaryMinus() = this * -1.0

    /**
     * Оператор унарного плюса введен для симметрии с оператором унарного минуса.
     * @return Копия текущего полинома
     */
    operator fun unaryPlus() = Polynomial(this)

    /**
     * Оператор вычитания вычисляет разность двух полиномов.
     * @param other Вычитаемый полином
     * @return Новый полином, представляющий собой разность текущего полинома и полинома, вычитаемого.
     */
    operator fun minus(other: Polynomial) = this + (-other)

    /**
     * Оператор вычитания с присвоением вычисляет разность двух полиномов
     * Результат вычитания заменяет собой значение первого полинома
     * @param other Вычитаемый полином
     */
    operator fun minusAssign(other: Polynomial) =
        LinkedHashMap(coef).let {
            other.coef.forEach { (k, v) -> it[k] = -v + (it[k] ?: 0.0) }
            coef = repairCoeffs(it)
        }

    /**
     * Оператор деления полинома на число
     * @param v делитель полинома (вещественное число)
     * @return Полином - частное от деления данного полинома на число,
     * либо null, если v равен 0.0
     */
    operator fun div(v: Double): Polynomial? =
        if (v eq 0.0) null else this * (1.0 / v)

    /**
     * Оператор деления текущего полинома на вещественное число.
     * Результат помещается вместо значения исходного полинома
     * @param v вещественный делитель
     * @throws ArithmeticException Возникает при значении v = 0.0
     */
    operator fun divAssign(v: Double) =
        if (v eq 0.0) throw ArithmeticException("Деление на 0")
        else coef.let { it.forEach {
                me -> it[me.key] = it[me.key]!! / v
        } }

    /**
     * Оператор умножения двух полиномов
     * @param other Полином, на которй нужно умножить текущий
     * @return Результат произведения двух полимномов
     */
    operator fun times(other: Polynomial) = Polynomial(
        LinkedHashMap<Int, Double>().also {
            coef.forEach { (k, v) ->
                other.coef.forEach { (ok, ov) ->
                    it[k + ok] = v * ov + (it[k + ok] ?: 0.0)
                }
            }
        }
    )

    /**
     * Оператор умножения двух полиномов.
     * Результат замещает собой значение первого полинома
     * @param other Полином, на который умножается первый полином
     */
    operator fun timesAssign(other: Polynomial) =
        LinkedHashMap<Int, Double>().let {
            coef.forEach { (k, v) ->
                other.coef.forEach { (ok, ov) ->
                    it[k + ok] = v * ov + (it[k + ok] ?: 0.0)
                }
            }
            coef = repairCoeffs(it)
        }

    /**
     * Оператор, вычисляющий значение полинома в указанной точке
     * @param x вещественное число - точка, в которой вычисляется значение полинома
     * @return Вычисленное значение полинома в точке
     */
    operator fun invoke(x: Double): Double{
        var res = 0.0
        coef.forEach { (k, v) -> res += v * Math.pow(x, k.toDouble()) }
        return res
    }

    /**
     * Функция, которая вычисляет производную от данного полинома
     * @return Полиномо, представляющий собой производную данного полинома
     */
    fun d() = Polynomial(LinkedHashMap<Int, Double>().apply{
        coef.forEach {
            this[it.key-1] = it.key * it.value
        }
    })

    /**
     * Оператор, позволяющий получить значение коэффициента полинома при указанной степени
     * @param i степень монома, для которой нужно получить значение коэффициента
     * @return null, для оотрицательных степеней, либо
     * коэффициент при указанной степени
     */
    operator fun get(i: Int): Double? = if (i < 0) null else coef[i] ?: 0.0

    /**
     * Оператор, позволяющий задать значение коэффициента полинома при указанной степени
     * @param i степень монома, для которой нужно получить значение коэффициента
     */
    open operator fun set(i: Int, v: Double) {
        coef = repairCoeffs(LinkedHashMap(coef).also { it[i] = v })
    }
}

class IllegalCoefficientException : Throwable(
    message = "Коэффициент полинома не может быть рассчитан корректно"
)