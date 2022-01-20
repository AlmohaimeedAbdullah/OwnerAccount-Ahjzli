package com.tuwaiq.owneraccount

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tuwaiq.owneraccount.databinding.ActivityMapsBinding
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    var currentMarker: Marker? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var currentLocation: Location? = null
    var fusedLocationProviderClient:FusedLocationProviderClient? = null
    private lateinit var buttonClicked:Button
    private lateinit var lat :String
    private lateinit var lon :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        buttonClicked = findViewById(R.id.buttonClicked)

        buttonClicked.setOnClickListener {
            var returnIntent =Intent()
            returnIntent.putExtra("latitude",lat)
            returnIntent.putExtra("longitude",lon)
            Log.e("print", "$lat $lon")
            setResult(Activity.RESULT_OK,returnIntent)
            finish()
        }

        fetchLocation()
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)
            return
        }
        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location ->
            if (location != null){
                this.currentLocation = location
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fetchLocation()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //current location
        val latLong = LatLng(currentLocation?.latitude!!,currentLocation?.longitude!!)
        drawMarker(latLong)
        mMap.setOnMarkerDragListener(object :GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(p0: Marker) {
                if(currentMarker != null)
                    currentMarker?.remove()

                val  newLatLng = LatLng(p0.position.latitude,p0.position.longitude)
                drawMarker(newLatLng)
            }

            override fun onMarkerDragStart(p0: Marker) {

            }

        })
    }
    private fun drawMarker(latLong:LatLng){
        //the location in the marker
        val markerOption = MarkerOptions().position(latLong).title("I'm here")
            .snippet(getAddress(latLong.latitude, latLong.longitude)).draggable(true)
        Log.e("drawMarker","${latLong.latitude}, ${latLong.longitude}")

        lat=latLong.latitude.toString()
        lon=latLong.longitude.toString()

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLong))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong,15f))
        currentMarker = mMap.addMarker(markerOption)
        currentMarker?.showInfoWindow()

    }

    fun getAddress(lat: Double, lon: Double):String {
        //change the lat and lon to address
        val geoCoder = Geocoder(this, Locale.getDefault())
        val addresses = geoCoder.getFromLocation(lat,lon,1)
        return addresses[0].getAddressLine(0).toString()
    }

}