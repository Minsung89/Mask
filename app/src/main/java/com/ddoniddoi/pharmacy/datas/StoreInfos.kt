package com.ddoniddoi.pharmacy.datas

class StoreInfos {
    var code: String

    var name: String

    var addr: String

    var type: String

    var lat: Number

    var lng: Number

    constructor(code: String, name: String, addr: String, type: String, lat: Number, lng: Number) {
        this.code = code
        this.name = name
        this.addr = addr
        this.type = type
        this.lat = lat
        this.lng = lng
    }
}