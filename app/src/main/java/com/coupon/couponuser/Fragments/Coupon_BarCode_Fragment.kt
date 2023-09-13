package com.couponusers.couponuser.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.*
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import com.coupon.couponuser.ModelClass.BarCode_DataModel
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentCouponBarCodeBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Coupon_BarCode_Fragment : Fragment() {

    lateinit var binding: FragmentCouponBarCodeBinding
    var data = ArrayList<BarCode_DataModel>()
    var couponCode = ""
    var offerCode = ""
    var vendorId = ""
    var bottomBar = ""
    var viewStore = ""
    var recomend = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentCouponBarCodeBinding.inflate(inflater, container, false)

            val dataBundle = arguments
            if (dataBundle != null) {
                couponCode = dataBundle.getString("couponCode").toString()
                offerCode = dataBundle.getString("offerCode").toString()
                bottomBar = dataBundle.getString("bottomBar").toString()
                viewStore = dataBundle.getString("viewStore").toString()
                recomend = dataBundle.getString("recomend").toString()
            }
            Log.d("sdfghj", "onCreateView: " + bottomBar)
            if (bottomBar == "bottom") {
                val layoutParams =
                    binding.imgBackArrowCoupons.layoutParams as ViewGroup.MarginLayoutParams

                val leftMargin = 0
                val topMargin = 150
                val rightMargin = 0
                val bottomMargin = 0

                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin)

                binding.imgBackArrowCoupons.layoutParams = layoutParams
            }

            if (viewStore == "viewStore") {
                binding.imgViewStore.visibility = GONE
            } else {
                binding.imgViewStore.visibility = VISIBLE
            }

            binding.progressBar.visibility = VISIBLE
            binding.constBarCodes.visibility = GONE

            binding.imgViewStore.setOnClickListener(View.OnClickListener {
                replaceFragment(ViewStore_Fragment())
            })

            binding.imgBackArrowCoupons.setOnClickListener(View.OnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            })

            if (offerCode != "null" && offerCode != "") {
                CoroutineScope(Dispatchers.IO).launch {
                    barCodeOfferApi()
                }

            } else if (couponCode != "null" && couponCode != "") {
                CoroutineScope(Dispatchers.IO).launch {
                    barCodeApi()
                }
            }


        }

        return binding.root
    }


    fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTrasaction = fragmentManager?.beginTransaction()
        val dataBundle = Bundle()
        dataBundle.putString("vendorId", vendorId)
        dataBundle.putString("viewStore", "viewStore")
        dataBundle.putString("recomend", recomend)
        fragment.arguments = dataBundle
        fragmentTrasaction?.add(R.id.fragmentContainar,fragment)
        fragmentTrasaction?.addToBackStack(null)
        fragmentTrasaction?.commit()
    }

    suspend fun barCodeApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .barCodeApi(couponCode)

        if (response.isSuccessful){
            if (response.body()?.status == true){
                withContext(Dispatchers.Main){
                //    Toast.makeText(requireActivity(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = GONE
                    binding.constBarCodes.visibility = VISIBLE
                    data = response.body()?.data!!
                    Picasso.get().load(ApiHelper.imageUrl+ data[0].couopn_image).into(binding.imgBrandLogo)
                    binding.textStoreName.text = data[0].store_name
                    binding.textStoreRating.text = data[0].rating
                    binding.textParcent.text = data[0].offer_discount
                    binding.textBrands.text = data[0].brand_name
                    binding.textTitle.text = data[0].offer_subtitle
                    binding.textDes1.text = data[0].description
                    binding.textDate.text = data[0].validate_upto
                    vendorId = data[0].id

                    couponCode = data[0].coupon_code

                    Log.d("hjkhj", "barCodeApi: "+couponCode)

                    binding.btnRedemNow.setOnClickListener(View.OnClickListener {
                        if (couponCode.equals("") || couponCode.isEmpty() || couponCode == null){
                            Toast.makeText(requireActivity(), "Coupon Code not Provided for QR Code", Toast.LENGTH_SHORT).show()
                        }else{
                            val encoder  = QRGEncoder(couponCode,null,QRGContents.Type.TEXT,800)
                            binding.imgQrCode.setImageBitmap(encoder.bitmap)
                            binding.textCouponCode.text = data[0].coupon_code
                        }
                    })

                }

            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    binding.constBarCodes.visibility = VISIBLE
                    Toast.makeText(requireActivity(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = GONE
                binding.constBarCodes.visibility = VISIBLE
                Toast.makeText(requireActivity(), "not success"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun barCodeOfferApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .couponByOfferCode(offerCode)

        if (response.isSuccessful){
            if (response.body()?.status == true){
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    binding.constBarCodes.visibility = VISIBLE
                      //  Toast.makeText(requireActivity(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                    data = response.body()?.data!!
                    Picasso.get().load(ApiHelper.imageUrl+ data[0].couopn_image).into(binding.imgBrandLogo)
                    binding.textStoreName.text = data[0].store_name
                    binding.textStoreRating.text = data[0].rating
                    binding.textParcent.text = data[0].offer_discount
                    binding.textBrands.text = data[0].brand_name
                    binding.textTitle.text = data[0].offer_subtitle
                    binding.textDes1.text = data[0].description
                    binding.textDate.text = data[0].validate_upto
                    vendorId = data[0].id

                    couponCode = data[0].coupon_code

                    Log.d("hjkhj", "barCodeApi: "+couponCode)

                    binding.btnRedemNow.setOnClickListener(View.OnClickListener {
                        if (couponCode.equals("") || couponCode.isEmpty() || couponCode == null){
                            Toast.makeText(requireActivity(), "Coupon Code not Provided for QR Code", Toast.LENGTH_SHORT).show()
                        }else{
                            val encoder  = QRGEncoder(couponCode,null,QRGContents.Type.TEXT,800)
                            binding.imgQrCode.setImageBitmap(encoder.bitmap)
                            binding.textCouponCode.text = data[0].coupon_code
                        }
                    })



                }

            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    binding.constBarCodes.visibility = VISIBLE
                    Toast.makeText(requireActivity(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = GONE
                binding.constBarCodes.visibility = VISIBLE
                Toast.makeText(requireActivity(), "not success"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}