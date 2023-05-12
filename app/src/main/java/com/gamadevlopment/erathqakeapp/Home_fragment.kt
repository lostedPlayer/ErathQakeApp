package com.gamadevlopment.erathqakeapp


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class Home_fragment(val mContext: Context) : Fragment(R.layout.home_fragment) {

    var mEarthQakesData = ArrayList<EarthQuake>()
    lateinit var loadingProgressBar: ProgressBar
    lateinit var recyclerView: RecyclerView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingProgressBar = view.findViewById(R.id.fragment_home_loading_bar) as ProgressBar
        recyclerView = view.findViewById(R.id.home_fragment_recyclerView) as RecyclerView

        //get Data from Api inside Coroutine
        GlobalScope.launch(Dispatchers.IO) {
            //get Data from internet in separate Thread
            getDataFromAPI("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02")

        }
    }


    ///retrieve api data frm server
    fun getDataFromAPI(url: String) {
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
                        mEarthQakesData.add(earthQake)

                        Log.d("Home_Fragment", "onResponse Place: " + place)
                        Log.d("Home_Fragment", "onResponse Time: " + time.toString())

                    }


                    GlobalScope.launch(Dispatchers.Main) {
                        recyclerView.layoutManager = LinearLayoutManager(mContext)
                        val recyclerAdapter = recyclerAdapter(mContext, mEarthQakesData)
                        recyclerView.adapter = recyclerAdapter

                        //setting up recyclerView
                        loadingProgressBar.visibility = View.INVISIBLE
                        recyclerAdapter.notifyDataSetChanged()
                    }


                } else {
                    Log.d("TEST", "Api Request failed:")
                }


            }


        })

    }
}