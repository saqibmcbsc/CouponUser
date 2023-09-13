package com.coupon.couponuser.Utils

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.couponusers.couponuser.ModelClass.BestOffer_DataModel
import com.couponusers.couponuser.ModelClass.FeaturedStore_DataModel

class MyApplication: ViewModel() {
    val apiData: MutableLiveData<FeaturedStore_DataModel?> = MutableLiveData()

    val apiCallMade: MutableLiveData<Boolean> = MutableLiveData(false)
}