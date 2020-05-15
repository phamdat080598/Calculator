package com.example.calculator.mvp

interface Presenter {
    fun attachView(view:View)
    fun dispose()
}