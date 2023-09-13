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
import com.couponusers.couponuser.Adapter.recycler_categoryAdaptor
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.Category_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentAllCategoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class AllCategory_Fragment : Fragment(), recycler_categoryAdaptor.IntrestClickCategory {
    lateinit var binding: FragmentAllCategoryBinding
    var list = ArrayList<Category_DataModel>()
    var adapter: recycler_categoryAdaptor? = null
    var categoryId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentAllCategoryBinding.inflate(inflater, container, false)

            binding.searchViewCategory.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterList(newText)
                    return true
                }

            })
            binding.imgBackCategory.setOnClickListener(View.OnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            })

            binding.progressBar.visibility = VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                allCategoryApi()
            }

        }
        return binding.root
    }


    private fun filterList(query:String?){
        if (query!=null){
            val filterList = ArrayList<Category_DataModel>()
            for (i in list){
                if (i.category.toLowerCase(Locale.ROOT).contains(query)){
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
        categoryId = list[position].id
        replaceFragment(SelectCoupons_Fragment())
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTrasaction = fragmentManager?.beginTransaction()
        val dataBundle = Bundle()
        dataBundle.putString("categoryId", categoryId)
        fragment.arguments = dataBundle
        fragmentTrasaction?.add(R.id.fragmentConta,fragment)
        fragmentTrasaction?.addToBackStack(null)
        fragmentTrasaction?.commit()
    }

    suspend fun allCategoryApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .showAllCategory()

        if (response.isSuccessful){
            if (response.body()?.status==true){
                list = response.body()?.data!!
                binding?.recyclerAllCategory?.setHasFixedSize(true)
                adapter = recycler_categoryAdaptor(list,requireContext(),this)
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    binding!!.recyclerAllCategory.adapter = adapter
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
                Toast.makeText(requireContext(), "not sucessfull"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}