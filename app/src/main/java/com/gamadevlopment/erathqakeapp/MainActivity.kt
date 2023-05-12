package com.gamadevlopment.erathqakeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = Home_fragment(this)
        val mapFragment = Map_Fragment()

        //on App start run Home Fragment
        runFragment(homeFragment)
        //Bottom Nav Bar
        bottomNavBar = findViewById(R.id.bottom_Navigation_main)
        bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_home -> runFragment(homeFragment)
                R.id.bottom_nav_map -> runFragment(mapFragment)
            }
            true
        }

    }


    //Function to run Fragment
    fun runFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view, fragment)
            addToBackStack(null)
            commit()
        }
    }
}