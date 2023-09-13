package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.ProductDetails_Model
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso

class Recycler_ProductAdapter(var list:ArrayList<ProductDetails_Model>, var context: Context): RecyclerView.Adapter<Recycler_ProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_product_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(ApiHelper.imageUrl+list[position].image).into(holder.img_product)
        holder.text_brand_name.text = list.get(position).product_name
        holder.text_productDes.text = list.get(position).description
        holder.text_productPrice.text = list.get(position).discount_price
        holder.text_productPriceDepricate.text = list.get(position).price
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView:View):RecyclerView.ViewHolder(ItemView){
       var img_product:ImageView = itemView.findViewById(R.id.img_product)
       var text_brand_name: TextView = itemView.findViewById(R.id.text_brand_name)
       var text_productDes: TextView = itemView.findViewById(R.id.text_productDes)
       var text_productPrice: TextView = itemView.findViewById(R.id.text_productPrice)
       var text_productPriceDepricate: TextView = itemView.findViewById(R.id.text_productPriceDepricate)

    }

}