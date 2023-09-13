package com.couponusers.couponuser.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.couponusers.couponuser.Adapter.Recycler_SelectCoupon_Adapet
import com.couponusers.couponuser.ModelClass.SelectCoupon_DataModel
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentViewAllBestOffersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class ViewAll_BestOffers : Fragment(), Recycler_SelectCoupon_Adapet.ClickSelectCoupon {

    lateinit var binding: FragmentViewAllBestOffersBinding
    var adapter: Recycler_SelectCoupon_Adapet? = null
    var list = ArrayList<SelectCoupon_DataModel>()
    var userId = ""
    var offerCode = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentViewAllBestOffersBinding.inflate(inflater, container, false)

            val sharedPreferences =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences?.getString("userId", null).toString()

            binding.searchViewAllOffer.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterList(newText)
                    return true
                }

            })

            // View Flipper
            val imagearray =
                intArrayOf(R.drawable.view_flip1, R.drawable.view_flip2, R.drawable.view_flip3)
            for (i in imagearray.indices) showimage(imagearray[i])

//        CoroutineScope(Dispatchers.IO).launch {
//            allBestOfferApi()
//        }

        }

        return binding.root
    }

    fun showimage(img: Int) {
        val imageView = ImageView(requireContext())
        imageView.setBackgroundResource(img)
        binding.viewflipper2.addView(imageView)
        binding.viewflipper2.setFlipInterval(3000)
        binding.viewflipper2.setAutoStart(true)
        binding.viewflipper2.setInAnimation(requireContext(), android.R.anim.slide_in_left)
        binding.viewflipper2.setOutAnimation(requireContext(), android.R.anim.slide_out_right)
    }

    private fun filterList(query:String?){
        if (query!=null){
            val filterList = ArrayList<SelectCoupon_DataModel>()
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

    override fun onClick(position: Int) {
        offerCode = list[position].offer_code
        replaceFragment(Coupon_BarCode_Fragment())
    }

    override fun onClick2(position: Int) {
        Toast.makeText(requireContext(), "Save Coupon", Toast.LENGTH_SHORT).show()
    }

    override fun onClick3(position: Int) {
        Toast.makeText(requireContext(), "UnSave Coupon", Toast.LENGTH_SHORT).show()
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTrasaction = fragmentManager?.beginTransaction()
        val dataBundle = Bundle()
        dataBundle.putString("offerCode", offerCode)
        fragment.arguments = dataBundle
        fragmentTrasaction?.add(R.id.fragmentContar,fragment)
        fragmentTrasaction?.addToBackStack(null)
        fragmentTrasaction?.commit()
    }

//    suspend fun allBestOfferApi(){
//        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
//            .bestAllOfferApi(userId)
//
//        if (response.isSuccessful){
//            if (response.body()?.status==true){
//                withContext(Dispatchers.Main){
//                 //   Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
//                    list = response.body()?.data!!
//
//                    adapter = Recycler_SelectCoupon_Adapet(list,requireContext(),this@ViewAll_BestOffers)
//                    binding.recyclerAllOffers.adapter = adapter
//
//                }
//            }else{
//                withContext(Dispatchers.Main){
//                    Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }else{
//            withContext(Dispatchers.Main){
//                Toast.makeText(requireContext(), "not success"+response.body()?.message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}