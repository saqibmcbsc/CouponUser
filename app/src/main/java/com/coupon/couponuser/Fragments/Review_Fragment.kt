package com.couponusers.couponuser.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.couponusers.couponuser.Adapter.Recycler_SelectCoupon_Adapet
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.SelectCoupon_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentReviewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.notify

class Review_Fragment : Fragment(), Recycler_SelectCoupon_Adapet.ClickSelectCoupon{

    lateinit var binding: FragmentReviewBinding
    var adapter: Recycler_SelectCoupon_Adapet? = null
    var list = ArrayList<SelectCoupon_DataModel>()
    var userId = ""
    var couponCode = ""
    var offerCode = ""
    var save_unsave = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (view == null) {

            // Inflate the layout for this fragment
            binding = FragmentReviewBinding.inflate(inflater, container, false)

            val sharedPreferences =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences?.getString("userId", null).toString()

            binding.imgBackSaved.setOnClickListener(View.OnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            })

            val layoutManager = GridLayoutManager(requireContext(), 2)
            binding.recylerSavedCoupon.layoutManager = layoutManager


            binding.progressBar.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                showSavedCoupon()
            }

        }
        return binding.root
    }

    override fun onClick(position: Int) {
        couponCode = list[position].coupon_code
        offerCode = list[position].offer_code
    }

    override fun onClick2(position: Int) {
        couponCode = list[position].coupon_code
        offerCode = list[position].offer_code

    }

    override fun onClick3(position: Int) {
        couponCode = list[position].coupon_code
        offerCode = list[position].offer_code
        save_unsave = "unsaved"
        list.removeAt(position)
        adapter?.notifyDataSetChanged()
        CoroutineScope(Dispatchers.IO).launch {
            savedCoupon()
        }

    }

    suspend fun showSavedCoupon(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .showSavedCoupon(userId)

        if (response.isSuccessful){
            if (response.body()?.status == true){
                withContext(Dispatchers.Main){
                    if (isAdded){
                        list = response.body()?.data!!
                        binding.progressBar.visibility = View.GONE

                        adapter = Recycler_SelectCoupon_Adapet(list,requireContext(),this@Review_Fragment)
                        binding.recylerSavedCoupon.adapter = adapter
                    }

                }
            }else{
                withContext(Dispatchers.Main){
                    if (isAdded){
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }else{
            withContext(Dispatchers.Main){
                if (isAdded){
                    binding.progressBar.visibility = View.GONE
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
                    if (isAdded){
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }else{
                withContext(Dispatchers.Main){
                    if (isAdded){
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            withContext(Dispatchers.Main){
                if (isAdded){
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "not success"+response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

}