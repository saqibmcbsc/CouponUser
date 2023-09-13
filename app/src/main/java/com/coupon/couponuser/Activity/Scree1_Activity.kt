package com.couponusers.couponuser.Activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.couponusers.couponuser.Utils.setFullScreen
import com.example.couponuser.R
import com.example.couponuser.databinding.ActivityScree1Binding


class Scree1_Activity : AppCompatActivity() {
    lateinit var binding: ActivityScree1Binding
    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scree1)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        binding.imgNext.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@Scree1_Activity,Login_Activity::class.java)
            startActivity(intent)
        })
    }
}