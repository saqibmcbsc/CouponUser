package com.couponusers.couponuser.Fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.couponusers.couponuser.Iterface.ApiInterface
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.example.couponuser.databinding.FragmentEditProfileBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.common.api.Api
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.internal.notify
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class Edit_Profile_Fragment : Fragment() {

    lateinit var binding: FragmentEditProfileBinding
    var name = ""
    var dob = ""
    var email = ""
    var location = ""
    lateinit var datePicker: DatePickerDialog
    var uri: Uri? = null
    var bmp: Bitmap? = null
    var baos: ByteArrayOutputStream? = null
    var storageRef: StorageReference? = null
    var userId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentEditProfileBinding.inflate(inflater, container, false)

            val sharedPreferences =
                activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            userId = sharedPreferences?.getString("userId", null).toString()


            val calendar = Calendar.getInstance();
            val day = calendar.get(Calendar.DAY_OF_MONTH);
            val year = calendar.get(Calendar.YEAR);
            val month = calendar.get(Calendar.MONTH);

            binding.progressBar.visibility = VISIBLE
            binding.constEditProfile.visibility = GONE

            binding.btnEditSave.setOnClickListener(View.OnClickListener {
                name = binding.edtEditName.text.toString()
                dob = binding.edtDob.text.toString()
                email = binding.edtEmail.text.toString()
                location = binding.edtLocation.text.toString()

                if (name.isEmpty()) {
                    Toast.makeText(requireContext(), "please enter your name", Toast.LENGTH_SHORT)
                        .show()
                } else if (dob.isEmpty()) {
                    Toast.makeText(requireContext(), "please enter your dob", Toast.LENGTH_SHORT)
                        .show()
                } else if (email.isEmpty()) {
                    Toast.makeText(requireContext(), "please enter your email", Toast.LENGTH_SHORT)
                        .show()
                } else if (location.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "please enter your location",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    binding.progressBar.visibility = VISIBLE
                    binding.constEditProfile.visibility = VISIBLE
                    CoroutineScope(Dispatchers.IO).launch {
                        updateProfile()
                    }
                }
            })

            binding.edtEditMobileNumber.setOnClickListener(View.OnClickListener {
                Toast.makeText(
                    requireContext(),
                    "Mobile Number is not Editable",
                    Toast.LENGTH_SHORT
                ).show()
            })
            binding.edtEmail.setOnClickListener(View.OnClickListener {
                Toast.makeText(requireContext(), "Email Id is not Editable", Toast.LENGTH_SHORT)
                    .show()
            })

            binding.btnImageChange.setOnClickListener(View.OnClickListener {
                ImagePicker.with(this)
                    .start()
            })

            binding.edtDob.setOnClickListener(View.OnClickListener {
                datePicker = DatePickerDialog(
                    requireContext(),
                    { view, year, month, dayOfMonth -> // adding the selected date in the edittext
                        binding.edtDob.setText(dayOfMonth.toString() + "/" + (month + 1) + "/" + year)
                    }, year, month, day
                )
                datePicker.datePicker.maxDate = calendar.timeInMillis
                datePicker.show()
            })

            CoroutineScope(Dispatchers.IO).launch {
                userEditProfileShow()
            }
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        uri = data?.data
        binding.edtProfileImage.setImageURI(uri)

    }
    suspend fun userEditProfileShow(){
        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .userEditProfileShow(userId)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                val getData = response.body()?.data
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    binding.constEditProfile.visibility = VISIBLE

                  //  Picasso.get().load(ApiHelper.imageUrl+ getData?.get(0)?.image).into(binding.edtProfileImage)
                    val imageUrl = ApiHelper.imageUrl + getData?.get(0)?.image

                    Picasso.get().load(imageUrl).error(R.drawable.user_icon).into(binding.edtProfileImage)
                    binding.edtEditName.setText(getData?.get(0)?.name)
                    binding.textEdtProfileName.setText(getData?.get(0)?.name)
                    binding.edtDob.setText(getData?.get(0)?.dob)
                    binding.edtEmail.setText(getData?.get(0)?.email)
                    binding.edtEditMobileNumber.setText(getData?.get(0)?.phone)
                    binding.edtLocation.setText(getData?.get(0)?.address)


                }
            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    binding.constEditProfile.visibility = VISIBLE
                    Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = GONE
                binding.constEditProfile.visibility = VISIBLE
                Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    suspend fun updateProfile(){

        val filesDir = requireContext().filesDir
        val file = File(filesDir,"image.png")

        val inputStream = uri?.let { activity?.contentResolver?.openInputStream(it) }
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val partts = MultipartBody.Part.createFormData("image",file.name,requestBody)

        val response = ApiHelper.getInstance().create(ApiInterface::class.java)
            .updateProfile(userId,name,dob,location,partts)

        if (response.isSuccessful){
            if (response.body()?.status==true){
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    binding.constEditProfile.visibility = VISIBLE
                    Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()

                }
            }else{
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = GONE
                    binding.constEditProfile.visibility = VISIBLE
                    Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = GONE
                binding.constEditProfile.visibility = VISIBLE
                Toast.makeText(requireContext(), "not success"+response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}