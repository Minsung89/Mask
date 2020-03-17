package com.ddoniddoi.pharmacy.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import com.ddoniddoi.pharmacy.R
import kotlinx.android.synthetic.main.location_dialog.*

class LocationDialog(mContext : Context) : Dialog(mContext) {

    var mContext = mContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        setContentView(R.layout.location_dialog)
        locatinOk.setOnClickListener {
            var intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            mContext.startActivity(intent)
        }
        locatinCancel.setOnClickListener {
            this.dismiss()
        }
    }
}