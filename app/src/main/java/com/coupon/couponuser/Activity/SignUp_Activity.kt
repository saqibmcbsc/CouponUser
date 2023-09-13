package com.couponusers.couponuser.Activity

import android.content.Intent
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
import androidx.databinding.DataBindingUtil
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.Utils.ApiHelper
import com.couponusers.couponuser.Utils.setFullScreen
import com.example.couponuser.R
import com.example.couponuser.databinding.ActivitySignUpBinding
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.*

class SignUp_Activity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    var numberReg = ""
    var token = ""
    var register = "register"

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                 token = task.result
                if (token != null) {
                    Log.d("Token", token)
                }
            } else {
                Log.e("TokenDetails", "Token Failed to receive!",)
            }
        }

        binding.btnNextSignUp.setOnClickListener(View.OnClickListener {
            numberReg = binding.edtNumberSignUp.text.toString()
            if (numberReg.isEmpty()){
                Toast.makeText(this, "Please Enter Your Number", Toast.LENGTH_SHORT).show()
            }else if (numberReg.length<10){
                Toast.makeText(this, "Please Enter 10 digits Number", Toast.LENGTH_SHORT).show()
            }else if (!binding.checkboxCondition.isChecked){
                Toast.makeText(this, "Please Check terms & Condition", Toast.LENGTH_SHORT).show()
            }else{
                binding.progressBar.visibility = VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    signUpApi()
                }
            }
        })
        binding.textLogin.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@SignUp_Activity,Login_Activity::class.java)
            startActivity(intent)
            finish()
        })

    }

    suspend fun signUpApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .signUpApi(numberReg,token)

        if(response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    val intent = Intent(this@SignUp_Activity,Otp_Activity::class.java)
                    intent.putExtra("number",numberReg)
                    intent.putExtra("register",register)
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