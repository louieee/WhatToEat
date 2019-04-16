package com.wordpress.louieefitness.whattoeat2.foodUI
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wordpress.louieefitness.whattoeat2.R
import com.wordpress.louieefitness.whattoeat2.foodObject.Food
import java.util.ArrayList
class Adapter(private val foodList: ArrayList<Food>, private val C: Context) : RecyclerView.Adapter<FoodHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.model, parent, false)
        return FoodHolder(v)
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        val myFood = foodList[position]
        holder.name.text = myFood.name
        if (myFood.image.isNotEmpty()) {
            holder.img.setImageBitmap(BitmapFactory.decodeFile(myFood.image))
        }
        holder.desc.text = myFood.desc

        /**holder.options.setOnClickListener {
            val popup = PopupMenu(C, holder.options)
            popup.inflate(R.menu.option_menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.delete ->
                        true
                    R.id.update ->
                        true
                    else -> false
                }
            }
            popup.show()
        }
        **/

    }



    override fun getItemCount(): Int {
        return foodList.size
    }

    fun remove(position: Int) {
        val item = foodList[position]
        if (foodList.contains(item)) {
            foodList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    fun restore(position:Int, food:Array<String>){
        val myFood= Food()
        myFood.name = food[3]
        myFood.desc = food[2]
        myFood.image = food[1]
        myFood.id = food[0]
        foodList.add(position, myFood)
        notifyItemInserted(position)
    }

    fun foodDetails(position:Int):Array<String>{
        val item = foodList[position]
        return arrayOf(item.id, item.image, item.desc, item.name)
    }

}
