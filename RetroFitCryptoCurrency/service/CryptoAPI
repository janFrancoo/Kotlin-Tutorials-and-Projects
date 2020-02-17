package com.janfranco.retrofitcryptocurrencyapp.service

import com.janfranco.retrofitcryptocurrencyapp.model.CryptoModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {

    @GET("prices?key=a683cd45a3fd13b19c0db25c0ee17160")
    fun getData(): Observable<List<CryptoModel>>
    // fun getData(): Call<List<CryptoModel>>

}
