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
import com.coupon.couponuser.ModelClass.Filter_Category_DataModel
import com.couponusers.couponuser.Adapter.Recycler_OfferCoupon_Adapter
import com.couponusers.couponuser.Adapter.Recycler_SubCategory_Filter
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.Filter_Category_Model
import com.couponusers.couponuser.ModelClass.SelectCoupon_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentSelectOfferCouponBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class SelectOffer_CouponFragment : Fragment(), Recycler_OfferCoupon_Adapter.ClickSelectCoupon {

    lateinit var binding:FragmentSelectOfferCouponBinding
    var adapter: Recycler_OfferCoupon_Adapter? = null
    var list = ArrayList<SelectCoupon_DataModel>()
    var adapterFilter: Recycler_SubCategory_Filter? = null
    val listFilter = ArrayList<Filter_Category_DataModel>()
    var offerCode = ""
    var couponCode = ""
    var userId = ""
    var save_unsave = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentSelectOfferCouponBinding.inflate(inflater, container, false)

            val dataBundle = arguments
            if (dataBundle != null) {
                offerCode = dataBundle.getString("offerCode").toString()
            }

            val sharedPreferences2 =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences2?.getString("userId", null).toString()


            binding.searchViewSelectOfferCoupon.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterData(newText)
                    return true
                }

            })

            binding.progressBar.visibility = VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                offerCouponApi()
            }
        }
        return binding.root
    }

    override fun onClick(position: Int) {
        offerCode = list[position].offer_code
        couponCode = list[position].coupon_code
        replaceFragment(Coupon_BarCode_Fragment())
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


    private fun filterData(query:String?){
        if (query!=null){
            val filter = ArrayList<Filter_Category_DataModel>()
            for (i in listFilter){
                if (i.sub_category.toLowerCase(Locale.ROOT).contains(query)){
                    filter.add(i)
                }
            }
            if (filter.isEmpty()){
                Toast.makeText(requireContext(), "No Data found..!!", Toast.LENGTH_SHORT).show()
            }
            adapterFilter?.setFilterList(filter)
        }
    }

    suspend fun offerCouponApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .bestAllOfferApi(userId)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    list = response.body()?.data!!


                    adapter = Recycler_OfferCoupon_Adapter(list,requireContext(),this@SelectOffer_CouponFragment)
                    binding.recyclerSelectCouponOffer.adapter = adapter

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

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTrasaction = fragmentManager?.beginTransaction()
        val dataBundle = Bundle()
        dataBundle.putString("offerCode", offerCode)
        fragment.arguments = dataBundle
        fragmentTrasaction?.add(R.id.fragmentCon,fragment)
        fragmentTrasaction?.addToBackStack(null)
        fragmentTrasaction?.commit()
    }

    suspend fun savedCoupon(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .saveCoupon(userId,couponCode,offerCode,save_unsave)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                }
            }else{
                withContext(Dispatchers.Main){
                    Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                Toast.makeText(requireContext(), "not success"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}