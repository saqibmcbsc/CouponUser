package com.couponusers.couponuser.Activity

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.Utils.ApiHelper
import com.couponusers.couponuser.Utils.setFullScreen
import com.example.couponuser.R
import com.example.couponuser.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    var userId = ""
    var number = ""

    lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen(this)
        val metrics = resources.displayMetrics
        val densityDpi = (metrics.density * 160f)
        Log.d("asdfghj", "onCreate: $densityDpi  $metrics")
        Log.d("asdfghj", "onCreate: ${resources.displayMetrics.density}")
       binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        userId = sharedPreferences?.getString("userId", null).toString()


        //location code
        val locationRequest = LocationRequest.create().apply {
            interval = 3000
            priority = PRIORITY_HIGH_ACCURACY

        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        task.addOnSuccessListener { response ->

            val states = response.locationSettingsStates

            if (states!!.isLocationPresent){
                val handler = Handler()
                handler.postDelayed(object : Runnable {
                    override fun run() {


                        handler.postDelayed(this, 2000)

                        handler.removeCallbacks(this)
                        if (userId != "null"){
                            CoroutineScope(Dispatchers.IO).launch {
                                usersDetailsCheck()
                            }

                        }else{
                            val intent = Intent(this@MainActivity, Scree1_Activity::class.java)
                            startActivity(intent)
                            finish()
                        }


                    }
                }, 2000)

            }

        }
            .addOnFailureListener{e ->
                val statusCode = (e as ResolvableApiException).statusCode
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                    try {
                         e.startResolutionForResult(this,100)
                    }catch (sendEx: IntentSender.SendIntentException){}

                }

            }


    }

    suspend fun usersDetailsCheck(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .userDetails_Check(userId)

        if (response.isSuccessful){
            number = response.body()?.mobile_no!!
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext, ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, DashBoard_Activity::class.java)
                    startActivity(intent)
                    finish()
                }
            }else{
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext, ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, CreateProfile_Activity::class.java)
                    intent.putExtra("number",number)
                    startActivity(intent)
                    finish()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                Toast.makeText(applicationContext, ""+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100){
            if (resultCode== RESULT_OK){
                val handler = Handler()
                handler.postDelayed(object : Runnable {
                    override fun run() {


                        handler.postDelayed(this, 2000)

                        handler.removeCallbacks(this)
                        if (userId != "null"){
                            CoroutineScope(Dispatchers.IO).launch {
                                usersDetailsCheck()
                            }

                        }else{
                            val intent = Intent(this@MainActivity, Scree1_Activity::class.java)
                            startActivity(intent)
                            finish()
                        }


                    }
                }, 2000)
            }else{
                finishAffinity()
            }
        }
    }
}