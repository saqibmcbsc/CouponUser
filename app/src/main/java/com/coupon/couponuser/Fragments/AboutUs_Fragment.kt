package com.couponusers.couponuser.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.couponusers.couponuser.Adapter.Recycler_AboutUs_Adapter
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.AboutUs_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentAboutUsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AboutUs_Fragment : Fragment() {

    lateinit var binding:FragmentAboutUsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        if(view == null) {
            binding = FragmentAboutUsBinding.inflate(inflater, container, false)


            binding.imgBackArrowAbout.setOnClickListener(View.OnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            })

            binding.progressBar.visibility = VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                aboutUsApi()
            }
        }
        return binding.root
    }

    suspend fun aboutUsApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .aboutUsApi()

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    if (isAdded){
                        var layoutManager = LinearLayoutManager(requireContext(),
                            LinearLayoutManager.VERTICAL,false)
                        binding.recyclerAboutUs.layoutManager = layoutManager
                        binding.progressBar.visibility = GONE
                        var list = ArrayList<AboutUs_DataModel>()
                        list = response.body()!!.data

                        binding.recyclerAboutUs.adapter = Recycler_AboutUs_Adapter(list,requireContext())
                    }
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
                Toast.makeText(requireContext(), "Not Sucessfull"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}