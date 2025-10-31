import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

fun <A, B> firstThenApply(
    arr: Array<A>,
    pred: (A) -> Boolean,
    transform: (A) -> B
): B? {
    return arr.firstOrNull { x -> pred(x) }?.let { x ->
        transform(x)
    }
}

// exercise 1
fun firstThenLowerCase(
    list: List<String>,
    pred: (String) -> Boolean
): String? {
    return list.firstOrNull { s -> pred(s) }?.lowercase()
}

// exercise 2
data class PhraseChain private constructor(val phrase: String) {
    fun and(next: String): PhraseChain =
        PhraseChain("$phrase $next")

    companion object {
        fun start(first: String): PhraseChain =
            PhraseChain(first)
        fun startEmpty(): PhraseChain =
            PhraseChain("")
    }
}

fun say(): PhraseChain = PhraseChain.startEmpty()
fun say(first: String): PhraseChain = PhraseChain.start(first)

// exercise 3
@Throws(IOException::class)
fun meaningfulLineCount(filename: String): Long {
    BufferedReader(FileReader(filename)).use { reader ->
        return reader
            .lineSequence()
            .filter { line ->
                val trimmed = line.trim()
                !(trimmed.isEmpty() || trimmed.startsWith("#"))
            }
            .count()
            .toLong()
    }
}


// exercise 4
data class Quaternion(
    val a: Double,
    val b: Double,
    val c: Double,
    val d: Double
) {

    init {
        if (a.isNaN() || b.isNaN() || c.isNaN() || d.isNaN()) {
            throw IllegalArgumentException("Coefficients cannot be NaN")
        }
    }

    fun coefficients(): List<Double> =
        listOf(a, b, c, d)

    fun conjugate(): Quaternion =
        Quaternion(a, -b, -c, -d)

    operator fun plus(other: Quaternion): Quaternion =
        Quaternion(
            a + other.a,
            b + other.b,
            c + other.c,
            d + other.d
        )

    operator fun times(other: Quaternion): Quaternion {
        val A = a; val B = b; val C = c; val D = d
        val E = other.a; val F = other.b; val G = other.c; val H = other.d
        return Quaternion(
            A * E - B * F - C * G - D * H,
            A * F + B * E + C * H - D * G,
            A * G - B * H + C * E + D * F,
            A * H + B * G - C * F + D * E
        )
    }

    private fun appendComponent(
        sb: StringBuilder,
        coef: Double,
        symbol: String,
        isFirstTerm: Boolean
    ) {
        if (coef == 0.0) return
        val neg = coef < 0
        val absVal = kotlin.math.abs(coef)
        if (isFirstTerm) {
            if (neg) sb.append("-")
        } else {
            if (neg) sb.append("-") else sb.append("+")
        }
        val isUnit = (absVal == 1.0)
        if (!isUnit) sb.append(absVal.toString())
        sb.append(symbol)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (a != 0.0) sb.append(a.toString())
        appendComponent(sb, b, "i", sb.isEmpty())
        appendComponent(sb, c, "j", sb.isEmpty())
        appendComponent(sb, d, "k", sb.isEmpty())
        if (sb.isEmpty()) return "0"
        return sb.toString()
    }

    companion object {
        val ZERO = Quaternion(0.0, 0.0, 0.0, 0.0)
        val I    = Quaternion(0.0, 1.0, 0.0, 0.0)
        val J    = Quaternion(0.0, 0.0, 1.0, 0.0)
        val K    = Quaternion(0.0, 0.0, 0.0, 1.0)
    }
}

// exercise 5
sealed interface BinarySearchTree {
    fun insert(value: String): BinarySearchTree
    fun contains(value: String): Boolean
    fun size(): Int
    override fun toString(): String

    object Empty : BinarySearchTree {
        override fun insert(value: String): BinarySearchTree =
            Node(value, this, this)
        override fun contains(value: String): Boolean = false
        override fun size(): Int = 0
        override fun toString(): String = "()"
    }

    data class Node(
        val value: String,
        val left: BinarySearchTree,
        val right: BinarySearchTree
    ) : BinarySearchTree {
        override fun insert(value: String): BinarySearchTree {
            val cmp = value.compareTo(this.value)
            return when {
                cmp == 0 -> this
                cmp < 0 -> Node(this.value, left.insert(value), right)
                else -> Node(this.value, left, right.insert(value))
            }
        }

        override fun contains(value: String): Boolean {
            val cmp = value.compareTo(this.value)
            return when {
                cmp == 0 -> true
                cmp < 0 -> left.contains(value)
                else -> right.contains(value)
            }
        }

        override fun size(): Int = 1 + left.size() + right.size()

        override fun toString(): String {
            val leftStr = if (left === Empty) "" else left.toString()
            val rightStr = if (right === Empty) "" else right.toString()
            return "(${leftStr}${value}${rightStr})"
        }
    }
}

