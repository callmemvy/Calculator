package com.example.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var buttonsResultText: Map<Int, String>

    private val resultExpr = mutableListOf<Int>()

    private lateinit var result: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        result = findViewById(R.id.result)
        result.movementMethod = ScrollingMovementMethod()
        result.setRawInputType(InputType.TYPE_NULL)
        result.setTextIsSelectable(true)
        result.isSingleLine = false

        buttonsResultText = mapOf(
            R.id.keyboard0 to resources.getString(R.string.keyboard0),
            R.id.keyboard1 to resources.getString(R.string.keyboard1),
            R.id.keyboard2 to resources.getString(R.string.keyboard2),
            R.id.keyboard3 to resources.getString(R.string.keyboard3),
            R.id.keyboard4 to resources.getString(R.string.keyboard4),
            R.id.keyboard5 to resources.getString(R.string.keyboard5),
            R.id.keyboard6 to resources.getString(R.string.keyboard6),
            R.id.keyboard7 to resources.getString(R.string.keyboard7),
            R.id.keyboard8 to resources.getString(R.string.keyboard8),
            R.id.keyboard9 to resources.getString(R.string.keyboard9),
            R.id.keyboardPoint to resources.getString(R.string.keyboardPoint),
            R.id.keyboardLeftParenthesis to resources.getString(R.string.keyboardLeftParenthesis),
            R.id.keyboardRightParenthesis to resources.getString(R.string.keyboardRightParenthesis),
            R.id.keyboardPlus to " ${resources.getString(R.string.keyboardPlus)} ",
            R.id.keyboardMinus to " ${resources.getString(R.string.keyboardMinus)} ",
            R.id.keyboardMultiply to " ${resources.getString(R.string.keyboardMultiply)} ",
            R.id.keyboardDivide to " ${resources.getString(R.string.keyboardDivide)} ",
        )

        listOf(
            R.id.keyboard0,
            R.id.keyboard1,
            R.id.keyboard2,
            R.id.keyboard3,
            R.id.keyboard4,
            R.id.keyboard5,
            R.id.keyboard6,
            R.id.keyboard7,
            R.id.keyboard8,
            R.id.keyboard9,
            R.id.keyboardPoint,
            R.id.keyboardBackspace,
            R.id.keyboardLeftParenthesis,
            R.id.keyboardRightParenthesis,
            R.id.keyboardPlus,
            R.id.keyboardMinus,
            R.id.keyboardMultiply,
            R.id.keyboardDivide,
        ).map<Int, Button?>(this@MainActivity::findViewById)
            .forEach { it!!.setOnClickListener(this) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putIntegerArrayList("resultExpr", resultExpr as ArrayList<Int>)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        resultExpr.addAll(savedInstanceState.getIntegerArrayList("resultExpr")!!)
    }

    @SuppressLint("SetTextI18n")
    private fun renderResult() {
        val exprText = resultExpr
            .map(buttonsResultText::get)
            .joinToString(separator = "")
            .trim()

        val resultText = when (val result = evalResult()) {
            null -> exprText
            else -> "$exprText = ${formatDouble(result)}"
        }

        result.setText(resultText)
        result.setSelection(resultText.length)
    }

    private fun formatDouble(n: Double) = when (n.isFinite()) {
        true -> n.toBigDecimal().stripTrailingZeros().toPlainString()
        false -> n.toString()
    }

    private fun mapResultIdToExpr(id: Int) = when (id) {
        R.id.keyboard0 -> "0"
        R.id.keyboard1 -> "1"
        R.id.keyboard2 -> "2"
        R.id.keyboard3 -> "3"
        R.id.keyboard4 -> "4"
        R.id.keyboard5 -> "5"
        R.id.keyboard6 -> "6"
        R.id.keyboard7 -> "7"
        R.id.keyboard8 -> "8"
        R.id.keyboard9 -> "9"
        R.id.keyboardPoint -> "."
        R.id.keyboardLeftParenthesis -> "("
        R.id.keyboardRightParenthesis -> ")"
        R.id.keyboardPlus -> "+"
        R.id.keyboardMinus -> "-"
        R.id.keyboardMultiply -> "*"
        R.id.keyboardDivide -> "/"
        else -> ""
    }

    private fun resultToExpr() = resultExpr
        .joinToString(separator = "", transform = this::mapResultIdToExpr)

    private fun evalResult() = eval(resultToExpr())

    override fun onClick(v: View?) {
        val id = v?.id ?: return

        when (id) {
            R.id.keyboardBackspace -> resultExpr.removeLastOrNull()
            else -> resultExpr.add(id)
        }

        renderResult()
    }
}
