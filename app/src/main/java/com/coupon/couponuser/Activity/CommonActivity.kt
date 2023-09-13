package com.couponusers.couponuser.Activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.couponusers.couponuser.Fragments.*
import com.couponusers.couponuser.Utils.setFullScreen
import com.example.couponuser.R
import com.example.couponuser.databinding.ActivityCommonBinding

class CommonActivity : AppCompatActivity() {
    lateinit var binding:ActivityCommonBinding
    var from=""
    var couponCode = ""
    var bottomBar = "bottom"
    var viewStore = ""
    var recomend = ""
    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen(this)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_common)
        from = intent.getStringExtra("from").toString()
        couponCode = intent.getStringExtra("couponCode").toString()
        viewStore = intent.getStringExtra("viewStore").toString()
        recomend = intent.getStringExtra("recomend").toString()
        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        when(from){
            "term"->{loadFragment(TermsCondition_Fragment())}
            "about"->{loadFragment(AboutUs_Fragment())}
            "editProfile"->{loadFragment(Edit_Profile_Fragment())}
            "coupon"->{loadFragment(Coupons_Fragment())}
            "help"->{loadFragment(HelpSupport_Fragment())}
            "barCode"->{loadFragment(Coupon_BarCode_Fragment())}
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTrasaction = fragmentManager.beginTransaction()
        val dataBundle = Bundle()
        dataBundle.putString("couponCode", couponCode)
        dataBundle.putString("bottomBar", bottomBar)
        dataBundle.putString("recomend", recomend)
        dataBundle.putString("viewStore", viewStore)
        fragment.arguments = dataBundle
        fragmentTrasaction.add(R.id.commonConainer,fragment)
        fragmentTrasaction.commit()
    }
}