package com.couponusers.couponuser.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.coupon.couponuser.ModelClass.GenralNotification_DataModel
import com.couponusers.couponuser.Adapter.RecyclerGenral_NotificationAdapter
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.GenralNotificationModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentNotificationGenralBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Notification_GenralFragment : Fragment() {

    lateinit var binding: FragmentNotificationGenralBinding
    var userId = ""
    var list = ArrayList<GenralNotification_DataModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentNotificationGenralBinding.inflate(inflater, container, false)

            val sharedPreferences =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences?.getString("userId", null).toString()


            CoroutineScope(Dispatchers.IO).launch {
                genralNotification()
            }
        }
        return binding.root

    }

    suspend fun genralNotification(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .genralNotification(userId)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    if (isAdded){
                        list = response.body()?.data!!
                        binding.recycelerGeneralNotification.layoutManager = LinearLayoutManager(context)

                        binding.recycelerGeneralNotification.adapter = RecyclerGenral_NotificationAdapter(list,requireContext())

                    }
                }
            }else{
                withContext(Dispatchers.Main){
                    if (isAdded){
                        Toast.makeText(requireActivity(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            withContext(Dispatchers.Main){
                if (isAdded){
                    Toast.makeText(requireActivity(), "not success"+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}