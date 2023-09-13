package com.couponusers.couponuser.ModelClass

data class StoreBottom_Model(val status:Boolean,val message:String,val store:Store,val product:ArrayList<ProductDetails_Model>,val rating: ArrayList<Rating_Model>,val ratingaverage:String,val totalcoupon:String,val productcount:String,val ratingcount:String)
