package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.coupon.couponuser.ModelClass.DailyCoupon_DataModel
import com.couponusers.couponuser.ModelClass.TrandingCoupon_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso
import kotlin.math.PI

class Recycler_DailyCoupon_Adapter(var list: ArrayList<DailyCoupon_DataModel>, var context: Context, var dailyCoupon:DailyCoupon): RecyclerView.Adapter<Recycler_DailyCoupon_Adapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_daily_coupon_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(ApiHelper.imageUrl+list[position].image).into(  holder.img_brandCoupon_daily)
        holder.text_parcents_daily.text = list.get(position).offer_discount
        holder.text_store_daily.text = list.get(position).brand_name
        holder.text_validUp_daily.text = list.get(position).validate_upto

        holder.const_dailyCoupon.setOnClickListener(View.OnClickListener {
            dailyCoupon.dailyClick(position)

        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
        var img_brandCoupon_daily: ImageView = itemView.findViewById(R.id.img_brandCoupon_daily)
        var text_parcents_daily: TextView = itemView.findViewById(R.id.text_parcents_daily)
        var text_offs_daily: TextView = itemView.findViewById(R.id.text_offs_daily)
        var text_store_daily: TextView = itemView.findViewById(R.id.text_store_daily)
        var text_validUp_daily: TextView = itemView.findViewById(R.id.text_validUp_daily)
        var const_dailyCoupon: ConstraintLayout = itemView.findViewById(R.id.const_dailyCoupon)
    }

    interface DailyCoupon{
        fun dailyClick(position: Int)
    }

}
