package com.gamadevlopment.erathqakeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class recyclerAdapter(val context: Context, var mEarthQakeData: ArrayList<EarthQuake>) :
    RecyclerView.Adapter<recyclerAdapter.mViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.earthqake_card, parent, false)
        return mViewHolder(view)
    }


    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        if (mEarthQakeData.size != 0) {
            val earthQuake: EarthQuake = mEarthQakeData.get(position)
            val magnitude = earthQuake.mag
            val place = earthQuake.place
            val time = earthQuake.time
            val longitude = earthQuake.longitude
            val latitude = earthQuake.latitude
            val depth = earthQuake.depth

            //convert long time to user readble time
            val timestamp = time // Replace this with your timestamp
            val date = Date(timestamp)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(date)
            val formattedTime = timeFormat.format(date)


            holder.place_tv.text = place
            holder.magnitude_tv.text = magnitude.toString()
            holder.time_tv.text = "Date and Time: \n $formattedDate \n $formattedTime"
        }
    }

    override fun getItemCount(): Int {
        if (mEarthQakeData.size != 0) {
            return mEarthQakeData.size
        } else {
            return 0
        }
    }


    fun setData(mData: ArrayList<EarthQuake>) {
        this.mEarthQakeData = mData
        notifyDataSetChanged()
    }


    class mViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var place_tv: TextView
        lateinit var magnitude_tv: TextView
        lateinit var time_tv: TextView
        lateinit var depth_tv: TextView

        init {
            place_tv = itemView.findViewById(R.id.earthQake_card_tv_place)
            magnitude_tv = itemView.findViewById(R.id.earthQake_card_tv_magnitude)
            time_tv = itemView.findViewById(R.id.earthQake_card_tv_time)

        }
    }
}