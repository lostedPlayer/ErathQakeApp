package com.gamadevlopment.erathqakeapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

class Map_Fragment : Fragment(R.layout.map_fragment) {

    lateinit var mapView: MapView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.MapView)

        try {
            mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            Configuration.getInstance().userAgentValue = "EarthQakeApp"
            mapView.controller.setZoom(5)

        } catch (e: Exception) {
            Log.d("Map_Fragment", "map loading process : " + e.message)
        }


    }
}