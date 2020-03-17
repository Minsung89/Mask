package com.ddoniddoi.pharmacy.datas

class StoreSaleList {



    var count : Int

    var  stores : List<Stores>


    constructor(
        count: Int,
        stores: List<Stores>
    ) {
        this.count = count
        this.stores = stores
    }

}