package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.RedeemNow_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class RedeemCoupon_Adaptor(var list: ArrayList<RedeemNow_DataModel>, var context: Context):
    RecyclerView.Adapter<RedeemCoupon_Adaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.reeddem_coupon_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(ApiHelper.imageUrl+list[position].image).into(holder.img_selectCoupon)
        holder.text_parcent_selectCoupon.text = list.get(position).coupon_discount
        holder.text_validDate.text = list.get(position).valid_date
        holder.text_brandName_selectCoupon.text = list.get(position).brand_name
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){

        var img_selectCoupon: CircleImageView = itemView.findViewById(R.id.img_selectCoupon)
//        var img_savedUnclick: ImageView = itemView.findViewById(R.id.img_savedUnclick)
//        var img_savedClick: ImageView = itemView.findViewById(R.id.img_savedClick)
        var text_parcent_selectCoupon: TextView = itemView.findViewById(R.id.text_parcent_selectCoupon)
        var text_validDate: TextView = itemView.findViewById(R.id.text_validDate)
        var text_redemNow: TextView = itemView.findViewById(R.id.text_redemNow)
        var text_brandName_selectCoupon: TextView = itemView.findViewById(R.id.text_brandName_selectCoupon)
        var card_selectCoupon: CardView = itemView.findViewById(R.id.card_selectCoupon)

    }

}