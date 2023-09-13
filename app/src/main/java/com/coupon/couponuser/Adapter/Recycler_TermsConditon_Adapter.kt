package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.TermsCondition_DataModel
import com.example.couponuser.R

class Recycler_TermsConditon_Adapter(var list: ArrayList<TermsCondition_DataModel>, var context: Context):
    RecyclerView.Adapter<Recycler_TermsConditon_Adapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_termscondition_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text_title_termsCondition.text = list[position].title
        holder.text_description.text = list[position].description
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){

        var text_title_termsCondition: TextView = itemView.findViewById(R.id.text_title_termsCondition)
        var text_description: TextView = itemView.findViewById(R.id.text_description)
    }
}