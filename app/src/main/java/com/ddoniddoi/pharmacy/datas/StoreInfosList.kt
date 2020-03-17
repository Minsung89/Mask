package com.ddoniddoi.pharmacy.datas

class StoreInfosList {

    var totalPages : Int

    var totalCount : Int

    var page : Int

    var count : Int

    var  storeInfos : List<StoreInfos>


    constructor(
        totalPages: Int,
        totalCount: Int,
        page: Int,
        count: Int,
        storeInfos: List<StoreInfos>
    ) {
        this.totalPages = totalPages
        this.totalCount = totalCount
        this.page = page
        this.count = count
        this.storeInfos = storeInfos
    }
}