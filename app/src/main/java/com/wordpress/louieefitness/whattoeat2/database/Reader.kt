package com.wordpress.louieefitness.whattoeat2.database

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import com.wordpress.louieefitness.whattoeat2.foodObject.Food
import com.wordpress.louieefitness.whattoeat2.foodUI.Adapter


class Reader(@field:SuppressLint("StaticFieldLeak")
                      private val con: Context, @field:SuppressLint("StaticFieldLeak")
                      private val rv: RecyclerView?) : AsyncTask<Void, Void, Int>() {
    private val foodList= arrayListOf<Food>()

    override fun doInBackground(vararg voids: Void): Int? {
        return this.readData()
    }

    override fun onPostExecute(result: Int?) {
        super.onPostExecute(result)
        if (result == 0) {

        } else {
            val customAdapter = Adapter(foodList, con)
            rv?.adapter = customAdapter
        }

    }

    private fun readData(): Int {
        val database = DataBaseHelper(con)
        var databaseList: Food
        foodList.clear()
        val c = database.getAllFood
        if (c.count > 0) {
            while (c.moveToNext()) {
                val id = c.getString(c.getColumnIndex(DataBaseHelper.ID))
                val name = c.getString(c.getColumnIndex(DataBaseHelper.NAME))
                val desc = c.getString(c.getColumnIndex(DataBaseHelper.DESC))
                val image = c.getString(c.getColumnIndex(DataBaseHelper.IMAGE))
                databaseList = Food()
                databaseList.desc = desc
                databaseList.id = id
                databaseList.image = image
                databaseList.name = name
                foodList.add(databaseList)
            }

        }
        val count = foodList.size
        return if (count > 0) { 1 } else { 0 }
    }
}
