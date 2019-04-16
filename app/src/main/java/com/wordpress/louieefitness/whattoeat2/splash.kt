package com.wordpress.louieefitness.whattoeat2
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val appIcon = this.findViewById<ImageView>(R.id.iconDisplay)
        val appName = findViewById<TextView>(R.id.iconName)
        val fadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        appIcon.startAnimation(fadeIn)
        appName.startAnimation(fadeIn)
        val splashTimeOut = 7000
        Handler().postDelayed({
            Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_SHORT).show()
            val start = Intent(this@Splash, MainActivity::class.java)
            startActivity(start)
            finish()
        }, splashTimeOut.toLong())
    }
}
