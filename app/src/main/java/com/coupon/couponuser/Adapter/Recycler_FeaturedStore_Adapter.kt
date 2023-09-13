package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.FeaturedStore_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.google.android.gms.common.api.Api
import com.squareup.picasso.Picasso

class Recycler_FeaturedStore_Adapter(var list:ArrayList<FeaturedStore_DataModel>, var context: Context, var storeIntreface:StoreIntreface): RecyclerView.Adapter<Recycler_FeaturedStore_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_featured_store_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(ApiHelper.imageUrl+list[position].image).into(holder.img_featureStore)
        holder.const_featuredStore.setOnClickListener(View.OnClickListener {
            storeIntreface.storeClick(position)
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
        var const_featuredStore: ConstraintLayout = itemView.findViewById(R.id.const_featuredStore)
        var img_featureStore: ImageView = itemView.findViewById(R.id.img_featureStore)
    }

    interface StoreIntreface{
        fun storeClick(position: Int)
    }
}