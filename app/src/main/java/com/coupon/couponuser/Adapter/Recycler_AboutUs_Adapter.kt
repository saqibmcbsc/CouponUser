package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.AboutUs_DataModel
import com.example.couponuser.R

class Recycler_AboutUs_Adapter(var list: ArrayList<AboutUs_DataModel>, var context: Context):
    RecyclerView.Adapter<Recycler_AboutUs_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_aboutus_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text_title_aboutUs.text = list.get(position).title
        holder.text_description_aboutUs.text = list.get(position).description
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        var text_title_aboutUs: TextView = itemView.findViewById(R.id.text_title_aboutUs)
        var text_description_aboutUs: TextView = itemView.findViewById(R.id.text_description_aboutUs)

    }
}