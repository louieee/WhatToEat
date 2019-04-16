package com.wordpress.louieefitness.whattoeat2.foodUI

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wordpress.louieefitness.whattoeat2.R

class FoodHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    override fun onClick(v: View?) {

    }

    internal var name: TextView
    internal var desc: TextView
    internal var img: ImageView

    init {
        itemView.setOnClickListener(this)
        name = itemView.findViewById(R.id.Food_Item)
        img = itemView.findViewById(R.id.Food_Image)
        desc = itemView.findViewById(R.id.Food_Description)
    }



}
