package com.example.calculator.api

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v4/")
    fun getResultCalculator(@Query("expr") expr:String):Observable<Response<String>>
}