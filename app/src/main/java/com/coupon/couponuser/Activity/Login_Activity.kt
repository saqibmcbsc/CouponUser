package com.couponusers.couponuser.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.Utils.ApiHelper
import com.couponusers.couponuser.Utils.setFullScreen
import com.example.couponuser.R
import com.example.couponuser.databinding.ActivityLoginBinding
import kotlinx.coroutines.*

class Login_Activity : AppCompatActivity() {
  lateinit var binding: ActivityLoginBinding
    var number =""
    var userId =""
    var REQUEST_CODE = 101

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }



        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        userId = sharedPreferences?.getString("userId", null).toString()


        binding.btnNextLogin.setOnClickListener(View.OnClickListener {
            number = binding.edtNumberLogin.text.toString()
            if (number.isEmpty()){
                Toast.makeText(this, "Please Enter Your Number", Toast.LENGTH_SHORT).show()
            }else if (number.length<10){
                Toast.makeText(this, "Please Enter 10 digits Number", Toast.LENGTH_SHORT).show()
            }else if (!binding.checkboxConditionLogin.isChecked){
                Toast.makeText(this, "Plese Check Conditon CheckBox", Toast.LENGTH_SHORT).show()
            }else{
                binding.progressBar.visibility = VISIBLE
               CoroutineScope(Dispatchers.IO).launch {
                   loginApi()
               }
            }
        })
        binding.textSignUp.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@Login_Activity, SignUp_Activity::class.java)
            startActivity(intent)
            finish()
        })


        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE)

//            return
        }
    }

    suspend fun loginApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .loginApi(number)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    val intent = Intent(this@Login_Activity, Otp_Activity::class.java)
                    intent.putExtra("number",number)
                    intent.putExtra("userId",userId)
                    startActivity(intent)
                }

            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    Toast.makeText(applicationContext, ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = GONE
                Toast.makeText(applicationContext, "not sucessfull"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}