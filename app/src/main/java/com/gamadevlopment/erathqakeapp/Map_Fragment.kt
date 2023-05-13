package com.gamadevlopment.erathqakeapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.IOException

class Map_Fragment(val earthQuake: EarthQuake) : Fragment(R.layout.map_fragment) {

    lateinit var mapView: MapView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.MapView)


        //load map
        try {
            mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            Configuration.getInstance().userAgentValue = "EarthQakeApp"
            mapView.controller.setZoom(15.0)
            mapView.minZoomLevel = 4.0
            mapView.setMultiTouchControls(true)

            val marker = Marker(mapView)
            marker.position = GeoPoint(earthQuake.latitude, earthQuake.longitude)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(marker)

            //custom Map Pin
            marker.icon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_location)
            marker.title =
                earthQuake.place + " \n Magnitude: ${earthQuake.mag.toString()} "
            marker.snippet = "Click For more details"


            //move map to pin location
            mapView.controller.setCenter(marker.position)

            // Disable map movement and zoom controls
            // Disable all touch events on the map
            mapView.setOnTouchListener { _, _ ->
                true // consume the touch event
            }



        } catch (e: Exception) {
            Log.d("Map_Fragment", "map loading process : " + e.message)
        }


        //Only show location marker for used requested EarthQuake
        //get Data from Api inside Coroutine
        /*
        GlobalScope.launch(Dispatchers.IO) {
            //get Data from internet in separate Thread
            getDataFromAPI("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02")

        }

         */


    }


    ///retrieve api data frm server
    fun getDataFromAPI(url: String) {

        val mData = ArrayList<EarthQuake>()
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", "Api Request failed: " + e.message)
            }


            override fun onResponse(call: Call, response: Response) {

                if (response.isSuccessful) {

                    val responseData = response.body?.string()
                    val jsonObject = JSONObject(responseData)
                    val featuresArray = jsonObject.getJSONArray("features")

                    // Loop through the array of features
                    for (i in 0 until featuresArray.length()) {
                        val featureObject = featuresArray.getJSONObject(i)
                        val propertiesObject = featureObject.getJSONObject("properties")

                        // Access the values from the properties object
                        val mag = propertiesObject.getDouble("mag")
                        val place = propertiesObject.getString("place")
                        val time = propertiesObject.getLong("time")


                        // Access the geometry object
                        val geometryObject = featureObject.getJSONObject("geometry")
                        val coordinatesArray = geometryObject.getJSONArray("coordinates")

                        // Access the coordinates from the array
                        val longitude = coordinatesArray.getDouble(0)
                        val latitude = coordinatesArray.getDouble(1)
                        val depth = coordinatesArray.getDouble(2)

                        // add data to arraylist
                        val earthQake = EarthQuake(mag, place, time, longitude, latitude, depth)
                        mData.add(earthQake)

                        Log.d("Home_Fragment", "onResponse Place: " + place)
                        Log.d("Home_Fragment", "onResponse Time: " + time.toString())

                    }


                    GlobalScope.launch(Dispatchers.Main) {
                        //add pins to places on map
                        for (earthQake in mData) {
                            val marker = Marker(mapView)
                            marker.position = GeoPoint(earthQake.latitude, earthQake.longitude)
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            mapView.overlays.add(marker)

                            //custom Map Pin
                            marker.icon =
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_location)
                            marker.title =
                                earthQake.place + " \n Magnitude: ${earthQake.mag.toString()} "
                            marker.snippet = "Click For more details"


                        }

                    }


                } else {
                    Log.d("TEST", "Api Request failed:")
                }


            }


        })

    }


}