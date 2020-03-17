package com.ddoniddoi.pharmacy

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ddoniddoi.pharmacy.api.retrofit.StoresByAddrJsonRetrofit
import com.ddoniddoi.pharmacy.api.retrofit.StoresByGeoJsonRetrofit
import com.ddoniddoi.pharmacy.api.retrofit.StoresJsonRetrofit
import com.ddoniddoi.pharmacy.datas.StoreSaleList
import com.ddoniddoi.pharmacy.db.AppDatabase
import com.ddoniddoi.pharmacy.dialog.FiveDaysDialog
import com.ddoniddoi.pharmacy.dialog.LocationDialog
import com.ddoniddoi.pharmacy.dialog.LocationPermissionsDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class MainActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    lateinit var selectCity : String
    lateinit var  selectCounty : String
    lateinit var  selectDistrict : String
    lateinit var mMap : GoogleMap
    var backKeyPressedTime : Long = 0
    var gpsLocationListener: LocationListener? = null
    var address : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setValues()
        setupEvents()
        var mapFramgment  = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFramgment.getMapAsync(this)
    }
    var toast : Toast? = null
    override fun onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
        backKeyPressedTime = System.currentTimeMillis()
            toast = Toast.makeText(mContext, "버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT)
            toast!!.show()
        }else{
            toast!!.cancel()
            super.onBackPressed()
       }
    }

    override fun setupEvents() {
        //도시 클릭
        spCity.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0 -> selectCity = "선택"
                    else -> selectCity = spCity.getItemAtPosition(position).toString()

                }

                getCountyData(selectCity)
            }
        }
        //군, 구 클릭
        spCounty.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0 -> selectCounty = "선택"
                    else -> selectCounty = spCounty.getItemAtPosition(position).toString()
                }
                getDistrictData(selectCity, selectCounty)
            }
        }
        //동, 읍, 면 클릭
        spDistrict.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when(position){
                        0 -> selectDistrict = ""
                        else ->  selectDistrict = spDistrict.getItemAtPosition(position).toString()
                    }
            }
        }

        //검색
        addressSearch.setOnClickListener {
            //검색 시 현재위치 초기화 및 이미지도 off로
            if(gpsLocationListener != null){
                (getSystemService(Context.LOCATION_SERVICE) as LocationManager).removeUpdates(gpsLocationListener)
                myLocation.isChecked = false
                myLocation.setBackgroundResource(R.drawable.my_location_off)
                //마커 클리어
                mMap.clear()
            }
            if(selectCity == "선택"){
                Toast.makeText(mContext, "시, 도를 선택해주세요",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else if(selectCounty == "선택"){
                Toast.makeText(mContext, "시, 군, 구를 선택해주세요",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            when(selectDistrict){
                "" -> address = "${selectCity} ${selectCounty}"
                else -> address = "${selectCity} ${selectCounty} ${selectDistrict}"
            }

            //마커 클리어
            mMap.clear()
            var storesByAddrJsonRetrofit = StoresByAddrJsonRetrofit(this,address)
            var call = storesByAddrJsonRetrofit.getStoresJson()
            call.enqueue(object : Callback<StoreSaleList> {
                override fun onFailure(call: Call<StoreSaleList>, t: Throwable) {
                    Log.d("StoreSaleList", "Error")
                }
                override fun onResponse(
                    call: Call<StoreSaleList>,
                    response: Response<StoreSaleList>
                ) {
                    var datas = response.body()!!
                    //마커 찍기
                    markerSelect(datas)
                }
            })
        }
        //내위치 버튼
        myLocation.setOnClickListener {
            //권한 설정이 안되있을 경우 요청
            var permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if(permssionCheck != PackageManager.PERMISSION_GRANTED){
                //다이얼로그 띄워주기
                var locationPermissionsDialog = LocationPermissionsDialog(mContext)
                locationPermissionsDialog.show()
                myLocation.isChecked = false
                return@setOnClickListener
            }
            var manger = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if(manger.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                when(myLocation.isChecked){
                    true ->{
                        myLoaction()
                        myLocation.setBackgroundResource(R.drawable.my_location_on)
                    }
                    false -> {
                        mMap.clear()
                        (getSystemService(Context.LOCATION_SERVICE) as LocationManager).removeUpdates(gpsLocationListener)
                        myLocation.setBackgroundResource(R.drawable.my_location_off)
                    }
                }
            }else{
                myLocation.isChecked = false
                //다이얼로그 띄우기
                var locationDialog = LocationDialog(mContext)
                locationDialog.show()
            }
        }

        //새로고침
        refreshBtn.setOnClickListener {
            if(myLocation.isChecked){
                myLoaction()
            }else{
                //마커 클리어
                mMap.clear()
                var storesByAddrJsonRetrofit = StoresByAddrJsonRetrofit(this,address)
                var call = storesByAddrJsonRetrofit.getStoresJson()
                call.enqueue(object : Callback<StoreSaleList> {
                    override fun onFailure(call: Call<StoreSaleList>, t: Throwable) {
                        Log.d("StoreSaleList", "Error")
                    }
                    override fun onResponse(
                        call: Call<StoreSaleList>,
                        response: Response<StoreSaleList>
                    ) {
                        var datas = response.body()!!
                        //마커 찍기
                        markerSelect(datas)
                    }
                })
            }
            val currentDegree = refreshBtn.rotation
            ObjectAnimator.ofFloat(refreshBtn, View.ROTATION, currentDegree, currentDegree + 360)
                .setDuration(400)
                .start()
        }
        //5부제
        fiveDays.setOnClickListener {
           FiveDaysDialog(mContext).show()
        }


    }
    //마커찍기
    fun markerSelect(datas : StoreSaleList){
        if((datas == null) or (datas.count == 0)) {
            Toast.makeText(mContext,"마스크 파는 곳이 없습니다",Toast.LENGTH_LONG).show()
        }else{
//                        markerRemove(markers)
            //마커 찍기
            for (data in datas!!.stores) {
                var markerOptions = MarkerOptions()

                //마커 위치
                if((data != null) and (data.addr != null) and (data.lat != null)
                    and (data.lng != null) and (data.created_at != null) and (data.stock_at != null)) {
                    markerOptions.position(
                            LatLng(
                                data.lat.toDouble(),
                                data.lng.toDouble()
                            )
                        )
                        .title(data.name)//마커 제목

                    if(data.remain_stat != null){
                        when(data.remain_stat){
                            "break" ->{
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.x_break))
                                    .snippet("${data.addr}\n입고시간 : ${data.stock_at}\n제고현황 : 판매중단")
                            }
                            "plenty" -> {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.plenty))
                                    .snippet("${data.addr}\n입고시간 : ${data.stock_at}\n제고현황 : 100~개")
                            }
                            "some" -> {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.some))
                                    .snippet("${data.addr}\n입고시간 : ${data.stock_at}\n제고현황 : 30~100개")
                            }
                            "few" ->{
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.few))
                                    .snippet("${data.addr}\n입고시간 : ${data.stock_at}\n제고현황 : 2~30개")
                            }
                            "empty" -> {markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.empty))
                                .snippet("${data.addr}\n입고시간 : ${data.stock_at}\n제고현황 : 1개이하")
                            }
                        }
                    }

                    var marker = mMap.addMarker(markerOptions)
                }
            }

            for (data in datas!!.stores){
                if((data != null) and (data.lat != null) and (data.lng != null) and (data.created_at != null) and (!myLocation.isChecked)) {
                    var update = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            data.lat.toDouble(),
                            data.lng.toDouble()
                        ), when(selectDistrict){
                            "" -> 11F
                            else -> 14F
                        }
                    )
                    mMap.moveCamera(update)
                    return
                }
            }


        }
    }

