package com.ddoniddoi.pharmacy.api.retrofit

import android.app.Activity
import android.util.Log
import com.ddoniddoi.pharmacy.api.service.PharmacyService
import com.ddoniddoi.pharmacy.datas.StoreInfosList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StoresJsonRetrofit(activity : Activity) {

    //약국 URL
    var URL = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/"

    var activity = activity


//    fun getStoresJson(){
//        var retrofit = Retrofit.Builder()
//            .baseUrl(URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        var storesJsonService = retrofit.create(PharmacyService::class.java)
//        var call = storesJsonService.getStoresJson(1,500)
//        call.enqueue(object : Callback<StoreInfosList>{
//            override fun onFailure(call: Call<StoreInfosList>, t: Throwable) {
//                Log.d("storeInfosList", "Error")
//            }
//
//            override fun onResponse(
//                call: Call<StoreInfosList>,
//                response: Response<StoreInfosList>
//            ) {
//                var datas = response.body()
//                for (data in datas!!.storeInfos){
//                    Log.d("storeInfosList", "${data!!.addr}")
//                }
//
//            }
//
//        })
//    }

}