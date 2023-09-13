package com.couponusers.couponuser.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.couponusers.couponuser.Adapter.Recycler_TermsConditon_Adapter
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.ModelClass.TermsCondition_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentAboutUsBinding
import com.example.couponuser.databinding.FragmentTermsConditionBinding
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TermsCondition_Fragment : Fragment() {

    lateinit var binding: FragmentTermsConditionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentTermsConditionBinding.inflate(inflater, container, false)

            binding.imgBackArrowTermsCondition.setOnClickListener(View.OnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            })

            binding.progressBar.visibility = VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                termsConditionApi()
            }

        }
        return binding.root
    }

    suspend fun termsConditionApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .termsConditionApi()

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    if (isAdded){
                        binding.progressBar.visibility = GONE
                        val layoutManager = LinearLayoutManager(requireContext(),
                            LinearLayoutManager.VERTICAL,false)
                        binding.recyclerTermsCondition.layoutManager = layoutManager

                        var list = ArrayList<TermsCondition_DataModel>()
                        list = response.body()!!.data

                        binding.recyclerTermsCondition.adapter = Recycler_TermsConditon_Adapter(list,requireContext())
                    }
                }
            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    Toast.makeText(requireContext(), ""+response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = GONE
                Toast.makeText(requireContext(), "Not Sucessfull"+response.body()!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}