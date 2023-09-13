package com.couponusers.couponuser.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coupon.couponuser.ModelClass.Filter_Category_DataModel
import com.couponusers.couponuser.Adapter.Recycler_SelectCoupon_Adapet
import com.couponusers.couponuser.Adapter.Recycler_SubCategory_Filter
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.Filter_Category_Model
import com.couponusers.couponuser.ModelClass.SelectCoupon_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentSelectCouponsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class SelectCoupons_Fragment : Fragment(), Recycler_SelectCoupon_Adapet.ClickSelectCoupon,Recycler_SubCategory_Filter.SubCatClick {

    lateinit var binding: FragmentSelectCouponsBinding
    var adapter: Recycler_SelectCoupon_Adapet? = null
    var list = ArrayList<SelectCoupon_DataModel>()
    var adapterFilter: Recycler_SubCategory_Filter? = null
    var listFilter = ArrayList<Filter_Category_DataModel>()
    var recycler_category_Filter: RecyclerView? = null
    var categoryId = ""
    var userId = ""
    var couponId = ""
    var couponCode = ""
    var offerCode = ""
    var save_unsave = ""
    var subCatId = ""
    var subCatIdList = ArrayList<String>()


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentSelectCouponsBinding.inflate(inflater, container, false)

            val dataBundle = arguments
            if (dataBundle != null) {
                categoryId = dataBundle.getString("categoryId").toString()
            }

            val sharedPreferences2 =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences2?.getString("userId", null).toString()

            binding.progressBar.visibility = VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                selectCategoryApi()

            }

            binding.searchViewSelectCoupon.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterList(newText)
                    return true
                }

            })

            binding.imgFilterSubCat.setOnClickListener(View.OnClickListener {
                subSategoryFilterBottomSheet()
            })
        }

        return binding.root
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
        couponCode = list[position].coupon_code
        offerCode = list[position].offer_code
        couponId = list[position].id
        replaceFragment(Coupon_BarCode_Fragment())
    }

    override fun onClick2(position: Int) {
        couponCode = list[position].coupon_code
        offerCode = list[position].offer_code
        save_unsave = "saved"
      CoroutineScope(Dispatchers.IO).launch {
          savedCoupon()
      }
    }

    override fun onClick3(position: Int) {
        couponCode = list[position].coupon_code
        offerCode = list[position].offer_code
        save_unsave = "unsaved"
        CoroutineScope(Dispatchers.IO).launch {
            savedCoupon()
        }
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTrasaction = fragmentManager?.beginTransaction()
        val dataBundle = Bundle()
        dataBundle.putString("couponCode", couponCode)
        fragment.arguments = dataBundle
        fragmentTrasaction?.add(R.id.fragmentCont,fragment)
        fragmentTrasaction?.addToBackStack(null)
        fragmentTrasaction?.commit()
    }

    @SuppressLint("MissingInflatedId")
    fun subSategoryFilterBottomSheet(){
        var dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.sub_category_filter_bottomsheet, null)
        dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme)
         recycler_category_Filter = view.findViewById<RecyclerView>(R.id.recycler_subCategory_Filter)
        val btn_subCat = view.findViewById<AppCompatButton>(R.id.btn_subCat)
        val searchView_SubCategory_filter = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView_SubCategory_filter)


        btn_subCat.setOnClickListener(View.OnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                selectCategoryApi()
                dialog.dismiss()
            }
        })


        searchView_SubCategory_filter.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText)
                return true
            }

        })

         CoroutineScope(Dispatchers.IO).launch {
             subCategoryApi()
         }

        dialog.setCancelable(true)

        dialog.setContentView(view)

        dialog.show()
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

        suspend fun selectCategoryApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .userCategoryClick(userId,categoryId,subCatIdList.joinToString(","))


        if (response.isSuccessful){
            if (response.body()?.status==true){
                list = response.body()?.data!!
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    val layoutManager = GridLayoutManager(requireContext(),2)
                    binding.recyclerSelectCoupon.layoutManager = layoutManager

                    adapter = Recycler_SelectCoupon_Adapet(list,requireContext(),this@SelectCoupons_Fragment)
                    binding.recyclerSelectCoupon.adapter = adapter
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

    suspend fun subCategoryApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .subCategory(categoryId)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    listFilter = response.body()?.data!!

                    adapterFilter = Recycler_SubCategory_Filter(listFilter,requireContext(),this@SelectCoupons_Fragment)
                    recycler_category_Filter?.adapter = adapterFilter

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

    override fun subCatClick(position: Int) {
        subCatId = listFilter[position].id

        if (subCatIdList.contains(subCatId)) {
            subCatIdList.remove(subCatId)
        } else {
            subCatIdList.add(subCatId)
        }
    }
}