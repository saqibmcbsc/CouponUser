package com.couponusers.couponuser.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.coupon.couponuser.ModelClass.Filter_Category_DataModel
import com.couponusers.couponuser.ModelClass.Filter_Category_Model
import com.example.couponuser.R

class Recycler_SubCategory_Filter(var list: ArrayList<Filter_Category_DataModel>, var context: Context,var subCatClick:SubCatClick):
    RecyclerView.Adapter<Recycler_SubCategory_Filter.ViewHolder>() {

    var clickable = BooleanArray(list.size) { false }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_category_filter,parent,false)

        return ViewHolder(view)
    }

    fun setFilterList(list: ArrayList<Filter_Category_DataModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text_filterCat.text = list.get(position).sub_category
        holder.const_filterCat.setOnClickListener(View.OnClickListener {
            if (clickable[position]) {
                holder.const_filterCat.setBackgroundResource(R.drawable.filter_cat_bg_orange)
                holder.text_filterCat.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                holder.const_filterCat.setBackgroundResource(R.drawable.filter_cat_bg)
                holder.text_filterCat.setTextColor(Color.parseColor("#757575"))
            }


            holder.const_filterCat.setOnClickListener(View.OnClickListener {
                // Toggle the 'clickable' state
                clickable[position] = !clickable[position]

                if (clickable[position]) {
                    holder.const_filterCat.setBackgroundResource(R.drawable.filter_cat_bg_orange)
                    holder.text_filterCat.setTextColor(Color.parseColor("#FFFFFF"))
                } else {
                    holder.const_filterCat.setBackgroundResource(R.drawable.filter_cat_bg)
                    holder.text_filterCat.setTextColor(Color.parseColor("#757575"))
                }

                subCatClick.subCatClick(position)
            })
        })

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){

        var const_filterCat: ConstraintLayout = itemView.findViewById(R.id.const_filterCat)
        var text_filterCat: TextView = itemView.findViewById(R.id.text_filterCat)

    }

    interface SubCatClick{
        fun subCatClick(position: Int)
    }
}