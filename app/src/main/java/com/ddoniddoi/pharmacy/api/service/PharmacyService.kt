package com.ddoniddoi.pharmacy.api.service

import com.ddoniddoi.pharmacy.datas.StoreInfosList
import com.ddoniddoi.pharmacy.datas.StoreSaleList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PharmacyService {
    @GET("stores/json")
    fun getStoresJson( @Query("page") page : Int , @Query("perPage") perPage: Int) : Call<StoreInfosList>

    @GET("storesByAddr/json")
    fun getStoresByAddrJson( @Query("address") address : String) : Call<StoreSaleList>

    @GET("storesByGeo/json")
    fun getStroresByGeoJson( @Query("lat") lat : Number, @Query("lng") lng : Number, @Query("m") m : Int): Call<StoreSaleList>
}