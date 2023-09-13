package com.couponusers.couponuser.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.couponusers.couponuser.Adapter.Recycler_StaggerdAdapter
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.AllInterest_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.couponusers.couponuser.Utils.setFullScreen
import com.example.couponuser.R
import com.example.couponuser.databinding.ActivityInterestBinding
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class Interest_Activity : AppCompatActivity(), Recycler_StaggerdAdapter.IntrestClick {

    lateinit var binding: ActivityInterestBinding
    var list = ArrayList<AllInterest_DataModel>()
    var adapter: Recycler_StaggerdAdapter? = null
    var userId = ""
    var categoryId = ""
    var selectedCategoryIds = ArrayList<String>()
    var updateCategoryId = ""
    var profile = ""

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_interest)
        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        profile = intent.getStringExtra("profile").toString()

        binding.progressBar.visibility = VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            interestShowApi()
        }

        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
       userId = sharedPreferences.getString("userId", null).toString()


        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        if (profile == "profile"){
            CoroutineScope(Dispatchers.IO).launch{
                selectedInterest()
            }
        }

     binding.btnNextDashboard.setOnClickListener(View.OnClickListener {
         binding.progressBar.visibility = VISIBLE
         var listsContain = false
         for (i in 0 until list.size){
             if (list[i].isChecked){
                 listsContain = true
                 break
             }
         }
             if (!listsContain){
                 Toast.makeText(this, "Please Select Atleast 1 Interest", Toast.LENGTH_SHORT).show()
             }else{
                 CoroutineScope(Dispatchers.IO).launch {
                     if (profile == "profile"){
                         updateInterest()
                     }else{
                         selectInterestApi()
                     }

             }

         }

     })
    }

    private fun filterList(query:String?){
        if (query!=null){
            val filterList = ArrayList<AllInterest_DataModel>()
            for (i in list){
                if (i.category.toLowerCase(Locale.ROOT).contains(query)){
                    filterList.add(i)
                }
            }

            if (filterList.isEmpty()){
                Toast.makeText(this, "No Data found..!!", Toast.LENGTH_SHORT).show()
            }
            adapter?.setFilterList(filterList)
        }
    }

    override fun onClick(position: Int) {
        list[position].isChecked = !list[position].isChecked
        adapter?.notifyItemChanged(position)
    }

    override fun onClickCheck(position: Int) {
        list[position].isChecked = !list[position].isChecked
        adapter?.notifyItemChanged(position)
    }

    suspend fun interestShowApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .showAllInterest()

        if (response.isSuccessful){
            if (response.body()?.status==true){
                list = response.body()!!.data
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = View.GONE
                    val layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
                    binding.recyclerStaggerd.layoutManager = layoutManager

                    selectedInterest()



                }

            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, "not Success"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun selectInterestApi(){
        var catId = ""
        for (i in 0 until list.size){

            if (list[i].isChecked){
                if (catId==""){
                    catId = list[i].id
                }else{
                    catId = catId+","+list[i].id
                }
            }
        }
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .userInterest(userId,catId)

        Log.d("vfghjkl", "selectInterestApi: "+catId)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = View.GONE

                        val intent = Intent(this@Interest_Activity,DashBoard_Activity::class.java)
                        startActivity(intent)

                }
            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, ""+ response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, "not success"+ response.body()!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun selectedInterest(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .selectedInterest(userId)

        if (response.isSuccessful) {
            if (response.body()?.status == true) {
               // val selectedCategoryIds = mutableListOf<String>()
                withContext(Dispatchers.Main) {
                    if (profile == "profile"){
                        for(i in 0 until list.size){
                            for( j in 0 until response.body()!!.data.size){
                                if(list[i].id== response.body()!!.data[j].id){
                                    list[i].isChecked = true
                                  //  selectedCategoryIds.add(list[i].id)
                                    break
                                }
                            }
                        }
                       // val categoryIdsString = selectedCategoryIds.joinToString(",")

                       // updateInterest(userId, categoryIdsString)
                    }

                    adapter  = Recycler_StaggerdAdapter(list,applicationContext,this@Interest_Activity)
                    binding.recyclerStaggerd.adapter = adapter
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "" + response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                Toast.makeText(applicationContext, ""+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun updateInterest(){
        var catId = ""
        for (i in 0 until list.size){

            if (list[i].isChecked){
                if (catId==""){
                    catId = list[i].id
                }else{
                    catId = catId+","+list[i].id
                }
            }
        }
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .updateInterest(userId, catId)
        if (response.isSuccessful) {
            if (response.body()?.status == true) {
                withContext(Dispatchers.Main) {
                  // binding.progressBar.visibility = View.VISIBLE
                    Log.d("fghjk", "updateInterest: "+catId)
                    onBackPressed()
                    Toast.makeText(applicationContext, ""+response.body()?.message, Toast.LENGTH_SHORT).show()

                }
            } else {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "" + response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, "not success" + response.body()!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}