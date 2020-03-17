package com.ddoniddoi.pharmacy.datas

class Stores {
    var code: String

    var name: String

    var addr: String

    var type: String

    var lat: Number

    var lng: Number

    var stock_at: String

    var remain_stat: String

    var created_at: String


    constructor(
        code: String,
        name: String,
        addr: String,
        type: String,
        lat: Number,
        lng: Number,
        stock_at: String,
        remain_stat: String,
        created_at: String
    ) {
        this.code = code
        this.name = name
        this.addr = addr
        this.type = type
        this.lat = lat
        this.lng = lng
        this.stock_at = stock_at
        this.remain_stat = remain_stat
        this.created_at = created_at
    }
}