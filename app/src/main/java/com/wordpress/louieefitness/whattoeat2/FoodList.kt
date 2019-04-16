package com.wordpress.louieefitness.whattoeat2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.wordpress.louieefitness.whattoeat2.database.DataBaseHelper
import com.wordpress.louieefitness.whattoeat2.database.Reader
import com.wordpress.louieefitness.whattoeat2.foodUI.Adapter

class FoodList : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager:RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        recyclerView = findViewById(R.id.recycler)
        viewManager = LinearLayoutManager(this,LinearLayout.VERTICAL, false)
        recyclerView.layoutManager = viewManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_home_yellow_900_24dp)
        mDrawerLayout = findViewById(R.id.list)
        mToggle = ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close)
        mDrawerLayout!!.addDrawerListener(mToggle!!)
        mToggle!!.syncState()
        val navigationView = findViewById<NavigationView>(R.id.Nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        setSupportActionBar(toolbar)
        val reader = Reader(this@FoodList, recyclerView)
        reader.execute()
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val adapter = recyclerView.adapter as Adapter
                val database = DataBaseHelper(applicationContext)
                val food = adapter.foodDetails(position)
                adapter.remove(position)
                
                val id = food[0]
                database.deleteFood(id)
                val snack = Snackbar.make(recyclerView,food[3] + " removed from list!",
                        Snackbar.LENGTH_LONG)

                snack.setAction("UNDO") {
                    adapter.restore(position, food)
                    database.insertData(food[1],food[3],food[2])
                    Reader(applicationContext, recyclerView).execute()
                    Toast.makeText(applicationContext,food[3] +"has been restored",Toast.LENGTH_SHORT).show()
                }
                snack.setActionTextColor(Color.YELLOW)
                snack.show()
                Reader(applicationContext, recyclerView).execute()
            }
        }).attachToRecyclerView(recyclerView)
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
                Toast.makeText(applicationContext, "Exiting App", Toast.LENGTH_SHORT).show()
                finish()
            }
            R.id.home -> {
                val home = Intent(application, MainActivity::class.java)
                startActivity(home)
            }
        }
        mDrawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }
    companion object{
        @SuppressLint("StaticFieldLeak")
        private var mDrawerLayout:DrawerLayout? = null
        private var mToggle: ActionBarDrawerToggle? = null
        private var exit: Boolean? = false


    }


}
