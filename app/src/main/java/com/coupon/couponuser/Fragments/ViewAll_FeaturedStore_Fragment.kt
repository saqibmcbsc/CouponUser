package com.couponusers.couponuser.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.coupon.couponuser.Fragments.Recycler_FilterStore_Adaptor
import com.couponusers.couponuser.Adapter.Recycler_AllStored_Adapter
import com.couponusers.couponuser.Adapter.Recycler_ProductAdapter
import com.couponusers.couponuser.Adapter.Recycler_StoreRatingAdapter
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.AllFeaturedStore_DataModel
import com.couponusers.couponuser.ModelClass.AllInterest_DataModel
import com.couponusers.couponuser.ModelClass.ProductDetails_Model
import com.couponusers.couponuser.ModelClass.Rating_Model
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentViewAllFeaturedStoreBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class ViewAll_FeaturedStore_Fragment : Fragment(), Recycler_AllStored_Adapter.StoreInter,Recycler_FilterStore_Adaptor.StoreCatClick{

    lateinit var binding: FragmentViewAllFeaturedStoreBinding
    var adapter: Recycler_AllStored_Adapter? = null
    var list = ArrayList<AllFeaturedStore_DataModel>()
    var adapterFilter: Recycler_FilterStore_Adaptor? = null
    var listFilter = ArrayList<AllInterest_DataModel>()
    var adapterBottom: Recycler_ProductAdapter? = null
    var listBottom = java.util.ArrayList<ProductDetails_Model>()
    var adapterBottom2: Recycler_StoreRatingAdapter? = null
    var recycler_category_Filter: RecyclerView? = null
    var listBottom2 = java.util.ArrayList<Rating_Model>()
    lateinit var recycler_productImage:RecyclerView
    lateinit var recycler_ratings:RecyclerView
    lateinit var image_store:CircleImageView
    lateinit var text_storeNames:TextView
    lateinit var text_storeDetalis:TextView
    lateinit var text_ratings:TextView
    lateinit var text_totalCoupons:TextView
    lateinit var text_five:TextView
    lateinit var text_fiveReview:TextView
    lateinit var edtBg:EditText
    var ratingNo = ""
    var review = ""
    var vendorId = ""
    var userId = ""
    var storeCatId = ""
    var dashSearch = ""
    var storeCatIdList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentViewAllFeaturedStoreBinding.inflate(inflater, container, false)

            val dataBundle = arguments
            if (dataBundle != null) {
                dashSearch = dataBundle.getString("dashSearch").toString()

            }
            Log.d("sdfgh", "onCreateView: " + dashSearch)

            val sharedPreferences =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences?.getString("userId", null).toString()

            binding.imgFilterStore.setOnClickListener(View.OnClickListener {
                categoryFilterBottomSheet()
            })

            if (dashSearch == "dashSearch") {
                val editText =
                    binding.searchViewStore.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView_Store)
                editText.requestFocus()

                binding.searchViewStore.setOnQueryTextFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        showInputMethod(view.findFocus())
                    }
                })
            }


            binding.searchViewStore.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterListStore(newText)


                    return true
                }

            })

            binding.progressBar.visibility = VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                viewAllFeatured()
            }

        }
        return binding.root
    }

    private fun showInputMethod(view: View) {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(view, 0)
    }

    override fun storeClick(position: Int) {
        vendorId = list[position].id
        storeBottomSheet()
    }

    fun storeBottomSheet(){
        var dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.store_bottomsheet, null)
        dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme)
        val seekBaar = view.findViewById<SeekBar>(R.id.seekBar)
        val img_worstUnclick = view.findViewById<ImageView>(R.id.img_worstUnclick)
        val img_notGoodUnclick = view.findViewById<ImageView>(R.id.img_notGoodUnclick)
        val img_fineUnclick = view.findViewById<ImageView>(R.id.img_fineUnclick)
        val img_lookGoodUnclick = view.findViewById<ImageView>(R.id.img_lookGoodUnclick)
        val img_veryGoodUnclick = view.findViewById<ImageView>(R.id.img_veryGoodUnclick)
        val btn_viewCoupons_bootom = view.findViewById<AppCompatButton>(R.id.btn_viewCoupons_bootom)
        recycler_productImage = view.findViewById<RecyclerView>(R.id.recycler_productImage)
        recycler_ratings = view.findViewById<RecyclerView>(R.id.recycler_ratings)
        image_store = view.findViewById<CircleImageView>(R.id.image_store)
        text_storeNames = view.findViewById<TextView>(R.id.text_storeNames)
        text_storeDetalis = view.findViewById<TextView>(R.id.text_storeDetalis)
        text_ratings = view.findViewById<TextView>(R.id.text_ratings)
        text_totalCoupons = view.findViewById<TextView>(R.id.text_totalCoupons)
        text_five = view.findViewById<TextView>(R.id.text_five)
        text_fiveReview = view.findViewById<TextView>(R.id.text_fiveReview)
        val btn_submit_bootom = view.findViewById<AppCompatButton>(R.id.btn_submit_bootom)
        edtBg = view.findViewById<EditText>(R.id.edtBg)

        btn_viewCoupons_bootom.setOnClickListener(View.OnClickListener {
            replaceFragment(ViewStore_Fragment())
            dialog.dismiss()
        })



        seekBaar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
              if (seek.progress>2&&seek.progress<25){
                  img_worstUnclick.setImageResource(R.drawable.worst_emoji_click)
                  img_notGoodUnclick.setImageResource(R.drawable.not_goodemoji_unclick)
                  img_fineUnclick.setImageResource(R.drawable.fine_emoji_unclick)
                  img_lookGoodUnclick.setImageResource(R.drawable.look_goodemoji_unclick)
                  img_veryGoodUnclick.setImageResource(R.drawable.very_goodemoji_unclick)
              }else if (seek.progress>25&&seek.progress<40){
                  img_notGoodUnclick.setImageResource(R.drawable.not_goodemoji_click)
                  img_worstUnclick.setImageResource(R.drawable.worst_emoji_unclick)
                  img_fineUnclick.setImageResource(R.drawable.fine_emoji_unclick)
                  img_lookGoodUnclick.setImageResource(R.drawable.look_goodemoji_unclick)
                  img_veryGoodUnclick.setImageResource(R.drawable.very_goodemoji_unclick)
              }else if (seek.progress>40&&seek.progress<60){
                  img_fineUnclick.setImageResource(R.drawable.fine_emoji_click)
                  img_notGoodUnclick.setImageResource(R.drawable.not_goodemoji_unclick)
                  img_worstUnclick.setImageResource(R.drawable.worst_emoji_unclick)
                  img_lookGoodUnclick.setImageResource(R.drawable.look_goodemoji_unclick)
                  img_veryGoodUnclick.setImageResource(R.drawable.very_goodemoji_unclick)
              }else if (seek.progress>60&&seek.progress<80){
                  img_lookGoodUnclick.setImageResource(R.drawable.look_goodemoji_click)
                  img_fineUnclick.setImageResource(R.drawable.fine_emoji_unclick)
                  img_notGoodUnclick.setImageResource(R.drawable.not_goodemoji_unclick)
                  img_worstUnclick.setImageResource(R.drawable.worst_emoji_unclick)
                  img_veryGoodUnclick.setImageResource(R.drawable.very_goodemoji_unclick)
              }else if (seek.progress==100){
                  img_veryGoodUnclick.setImageResource(R.drawable.very_goodemoji_click)
                  img_lookGoodUnclick.setImageResource(R.drawable.look_goodemoji_unclick)
                  img_fineUnclick.setImageResource(R.drawable.fine_emoji_unclick)
                  img_notGoodUnclick.setImageResource(R.drawable.not_goodemoji_unclick)
                  img_worstUnclick.setImageResource(R.drawable.worst_emoji_unclick)
              }else{
                  img_veryGoodUnclick.setImageResource(R.drawable.very_goodemoji_unclick)
                  img_lookGoodUnclick.setImageResource(R.drawable.look_goodemoji_unclick)
                  img_fineUnclick.setImageResource(R.drawable.fine_emoji_unclick)
                  img_notGoodUnclick.setImageResource(R.drawable.not_goodemoji_unclick)
                  img_worstUnclick.setImageResource(R.drawable.worst_emoji_unclick)
              }
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
               // Toast.makeText(requireContext(), "Progress is: " + seek.progress + "%", Toast.LENGTH_SHORT).show()
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            bottomStore()
        }

        dialog.setCancelable(true)

        dialog.setContentView(view)

        dialog.show()
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTrasaction = fragmentManager?.beginTransaction()
        val dataBundle = Bundle()
        dataBundle.putString("vendorId", vendorId)
        fragment.arguments = dataBundle
        fragmentTrasaction?.add(R.id.fragmentContainrr,fragment)
        fragmentTrasaction?.addToBackStack(null)
        fragmentTrasaction?.commit()
    }

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    fun categoryFilterBottomSheet(){
        var dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.store_filter_categorybottomsheet, null)
        dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme)
         recycler_category_Filter = view.findViewById<RecyclerView>(R.id.recycler_category_Filter)
        val storeFiler_btn = view.findViewById<AppCompatButton>(R.id.storeFiler_btn)
        val searchView_StoreCategory_filter = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView_storeCategory_filter)

           storeFiler_btn.setOnClickListener(View.OnClickListener {
               CoroutineScope(Dispatchers.IO).launch {
                   viewAllFeatured()
                   dialog.dismiss()
               }
           })


        searchView_StoreCategory_filter.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

           CoroutineScope(Dispatchers.IO).launch {
          categoryShowApi()
        }

        dialog.setCancelable(true)

        dialog.setContentView(view)

        dialog.show()
    }


    suspend fun bottomStore(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .showStoreBottom(vendorId)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    listBottom = response.body()?.product!!
                    listBottom2 = response.body()?.rating!!
                    val storeData = response.body()?.store!!

                    text_storeNames.text = storeData.store_name
                    text_storeDetalis.text = storeData.address
                    Picasso.get().load(ApiHelper.imageUrl+storeData.image).into(image_store)
                    text_ratings.text = response.body()?.ratingaverage
                    text_totalCoupons.text = response.body()?.totalcoupon
                    text_five.text = response.body()?.productcount
                    text_fiveReview.text = response.body()?.ratingcount


                    adapterBottom = Recycler_ProductAdapter(listBottom,requireContext())
                    recycler_productImage?.adapter = adapterBottom

                    adapterBottom2 = Recycler_StoreRatingAdapter(listBottom2,requireContext())
                    recycler_ratings.adapter = adapterBottom2

                }
            }else{
                withContext(Dispatchers.Main){
                    Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                Toast.makeText(requireContext(), "not successfull"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun viewAllFeatured(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .allFeaturedStoredApi(userId,storeCatIdList.joinToString(","))


        if (response.isSuccessful){
            if (response.body()?.status == true){
                withContext(Dispatchers.Main){
                    list = response.body()?.data!!

                    binding.progressBar.visibility = GONE

                    adapter = Recycler_AllStored_Adapter(list,requireContext(),this@ViewAll_FeaturedStore_Fragment)
                    binding.recyclerSelectStore.adapter = adapter
                }
            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    Toast.makeText(requireActivity(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = GONE
                Toast.makeText(requireActivity(), "not success "+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun categoryShowApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .showAllInterest()

        if (response.isSuccessful){
            if (response.body()?.status==true){
                listFilter = response.body()!!.data
                withContext(Dispatchers.Main){


                    adapterFilter  = Recycler_FilterStore_Adaptor(listFilter,requireContext(),this@ViewAll_FeaturedStore_Fragment)
                    recycler_category_Filter?.adapter = adapterFilter

                }

            }else{
                withContext(Dispatchers.Main){
                    Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                Toast.makeText(requireContext(), "not Success"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filterList(query:String?){
        if (query!=null){
            val filterList = ArrayList<AllInterest_DataModel>()
            for (i in listFilter){
                if (i.category.toLowerCase(Locale.ROOT).contains(query)){
                    filterList.add(i)
                }
            }

            if (filterList.isEmpty()){
                Toast.makeText(requireContext(), "No Data found..!!", Toast.LENGTH_SHORT).show()
            }
            adapterFilter?.setFilterList(filterList)
        }
    }

    private fun filterListStore(query:String?){
        if (query!=null){
            val listStore = ArrayList<AllFeaturedStore_DataModel>()
            for (i in list){
                if (i.store_name.toLowerCase(Locale.ROOT).contains(query)){
                    listStore.add(i)
                }
            }

            if (listStore.isEmpty()){
                Toast.makeText(requireContext(), "No Data found..!!", Toast.LENGTH_SHORT).show()
            }
            adapter?.setFilterList(listStore)
        }
    }

    override fun storeCatClick(position: Int) {
        storeCatId = listFilter[position].id

        if (storeCatIdList.contains(storeCatId)) {
            storeCatIdList.remove(storeCatId)
        } else {
            storeCatIdList.add(storeCatId)
        }
    }
}