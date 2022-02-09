package com.example.mapas.fragments

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mapas.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private var miVista : View? = null
    private var geoCoder: Geocoder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        miVista = inflater.inflate(R.layout.fragment_maps, container, false)

        return miVista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView : MapView?
        mapView = miVista?.findViewById(R.id.mapView) as MapView
        mapView.onCreate(null)
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val gMap: GoogleMap = googleMap

        val logrono = LatLng(42.458388955061096, -2.4640059471130376)
        val zoom = CameraUpdateFactory.zoomTo(10f)
        gMap.addMarker(MarkerOptions().position(logrono).title("Marker in Logroño, La Rioja").draggable(true))

        gMap.moveCamera(CameraUpdateFactory.newLatLng(logrono))
        gMap.animateCamera(zoom)
        gMap.setOnMarkerDragListener(object: GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragStart(p0: Marker) {

            }

            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(marker: Marker) {

                var direccion: List<Address>? = null
                val latitud = marker.position.latitude
                val longitud = marker.position.longitude

                try {
                    direccion = geoCoder?.getFromLocation(latitud, longitud, 1)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val pais = direccion?.get(0)?.countryName
                val estado = direccion?.get(0)?.adminArea
                val ciudad = direccion?.get(0)?.locality
                val calle = direccion?.get(0)?.getAddressLine(0)
                val codigoPostal = direccion?.get(0)?.postalCode

                Toast.makeText(context, "La Locación es: \n" +
                        "Pais: " + pais + "\n" +
                        "Estado: " + estado + "\n" +
                        "Ciudad: " + ciudad + "\n" +
                        "Calle: " + calle + "\n" +
                        "Codigo Postal: " + codigoPostal + "\n",
                Toast.LENGTH_SHORT).show()
            }
        })

        geoCoder = Geocoder(context, Locale.getDefault())
    }
}