package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.CouponInDetails_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Recomended_CouponAdapter(var list: ArrayList<CouponInDetails_DataModel>, var context: Context, var clickRecomended:ClickRecomended):
    RecyclerView.Adapter<Recomended_CouponAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_tranding_coupon_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(ApiHelper.imageUrl+list[position].image).into(holder.img_brandCoupon)
        holder.text_parcents.text = list.get(position).offer_discount
        holder.text_store.text = list.get(position).brand_name
        holder.text_validUp.text = list.get(position).validate_upto

        holder.const_trandingCoupon.setOnClickListener(View.OnClickListener {
            clickRecomended.recomendCoupon(position)
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
        var img_brandCoupon: CircleImageView = itemView.findViewById(R.id.img_brandCoupon)
        var text_parcents: TextView = itemView.findViewById(R.id.text_parcents)
        var text_offs: TextView = itemView.findViewById(R.id.text_offs)
        var text_store: TextView = itemView.findViewById(R.id.text_store)
        var text_validUp: TextView = itemView.findViewById(R.id.text_validUp)
        var const_trandingCoupon: ConstraintLayout = itemView.findViewById(R.id.const_trandingCoupon)
    }

    interface ClickRecomended{
        fun recomendCoupon(position: Int)
    }
}