package com.example.calculator.mvp.presenter

import com.example.calculator.api.ApiUtil
import com.example.calculator.mvp.Presenter
import com.example.calculator.mvp.View
import com.example.calculator.mvp.view.CalculatorViewPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CalculatorPresenter : Presenter {

    private lateinit var viewPresenter: CalculatorViewPresenter
    private val composite = CompositeDisposable()

    override fun attachView(view: View) {
        viewPresenter = view as CalculatorViewPresenter
    }

    override fun dispose() {
        composite.dispose()
    }

    fun calculatorExpression(expr: String) {
        composite.add(ApiUtil.getAPIService().getResultCalculator(expr)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when (it.raw().code()) {
                        200 -> {
                            viewPresenter.getResultCalculatorSuccess(it.body()!!)
                        }
                        400 -> {
                            viewPresenter.showError(it.errorBody()!!.string())
                        }
                        else -> {
                            viewPresenter.showError("Error Unknown ")
                        }
                    }
                }, {
                    viewPresenter.showError("Error Unknown (please check for internet)" )
                }
            )
        )
    }
}