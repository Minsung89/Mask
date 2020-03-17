package com.ddoniddoi.pharmacy

import androidx.appcompat.app.AppCompatActivity
import com.ddoniddoi.pharmacy.dataSet.AddressdataSet
import com.ddoniddoi.pharmacy.db.AppDatabase

/**
 *
 * Base 엑티비티
 *
 */

abstract class BaseActivity : AppCompatActivity(){

    var mContext = this
    //데이터베이스 사용하기 위해 써둠
    var appDatabase : AppDatabase? = null


    abstract fun setValues()

    abstract fun setupEvents()


}