//    //마커 지우기
//    fun markerRemove(markers : List<Marker>){
//        if(markers.isNotEmpty()){
//            for (marker in markers){
//                marker.remove()
//            }
//        }
//
//    }
    override fun setValues() {

        //메인 엑티비티에서 데이터 베이스 사용하기 위해 인스턴스
        appDatabase = AppDatabase.getInstance(mContext)
        getCityData()
//        StoresJsonRetrofit(this).getStoresJson()
    }
    //반경 3킬로미터
    fun myLocationCircle(latLng: LatLng){
        var circle1KM = CircleOptions().center(latLng) //원점
            .radius(3000.0)      //반지름 단위 : m
            .strokeWidth(0f)  //선너비 0f : 선없음
            .fillColor(Color.parseColor("#800000FF")) //배경색
        mMap.addCircle(circle1KM)

    }

    //현재 내위치
    @SuppressLint("MissingPermission")
    fun myLoaction(){
        //내 위치 찍을 시 검색 데이터 초기화
        address = ""
        var lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(location == null){
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            setLocation(location)
        }else{
            setLocation(location)
        }
        gpsLocationListener = object : LocationListener{
            override fun onLocationChanged(location: Location?) {//실시간
//                setLocation(location)
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {}
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0F, gpsLocationListener!!)
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, gpsLocationListener!!)
    }
    //내 위치 마커 및 이동
    fun setLocation(location: Location?){
        mMap.clear()
        //경도/위도
        var latlng = LatLng(location!!.latitude,location!!.longitude)
        //반경 3KM
        myLocationCircle(latlng)

        //내위치 마커 찍기
        var markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.my_location))
        markerOptions.position(latlng)
        mMap.addMarker(markerOptions)
        var storesByGeoJsonRetrofit = StoresByGeoJsonRetrofit(mContext, location!!.latitude,location!!.longitude, 3000)
        var call = storesByGeoJsonRetrofit.getStoresJson()
        call.enqueue(object : Callback<StoreSaleList> {
            override fun onFailure(call: Call<StoreSaleList>, t: Throwable) {
                Log.d("StoreSaleList", "Error")
            }
            override fun onResponse(call: Call<StoreSaleList>,response: Response<StoreSaleList>){
                if(response.body() != null){
                    var datas = response.body()!!
                    //마커 찍기
                    markerSelect(datas)
                }
            }
        })
        //내위치 카메라 이동
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,13F))
    }

    //spinner
    fun getCityData(){
        val runnable = Runnable {
            //city Data
            var selectCity = appDatabase?.pharmacyAddressdao()?.selectCity()
            var selectCityArray : ArrayList<String> = ArrayList()
            selectCityArray.add(getString(R.string.spinner_city))
            if (selectCity != null) {
                selectCityArray.addAll(selectCity)
            }
            runOnUiThread{
                var arrayAdapter = ArrayAdapter(mContext,android.R.layout.simple_spinner_dropdown_item, selectCityArray)
                spCity.adapter = arrayAdapter
            }
        }
        var appThread = Thread(runnable)
        appThread.start()
    }
    fun getCountyData(city : String){
        val runnable = Runnable {
            //county Data
            var selectCounty = appDatabase?.pharmacyAddressdao()?.selectCounty(city)

            var selectCountyArray : ArrayList<String> = ArrayList()
            selectCountyArray.add(getString(R.string.spinner_county))
            if (selectCounty != null) {
                selectCountyArray.addAll(selectCounty)
            }
            runOnUiThread{
                var arrayAdapter = ArrayAdapter(mContext,android.R.layout.simple_spinner_dropdown_item, selectCountyArray)

                spCounty.adapter = arrayAdapter
            }
        }
        var appThread = Thread(runnable)
        appThread.start()
    }

    fun getDistrictData(city : String, county : String){
        val runnable = Runnable {
            //city Data
            var selectDistrict = appDatabase?.pharmacyAddressdao()?.selectDistrict(city, county)
            var selectDistrictArray : ArrayList<String> = ArrayList()
            selectDistrictArray.add(getString(R.string.spinner_district))
            if (selectDistrict != null) {
                selectDistrictArray.addAll(selectDistrict)
            }
            runOnUiThread{
                var arrayAdapter = ArrayAdapter(mContext,android.R.layout.simple_spinner_dropdown_item, selectDistrictArray)
                spDistrict.adapter = arrayAdapter
            }
        }
        var appThread = Thread(runnable)
        appThread.start()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        //카메라 이동
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.541, 126.986),12F))
        mMap.setOnMarkerClickListener(this)
    }

    //MarkerClick
    override fun onMarkerClick(marker: Marker?): Boolean {
        var latLng = marker!!.position
        marker.showInfoWindow()
        //말풍선 클릭 리스너
        mMap.setOnInfoWindowClickListener(object : GoogleMap.OnInfoWindowClickListener{
            override fun onInfoWindowClick(marker: Marker?) {
                Toast.makeText(mContext, "${marker!!.snippet}",Toast.LENGTH_LONG).show()
            }
        })
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16F))
        return true
    }




}
