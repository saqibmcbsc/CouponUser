package com.couponusers.couponuser.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.couponusers.couponuser.Adapter.RedeemCoupon_Adaptor
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.RedeemNow_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentCouponsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Coupons_Fragment : Fragment() {
    lateinit var binding: FragmentCouponsBinding
    var adapter: RedeemCoupon_Adaptor? = null
    var list = ArrayList<RedeemNow_DataModel>()
    var userId =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentCouponsBinding.inflate(inflater, container, false)


            val sharedPreferences =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences?.getString("userId", null).toString()

            binding.imgBackMyCoupon.setOnClickListener(View.OnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            })


            binding.progressBar.visibility = VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                redemeedCoupon()
            }

        }
        return binding.root
    }



    suspend fun redemeedCoupon(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .couponRedeemed(userId)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                   list = response.body()?.data!!
                    binding.progressBar.visibility = GONE
                    adapter = RedeemCoupon_Adaptor(list,requireContext())
                    binding.recylerMyCoupons.adapter = adapter

                }
            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = GONE
                Toast.makeText(requireContext(), "not success"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}