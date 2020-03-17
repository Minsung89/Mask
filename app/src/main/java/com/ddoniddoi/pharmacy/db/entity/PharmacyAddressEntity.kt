package com.ddoniddoi.pharmacy.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pharmacyAddress")
class PharmacyAddressEntity(@PrimaryKey(autoGenerate = true) val id : Long?,
                            @ColumnInfo(name = "city") var city: String,
                            @ColumnInfo(name = "county") var county: String,
                            @ColumnInfo(name = "district") var district: String)
                            {

    constructor() : this(null,"","","")




}