package com.couponusers.couponuser.Activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.Utils.ApiHelper
import com.couponusers.couponuser.Utils.setFullScreen
import com.example.couponuser.R
import com.example.couponuser.databinding.ActivityCreateProfileBinding
import kotlinx.coroutines.*
import java.util.*

class CreateProfile_Activity : AppCompatActivity() {
    lateinit var binding: ActivityCreateProfileBinding
    var number = ""
    var fullName = ""
    var dob = ""
    var email = ""
    var userId = ""
    var address = ""
    lateinit var datePicker:DatePickerDialog
    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_profile)
        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        val sharedPreferences = getSharedPreferences("sharedPref",Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId",null).toString()

        Log.d("sdfrtyui", "onCreate: "+userId)

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        number = intent.getStringExtra("number").toString()
        binding.edtNumber.setText(number)

        binding.edtDob.setOnClickListener(View.OnClickListener {

            datePicker = DatePickerDialog(this@CreateProfile_Activity,
                { view, year, month, dayOfMonth -> // adding the selected date in the edittext
                    binding.edtDob.setText(dayOfMonth.toString() + "/" + (month + 1) + "/" + year)
                }, year, month, day
            )
            datePicker.datePicker.maxDate = calendar.timeInMillis
            datePicker.show()

        })

        binding.edtNumber.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Mobile Number is not Editable", Toast.LENGTH_SHORT).show()
        })

        binding.btnNextCreateProfile.setOnClickListener(View.OnClickListener {
            fullName = binding.edtName.text.toString()
            dob = binding.edtDob.text.toString()
            email = binding.edtEmailId.text.toString()
            address = binding.edtAddress.text.toString()

            if (fullName.isEmpty()){
                Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show()
            }else if (dob.isEmpty()){
                Toast.makeText(this, "Please enter your DOB", Toast.LENGTH_SHORT).show()
            }else if (email.isEmpty()){
                Toast.makeText(this, "Please enter your Email Id", Toast.LENGTH_SHORT).show()
            }else if (address.isEmpty()){
                Toast.makeText(this, "Please enter your Address", Toast.LENGTH_SHORT).show()
            }else{
                binding.progressBar.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    createProfileApi()
                }

            }

        })

    }

    suspend fun createProfileApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .createProfileApi(userId,fullName,dob,email,address)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = View.GONE
                    val intent = Intent(this@CreateProfile_Activity, Interest_Activity::class.java)
                    startActivity(intent)
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
}