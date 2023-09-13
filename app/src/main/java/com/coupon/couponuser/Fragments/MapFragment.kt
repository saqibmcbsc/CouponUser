package com.couponusers.couponuser.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.couponuser.R
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*


class MapFragment : Fragment(),OnMapReadyCallback {

    lateinit var myMap:GoogleMap
    var currentLocation: Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    lateinit var locationSearch: AppCompatTextView
    lateinit var const_setLocation: ConstraintLayout
    var REQUEST_CODE = 101
    private val PICKUP_REQUEST_CODE = 999
    private var marker: Marker? = null
    var address = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_map, container, false)

//      activity?.supportFragmentManager?.findFragmentById(R.id.map)
//        mapFragment.getMapAsync()

        locationSearch = view.findViewById(R.id.locationSearch)
        const_setLocation = view.findViewById(R.id.const_setLocation)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        fetchLocation();

        const_setLocation.setOnClickListener(View.OnClickListener {
            address = locationSearch.text.toString()
            if (address.isEmpty()){
                Toast.makeText(requireContext(), "Please Select Location", Toast.LENGTH_SHORT).show()
            }else{
                var gc = Geocoder(requireActivity(),Locale.getDefault())
                var addresses = gc.getFromLocationName(address,2)
                addresses?.get(0)?.let {
                    var add = addresses?.get(0)
//                    val locality:String = addresses!![0].subLocality
//                    val city: String = addresses!![0].locality
//                    val state: String = addresses!![0].adminArea
                    if (add != null) {
                       // Toast.makeText(requireContext(), ""+add.latitude + "  "+add.longitude, Toast.LENGTH_SHORT).show()
                    }
                    val resultBundle = Bundle().apply {
                        putString("address", address)
                        putString("lat", add?.latitude.toString())
                        putString("lng", add?.longitude.toString())
                    }
                    requireActivity().supportFragmentManager.setFragmentResult("address", resultBundle)
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }


            }
        })



        locationSearch.setOnClickListener(View.OnClickListener {
            if (!Places.isInitialized()) {
                Places.initialize(requireContext(), getString(R.string.map_api_key), Locale.US);
            }
            launchLocationAutocompleteActivity(PICKUP_REQUEST_CODE)
        })

        return view
    }

    private fun fetchLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }
        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location

                val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
                mapFragment.getMapAsync(this)

                val geocoder: Geocoder
                val addresses: List<Address>?
                geocoder = Geocoder(requireContext(), Locale.getDefault())

                addresses = geocoder.getFromLocation(
                    currentLocation!!.latitude,
                    currentLocation!!.longitude,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


                val address: String =
                    addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                val locality:String = addresses!![0].subLocality
                val city: String = addresses!![0].locality
                val state: String = addresses!![0].adminArea
//                val country: String = addresses!![0].countryName
//                val postalCode: String = addresses!![0].postalCode
//                val knownName: String = addresses!![0].featureName
                locationSearch.text = locality+","+city
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
        marker = myMap.addMarker(markerOptions)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    private fun launchLocationAutocompleteActivity(requestCode: Int) {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(requireContext())
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICKUP_REQUEST_CODE) {

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    when (requestCode) {
                        PICKUP_REQUEST_CODE -> {
                            locationSearch.text = place.name
//                            pickUpLatLng = place.latLng
//                            val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                            val markerOptions = place.latLng?.let { MarkerOptions().position(it).title("I am here!") }
                            myMap.animateCamera(CameraUpdateFactory.newLatLng(place.latLng!!))
                            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng!!, 11f))
                          //  myMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng!!))
                            marker?.remove()
                            marker = null
                            if (markerOptions != null) {
                                marker = myMap.addMarker(markerOptions)
                            }
                        }
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status : Status = Autocomplete.getStatusFromIntent(data!!)
                    Log.d("TAG",status.statusMessage!!)
                }

                Activity.RESULT_CANCELED -> {

                }
            }
        }
    }

}