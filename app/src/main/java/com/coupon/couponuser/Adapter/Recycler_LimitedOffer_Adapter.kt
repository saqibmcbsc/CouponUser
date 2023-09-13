package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.LimitedOffer_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.google.android.gms.common.api.Api
import com.squareup.picasso.Picasso

class Recycler_LimitedOffer_Adapter(var list:ArrayList<LimitedOffer_DataModel>, var context: Context, var clickLimitedOffer: ClickLimitedOffer): RecyclerView.Adapter<Recycler_LimitedOffer_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_limited_offer_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(ApiHelper.imageUrl+list[position].image).into(holder.img_limitedOffer)
       holder.text_valid.text = list.get(position).validate_upto
        holder.text_brandName.text = list.get(position).brand_name
        holder.text_disSummery.text = list.get(position).offer_subtitle
        holder.text_parcentOff.text = list.get(position).offer_discount

        holder.const_limitedOffers.setOnClickListener(View.OnClickListener {
            clickLimitedOffer.onClic(position)
        })
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){

        var text_valid:TextView = itemView.findViewById(R.id.text_valid)
        var img_limitedOffer:ImageView = itemView.findViewById(R.id.img_limitedOffer)
        var text_brandName:TextView = itemView.findViewById(R.id.text_brandName)
        var text_disSummery:TextView = itemView.findViewById(R.id.text_disSummery)
        var text_parcentOff:TextView = itemView.findViewById(R.id.text_parcentOff)
        var const_limitedOffers:ConstraintLayout = itemView.findViewById(R.id.const_limitedOffers)

    }

    interface ClickLimitedOffer{
        fun onClic(position: Int)
    }
}