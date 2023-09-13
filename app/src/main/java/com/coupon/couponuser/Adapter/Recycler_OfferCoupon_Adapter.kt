package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.SelectCoupon_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Recycler_OfferCoupon_Adapter(var list: ArrayList<SelectCoupon_DataModel>, var context: Context, var clickSelectCoupon:ClickSelectCoupon):
    RecyclerView.Adapter<Recycler_OfferCoupon_Adapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_select_coupon_layout,parent,false)

        return ViewHolder(view)
    }

    fun setFilterList(list: ArrayList<SelectCoupon_DataModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(ApiHelper.imageUrl+list[position].image).into(holder.img_selectCoupon)
        holder.text_parcent_selectCoupon.text = list.get(position).offer_discount
        holder.text_validDate.text = list.get(position).validate_upto
        holder.text_brandName_selectCoupon.text = list.get(position).brand_name

        if (list[position].saved_coupon == "unsaved"){
            holder.img_savedClick.visibility = View.GONE
            holder.img_savedUnclick.visibility = View.VISIBLE
        }else{
            holder.img_savedClick.visibility = View.VISIBLE
            holder.img_savedUnclick.visibility = View.GONE
        }

        holder.img_savedUnclick.setOnClickListener(View.OnClickListener {
            holder.img_savedClick.visibility = View.VISIBLE
            holder.img_savedUnclick.visibility = View.GONE
            clickSelectCoupon.onClick2(position)
        })
        holder.img_savedClick.setOnClickListener(View.OnClickListener {
            holder.img_savedClick.visibility = View.GONE
            holder.img_savedUnclick.visibility = View.VISIBLE
            clickSelectCoupon.onClick3(position)
        })

        holder.card_selectCoupon.setOnClickListener(View.OnClickListener {
            clickSelectCoupon.onClick(position)
        })

    }


    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){

        var img_selectCoupon: CircleImageView = itemView.findViewById(R.id.img_selectCoupon)
        var img_savedUnclick: ImageView = itemView.findViewById(R.id.img_savedUnclick)
        var img_savedClick: ImageView = itemView.findViewById(R.id.img_savedClick)
        var text_parcent_selectCoupon: TextView = itemView.findViewById(R.id.text_parcent_selectCoupon)
        var text_validDate: TextView = itemView.findViewById(R.id.text_validDate)
        var text_redemNow: TextView = itemView.findViewById(R.id.text_redemNow)
        var text_brandName_selectCoupon: TextView = itemView.findViewById(R.id.text_brandName_selectCoupon)
        var card_selectCoupon: CardView = itemView.findViewById(R.id.card_selectCoupon)

    }

    interface ClickSelectCoupon {
        fun onClick(position: Int)
        fun onClick2(position: Int)
        fun onClick3(position: Int)
    }
}