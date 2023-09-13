package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.BrandCoupons_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso

class Recycler_AllBrand_Adapter(var list:ArrayList<BrandCoupons_DataModel>, var context: Context, var brandCoupon:BrandCoupon): RecyclerView.Adapter<Recycler_AllBrand_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_allbrand_layout,parent,false)

        return ViewHolder(view)

    }

    fun setFilterList(list: ArrayList<BrandCoupons_DataModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(ApiHelper.imageUrl+list.get(position).image).into(holder.img_brands)
        holder.text_brands.text = list.get(position).brand_name

        holder.card_brandCoupon.setOnClickListener(View.OnClickListener {
            brandCoupon.brandCouponClick(position)
        })


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
        var img_brands: ImageView = itemView.findViewById(R.id.img_brands)
        var text_brands: TextView = itemView.findViewById(R.id.text_brands)
        var card_brandCoupon: CardView = itemView.findViewById(R.id.card_brandCoupon)

    }

    interface BrandCoupon{
        fun brandCouponClick(position: Int)
    }
}