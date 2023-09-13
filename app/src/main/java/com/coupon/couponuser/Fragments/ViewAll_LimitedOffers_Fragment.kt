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
import androidx.recyclerview.widget.LinearLayoutManager
import com.couponusers.couponuser.Adapter.Recycler_LimitedOffer_Adapter
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.LimitedOffer_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentViewAllLimitedOffersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ViewAll_LimitedOffers_Fragment : Fragment(), Recycler_LimitedOffer_Adapter.ClickLimitedOffer{

     lateinit var binding: FragmentViewAllLimitedOffersBinding
    var listLimitedOffer = ArrayList<LimitedOffer_DataModel>()
    var adapterLimitedOffer: Recycler_LimitedOffer_Adapter? = null
    var userId = ""
    var offerCode = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentViewAllLimitedOffersBinding.inflate(inflater, container, false)

            val sharedPreferences =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences?.getString("userId", null).toString()

            binding.imgBackLimitedOffer.setOnClickListener(View.OnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            })

            binding.progressBar.visibility = VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                limitedAllOfferApi()
            }
        }
        return binding.root
    }

    override fun onClic(position: Int) {
        offerCode = listLimitedOffer[position].offer_code
        replaceFragment(Coupon_BarCode_Fragment())
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTrasaction = fragmentManager?.beginTransaction()
        val dataBundle = Bundle()
        dataBundle.putString("offerCode", offerCode)
        fragment.arguments = dataBundle
        fragmentTrasaction?.add(R.id.fragmentContainars,fragment)
        fragmentTrasaction?.addToBackStack(null)
        fragmentTrasaction?.commit()
    }

    suspend fun limitedAllOfferApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .limitedAllOfferApi(userId)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    listLimitedOffer = response.body()?.data!!

        adapterLimitedOffer = Recycler_LimitedOffer_Adapter(listLimitedOffer,requireContext(),this@ViewAll_LimitedOffers_Fragment)
        binding.recylerLimitedOffer.adapter = adapterLimitedOffer
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