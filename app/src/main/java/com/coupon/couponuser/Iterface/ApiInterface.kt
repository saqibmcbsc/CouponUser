package com.couponusers.couponuser.Iterface

import com.coupon.couponuser.ModelClass.*
import com.couponusers.couponuser.ModelClass.*
import com.couponusers.couponuser.ModelClass.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiInterface {

    @POST("user-registration")
    suspend fun signUpApi(@Query("phone") phone:String,@Query("user_token") notificationToken:String):Response<SignUp_Model>

    @POST("user-login")
    suspend fun loginApi(@Query("phone") phone:String):Response<Login_Model>

    @POST("user-login-by-otp")
    suspend fun otpApi(@Query("phone") phone:String,@Query("otp") otp:String):Response<Otp_Model>

    @POST("user-createprofile")
    suspend fun createProfileApi(@Query("user_id") userId:String,@Query("full_name") name:String,@Query("dob") dob:String,@Query("email_id") emailId:String,@Query("address") address:String):Response<CreateProfile_Model>

    @GET("show-categories")
    suspend fun showCategory():Response<Category_Model>

    @POST("user-interest")
    suspend fun userInterest(@Query("user_id") userId: String,@Query("category_id") categoryId:String):Response<UsersInterest_Model>

    @GET("show-allcategories")
    suspend fun showAllInterest():Response<AllInterest_Model>

    @POST("coupon-bycategory")
    suspend fun userCategoryClick(@Query("user_id") userId: String,@Query("category_id") categoryId:String,@Query("subcategory_id") subCategory:String):Response<SelectCoupon_Model >

    @GET("show-allcategories")
    suspend fun showAllCategory():Response<Category_Model>

    @GET("banner")
    suspend fun showBannerDashboard():Response<DashboardBanner_Model>

    @POST("best-offers")
    suspend fun bestOfferApi(@Query("user_id") userId:String):Response<BestOffer_Model>

//    @POST("showcoupon-by-offercode")
//    suspend fun offerCouponApi(@Query("offer_code") offerCode:String):Response<OfferCoupon_Model>

    @POST("all-best-offers")
    suspend fun bestAllOfferApi(@Query("user_id") userId:String): Response<SelectCoupon_Model>

    @POST("limited-offers")
    suspend fun limitedOfferApi(@Query("user_id") userId:String): Response<LimitedOffer_Model>

    @POST("all-limited-offers")
    suspend fun limitedAllOfferApi(@Query("user_id") userId:String): Response<LimitedOffer_Model>

    @POST("brand-coupon")
    suspend fun brandCouponApi(@Query("user_id") userId:String):Response<BrandCoupons_Model>

    @POST("all-brand-coupon")
    suspend fun brandAllCouponApi(@Query("user_id") userId:String):Response<BrandCoupons_Model>

    @POST("hot-deals")
    suspend fun hotDealsApi(@Query("user_id") userId:String):Response<TrandingCoupon_Model>

    @POST("daily-coupon")
    suspend fun dailyCouponsApi(@Query("user_id") userId:String):Response<DailyCoupon_Model>

    @POST("featured-store")
    suspend fun featuredStoredApi(@Query("user_id") userId:String):Response<FeaturedStore_Model>

    @POST("all-featured-store")
    suspend fun allFeaturedStoredApi(@Query("user_id") userId:String,@Query("category_id") cateId:String):Response<AllFeaturedStore_Model>

    @POST("show-vendorstore")
    suspend fun showStoreBottom(@Query("vendor_id") vendorId:String):Response<StoreBottom_Model>

    @POST("view-vendorcoupon")
    suspend fun storeCoupons(@Query("vendor_id") vendorId:String,@Query("user_id") userId: String):Response<SelectCoupon_Model>

    @POST("show-coupons")
    suspend fun bottomCoupons(@Query("user_id") userId:String):Response<SelectCoupon_Model>

    @GET("issues")
    suspend fun issuesType():Response<HelpSupport_IssueModel>

    @POST("usershelp")
    suspend fun helpDescription(@Query("user_id") userId:String, @Query("issue_type") issueType:String,@Query("description") description:String):Response<supportDescription_Model>

    @GET("user-aboutus")
    suspend fun aboutUsApi():Response<AboutUs_Model>

    @GET("user-terms")
    suspend fun termsConditionApi():Response<TermsCondtion_Model>

    @POST("save-coupon")
    suspend fun saveCoupon(@Query("user_id") userId:String,@Query("coupon_code") couponCode:String,@Query("offer_code") offerCode:String,@Query("saved_coupon") savedCoupon:String):Response<savedCoupon_Model>

    @POST("show-saved-coupon")
    suspend fun showSavedCoupon(@Query("user_id") userId:String):Response<SelectCoupon_Model>

    @POST("store-rating")
    suspend fun storeRating(@Query("user_id") userId: String,@Query("vendor_id") vendorId:String,@Query("rating") rating:String,@Query("review") review:String):Response<StoreRating_Model>

    @POST("user-profile")
    suspend fun userProfile(@Query("user_id") userId: String):Response<UserProfile_Model>

    @POST("user-profile")
    suspend fun userEditProfileShow(@Query("user_id") userId: String):Response<EditProfile_Show>

    @Multipart
    @POST("update-profile")
    suspend fun updateProfile(@Query("user_id") userId: String,@Query("name") name:String,@Query("dob") dob:String,@Query("address") address:String,@Part image: MultipartBody.Part):Response<UpdateProfile_Model>

    @POST("show-interest")
    suspend fun selectedInterest(@Query("user_id") userId: String):Response<SelectedInterset_Model>

    @POST("filter")
    suspend fun dashFilter(@Query("user_id") userId: String,@Query("range") range:String):Response<DashFilter_Model>

    @POST("user-location")
    fun userLocation(@Query("user_id") userId: String,@Query("latitude") lat:Double,@Query("longitude") long:Double): Call<UserLocation_Model>

    @POST("show-redeem-coupon")
    suspend fun couponRedeemed(@Query("user_id") userId: String):Response<RedeemNow_Model>

    @POST("show-coupons-indetail")
    suspend fun couponInDetails(@Query("user_id") userId: String):Response<CouponInDetails_Model>

    @POST("show-coupon-details")
    suspend fun barCodeApi(@Query("coupon_code") couponCode:String):Response<BarCode_Model>

    @POST("coupon-by-offercode")
    suspend fun couponByOfferCode(@Query("offer_code") offerCode:String):Response<BarCode_Model>

    @POST("show-subcategory")
    suspend fun subCategory(@Query("category_id") categoryId:String):Response<Filter_Category_Model>

    @POST("user-general-notification")
    suspend fun genralNotification(@Query("user_id") userId: String):Response<GenralNotificationModel>

    @POST("user-recomanded-notification")
    suspend fun recomendNotification(@Query("user_id") userId: String):Response<GenralNotificationModel>

    @POST("store-location")
    suspend fun vendorAddress(@Query("vendor_id") vendorId: String):Response<VendorAddress_Model>

    @POST("update-interest")
    suspend fun updateInterest(@Query("user_id") userId: String,@Query("category_id") categoryId: String):Response<UpdateInterest_Model>

    @POST("check-user-details")
    suspend fun  userDetails_Check(@Query("user_id") userId: String):Response<Users_DetailsCheck_Model>

}