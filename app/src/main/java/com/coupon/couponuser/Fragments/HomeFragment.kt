package com.couponusers.couponuser.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.coupon.couponuser.ModelClass.DailyCoupon_DataModel
import com.coupon.couponuser.Utils.MyApplication
import com.couponusers.couponuser.Adapter.*
import com.couponusers.couponuser.Fragments.*
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.*
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class HomeFragment : Fragment(), recycler_categoryAdaptor.IntrestClickCategory,
    Recycler_LimitedOffer_Adapter.ClickLimitedOffer, Recycler_OffersAdapter.BestOffer,
    Recycler_TrandingCoupon_Adapter.TrandCoupon, Recycler_DailyCoupon_Adapter.DailyCoupon,
    Recycler_BrandCoupon_Adapter.BrandCouponDash, Recycler_FeaturedStore_Adapter.StoreIntreface {

    var binding: FragmentHomeBinding? = null
    var list = ArrayList<Category_DataModel>()
    var adapter: recycler_categoryAdaptor? = null
    var listOffer = ArrayList<BestOffer_DataModel>()
    var adapterOffer: Recycler_OffersAdapter? = null
    var listLimitedOffer = ArrayList<LimitedOffer_DataModel>()
    var adapterLimitedOffer: Recycler_LimitedOffer_Adapter? = null
    var listbrandCoupon = ArrayList<BrandCoupons_DataModel>()
    var adapterBrandCoupon: Recycler_BrandCoupon_Adapter? = null
    var listtrandingCoupon = ArrayList<TrandingCoupon_DataModel>()
    var adapterTrandingCoupon: Recycler_TrandingCoupon_Adapter? = null
    var listDailyCoupon = ArrayList<DailyCoupon_DataModel>()
    var adapterDailyCoupon: Recycler_DailyCoupon_Adapter? = null
    var adapterFeaturedStore: Recycler_FeaturedStore_Adapter? = null
    var listFeatured = ArrayList<FeaturedStore_DataModel>()
    var adapterBottom: Recycler_ProductAdapter? = null
    var listBottom = ArrayList<ProductDetails_Model>()
    var adapterBottom2: Recycler_StoreRatingAdapter? = null
    private lateinit var locationCallback: LocationCallback
//    var listSearch = ArrayList<AllFeaturedStore_DataModel>()
//    var adapterFilter: Recycler_AllStored_Adapter? = null
    var listBottom2 = ArrayList<Rating_Model>()
    private lateinit var locationProvider: FusedLocationProviderClient
    lateinit var recycler_productImage: RecyclerView
    lateinit var recycler_ratings: RecyclerView
    lateinit var image_store: CircleImageView
    lateinit var text_storeNames: TextView
    lateinit var text_storeDetalis: TextView
    lateinit var text_ratings: TextView
    lateinit var text_totalCoupons: TextView
    lateinit var text_five: TextView
    lateinit var text_fiveReview: TextView
    lateinit var edtBg: EditText
    lateinit var text_numberKm: TextView
    var REQUEST_CODE = 101
    lateinit var datePicker: DatePickerDialog
    var userId = ""
    var categoryId = ""
    var offerCode = ""
    var vendorId = ""
    var ratingNo = ""
    var review = ""
    var range = ""
    var userAddress = ""
    var dashSearch = ""
    private var lattitude: Double = 0.0
    private var longitude: Double = 0.0
    lateinit var progressBar:ProgressDialog
    var currentLocation: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            "address",
            this
        ) { key, bundle ->
            if (key == "address") {
                val selectedSort = bundle.get("address")
                 lattitude = bundle.get("lat")?.toString()?.toDoubleOrNull() ?: 0.0
                 longitude = bundle.get("lng")?.toString()?.toDoubleOrNull() ?: 0.0
                latLongApi(lattitude, longitude)
                binding?.textLocation?.text = selectedSort as CharSequence?
            }

        }

    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this
        if(view == null){

        val sharedPreferences = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        userId = sharedPreferences?.getString("userId", null).toString()




        if (binding == null) {
            binding = FragmentHomeBinding.inflate(inflater, container, false)
           // progressBar = ProgressDialog.show(requireContext(), "", "Please Wait...")

            progressBar = ProgressDialog(context)
           // progressBar.setTitle("Loading")
            progressBar.setMessage("Please Wait...")
            progressBar.setCancelable(false)

            progressBar.show()
            val progressbar = progressBar.findViewById<View>(android.R.id.progress) as ProgressBar
            progressbar.indeterminateDrawable.setColorFilter(
                Color.parseColor("#FF8732"),
                PorterDuff.Mode.SRC_IN
            )

            locationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())

            fetchLocation()

            binding!!.constLocation.setOnClickListener(View.OnClickListener {
                replaceFragmentHome(com.couponusers.couponuser.Fragments.MapFragment())
            })

            binding!!.imgNotification.setOnClickListener(View.OnClickListener {
                replaceFragmentHome(Notification_Fragment())
            })

            // Filter Icon
            binding!!.imgFilter.setOnClickListener {
                filterBottomsheet()
            }
            // Category ViewAll
            binding!!.textViewAll.setOnClickListener(View.OnClickListener {
                replaceFragmentHome(AllCategory_Fragment())
            })



            CoroutineScope(Dispatchers.IO).launch {
                try {
//                   async {  bestOfferApi() }.await()
                   async {  categoryApi() }.await()
                   async {  dashBannerApi() }.await()
                   async { limitedOfferApi() }.await()
                   async {  brandCouponApi() }.await()
                    async { hotDealsApi() }.await()
                     async {dailyCouponApi()}.await()
                    async { featuredStoreApi() }.await()

                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
            //View All Best Offer
            binding!!.textViewAllOffers.setOnClickListener(View.OnClickListener {
                replaceFragmentHome(SelectOffer_CouponFragment())
            })

            //View All Limited Offer
            binding!!.textViewAllLimitedOffers.setOnClickListener(View.OnClickListener {
                replaceFragmentHome(ViewAll_LimitedOffers_Fragment())
            })

            //View All BrandCoupon
            binding!!.textViewAllBrandCoupons.setOnClickListener(View.OnClickListener {
                replaceFragmentHome(ViewAll_BrandCoupons_Fragment())
            })

            //View All Featured Store
            binding!!.textViewAllFeaturedStore.setOnClickListener(View.OnClickListener {
                dashSearch = ""
                replaceFragmentHome(ViewAll_FeaturedStore_Fragment())
            })

            binding!!.searchView.setOnClickListener(View.OnClickListener {
                dashSearch = "dashSearch"
                replaceFragmentHome(ViewAll_FeaturedStore_Fragment())
            })


//            if (ActivityCompat.checkSelfPermission(
//                    requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    REQUEST_CODE
//                )
//            }
        }
        }
        return binding!!.root


    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTrasaction = fragmentManager?.beginTransaction()
        fragmentTrasaction?.add(R.id.fragmentContain, fragment)
        fragmentTrasaction?.addToBackStack(null)
        fragmentTrasaction?.commit()
    }

    fun replaceFragmentHome(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTrasaction = fragmentManager?.beginTransaction()
        val dataBundle = Bundle()
        dataBundle.putString("categoryId", categoryId)
        dataBundle.putString("offerCode", offerCode)
        dataBundle.putString("vendorId", vendorId)
        dataBundle.putString("dashSearch", dashSearch)
        fragment.arguments = dataBundle
        fragmentTrasaction?.add(R.id.fragmentContai, fragment)
        fragmentTrasaction?.addToBackStack(null)
        fragmentTrasaction?.commit()
    }

    @SuppressLint("MissingInflatedId")
    fun filterBottomsheet() {
        var dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.filter_bottomsheet, null)
        dialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme)
        val range_seekBar: SeekBar = view.findViewById(R.id.range_seekBar)
        text_numberKm = view.findViewById(R.id.text_numberKm)
        val btn_apply: AppCompatButton = view.findViewById(R.id.btn_apply)
        var startPoint = 0
        var endPoint = 0

        range_seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                text_numberKm.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    startPoint = seekBar.progress
                }
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    endPoint = seekBar.progress
                }
            }
        })

        btn_apply.setOnClickListener(View.OnClickListener {
            range = text_numberKm.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                filterDashApi()
            }
            dialog.dismiss()
        })

        dialog.setCancelable(true)

        dialog.setContentView(view)

        dialog.show()
    }

    override fun onClick(position: Int) {
        categoryId = list[position].id
        replaceFragmentHome(SelectCoupons_Fragment())
    }


    override fun onClic(position: Int) {
        offerCode = listLimitedOffer[position].offer_code
        replaceFragmentHome(Coupon_BarCode_Fragment())

    }

    override fun clickOffer(position: Int) {
        offerCode = listOffer[position].offer_code
        replaceFragmentHome(Coupon_BarCode_Fragment())
    }

    override fun trandClick(position: Int) {
        offerCode = listtrandingCoupon[position].offer_code
        replaceFragmentHome(Coupon_BarCode_Fragment())
    }

    override fun dailyClick(position: Int) {
        offerCode = listDailyCoupon[position].offer_code
        replaceFragmentHome(Coupon_BarCode_Fragment())
    }

    override fun imgBrandCouponClick(position: Int) {
        offerCode = listbrandCoupon[position].offer_code
        replaceFragmentHome(Coupon_BarCode_Fragment())
    }

    override fun storeClick(position: Int) {
        vendorId = listFeatured[position].id
        storeBottomSheet()
    }

    suspend fun categoryApi() {
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .showCategory()

        try {
            if (response.isSuccessful) {
                if (response.body()?.status == true) {
                    list = response.body()?.data!!
                    binding?.recyclerCategory?.setHasFixedSize(true)
                    adapter = recycler_categoryAdaptor(list, requireActivity(), this)
                    withContext(Dispatchers.Main) {
                        binding!!.recyclerCategory.adapter = adapter
                    }

                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(),
                            "" + response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireActivity(),
                        "not sucessfull" + response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }


    }


    suspend fun dashBannerApi() {
        try {
            val response = ApiHelper.getInstance().create(ApiInterface::class.java)
                .showBannerDashboard()

            if (response.isSuccessful) {
                if (response.body()?.status == true) {
                    val banners = response.body()?.data
                    withContext(Dispatchers.Main) {
                        banners?.let {
                            loadBannerImages(banners)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(),
                            "" + response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireActivity(),
                        "not success" + response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(requireActivity(), "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    fun storeBottomSheet() {
        var dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.store_bottomsheet, null)
        dialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme)
        val seekBaar = view.findViewById<SeekBar>(R.id.seekBar)
        val img_worstUnclick = view.findViewById<ImageView>(R.id.img_worstUnclick)
        val img_notGoodUnclick = view.findViewById<ImageView>(R.id.img_notGoodUnclick)
        val img_fineUnclick = view.findViewById<ImageView>(R.id.img_fineUnclick)
        val img_lookGoodUnclick = view.findViewById<ImageView>(R.id.img_lookGoodUnclick)
        val img_veryGoodUnclick = view.findViewById<ImageView>(R.id.img_veryGoodUnclick)
        recycler_productImage = view.findViewById<RecyclerView>(R.id.recycler_productImage)
        recycler_ratings = view.findViewById<RecyclerView>(R.id.recycler_ratings)
        image_store = view.findViewById<CircleImageView>(R.id.image_store)
        text_storeNames = view.findViewById<TextView>(R.id.text_storeNames)
        text_storeDetalis = view.findViewById<TextView>(R.id.text_storeDetalis)
        text_ratings = view.findViewById<TextView>(R.id.text_ratings)
        text_totalCoupons = view.findViewById<TextView>(R.id.text_totalCoupons)
        text_five = view.findViewById<TextView>(R.id.text_five)
        text_fiveReview = view.findViewById<TextView>(R.id.text_fiveReview)
        edtBg = view.findViewById<EditText>(R.id.edtBg)

        val btn_viewCoupons_bootom = view.findViewById<AppCompatButton>(R.id.btn_viewCoupons_bootom)
        val btn_submit_bootom = view.findViewById<AppCompatButton>(R.id.btn_submit_bootom)




        btn_viewCoupons_bootom.setOnClickListener(View.OnClickListener {
            replaceFragmentHome(ViewStore_Fragment())
            dialog.dismiss()
        })




        seekBaar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                if (seek.progress > 2 && seek.progress < 25) {
                    img_worstUnclick.setImageResource(R.drawable.worst_emoji_click)
                    img_notGoodUnclick.setImageResource(R.drawable.not_goodemoji_unclick)
                    img_fineUnclick.setImageResource(R.drawable.fine_emoji_unclick)
                    img_lookGoodUnclick.setImageResource(R.drawable.look_goodemoji_unclick)
                    img_veryGoodUnclick.setImageResource(R.drawable.very_goodemoji_unclick)
                    ratingNo = "1"
                } else if (seek.progress > 25 && seek.progress < 40) {
                    img_notGoodUnclick.setImageResource(R.drawable.not_goodemoji_click)
                    img_worstUnclick.setImageResource(R.drawable.worst_emoji_unclick)
                    img_fineUnclick.setImageResource(R.drawable.fine_emoji_unclick)
                    img_lookGoodUnclick.setImageResource(R.drawable.look_goodemoji_unclick)
                    img_veryGoodUnclick.setImageResource(R.drawable.very_goodemoji_unclick)
                    ratingNo = "2"
                } else if (seek.progress > 40 && seek.progress < 60) {
                    img_fineUnclick.setImageResource(R.drawable.fine_emoji_click)
                    img_notGoodUnclick.setImageResource(R.drawable.not_goodemoji_unclick)
                    img_worstUnclick.setImageResource(R.drawable.worst_emoji_unclick)
                    img_lookGoodUnclick.setImageResource(R.drawable.look_goodemoji_unclick)
                    img_veryGoodUnclick.setImageResource(R.drawable.very_goodemoji_unclick)
                    ratingNo = "3"
                } else if (seek.progress > 60 && seek.progress < 80) {
                    img_lookGoodUnclick.setImageResource(R.drawable.look_goodemoji_click)
                    img_fineUnclick.setImageResource(R.drawable.fine_emoji_unclick)
                    img_notGoodUnclick.setImageResource(R.drawable.not_goodemoji_unclick)
                    img_worstUnclick.setImageResource(R.drawable.worst_emoji_unclick)
                    img_veryGoodUnclick.setImageResource(R.drawable.very_goodemoji_unclick)
                    ratingNo = "4"
                } else if (seek.progress == 100) {
                    img_veryGoodUnclick.setImageResource(R.drawable.very_goodemoji_click)
                    img_lookGoodUnclick.setImageResource(R.drawable.look_goodemoji_unclick)
                    img_fineUnclick.setImageResource(R.drawable.fine_emoji_unclick)
                    img_notGoodUnclick.setImageResource(R.drawable.not_goodemoji_unclick)
                    img_worstUnclick.setImageResource(R.drawable.worst_emoji_unclick)
                    ratingNo = "5"
                } else {
                    img_veryGoodUnclick.setImageResource(R.drawable.very_goodemoji_unclick)
                    img_lookGoodUnclick.setImageResource(R.drawable.look_goodemoji_unclick)
                    img_fineUnclick.setImageResource(R.drawable.fine_emoji_unclick)
                    img_notGoodUnclick.setImageResource(R.drawable.not_goodemoji_unclick)
                    img_worstUnclick.setImageResource(R.drawable.worst_emoji_unclick)
                    ratingNo = ""
                }
            }

            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seek: SeekBar) {

            }
        })

        btn_submit_bootom.setOnClickListener(View.OnClickListener {
            review = edtBg.text.toString()
            if (ratingNo.isEmpty() || ratingNo == "") {
                Toast.makeText(
                    requireActivity(),
                    "Please Select any emojie for rating",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    storeRatingApi()
                    dialog.dismiss()
                }
            }

        })


        CoroutineScope(Dispatchers.IO).launch {
            bottomStore()
        }

        dialog.setCancelable(true)

        dialog.setContentView(view)

        dialog.show()
    }

    private fun loadBannerImages(banners: List<DashboardBanner_DataMode>) {
        binding?.viewflipper?.removeAllViews()

        for (banner in banners) {
            val imageView = ImageView(requireContext())
            Picasso.get().load(ApiHelper.imageUrl + banner.image).into(imageView)
            binding?.viewflipper?.addView(imageView)
        }

        binding?.viewflipper?.flipInterval = 3000
        binding?.viewflipper?.isAutoStart = true
        binding?.viewflipper?.setInAnimation(requireActivity(), android.R.anim.slide_in_left)
        binding?.viewflipper?.setOutAnimation(requireActivity(), android.R.anim.slide_out_right)
        binding?.viewflipper?.startFlipping()
    }


    suspend fun bestOfferApi() {
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .bestOfferApi(userId)

       try {
           if (response.isSuccessful) {
               if (response.body()?.status == true) {
                   withContext(Dispatchers.Main) {
                       listOffer = response.body()?.data!!
                       adapterOffer = Recycler_OffersAdapter(listOffer, requireActivity(), this@HomeFragment)
                       binding!!.recyclerOffer.adapter = adapterOffer

                   }
               } else {
                   withContext(Dispatchers.Main) {
                       Toast.makeText(requireActivity(), "" + response.body()?.message, Toast.LENGTH_SHORT).show()
                   }
               }
           } else {
               withContext(Dispatchers.Main) {
                   Toast.makeText(requireActivity(), "not success "+response.body()?.message, Toast.LENGTH_SHORT).show()

               }
           }
       }catch (e:Exception){
           e.printStackTrace()
       }

    }

    suspend fun limitedOfferApi() {
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .limitedOfferApi(userId)

        try {
            if (response.isSuccessful) {
                if (response.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        listLimitedOffer = response.body()?.data!!

                        adapterLimitedOffer = Recycler_LimitedOffer_Adapter(
                            listLimitedOffer,
                            requireActivity(),
                            this@HomeFragment
                        )
                        binding!!.recyclerLimitedOffer.adapter = adapterLimitedOffer
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(),
                            "" + response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireActivity(),
                        "not success" + response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    suspend fun brandCouponApi() {

        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .brandCouponApi(userId)

        try {
            if (response.isSuccessful) {
                if (response.body()?.status == true) {
                    withContext(Dispatchers.Main) {

                        listbrandCoupon = response.body()?.data!!

                        adapterBrandCoupon = Recycler_BrandCoupon_Adapter(
                            listbrandCoupon,
                            requireActivity(),
                            this@HomeFragment
                        )
                        binding!!.recyclerBrandCoupons.adapter = adapterBrandCoupon


                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(),
                            "" + response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireActivity(),
                        "not sucess" + response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    suspend fun hotDealsApi() {
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .hotDealsApi(userId)

        try {
            if (response.isSuccessful) {
                if (response.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        listtrandingCoupon = response.body()?.data!!
                        adapterTrandingCoupon = Recycler_TrandingCoupon_Adapter(
                            listtrandingCoupon,
                            requireActivity(),
                            this@HomeFragment
                        )
                        binding!!.recyclerTrandingCoupons.adapter = adapterTrandingCoupon
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(),
                            "" + response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireActivity(),
                        "not success " + response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    suspend fun dailyCouponApi() {
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .dailyCouponsApi(userId)

        try {
            if (response.isSuccessful) {
                if (response.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        listDailyCoupon = response.body()?.data!!

                        adapterDailyCoupon = Recycler_DailyCoupon_Adapter(listDailyCoupon, requireActivity(), this@HomeFragment)
                        binding!!.recyclerDailyCoupons.adapter = adapterDailyCoupon

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireActivity(), "" + response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireActivity(), "not success " + response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    suspend fun featuredStoreApi() {
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .featuredStoredApi(userId)

        try {
            if (response.isSuccessful) {
                if (response.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        progressBar.dismiss()
                        listFeatured = response.body()?.data!!
                        adapterFeaturedStore = Recycler_FeaturedStore_Adapter(
                            listFeatured, requireActivity(), this@HomeFragment
                        )
                        binding!!.recyclerFeaturedStore.adapter = adapterFeaturedStore

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        progressBar.dismiss()
                        Toast.makeText(requireActivity(), "" + response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    progressBar.dismiss()
                    Toast.makeText(requireActivity(),
                        "not success " + response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    suspend fun bottomStore() {
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .showStoreBottom(vendorId)

        try {
            if (response.isSuccessful) {
                if (response.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        listBottom = response.body()?.product!!
                        listBottom2 = response.body()?.rating!!
                        val storeData = response.body()?.store!!

                        text_storeNames.text = storeData.store_name
                        text_storeDetalis.text = storeData.address
                        Picasso.get().load(ApiHelper.imageUrl + storeData.image).into(image_store)
                        text_ratings.text = response.body()?.ratingaverage
                        text_totalCoupons.text = response.body()?.totalcoupon
                        text_five.text = response.body()?.productcount
                        text_fiveReview.text = response.body()?.ratingcount


                        adapterBottom = Recycler_ProductAdapter(listBottom, requireContext())
                        recycler_productImage?.adapter = adapterBottom

                        adapterBottom2 = Recycler_StoreRatingAdapter(listBottom2, requireContext())
                        recycler_ratings.adapter = adapterBottom2

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(),
                            "" + response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireActivity(),
                        "not successfull" + response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    suspend fun storeRatingApi() {
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .storeRating(userId, vendorId, ratingNo, review)

        try {
            if (response.isSuccessful) {
                if (response.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(),
                            "" + response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(),
                            "" + response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireActivity(),
                        "not success" + response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    suspend fun filterDashApi() {
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .dashFilter(userId, range)

        try {
            if (response.isSuccessful) {
                if (response.body()?.status == true) {
                    withContext(Dispatchers.Main) {
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireActivity(), "" + response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireActivity(), "not success" + response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    fun latLongApi(lat: Double, lng: Double) {
        if (!isNetworkConnected()) {
            Toast.makeText(requireActivity(), "No internet connection", Toast.LENGTH_SHORT).show()
            return
        }

        val apiService = ApiHelper.getInstance().create(ApiInterface::class.java)
        val call = apiService.userLocation(userId, lat, lng)


        call.enqueue(object : Callback<UserLocation_Model> {
            override fun onResponse(call: Call<UserLocation_Model>, response: Response<UserLocation_Model>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.status) {

                        } else {
                            // Handle unsuccessful response data if needed
                          //  Toast.makeText(requireActivity(), responseBody.message, Toast.LENGTH_SHORT).show()

                        }
                    } else {
                        // Handle the case where the response body is null
                      //  Toast.makeText(requireActivity(), "Response body is empty", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    // Handle non-successful response (e.g., 404, 500, etc.)
                    val errorBody = response.errorBody()?.string()
                  //  Toast.makeText(requireActivity(), "Error: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserLocation_Model>, t: Throwable) {
                // Handle network errors or request failure
             //   Toast.makeText(requireActivity(), "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()

            }
        })
    }

    // Helper function to check network connectivity
    private fun isNetworkConnected(): Boolean {
        val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun fetchLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE)

            return
        }

        if (isLocationEnabled()) {
            val task = locationProvider!!.lastLocation
         //   latLongApi(lattitude, longitude)
            task.addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = location


                    // Start a coroutine to perform the Geocoder API call asynchronously
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                            val addresses = geocoder.getFromLocation(
                                currentLocation!!.latitude,
                                currentLocation!!.longitude,
                                1
                            )

                            latLongApi(currentLocation!!.latitude, currentLocation!!.longitude)
                            // Update the UI on the main thread
                            withContext(Dispatchers.Main) {
                                if (addresses != null) {
                                    if (addresses.isNotEmpty()) {
                                        val address = addresses?.get(0)?.getAddressLine(0)
                                        val locality = addresses?.get(0)?.subLocality
                                        val city = addresses?.get(0)?.locality
                                        val state = addresses?.get(0)?.adminArea
                                        val postalCode = addresses?.get(0)?.postalCode

                                        binding?.textLocation?.text = address
                                        userAddress = binding?.textLocation?.text?.toString().toString()



                                    }
                                }
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                            // Handle the exception appropriately, e.g., show an error message to the user

                        }
                    }
                }
            }
        }

    }


    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireActivity(), "Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }


}