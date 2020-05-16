package com.example.calculator.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseActivity : AppCompatActivity() {
    @SuppressLint("NewApi")
    fun hideKeyboard() {
        edCalculator.run {
            requestFocus()
            showSoftInputOnFocus = false
        }

    }

}