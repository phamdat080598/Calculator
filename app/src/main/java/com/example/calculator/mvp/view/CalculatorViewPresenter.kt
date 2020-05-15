package com.example.calculator.mvp.view

import com.example.calculator.mvp.View

interface CalculatorViewPresenter : View {
    fun getResultCalculatorSuccess(string:String)
}