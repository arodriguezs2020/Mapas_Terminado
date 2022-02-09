package com.example.mapas.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.mapas.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import android.Manifest

@Suppress("DEPRECATION")
class MapsGpsFragment : Fragment(), OnMapReadyCallback {

    private var miVista : View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        miVista = inflater.inflate(R.layout.fragment_maps_gps, container, false)
        return miVista
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView : MapView?
        mapView = miVista?.findViewById(R.id.mapViewGps) as MapView
        mapView.onCreate(null)
        mapView.onResume()
        mapView.getMapAsync(this)
        checarSenialGPS()
    }

    override fun onResume() {
        super.onResume()
        checarSenialGPS()
    }

    fun checarSenialGPS() {

        try {
            val gps = Settings.Secure.getInt(activity?.contentResolver, Settings.Secure.LOCATION_MODE)

            if (gps == 0) {
                Toast.makeText(context, "Por favor, para continuar , habilita la señal GPS", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }

    }

    // Fallo al mostrar el GPS con la localización actual del usuario

    override fun onMapReady(googleMap: GoogleMap?) {

        val gMap: GoogleMap? = googleMap

        if (ActivityCompat.checkSelfPermission(miVista!!.context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                miVista!!.context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return
        }

        gMap!!.isMyLocationEnabled = true
    }
}