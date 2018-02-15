package com.think360.alexbloodbank

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import com.think360.alexbloodbank.api.AppController


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_activity)
        if(AppController.getSharedPref().getBoolean("is_login",false)){
            Handler().postDelayed(
                    {
                        startActivity(Intent(applicationContext,BloodCenterActivity::class.java))
                        finish()
                    }, 3000)
        }else{
            Handler().postDelayed(
                    {
                        startActivity(Intent(applicationContext, LoginRegTabActivity::class.java))
                        finish()
                    }, 3000)
        }

    }
}
