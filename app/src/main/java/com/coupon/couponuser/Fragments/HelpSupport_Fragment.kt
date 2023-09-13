package com.couponusers.couponuser.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentHelpSupportBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList


class HelpSupport_Fragment : Fragment() {

    lateinit var binding: FragmentHelpSupportBinding
    var userId = ""
    var spinnerValue = ""
    var description =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentHelpSupportBinding.inflate(inflater, container, false)


            val sharedPreferences =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences?.getString("userId", null).toString()

            binding.imgBackArrowHelpSupport.setOnClickListener(View.OnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            })


            binding.supportBtn.setOnClickListener(View.OnClickListener {
                spinnerValue = binding.spinnerIssues.selectedItem.toString();
                description = binding.edtBriefHelp.text.toString()

                binding.progressBar.visibility = VISIBLE

                CoroutineScope(Dispatchers.IO).launch {
                    helpSupportDescriptionApi()
                }

            })

            binding.progressBar.visibility = VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                helpSupportIssueApi()
            }

        }
        return binding.root
    }

    suspend fun helpSupportIssueApi(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .issuesType()

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    if(isAdded){
                        binding.progressBar.visibility = GONE
                        var list = ArrayList<String>()
                        var dataList = response.body()!!.data.size
                        for (i in 0 until dataList){
                            val  model = response.body()!!.data[i].issue
                            list.add(model)
                        }
                        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.style_spinner, list)
                        binding.spinnerIssues.adapter = arrayAdapter

                    }

                }
            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    Toast.makeText(requireContext(), ""+response?.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = GONE
                Toast.makeText(requireContext(), "not sucessfull"+response?.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun helpSupportDescriptionApi(){

        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .helpDescription(userId,spinnerValue,description)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                }
            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    Toast.makeText(requireContext(), ""+ response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = GONE
                Toast.makeText(requireContext(), "Not Sucessfull"+ response.body()!!.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

}