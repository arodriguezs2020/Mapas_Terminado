package com.example.mapas.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.mapas.R
import com.example.mapas.fragments.MainFragment
import com.example.mapas.fragments.MapFragment

class MainActivity : AppCompatActivity() {

    var fragmentoActual: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentoActual = MainFragment()
        cambiarFragmento(fragmentoActual)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_principal -> fragmentoActual = MainFragment()
            R.id.menu_mapa -> fragmentoActual = MapFragment()
        }
        cambiarFragmento(fragmentoActual)
        return super.onOptionsItemSelected(item)
    }

    fun cambiarFragmento(fragment: Fragment?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment!!)
            .commit()
    }
}