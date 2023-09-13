package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.BrandCoupons_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Recycler_BrandCoupon_Adapter(var list: ArrayList<BrandCoupons_DataModel>, var context: Context, var brandCouponDash:BrandCouponDash):RecyclerView.Adapter<Recycler_BrandCoupon_Adapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context)
           .inflate(R.layout.recycler_brandcoupon_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(ApiHelper.imageUrl+list[position].image).into(holder.img_brandCoupon)

        holder.img_brandCoupon.setOnClickListener(View.OnClickListener {
            brandCouponDash.imgBrandCouponClick(position)
        })
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
      var img_brandCoupon:CircleImageView = itemView.findViewById(R.id.img_brandCoupon)
    }

    interface BrandCouponDash{
        fun imgBrandCouponClick(position: Int)
    }
}