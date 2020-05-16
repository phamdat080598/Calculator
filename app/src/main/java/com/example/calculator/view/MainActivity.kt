package com.example.calculator.view

import android.os.Bundle
import com.example.calculator.R
import com.example.calculator.base.BaseActivity
import com.example.calculator.mvp.presenter.CalculatorPresenter
import com.example.calculator.mvp.view.CalculatorViewPresenter
import com.example.calculator.utils.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class MainActivity : BaseActivity(), CalculatorViewPresenter {

    private var expr: String = ""

    private lateinit var calculatorPresenter: CalculatorPresenter

    private val operator = "+-*/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculatorPresenter = CalculatorPresenter()
        calculatorPresenter.attachView(this)

        hideKeyboard()

        eventClick()

    }

    //event click
    private fun eventClick() {
        btnZero.setOnClickListener {
            expr.plusCalculator(resources.getString(R.string.zero))
        }
        btnOne.setOnClickListener {
            expr.plusCalculator(resources.getString(R.string.one))
        }
        btnTwo.setOnClickListener {
            expr.plusCalculator(resources.getString(R.string.two))
        }
        btnThree.setOnClickListener {
            expr.plusCalculator(resources.getString(R.string.three))
        }
        btnFour.setOnClickListener {
            expr.plusCalculator(resources.getString(R.string.four))
        }
        btnFive.setOnClickListener {
            expr.plusCalculator(resources.getString(R.string.five))
        }
        btnSix.setOnClickListener {
            expr.plusCalculator(resources.getString(R.string.six))
        }
        btnSeven.setOnClickListener {
            expr.plusCalculator(resources.getString(R.string.seven))
        }
        btnEight.setOnClickListener {
            expr.plusCalculator(resources.getString(R.string.eight))
        }
        btnNine.setOnClickListener {
            expr.plusCalculator(resources.getString(R.string.nine))
        }

        btnDot.setOnClickListener {
            expr.plusCalculator(".")
        }

        btnBracketLeft.setOnClickListener {
            expr.plusCalculator("(")
        }

        btnBracketRight.setOnClickListener {
            expr.plusCalculator(")")
        }

        btnPlus.setOnClickListener {
            expr.plusCalculator("+")
        }
        btnMinus.setOnClickListener {
            expr.plusCalculator("-")
        }
        btnMultiplication.setOnClickListener {
            expr.plusCalculator("*")
        }
        btnDivision.setOnClickListener {
            expr.plusCalculator("/")
        }

        btnClear.setOnClickListener {
            val index = edCalculator.selectionEnd
            if (index > 0) {
                expr = expr.substring(0, index - 1) + expr.substring(index, expr.length)
                edCalculator.setText(expr)
                edCalculator.setSelection(index - 1)
            }
        }
        btnAllClear.setOnClickListener {
            expr = ""
            edCalculator.setText(expr)
            edCalculator.setSelection(edCalculator.length())
            tvResult.text = resources.getString(R.string.pointZero)
        }

        btnResult.setOnClickListener {
            if(swApi.isChecked){
                formatExprApi()
                calculatorPresenter.calculatorExpression(expr)
            }else {
                val result = calculator().toString()
                if (result == "Infinity") {
                    tvResult.text = resources.getString(R.string.pointZero)
                    toast("Error: Infinity")
                } else {
                    tvResult.text = result
                }
                expr = edCalculator.text.toString()
                edCalculator.setSelection(expr.length)
            }
        }
    }

    //presenter
    override fun getResultCalculatorSuccess(string: String) {
        tvResult.text = string.toDouble().toString()
    }

    override fun showError(error: String) {
        toast(error)
    }

    //fun extension
    private fun String.plusCalculator(text: String) {
        if (edCalculator.selectionEnd == expr.length) {
            expr = expr.plus(text)
            edCalculator.setText(expr)
            edCalculator.setSelection(edCalculator.length())
        } else if (edCalculator.selectionEnd > 0 && edCalculator.selectionEnd < expr.length) {
            val index = edCalculator.selectionEnd
            expr = expr.substring(0, index) + text + expr.substring(index, expr.length)
            edCalculator.setText(expr)
            edCalculator.setSelection(index + 1)
        } else {
            expr = text + expr
            edCalculator.setText(expr)
            edCalculator.setSelection(1)
        }
    }

    // calculator expr
    private fun calculator(): Double {
        while (expr.contains('(')) {
            val begin = expr.lastIndexOf('(')
            val last = expr.indexOf(")", begin + 1)
            if (last == -1) {
                toast("Error : format ()")
                return 0.0
            }
            val result = calculatorSingle(expr.substring(begin + 1, last))
            this.expr =
                "${expr.substring(0, begin)}$result${expr.substring(last + 1, expr.length)}"
        }
        return calculatorSingle(this.expr)
    }

    private fun calculatorSingle(expr: String): Double {
        if (!expr.contains('+') && !expr.contains('-') && !expr.contains('*') && !expr.contains(
                '/'
            )
        ) {
            return expr.toDouble()
        }
        val typeNumber = arrayListOf<Double>()
        val typeOperator = arrayListOf<String>()
        var indexOperator = -1
        var result = 0.0
        for (i in 1 until expr.length) {
            if (operator.contains(expr[i]) && !operator.contains(expr[i - 1])) {
                try {
                    typeNumber.add(expr.substring(indexOperator + 1, i).toDouble())
                } catch (ex: NumberFormatException) {
                    toast(ex.message!!)
                    return 0.0
                }
                typeOperator.add(expr[i].toString())
                indexOperator = i
            }
        }
        try {
            typeNumber.add(expr.substring(indexOperator + 1, expr.length).toDouble())
        } catch (ex: java.lang.NumberFormatException) {
            toast(ex.message!!)
            return 0.0
        }
        var i = typeOperator.size - 1
        while (i > 1) {
            if (typeOperator[i] == "*" && typeOperator[i - 1] == "/") {
                typeOperator.removeAt(i)
                typeOperator.add(i - 1, "*")
                val value = typeNumber[i + 1]
                typeNumber.removeAt(i + 1)
                typeNumber.add(i, value)
            }
            i--
        }

        i = 0
        while (typeOperator.contains("*")) {
            when (typeOperator[i]) {
                "*" -> {
                    result = calculatorOperator(typeOperator, typeNumber, typeOperator[i])
                }
                else -> {
                    i++
                }
            }
        }
        i = 0
        while (typeOperator.contains("/")) {
            when (typeOperator[i]) {
                "/" -> {
                    result = calculatorOperator(typeOperator, typeNumber, typeOperator[i])
                }
                else -> {
                    i++
                }
            }
        }

        if (typeOperator.size > 0) {
            result = calculatorLeftToRight(typeOperator, typeNumber)
        }
        return result
    }

    private fun calculatorOperator(
        typeOperator: ArrayList<String>,
        typeNumber: ArrayList<Double>,
        operator: String
    ): Double {
        val index = typeOperator.indexOf(operator)
        var result = 0.0
        when (operator) {
            "*" -> {
                result = typeNumber[index].times(typeNumber[index + 1])
            }
            "/" -> {
                result = typeNumber[index].div(typeNumber[index + 1])
            }
            "+" -> {
                result = typeNumber[index] + typeNumber[index + 1]
            }
            "-" -> {
                result = typeNumber[index] - typeNumber[index + 1]
            }
        }
        typeNumber.run {
            removeAt(index)
            removeAt(index)
            add(index, result)
        }
        typeOperator.removeAt(index)
        return result
    }

    private fun calculatorLeftToRight(
        typeOperator: ArrayList<String>,
        typeNumber: ArrayList<Double>
    ): Double {
        var result = 0.0
        for (i in 0 until typeOperator.size) {
            when (typeOperator[i]) {
                "+" -> {
                    if (i == 0) {
                        result = typeNumber[i] + typeNumber[i + 1]
                    } else {
                        result += typeNumber[i + 1]
                    }
                }
                "-" -> {
                    if (i == 0) {
                        result += typeNumber[i] - typeNumber[i + 1]
                    } else {
                        result -= typeNumber[i + 1]
                    }
                }
            }
        }
        return result
    }

    //format string
    private fun formatExprApi(){
        for(i in expr){
            when(i){
                '/' -> expr.replace("/"," %2F")
                '+' -> expr.replace("+"," %2B")
            }
        }
    }
}
