package com.ddoniddoi.pharmacy.api.retrofit

import android.app.Activity
import com.ddoniddoi.pharmacy.api.service.PharmacyService
import com.ddoniddoi.pharmacy.datas.StoreSaleList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StoresByAddrJsonRetrofit(activity : Activity, address : String) {

    //약국 URL
    var URL = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/"

    var activity = activity
    
    var address = address


    lateinit var datas : StoreSaleList
    fun getStoresJson() : Call<StoreSaleList>{

        var retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var getStoresByAddrJsonService = retrofit.create(PharmacyService::class.java)
        return  getStoresByAddrJsonService.getStoresByAddrJson(address)


    }

}

