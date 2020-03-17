package com.ddoniddoi.pharmacy.db

import android.content.Context
import androidx.room.*
import com.ddoniddoi.pharmacy.db.dao.PharmacyAddressDao
import com.ddoniddoi.pharmacy.db.entity.PharmacyAddressEntity


//테이블 만들기(Entity 형태로)
@Database(entities = [PharmacyAddressEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pharmacyAddressdao() : PharmacyAddressDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room
                        .databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "appdatabase.db") //디비명
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }


}