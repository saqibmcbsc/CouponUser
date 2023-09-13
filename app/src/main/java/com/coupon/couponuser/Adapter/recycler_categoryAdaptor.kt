package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.Category_DataModel
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class recycler_categoryAdaptor(var list:ArrayList<Category_DataModel>, var context: Context, var intrestClickCategory: IntrestClickCategory):RecyclerView.Adapter<recycler_categoryAdaptor.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context)
           .inflate(R.layout.recycler_category_layout,parent,false)

        return ViewHolder(view)

    }

    fun setFilterList(list: ArrayList<Category_DataModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(ApiHelper.imageUrl+list.get(position).image).into(holder.img_category)
      // holder.img_category.setImageResource(list.get(position).catImage)
        holder.text_cetogory.text = list.get(position).category

        holder.const_category.setOnClickListener(View.OnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                intrestClickCategory.onClick(position)
            }

        })

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
     var const_category:ConstraintLayout = itemView.findViewById(R.id.const_category)
     var img_category:ImageView = itemView.findViewById(R.id.img_category)
     var text_cetogory:TextView = itemView.findViewById(R.id.text_cetogory)

    }

    interface IntrestClickCategory {
        fun onClick(position: Int)
    }
}