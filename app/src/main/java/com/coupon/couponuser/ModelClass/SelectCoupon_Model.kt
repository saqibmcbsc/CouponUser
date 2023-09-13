package com.couponusers.couponuser.ModelClass

import com.coupon.couponuser.ModelClass.StoreDetails_Model

data class SelectCoupon_Model(val status:Boolean,val message:String,val data:ArrayList<SelectCoupon_DataModel>,val vendor:ArrayList<StoreDetails_Model>)
