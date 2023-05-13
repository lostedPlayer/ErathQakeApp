package com.gamadevlopment.erathqakeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(), recyclerAdapter.onCardClickListner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = Home_fragment(applicationContext, this)

        //on App start run Home Fragment
        runFragment(homeFragment)


    }


    //Function to run Fragment
    fun runFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view, fragment)
            addToBackStack(null)
            commit()
        }
    }


    //Handle Card Click inside Home Fragment
    override fun onCardClicked(earthQake: EarthQuake) {
        val mapFragment = Map_Fragment(earthQake)
        runFragment(mapFragment)
    }
}