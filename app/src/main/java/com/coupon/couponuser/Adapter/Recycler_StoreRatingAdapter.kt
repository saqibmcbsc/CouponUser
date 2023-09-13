package com.couponusers.couponuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.couponusers.couponuser.ModelClass.Rating_Model
import com.couponusers.couponuser.Utils.ApiHelper
import com.example.couponuser.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Recycler_StoreRatingAdapter(var list:ArrayList<Rating_Model>, var context:Context):RecyclerView.Adapter<Recycler_StoreRatingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context)
           .inflate(R.layout.recycler_from_vendor_review,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(ApiHelper.imageUrl + list[position].image).into(holder.review_userImage)
        holder.text_userName.text = list.get(position).name
        holder.text_reviewRatingNo.text = list.get(position).rating
        val ratings = holder.text_reviewRatingNo.text.toString()
        try {
            if (!ratings.isEmpty()) {
                val ratingInt = ratings.toIntOrNull()
                if (ratingInt != null) {
                    holder.ratingBar.rating = ratingInt.toFloat()
                } else {

                }
            } else {

            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
        var review_userImage:CircleImageView = itemView.findViewById(R.id.review_userImage)
        var text_userName:TextView = itemView.findViewById(R.id.text_userName)
        var text_reviewRatingNo:TextView = itemView.findViewById(R.id.text_reviewRatingNo)
        var ratingBar:RatingBar = itemView.findViewById(R.id.ratingBar)

    }
}