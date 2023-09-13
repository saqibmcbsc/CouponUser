package com.couponusers.couponuser.Activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.Utils.ApiHelper
import com.couponusers.couponuser.Utils.setFullScreen
import com.example.couponuser.R
import com.example.couponuser.databinding.ActivityOtpBinding
import kotlinx.coroutines.*

class Otp_Activity : AppCompatActivity() {
    lateinit var binding: ActivityOtpBinding
    var number = ""
    var otp = ""
    var userId = ""
    var register = "register"

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        userId = sharedPreferences?.getString("userId", null).toString()

        number = intent.getStringExtra("number").toString()
        register = intent.getStringExtra("register").toString()


        Log.d("sdfghjk", "onCreate: "+register +"  "+userId)

        otpTimer()
        binding.btnResendOtp.setOnClickListener(View.OnClickListener {
            otpTimer()
            CoroutineScope(Dispatchers.IO).launch {
                loginApi()
            }
        })

        binding.pinview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used in this example
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if the length of entered text is equal to 4
                if (s?.length == 4) {
                    hideKeyboard( binding.pinview)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used in this example
            }
        })


        binding.btnSubmitOtp.setOnClickListener(View.OnClickListener {
            otp = binding.pinview.text.toString()
            if (otp.isEmpty()){
                Toast.makeText(applicationContext, "Please Enter 4 digits OTP", Toast.LENGTH_SHORT).show()
            }else{
                binding.progressBar.visibility = View.VISIBLE
               CoroutineScope(Dispatchers.IO).launch {
                   otpApi()
               }
            }
        })
    }


    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun otpTimer(){
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.otpTimer.text = ("00:" + millisUntilFinished / 1000)
                binding.btnResendOtp.visibility = View.GONE
                binding.btnSubmitOtp.visibility = View.VISIBLE
            }

            override fun onFinish() {
                binding.otpTimer.text = "0"
                binding.btnResendOtp.visibility = View.VISIBLE
                binding.btnSubmitOtp.visibility = View.GONE
            }

        }.start()

    }

    suspend fun otpApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .otpApi(number,otp)

        if (response.isSuccessful){
            if (response.body()?.status == true){
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = View.GONE
                    if (userId != null && register == "register"){
                        val userId = response.body()?.user_id

                        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.apply(){
                            putString("userId",userId)
                        }.apply()

                        val intent = Intent(this@Otp_Activity,CreateProfile_Activity::class.java)
                        intent.putExtra("number",number)
                        startActivity(intent)
//                        finishAffinity()
                    }else if (response.body()?.details_status == true){
                        binding.progressBar.visibility = View.GONE
                        //   Toast.makeText(applicationContext, ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                        val userId = response.body()?.user_id
                        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.apply(){
                            putString("userId",userId)
                        }.apply()


                        val intent = Intent(this@Otp_Activity,CreateProfile_Activity::class.java)
                        intent.putExtra("number",number)
                        startActivity(intent)

                    }else{
                        val userId = response.body()?.user_id

                        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.apply(){
                            putString("userId",userId)
                        }.apply()

                        val intent = Intent(this@Otp_Activity,DashBoard_Activity::class.java)
                        intent.putExtra("number",number)
                        startActivity(intent)
                        finishAffinity()
                    }

                }
            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, "not sucessfull"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun loginApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .loginApi(number)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){

                }

            }else{
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext, ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                Toast.makeText(applicationContext, "not sucessfull"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}