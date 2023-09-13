package com.couponusers.couponuser.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.couponusers.couponuser.Activity.CommonActivity
import com.couponusers.couponuser.Activity.Interest_Activity
import com.couponusers.couponuser.Activity.Login_Activity
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Profile_Fragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    var userId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (view == null) {
            // Inflate the layout for this fragment
            binding = FragmentProfileBinding.inflate(inflater, container, false)

            val sharedPreferences =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences?.getString("userId", null).toString()

//        binding.donutChart.donutColors = intArrayOf(
//            Color.parseColor("#FF8732"),
//            Color.parseColor("#9EFFFFFF"),
//            Color.parseColor("#8DFFFFFF")
//        )
//        binding.donutChart.animation.duration = animationDuration
//        binding.donutChart.animate(donutSet)


            binding.progressBar.visibility = VISIBLE
            binding.constProfile.visibility = GONE

            binding.cardHelpSupport.setOnClickListener(View.OnClickListener {
              //  replaceFragment(HelpSupport_Fragment())
                startActivity(Intent(requireContext(),CommonActivity::class.java).putExtra("from","help"))
            })
            binding.cardMyCoupons.setOnClickListener(View.OnClickListener {
              //  replaceFragment(Coupons_Fragment())
                startActivity(Intent(requireContext(),CommonActivity::class.java).putExtra("from","coupon"))
            })

            binding.cardInterest.setOnClickListener(View.OnClickListener {
                val intent = Intent(requireContext(), Interest_Activity::class.java)
                intent.putExtra("profile", "profile")
                startActivity(intent)
            })

            binding.imgEdtProfile.setOnClickListener(View.OnClickListener {
//                replaceFragment(Edit_Profile_Fragment())
                startActivity(Intent(requireContext(), CommonActivity::class.java).putExtra("from","editProfile"))

            })

            binding.cardAboutUs.setOnClickListener(View.OnClickListener {
//                replaceFragment(AboutUs_Fragment())
                startActivity(Intent(requireContext(),CommonActivity::class.java).putExtra("from","about"))
            })

            binding.cardTermsCondition.setOnClickListener(View.OnClickListener {
//                replaceFragment(TermsCondition_Fragment())
                startActivity(Intent(requireContext(),CommonActivity::class.java).putExtra("from","term"))
            })

            binding.cardLogout.setOnClickListener(View.OnClickListener {
                val preferences: SharedPreferences = requireContext().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = preferences.edit()
                editor.clear()
                editor.apply()

//                val intent = Intent(requireContext(), Login_Activity::class.java)
//                startActivity(intent)
//                requireActivity().finish()

                showPopup()
            })

            CoroutineScope(Dispatchers.IO).launch {
                profilApi()
            }

        }
        return binding.root
    }

    companion object {
        private val donutSet = listOf(
            80f,
            90f,
            100f
        )
        private const val animationDuration = 1000L
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTrasaction = fragmentManager.beginTransaction()
        fragmentTrasaction.add(R.id.profileContainer,fragment)
        fragmentTrasaction.addToBackStack(null)
        fragmentTrasaction.commit()
    }

    private fun showPopup() {
        val alert: androidx.appcompat.app.AlertDialog.Builder =
            androidx.appcompat.app.AlertDialog.Builder(requireActivity())
        alert.setMessage("Are you sure?")
            .setPositiveButton("Logout", DialogInterface.OnClickListener { dialog, which ->
                logout() // Last step. Logout function
            }).setNegativeButton("Cancel", null)
        val alert1: androidx.appcompat.app.AlertDialog = alert.create()
        alert1.show()
    }

    private fun logout() {
        val intent = Intent(requireContext(), Login_Activity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    suspend fun profilApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .userProfile(userId)

        if (response.isSuccessful){
            if (response.body()?.status == true){
                val getData = response.body()?.data
                withContext(Dispatchers.Main) {
                    if (isAdded) {
                        binding.progressBar.visibility = GONE
                        binding.constProfile.visibility = VISIBLE
//                        Picasso.get().load(ApiHelper.imageUrl + getData?.get(0)?.image)
//                            .into(binding.profileImage)
                        val imageUrl = ApiHelper.imageUrl + getData?.get(0)?.image

                        Picasso.get().load(imageUrl).error(R.drawable.user_icon).into(binding.profileImage)
                        binding.textProfileName.text = getData?.get(0)?.name
                        binding.textCouponsSummery.text = getData?.get(0)?.coupon
                    }
                }
            }else{
                withContext(Dispatchers.Main){
                    if (isAdded){
                        binding.progressBar.visibility = GONE
                        binding.constProfile.visibility = VISIBLE
                        Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }else{
            withContext(Dispatchers.Main){
                if (isAdded){
                    binding.progressBar.visibility = GONE
                    binding.constProfile.visibility = VISIBLE
                    Toast.makeText(requireContext(), "not success"+response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

}