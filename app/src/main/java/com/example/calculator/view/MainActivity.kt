package com.example.calculator.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.R
import com.example.calculator.mvp.presenter.CalculatorPresenter
import com.example.calculator.mvp.view.CalculatorViewPresenter
import com.example.calculator.utils.toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), CalculatorViewPresenter {

    private var expr: String = ""

    private lateinit var calculatorPresenter: CalculatorPresenter

    private val operator = "+-*/"

    private val number = "0123456789"


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
            expr.plusCalculator("0")
        }
        btnOne.setOnClickListener {
            expr.plusCalculator("1")
        }
        btnTwo.setOnClickListener {
            expr.plusCalculator("2")
        }
        btnThree.setOnClickListener {
            expr.plusCalculator("3")
        }
        btnFour.setOnClickListener {
            expr.plusCalculator("4")
        }
        btnFive.setOnClickListener {
            expr.plusCalculator("5")
        }
        btnSix.setOnClickListener {
            expr.plusCalculator("6")
        }
        btnSeven.setOnClickListener {
            expr.plusCalculator("7")
        }
        btnEight.setOnClickListener {
            expr.plusCalculator("8")
        }
        btnNine.setOnClickListener {
            expr.plusCalculator("9")
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
            if(index>0) {
                expr = expr.substring(0, index - 1) + expr.substring(index, expr.length)
                edCalculator.setText(expr)
                edCalculator.setSelection(index-1)
            }
        }
        btnAllClear.setOnClickListener {
            expr = ""
            edCalculator.setText(expr)
            edCalculator.setSelection(edCalculator.length())
            tvResult.text="0.0"
        }

        btnResult.setOnClickListener {
//            if(checkValidation(expr)) {
//                calculatorPresenter.calculatorExpression(expr)
//            }else{
//                toast("Lỗi biểu thức không đúng!")
//            }
            tvResult.text = calculator().toString()
            expr= edCalculator.text.toString()
            edCalculator.setSelection(expr.length)
        }
    }

    //presenter
    override fun getResultCalculatorSuccess(string: String) {
        tvResult.text = string
    }

    override fun showError(error: String) {
        toast(error)
    }

    //fun
    private fun String.plusCalculator(text: String) {
        if(edCalculator.selectionEnd==expr.length) {
            expr = expr.plus(text)
            edCalculator.setText(expr)
            edCalculator.setSelection(edCalculator.length())
        }else if(edCalculator.selectionEnd>0&&edCalculator.selectionEnd<expr.length){
            val index = edCalculator.selectionEnd
            expr = expr.substring(0, index) +text+ expr.substring(index, expr.length)
            edCalculator.setText(expr)
            edCalculator.setSelection(index+1)
        }else{
            expr=text + expr
            edCalculator.setText(expr)
            edCalculator.setSelection(1)
        }
    }

    @SuppressLint("NewApi")
    private fun hideKeyboard() {
        edCalculator.run {
            requestFocus()
            showSoftInputOnFocus = false
        }

    }

    private fun checkValidation(expr: String): Boolean {
        if ("$operator.)".contains(expr[0]) || "$operator.(".contains(expr.last())) {
            return false
        }
        for (i in expr) {
            if ("$operator.(".contains(i) && "$operator.)".contains(i + 1)) {//++
                return false
            }
        }
        return true
    }

    private fun calculator() :Double{
        while (expr.contains('(')) {
            val begin = expr.lastIndexOf('(')
            val last = expr.indexOf(")", begin + 1)
            val result = calculatorSingle(expr.substring(begin + 1, last))
            this.expr ="${expr.substring(0, begin)}$result${expr.substring(last+1, expr.length)}"
        }
        return calculatorSingle(this.expr)
    }

    private fun calculatorSingle(expr: String): Double {
        if(!expr.contains('+')&&!expr.contains('-')&&!expr.contains('*')&&!expr.contains('/')){
            return expr.toDouble()
        }
        val typeNumber = arrayListOf<Double>()
        val typeOperator = arrayListOf<String>()
        var indexOperator = -1
        var result = 0.0
        for (i in expr.indices) {
            if (operator.contains(expr[i])) {
                typeNumber.add(expr.substring(indexOperator + 1, i).toDouble())
                typeOperator.add(expr[i].toString())
                indexOperator = i
            }
        }
        typeNumber.add(expr.substring(indexOperator + 1, expr.length).toDouble())
        var i = typeOperator.size - 1
        while (i>1) {
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

        if(typeOperator.size>0) {
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

    private fun calculatorLeftToRight( typeOperator: ArrayList<String>,
                                       typeNumber: ArrayList<Double>
                                       ):Double{
        var result =0.0
        for(i in 0 until typeOperator.size){
            when(typeOperator[i]){
                "+"->{
                    if(i==0) {
                        result = typeNumber[i] + typeNumber[i + 1]
                    }else{
                        result +=typeNumber[i+1]
                    }
                }
                "-"->{
                    if(i==0) {
                        result += typeNumber[i] - typeNumber[i + 1]
                    }else{
                        result -=typeNumber[i+1]
                    }
                }
            }
        }
        return result
    }
}
