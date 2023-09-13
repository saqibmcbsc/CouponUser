package com.couponusers.couponuser.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.couponusers.couponuser.Activity.CommonActivity
import com.couponusers.couponuser.Adapter.Recycler_SelectCoupon_Adapet
import com.couponusers.couponuser.Fragments.Coupon_BarCode_Fragment
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.SelectCoupon_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentTotalCouponsBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TotalCoupons_Fragment : Fragment(), Recycler_SelectCoupon_Adapet.ClickSelectCoupon{

    lateinit var binding: FragmentTotalCouponsBinding
    var adapter: Recycler_SelectCoupon_Adapet? = null
    var list = ArrayList<SelectCoupon_DataModel>()
    var userId = ""
    var offerCode = ""
    var couponCode = ""
    var save_unsave = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (view == null) {
            // Inflate the layout for this fragment
            binding = FragmentTotalCouponsBinding.inflate(inflater, container, false)

            val sharedPreferences =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences?.getString("userId", null).toString()


            binding.progressBar.visibility = VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                showCoupons()
            }

        }

        return binding.root
    }

    override fun onClick(position: Int) {
        offerCode = list[position].offer_code
        couponCode = list[position].coupon_code
      //  replaceFragment(Coupon_BarCode_Fragment())
        startActivity(Intent(requireContext(), CommonActivity::class.java).putExtra("from","barCode").putExtra("couponCode",couponCode))
    }

    override fun onClick2(position: Int) {
        offerCode = list[position].offer_code
        couponCode = list[position].coupon_code
        save_unsave = "saved"
        CoroutineScope(Dispatchers.IO).launch{
            savedCoupon()
        }
    }

    override fun onClick3(position: Int) {
        offerCode = list[position].offer_code
        couponCode = list[position].coupon_code
        save_unsave = "unsaved"
        CoroutineScope(Dispatchers.IO).launch{
            savedCoupon()
        }


    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTrasaction = fragmentManager?.beginTransaction()
        val dataBundle = Bundle()
        dataBundle.putString("couponCode", couponCode)
        fragment.arguments = dataBundle
        fragmentTrasaction?.add(R.id.fragmentContr,fragment)
        fragmentTrasaction?.addToBackStack(null)
        fragmentTrasaction?.commit()
    }

    suspend fun showCoupons(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .bottomCoupons(userId)

        if (response.isSuccessful){
            if (response.body()?.status==true){

                withContext(Dispatchers.Main) {
                    if (isAdded) {
                        binding.progressBar.visibility = GONE
                        list = response.body()?.data!!

                        adapter = Recycler_SelectCoupon_Adapet(
                            list,
                            requireContext(),
                            this@TotalCoupons_Fragment
                        )
                        binding.recyclerTotalCoupon.adapter = adapter
                    }
                }
            }else{
                withContext(Dispatchers.Main){
                    if (isAdded){
                        binding.progressBar.visibility = GONE
                        Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }else{
            withContext(Dispatchers.Main){
                if (isAdded){
                    binding.progressBar.visibility = GONE
                    Toast.makeText(requireContext(), "not success"+response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    suspend fun savedCoupon(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .saveCoupon(userId,couponCode,offerCode,save_unsave)



        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                  //  Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }else{
                withContext(Dispatchers.Main){
                    if (isAdded){
                        Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            withContext(Dispatchers.Main){
                if (isAdded){
                    Toast.makeText(requireContext(), "not success"+response.body()?.message, Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

}