package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.AllFeaturedStore_DataModel
import com.couponusers.couponuser.ModelClass.AllInterest_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Recycler_AllStored_Adapter(var list:ArrayList<AllFeaturedStore_DataModel>, var context: Context, var storeInter:StoreInter):RecyclerView.Adapter<Recycler_AllStored_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_viewall_store_layout,parent,false)

        return ViewHolder(view)
    }

    fun setFilterList(list: ArrayList<AllFeaturedStore_DataModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder.img_store.setImageResource(list.get(position).storeImage)
        Picasso.get().load(ApiHelper.imageUrl+list[position].image).into(holder.img_store)
        holder.couponNumber.text = list.get(position).coupon
        holder.text_rating.text = list.get(position).ratings
        holder.text_storeName.text = list.get(position).store_name
        holder.text_storeSummery.text = list.get(position).business_details

        holder.card_store.setOnClickListener(View.OnClickListener {
            storeInter.storeClick(position)
        })
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){

        var img_store:CircleImageView = itemView.findViewById(R.id.img_store)
        var couponNumber:TextView = itemView.findViewById(R.id.couponNumber)
        var text_rating:TextView = itemView.findViewById(R.id.text_rating)
        var text_storeName:TextView = itemView.findViewById(R.id.text_storeName)
        var text_storeSummery:TextView = itemView.findViewById(R.id.text_storeSummery)
        var card_store:CardView = itemView.findViewById(R.id.card_store)

    }

    interface StoreInter{
        fun storeClick(position: Int)
    }
}