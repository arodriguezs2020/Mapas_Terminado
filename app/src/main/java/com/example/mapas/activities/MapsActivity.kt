package com.example.mapas.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mapas.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.mapas.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val marcador: MarkerOptions?

        // Add a marker in Logroño and move the camera
        val logrono = LatLng(42.458388955061096, -2.4640059471130376)
        // mMap.addMarker(MarkerOptions().position(logroño).title("Marker in Logroño, La Rioja").draggable(true))


        mMap.setMinZoomPreference(6.0f)
        mMap.setMaxZoomPreference(17.0f)

        // Modificamos nuestro marcador
        marcador = MarkerOptions()
        marcador.position(logrono)
        marcador.title("Este es mi marcador")
        marcador.snippet("Aquí puedes poner algún dato de localización")
        marcador.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.arrow_up_float))
        marcador.draggable(true)

        // Lo añadimos al Marker
        mMap.addMarker(marcador)

        val camara = CameraPosition.builder()
            .target(logrono)
            .zoom(17f)
            .bearing(0f) // Rotación
            .tilt(30f) // Ángulo
            .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camara))

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(logroño))

        mMap.setOnMapClickListener { latLng ->
            Toast.makeText(
                this, "Las coordenadas son: \n" +
                        "Latitud: " + latLng.latitude + "\n" +
                        "Longitud: " + latLng.longitude, Toast.LENGTH_SHORT
            ).show()
        }

        mMap.setOnMapLongClickListener { latLng ->
            Toast.makeText(
                this, "--Las coordenadas son: \n" +
                        "Latitud: " + latLng.latitude + "\n" +
                        "Longitud: " + latLng.longitude, Toast.LENGTH_SHORT
            ).show()
        }

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(p0: Marker) {

            }

            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(marker: Marker) {
                Toast.makeText(
                    this@MapsActivity, "Drag: Las coordenadas son: \n" +
                            "Latitud: " + marker.position.latitude + "\n" +
                            "Longitud: " + marker.position.longitude, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}