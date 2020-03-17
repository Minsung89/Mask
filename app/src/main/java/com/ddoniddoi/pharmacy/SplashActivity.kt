package com.ddoniddoi.pharmacy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ddoniddoi.pharmacy.dataSet.AddressdataSet
import com.ddoniddoi.pharmacy.db.AppDatabase

/**
 *
 * 인트로 엑티비티
 *
 *
 */

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //상태바 & 타이틀바 없애기
        setTheme(android.R.style.Theme_NoTitleBar_Fullscreen)

        setContentView(R.layout.activity_splash)
        //메인 엑티비티에서 데이터 베이스 사용하기 위해 인스턴스
        appDatabase = AppDatabase.getInstance(mContext)
        setValues()
        setupEvents()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun setValues() {

        //권한 승인체크 승인 시 PERMISSION_GRANTED
        var permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if(permssionCheck != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                pharmacyAddressData()
            }else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001)
            }
        }else{
            pharmacyAddressData()
        }

    }

    override fun setupEvents() {
    }

    private fun pharmacyAddressData(){

            Thread(Runnable {
                if(appDatabase!!.pharmacyAddressdao().selectCount() < 1) {
                    appDatabase!!.runInTransaction {
                        AddressdataSet().addressData1(appDatabase!!)
                    }
                    appDatabase!!.runInTransaction {
                        Thread(Runnable {
                            AddressdataSet().addressData2(appDatabase!!)
                        }).start()
                    }
                    appDatabase!!.runInTransaction {
                        Thread(Runnable {
                            AddressdataSet().addressData4(appDatabase!!)
                        }).start()
                    }
                    appDatabase!!.runInTransaction {
                        Thread(Runnable {
                            AddressdataSet().addressData3(appDatabase!!)
                            Thread(Runnable {
                                appDatabase!!.runInTransaction {
                                    AddressdataSet().addressData5(appDatabase!!)
                                    handler.sendEmptyMessageDelayed(0, 0)
                                }
                            }).start()
                        }).start()
                    }
                }else{
                    handler.sendEmptyMessageDelayed(0, 1000)
                }
            }).start()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            1001 ->
                if((grantResults.isNotEmpty()) and (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                }else{
//                    Toast.makeText(this,"아직 승인받지 않았습니다.",Toast.LENGTH_LONG).show()
                }
        }
        pharmacyAddressData()
    }

    var handler = object : Handler(){

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            //메인페이지로 이동
            var intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)


            finish()
            //처음화면에서 점점 사라지는 애니메이션효과
            overridePendingTransition(R.anim.load_fade_in,R.anim.load_fade_out)
        }
    }
}
