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
import com.couponusers.couponuser.Adapter.Recycler_AllBrand_Adapter
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.BrandCoupons_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentViewAllBrandCouponsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ViewAll_BrandCoupons_Fragment : Fragment(), Recycler_AllBrand_Adapter.BrandCoupon {

    lateinit var binding: FragmentViewAllBrandCouponsBinding
    var list = ArrayList<BrandCoupons_DataModel>()
    var adapter: Recycler_AllBrand_Adapter? = null
    var userId = ""
    var offerCode = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentViewAllBrandCouponsBinding.inflate(inflater, container, false)

            val sharedPreferences =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences?.getString("userId", null).toString()

            binding.imgBackBrandCoupon.setOnClickListener(View.OnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            })

            binding.searchViewBrand.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterList(newText)
                    return true
                }

            })

            binding.progressBar.visibility = VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                brandAllCouponApi()
            }

        }
        return binding.root
    }


    private fun filterList(query:String?){
        if (query!=null){
            val filterList = ArrayList<BrandCoupons_DataModel>()
            for (i in list){
                if (i.brand_name.toLowerCase(Locale.ROOT).contains(query)){
                    filterList.add(i)
                }
            }
            if (filterList.isEmpty()){
                Toast.makeText(requireContext(), "No Data found..!!", Toast.LENGTH_SHORT).show()
            }
            adapter?.setFilterList(filterList)
        }
    }

    override fun brandCouponClick(position: Int) {
        offerCode = list[position].offer_code
       replaceFragment(Coupon_BarCode_Fragment())
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTrasaction = fragmentManager?.beginTransaction()
        val dataBundle = Bundle()
        dataBundle.putString("offerCode", offerCode)
        fragment.arguments = dataBundle
        fragmentTrasaction?.add(R.id.fragmentContainr,fragment)
        fragmentTrasaction?.addToBackStack(null)
        fragmentTrasaction?.commit()
    }

    suspend fun brandAllCouponApi(){

        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .brandAllCouponApi(userId)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE

                    list = response.body()?.data!!

                    adapter = Recycler_AllBrand_Adapter(list, requireContext(),this@ViewAll_BrandCoupons_Fragment)
                    binding!!.recyclerAllBrand.adapter = adapter


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
                Toast.makeText(requireContext(), "not sucess"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}