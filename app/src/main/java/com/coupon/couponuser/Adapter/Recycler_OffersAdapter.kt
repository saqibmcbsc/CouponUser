package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.BestOffer_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso


class Recycler_OffersAdapter(var list:ArrayList<BestOffer_DataModel>, var context: Context, var bestOffer:BestOffer):RecyclerView.Adapter<Recycler_OffersAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_offers_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(ApiHelper.imageUrl+list[position].image).into(holder.img_offers)

        holder.const_offers.setOnClickListener(View.OnClickListener {
            bestOffer.clickOffer(position)
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
      var const_offers:ConstraintLayout = itemView.findViewById(R.id.const_offers)
      var img_offers:ImageView = itemView.findViewById(R.id.img_offers)
    }

    interface BestOffer{
        fun clickOffer(position: Int)
    }

}