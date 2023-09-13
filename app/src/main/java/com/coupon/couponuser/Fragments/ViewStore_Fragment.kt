package com.couponusers.couponuser.Fragments

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.coupon.couponuser.ModelClass.StoreDetails_Model
import com.coupon.couponuser.ModelClass.VendorAddress_DataModel
import com.couponusers.couponuser.Activity.CommonActivity
import com.couponusers.couponuser.Adapter.Recomended_CouponAdapter
import com.couponusers.couponuser.Adapter.Recycler_SelectCoupon_Adapet
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.CouponInDetails_DataModel
import com.couponusers.couponuser.ModelClass.SelectCoupon_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentViewStoreBinding
import com.google.android.gms.location.LocationRequest.create
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URI.create


class ViewStore_Fragment : Fragment(), Recycler_SelectCoupon_Adapet.ClickSelectCoupon,
    Recomended_CouponAdapter.ClickRecomended{

   lateinit var binding: FragmentViewStoreBinding
    var adapter: Recycler_SelectCoupon_Adapet? = null
    var list = ArrayList<SelectCoupon_DataModel>()
    var storelist = ArrayList<StoreDetails_Model>()
    var listtrandingCoupon = ArrayList<CouponInDetails_DataModel>()
    var adapterTrandingCoupon: Recomended_CouponAdapter? = null
    lateinit var locationRequest: LocationRequest
    var vendorId = ""
    var userId = ""
    var couponCode = ""
    var couponCodes = ""
    var offerCode = ""
    var save_unsave = ""
    var recomend = ""
 //   var userAddress = ""
    var vendorAddress = ""
    var viewStore = ""
    var listVendorAddress = ArrayList<VendorAddress_DataModel>()
//    lateinit var edt_Destination:EditText
//    lateinit var edt_yourLocation:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentViewStoreBinding.inflate(inflater, container, false)

            val dataBundle = arguments
            if (dataBundle != null) {
                vendorId = dataBundle.getString("vendorId").toString()
                viewStore = dataBundle.getString("viewStore").toString()
                recomend = dataBundle.getString("recomend").toString()
            }

            //   Log.d("ghjkl", "onCreateView: "+vendorId)

            val sharedPreferences2 =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences2?.getString("userId", null).toString()
            //   userAddress = sharedPreferences2?.getString("address", null).toString()

            binding.imgBackViewStore.setOnClickListener(View.OnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            })

            binding.progressBar.visibility = VISIBLE

            if (recomend == "recomend") {
                binding.textCoupons.visibility = GONE
                binding.recyclerViewStoreCoupon.visibility = GONE
            } else {
                binding.textCoupons.visibility = VISIBLE
                binding.recyclerViewStoreCoupon.visibility = VISIBLE
            }

            CoroutineScope(Dispatchers.IO).launch {
                showCouponApi()
                recomendedCoupon()
            }


            binding.imgMap.setOnClickListener(View.OnClickListener {
                //  showDialog()
                CoroutineScope(Dispatchers.IO).launch {
                    vendorAddress()
                }

            })

        }
        return binding.root
    }

    override fun onClick(position: Int) {
        couponCode = list[position].coupon_code
        offerCode = list[position].offer_code
     //  replaceFragment(Coupon_BarCode_Fragment())
        startActivity(Intent(requireContext(), CommonActivity::class.java)
            .putExtra("from","barCode")
            .putExtra("couponCode",couponCode)
            //.putExtra("couponCode", couponCode)
            .putExtra("viewStore", viewStore)
            .putExtra("recomend", recomend))


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

    override fun recomendCoupon(position: Int) {
            recomend = "recomend"
            viewStore = ""
            couponCode = listtrandingCoupon[position].coupon_codes
            replaceFragment(Coupon_BarCode_Fragment())


    }


    fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTrasaction = fragmentManager?.beginTransaction()
        val dataBundle = Bundle()
        dataBundle.putString("couponCode", couponCode)
        dataBundle.putString("viewStore", viewStore)
        dataBundle.putString("recomend", recomend)
        fragment.arguments = dataBundle
        fragmentTrasaction?.add(R.id.fragmentContaine,fragment)
        fragmentTrasaction?.addToBackStack(null)
        fragmentTrasaction?.commit()
    }




    suspend fun vendorAddress() {
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .vendorAddress(vendorId)

        if (response.isSuccessful) {
            if (response.body()?.status == true) {
                withContext(Dispatchers.Main) {
                    if (isAdded) {
                        listVendorAddress = response.body()?.data!!
                        vendorAddress = listVendorAddress[0].address

                        val geoUri = Uri.parse("geo:0,0?q=$vendorAddress")
                        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)

                        // Check if there are any apps that can handle the map intent
                        val packageManager = requireContext().packageManager
                        val activities = packageManager.queryIntentActivities(mapIntent, 0)

                        if (activities.isNotEmpty()) {
                            startActivity(mapIntent)
                        } else {
                            // Handle the case when no map-related apps are installed
                            Toast.makeText(requireContext(), "No map app found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    if (isAdded) {
                        Toast.makeText(requireContext(), "" + response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "not success" + response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun showCouponApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .storeCoupons(vendorId,userId)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                      list = response.body()?.data!!
                    storelist = response.body()?.vendor!!
                    binding.progressBar.visibility = GONE
                    adapter = Recycler_SelectCoupon_Adapet(list,requireContext(),this@ViewStore_Fragment)
                    binding.recylerViewStore.adapter = adapter
                    binding.textStoreName.text = storelist[0].store_name
                    binding.textStoreRating.text = storelist[0].rating
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



    suspend fun recomendedCoupon(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .couponInDetails(userId)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){

                    listtrandingCoupon = response.body()?.data!!
                    adapterTrandingCoupon = Recomended_CouponAdapter(listtrandingCoupon,requireContext(),this@ViewStore_Fragment)
                    binding.recyclerViewStoreCoupon.adapter = adapterTrandingCoupon



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