package com.couponusers.couponuser.Activity

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowInsetsController
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.couponusers.couponuser.Fragments.*
import com.couponusers.couponuser.Fragments.HomeFragment
import com.couponusers.couponuser.Fragments.Profile_Fragment
import com.couponusers.couponuser.Fragments.Review_Fragment
import com.couponusers.couponuser.Fragments.TotalCoupons_Fragment
import com.couponusers.couponuser.InterFace.FragmentCallBack
import com.example.couponuser.R
import com.example.couponuser.databinding.ActivityDashBoardBinding
import com.google.firebase.messaging.FirebaseMessaging
import androidx.navigation.ui.setupActionBarWithNavController


class DashBoard_Activity : AppCompatActivity() {

    lateinit var binding: ActivityDashBoardBinding
    var userId = ""
    private lateinit var currentNavController: LiveData<NavController>
   lateinit var navController:NavController

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setFullScreen(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board)
        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        if (Build.VERSION.SDK_INT >= 23) {
            val decor: View = this@DashBoard_Activity.getWindow().getDecorView()
            if (decor.systemUiVisibility != View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decor.systemUiVisibility = 0
            }
        }



        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        userId = sharedPreferences?.getString("userId", null).toString()


        val navHomeFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
         navController = navHomeFragment!!.findNavController()
        setupActionBarWithNavController(navController)
        val popupMenu = PopupMenu(this,null)
        popupMenu.inflate(R.menu.clock_tabs)
        binding.bottomBar.setupWithNavController(popupMenu.menu, navController!!)




    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }



//    override fun onBackPressed() {
//        super.onBackPressed()
//        if (getVisibleFragment() is HomeFragment) {
//            finishAffinity()
//            finish()
//        }
//
//
//    }


//        manger = supportFragmentManager
//        mAllowCommit = true

//        FragmentCallBack.FragmentType.HOME.toString()
//        changeFragment(HomeFragment(), FragmentCallBack.FragmentType.HOME.toString(), true)



//        binding.bottomBar.setOnItemSelectedListener {
//            when (it) {
//                0 -> {
//                    FragmentCallBack.FragmentType.HOME.toString()
//                    changeFragment(
//                        HomeFragment(),
//                        FragmentCallBack.FragmentType.HOME.toString(),
//                        false
//                    )
//
//                }
//                1 -> {
//                    FragmentCallBack.FragmentType.COUPONS.toString()
//                    changeFragment(
//                        TotalCoupons_Fragment(),
//                        FragmentCallBack.FragmentType.COUPONS.toString(),
//                        false
//                    )
//
//                }
//
//                2 -> {
//                    FragmentCallBack.FragmentType.REVIEW.toString()
//                    changeFragment(
//                        Review_Fragment(),
//                        FragmentCallBack.FragmentType.REVIEW.toString(),
//                        false
//                    )
//
//                }
//
//                3 -> {
//                    FragmentCallBack.FragmentType.PROFILE.toString()
//                    changeFragment(
//                        Profile_Fragment(),
//                        FragmentCallBack.FragmentType.PROFILE.toString(),
//                        false
//                    )
//
//                }
//            }
//        }



//
//    override fun onSaveInstanceState(outState: Bundle) {
////        mAllowCommit = false
//        super.onSaveInstanceState(outState)
//    }

//    override fun onResumeFragments() {
////        mAllowCommit = true
//        super.onResumeFragments()
//    }

//    private fun allowFragmentCommit(): Boolean {
//        return mAllowCommit
//    }

//    fun changeFragment(mFragment: Fragment, token: String, isAdd: Boolean) {
//        val tr = manger.beginTransaction()
//        if (isAdd) {
//            tr.add(R.id.fragmentContainer, mFragment)
//        } else {
//            tr.replace(R.id.fragmentContainer, mFragment, token).addToBackStack(token)
//        }
//
//        if (allowFragmentCommit() && !isFinishing) {
//            tr.commitAllowingStateLoss()
//        }
//    }


//    override fun onBackPressed() {
//        super.onBackPressed()
//        Log.d("sdvf", "onBackPressed: " + getVisibleFragment())
//        if (getVisibleFragment() is HomeFragment) {
////            bottomNavigation.show(1,true)
//            binding.bottomBar.itemActiveIndex = 0
//
//        } else if (getVisibleFragment() is TotalCoupons_Fragment) {
////            bottomNavigation.show(2,true)
//            binding.bottomBar.itemActiveIndex = 1
//        } else if (getVisibleFragment() is Review_Fragment) {
////            bottomNavigation.show(4,true)
//            binding.bottomBar.itemActiveIndex = 2
//        } else if (getVisibleFragment() is Profile_Fragment) {
////            bottomNavigation.show(5,true)
//            binding.bottomBar.itemActiveIndex = 3
//        }
//
//
//    }



    fun getVisibleFragment(): Fragment? {
        val fragmentManager: FragmentManager = this@DashBoard_Activity.supportFragmentManager
        val fragments = fragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible) return fragment
            }
        }
        return null
    }

}