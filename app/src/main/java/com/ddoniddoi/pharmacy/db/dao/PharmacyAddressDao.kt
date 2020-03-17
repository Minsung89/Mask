package com.ddoniddoi.pharmacy.db.dao

import androidx.room.*
import com.ddoniddoi.pharmacy.db.entity.PharmacyAddressEntity

@Dao
interface PharmacyAddressDao {



    @Query("SELECT * FROM pharmacyAddress")
    fun selectAll(): List<PharmacyAddressEntity>

    @Query("SELECT count(*) FROM pharmacyAddress")
    fun selectCount(): Int

    @Query("DELETE FROM pharmacyAddress")
    fun deleteAllData()

    //도시검색
    @Query("SELECT DISTINCT city FROM pharmacyAddress  ORDER BY city ASC")
    fun selectCity(): List<String>

    //군, 구 검색
    @Query("SELECT DISTINCT county FROM pharmacyAddress  where city = :city ORDER BY county ASC")
    fun selectCounty(city: String): List<String>

    //동,읍 검색
    @Query("SELECT DISTINCT district FROM pharmacyAddress where city = :city and county = :county ORDER BY district ASC" )
    fun selectDistrict(city: String, county: String): List<String>
//
//    @Query("SELECT content FROM lessonPlan where medium_category = :mediumCategory and small_category = :smallCategory")
//    fun selectContent(mediumCategory: String, smallCategory: String): List<String>


    @Insert(onConflict = OnConflictStrategy.REPLACE) //이미 저장된 항목이 있을 경우 데이터를 덮어씁니다.
    fun intsert(vararg pharmacyAddressEntity: PharmacyAddressEntity)


}