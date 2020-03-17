package com.ddoniddoi.pharmacy.dialog

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ddoniddoi.pharmacy.R
import kotlinx.android.synthetic.main.five_days_dialog.*
import kotlinx.android.synthetic.main.location_dialog.*
import java.util.*

class FiveDaysDialog(mContext : Context) : Dialog(mContext) {

    var mContext = mContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        setContentView(R.layout.five_days_dialog)

        var cal = Calendar.getInstance()
        when(cal.get(Calendar.DAY_OF_WEEK)){
            2 -> fiveDaysSelect(fiveDaysLayout1,fiveDaysDaysTv1,fiveDaysAge1Tv1,fiveDaysAge2Tv1)
            3 -> fiveDaysSelect(fiveDaysLayout2,fiveDaysDaysTv2,fiveDaysAge1Tv2,fiveDaysAge2Tv2)
            4 -> fiveDaysSelect(fiveDaysLayout3,fiveDaysDaysTv3,fiveDaysAge1Tv3,fiveDaysAge2Tv3)
            5 -> fiveDaysSelect(fiveDaysLayout4,fiveDaysDaysTv4,fiveDaysAge1Tv4,fiveDaysAge2Tv4)
            6 -> fiveDaysSelect(fiveDaysLayout5,fiveDaysDaysTv5,fiveDaysAge1Tv5,fiveDaysAge2Tv5)
        }



        fiveDaysOk.setOnClickListener {
            ActivityCompat.requestPermissions(
                mContext as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001)
            this.dismiss()
        }
    }

    fun fiveDaysSelect(fiveDaysLayout : LinearLayout,  fiveDaysDaysTv : TextView,  fiveDaysAge1Tv : TextView, fiveDaysAge2Tv: TextView){
        fiveDaysLayout.background = ContextCompat.getDrawable(mContext,R.drawable.five_days_line_select)
        fiveDaysDaysTv.setBackgroundColor(Color.rgb(47,85,173))
        fiveDaysAge1Tv.setTextColor(Color.rgb(47,85,173))
        fiveDaysAge2Tv.setTextColor(Color.rgb(47,85,173))
    }



}