package com.ddoniddoi.pharmacy.dialog

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import androidx.core.app.ActivityCompat
import com.ddoniddoi.pharmacy.R
import kotlinx.android.synthetic.main.location_dialog.*

class LocationPermissionsDialog(mContext : Context) : Dialog(mContext) {

    var mContext = mContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        setContentView(R.layout.location_permissions_dialog)
        locatinOk.setOnClickListener {
            ActivityCompat.requestPermissions(
                mContext as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001)
            this.dismiss()
        }
        locatinCancel.setOnClickListener {
            this.dismiss()
        }
    }
}