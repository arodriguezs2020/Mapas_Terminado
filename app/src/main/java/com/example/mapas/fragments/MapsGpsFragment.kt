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
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapsGpsFragment : Fragment(), OnMapReadyCallback, LocationListener {

    // --- Inicializamos una serie de variables que nos servirán a la hora de realizar las siguientes implementaciones --- /
    private lateinit var miVista: View
    private lateinit var gestorDeLocation: LocationManager
    private var locationActual: Location? = null
    private var marcador: Marker? = null
    private var gMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        miVista = inflater.inflate(R.layout.fragment_maps_gps, container, false)

        // Instanciamos el boton flotante que hemos metido en nuestro xml
        val fab: FloatingActionButton = miVista.findViewById(R.id.fab) as FloatingActionButton

        // Cuando clicken en el boton
        fab.setOnClickListener {
            // Checkeamos los permisos y los redirigimos a la misma pantalla
            if (ActivityCompat.checkSelfPermission(
                    miVista.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    miVista.context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@setOnClickListener
            }

            // Comprobamos si nos esta actualizando a traves del GPS o a traves del NETWORK
            var location: Location? =
                gestorDeLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location == null) {
                location = gestorDeLocation.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            // Lo asignamos a la variable locationActual
            locationActual = location

            // Y comprobamos si es nulo o no, si no lo es actualizamos el marcador a esa localización
            if (locationActual != null) {
                actualizarOCrearMarcador(location!!)

                // Hacemos la animación de la cámara para darle mejor experiencia al usuario
                val camara = CameraPosition.builder()
                    .target(LatLng(location.latitude, location.longitude))
                    .zoom(17f)
                    .bearing(0f) // Rotación
                    .tilt(30f) // Ángulo
                    .build()

                gMap?.animateCamera(CameraUpdateFactory.newCameraPosition(camara))
            }
        }

        // Retornamos la vista
        return miVista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView: MapView?
        mapView = miVista.findViewById(R.id.mapViewGps) as MapView
        mapView.onCreate(null)
        mapView.onResume()
        mapView.getMapAsync(this)
        checarSenialGPS()
    }

    override fun onResume() {
        super.onResume()
        checarSenialGPS()
    }

    // Con esta función checkeamos la señal GPS y si el usuario no tiene activado el GPS le redirigimos a los Settings y le mostramos un Toast incicandole que tiene que activar el GPS
    private fun checarSenialGPS() {
        try {
            val gps =
                Settings.Secure.getInt(activity?.contentResolver, Settings.Secure.LOCATION_MODE)
            if (gps == 0) {
                Toast.makeText(
                    context,
                    "Por favor, para continuar , habilita la señal GPS",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
    }

    // Con este metodo renderizamos el mapa

    override fun onMapReady(googleMap: GoogleMap?) {

        gMap = googleMap!!

        if (ActivityCompat.checkSelfPermission(
                miVista.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                miVista.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        gMap?.isMyLocationEnabled = true
        // gMap.uiSettings.isMyLocationButtonEnabled = false     ------> Con esta opcion desactivamos la localización del usuario

        gestorDeLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
        gestorDeLocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, this)
    }

    override fun onLocationChanged(location: Location) {
        Toast.makeText(context, "Actualizado -> " + location.provider, Toast.LENGTH_SHORT).show()

        actualizarOCrearMarcador(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        super.onStatusChanged(provider, status, extras)
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
    }

    private fun actualizarOCrearMarcador(location: Location) {
        if (marcador == null) {
            marcador = gMap?.addMarker(
                MarkerOptions().position(
                    LatLng(
                        location.latitude,
                        location.longitude
                    )
                ).draggable(true)
            )
        } else {
            marcador?.position = LatLng(location.latitude, location.longitude)
        }
    }
}