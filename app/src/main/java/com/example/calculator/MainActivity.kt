package com.example.calculator

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var textResult: String = "6*6"
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideKeyboard()

        eventClick()

//        val inputMethodManager: InputMethodManager =getSystemService(
//            INPUT_METHOD_SERVICE
//        ) as InputMethodManager
//        inputMethodManager.hideSoftInputFromWindow(
//            currentFocus?.windowToken, 0
//        )
    }

    private fun eventClick() {
        btnOne.setOnClickListener {
            textResult.plusCalculator("1")
        }
        btnTwo.setOnClickListener {
            textResult.plusCalculator("2")
        }
        btnThree.setOnClickListener {
            textResult.plusCalculator("3")
        }
        btnFour.setOnClickListener {
            textResult.plusCalculator("4")
        }
        btnFive.setOnClickListener {
            textResult.plusCalculator("5")
        }
        btnSix.setOnClickListener {
            textResult.plusCalculator("6")
        }
        btnSeven.setOnClickListener {
            textResult.plusCalculator("7")
        }
        btnEight.setOnClickListener {
            textResult.plusCalculator("8")
        }
        btnNine.setOnClickListener {
            textResult.plusCalculator("1")
        }

        btnDot.setOnClickListener {
            textResult.plusCalculator("9")
        }

        btnPlus.setOnClickListener {
            textResult.plusCalculator("+")
        }
        btnMinus.setOnClickListener {
            textResult.plusCalculator("-")
        }
        btnMultiplication.setOnClickListener {
            textResult.plusCalculator("*")
        }
        btnDivision.setOnClickListener {
            textResult.plusCalculator("/")
        }

        btnClear.setOnClickListener {
            textResult = textResult.substring(edCalculator.selectionEnd, edCalculator.selectionEnd-1)
            edCalculator.setText(textResult)
            edCalculator.setSelection(edCalculator.length())
        }
        btnAllClear.setOnClickListener {
            textResult = ""
            edCalculator.setText(textResult)
            edCalculator.setSelection(edCalculator.length())
        }

        btnResult.setOnClickListener {
            tvResult.text = textResult.toInt().toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun hideKeyboard() {
        edCalculator.run {
            requestFocus()
            showSoftInputOnFocus=false
        }

    }

    private fun String.plusCalculator(text: String) {
        textResult = textResult.plus(text)
        edCalculator.setText(textResult)
        edCalculator.setSelection(edCalculator.length())
    }
}
