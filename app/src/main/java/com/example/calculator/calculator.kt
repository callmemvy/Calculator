package com.example.calculator


private val parensExpr = Regex("""^(.*)\(([^()]+)\)(.*)$""")
private val mulDivExpr = Regex("""^(.+)([*/])(.+)$""")
private val plusMinusExpr = Regex("""^(.+)([+-])(.+)$""")

fun eval(expr: String): Double? = evalBraces(expr)

private fun evalBraces(expr: String): Double? {
    val match = parensExpr.find(expr) ?: return evalPlusMinusExpr(expr)

    return eval(match.groupValues[1] + eval(match.groupValues[2]) + match.groupValues[3])
}

private fun evalPlusMinusExpr(expr: String): Double? {
    val match = plusMinusExpr.find(expr) ?: return evalMulDivExpr(expr)

    val a = eval(match.groupValues[1]) ?: return null
    val b = eval(match.groupValues[3]) ?: return null

    return when (match.groupValues[2]) {
        "+" -> a + b
        "-" -> a - b
        else -> null
    }
}

private fun evalMulDivExpr(expr: String): Double? {
    val match = mulDivExpr.find(expr) ?: return evalNumberExpr(expr)

    val a = eval(match.groupValues[1]) ?: return null
    val b = eval(match.groupValues[3]) ?: return null

    return when (match.groupValues[2]) {
        "*" -> a * b
        "/" -> a / b
        else -> null
    }
}

private fun evalNumberExpr(expr: String) = expr.toDoubleOrNull()
