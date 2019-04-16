package com.wordpress.louieefitness.whattoeat2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.wordpress.louieefitness.whattoeat2.database.DataBaseHelper
import com.wordpress.louieefitness.whattoeat2.foodObject.Food
import com.wordpress.louieefitness.whattoeat2.database.CircleTransform
import java.io.File

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("StaticFieldLeak")
    private inner class Decider:AsyncTask<Void, Void, ArrayList<Food>>(){
        override fun doInBackground(vararg params: Void?): ArrayList<Food> {
            return getFood()
        }
        override fun onPostExecute(result: ArrayList<Food>?) {
            super.onPostExecute(result)
            if (result!!.size == 0) {
                Toast.makeText(this@MainActivity,"No Food in Database, Please Add Food", Toast.LENGTH_SHORT).show()
            } else {
                val num = (Math.random() * result.size).toInt()
                val chosenFood = result[num]
                name!!.text = chosenFood.name
                if (chosenFood.desc.isNotEmpty()){
                    desc!!.visibility = View.VISIBLE
                }else{
                    desc!!.visibility = View.GONE
                }
                desc!!.text = chosenFood.desc

                if (chosenFood.image.isNotEmpty()) {
                    pic!!.setImageBitmap(BitmapFactory.decodeFile(chosenFood.image))
                }else{
                    //set pic to default
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDrawerLayout = findViewById(R.id.Foodguesser)
        name = findViewById(R.id.name)
        desc = findViewById(R.id.desc)
        pic = findViewById(R.id.food_pic)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_home_yellow_900_24dp)
        mToggle = ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close)
        mDrawerLayout!!.addDrawerListener(mToggle!!)
        mToggle!!.syncState()
        val navigationView = findViewById<NavigationView>(R.id.Nav_view4)
        navigationView.setNavigationItemSelectedListener(this)
        setSupportActionBar(toolbar)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return mToggle!!.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)

    }

    override fun onBackPressed() {
        if (exit!!) {
            finish()
        } else {
            Toast.makeText(applicationContext, "Press Back Again to Exit", Toast.LENGTH_SHORT).show()
            exit = true
            Handler().postDelayed({ exit = false }, (3 * 1000).toLong())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit -> {
                Toast.makeText(this@MainActivity, "Exiting App", Toast.LENGTH_SHORT).show()
                finish()
            }
            R.id.food_list -> {
                val foodList = Intent(this@MainActivity, FoodList::class.java)
                startActivity(foodList)
            }
            R.id.add_food -> {
                val add = Intent(this@MainActivity, AddFood::class.java)
                startActivity(add)
            }

        }
        mDrawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }


    private fun getFood():ArrayList<Food>{
        val database = DataBaseHelper(this)
        var myFood: Food
        foodList.clear()
        val c = database.getAllFood
        if (c.count > 0) {
            while (c.moveToNext()) {
                val id = c.getString(c.getColumnIndex(DataBaseHelper.ID))
                val name = c.getString(c.getColumnIndex(DataBaseHelper.NAME))
                val desc = c.getString(c.getColumnIndex(DataBaseHelper.DESC))
                val image = c.getString(c.getColumnIndex(DataBaseHelper.IMAGE))
                myFood = Food()
                myFood.desc = desc
                myFood.id = id
                myFood.image = image
                myFood.name = name
                foodList.add(myFood)
            }
        }
    return foodList
}


    fun decide(v: View){
        this.Decider().execute()
    }
    companion object {
        private var foodList = arrayListOf<Food>()
        private var mToggle: ActionBarDrawerToggle? = null
        @SuppressLint("StaticFieldLeak")
        private var mDrawerLayout:DrawerLayout?=null
        private var exit: Boolean? = false
        @SuppressLint("StaticFieldLeak")
        private var name:TextView? = null
        @SuppressLint("StaticFieldLeak")
        private var desc:TextView? =null
        @SuppressLint("StaticFieldLeak")
        private var pic:ImageView? = null

    }
}
