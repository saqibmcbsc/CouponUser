package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.coupon.couponuser.ModelClass.GenralNotification_DataModel
import com.couponusers.couponuser.ModelClass.GenralNotificationModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso

class RecyclerGenral_NotificationAdapter(var list: ArrayList<GenralNotification_DataModel>, var context:Context):RecyclerView.Adapter<RecyclerGenral_NotificationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.recycler_genral_notification_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     // holder.img_notification.setImageResource(list.get(position).image)
        Picasso.get().load(ApiHelper.imageUrl+list[position].image).into(holder.img_notification)
        holder.text_nameNotification.text = list.get(position).title
        holder.text_summeryNotification.text = list.get(position).message
        holder.text_timeNotification.text = list.get(position).time
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
       var img_notification = itemView.findViewById<ImageView>(R.id.img_notification)
       var text_nameNotification = itemView.findViewById<TextView>(R.id.text_nameNotification)
       var text_summeryNotification = itemView.findViewById<TextView>(R.id.text_summeryNotification)
       var text_timeNotification = itemView.findViewById<TextView>(R.id.text_timeNotification)
    }

}