package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.AllInterest_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso

class Recycler_StaggerdAdapter(var list: ArrayList<AllInterest_DataModel>, var context: Context, var intrestClick: IntrestClick
) : RecyclerView.Adapter<Recycler_StaggerdAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_staggerd_layout, parent, false)
        return ViewHolder(view)
    }

    fun setFilterList(list: ArrayList<AllInterest_DataModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.checkInterest.isChecked = list[position].isChecked

        Picasso.get().load(ApiHelper.imageUrl+list.get(position).image).into(holder.img_interest)
        holder.text_interest.text = list[position].category

        holder.card_container.setOnClickListener {
            intrestClick.onClick(position)
        }

        holder.checkInterest.setOnClickListener(View.OnClickListener {
            intrestClick.onClickCheck(position)
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        var card_container: CardView = itemView.findViewById(R.id.card_container)
        var text_interest: TextView = itemView.findViewById(R.id.text_interest)
        var img_interest: ImageView = itemView.findViewById(R.id.img_interest)
        var checkInterest: CheckBox = itemView.findViewById(R.id.checkInterest)
    }

    interface IntrestClick {
        fun onClick(position: Int)
        fun onClickCheck(position: Int)
    }

}