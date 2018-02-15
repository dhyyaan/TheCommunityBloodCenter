package com.think360.alexbloodbank

import android.content.Intent
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.think360.alexbloodbank.fragments.*
import kotlinx.android.synthetic.main.blood_center_activity.*
import kotlinx.android.synthetic.main.app_bar_blood_center.*
import kotlinx.android.synthetic.main.content_blood_center.*
import java.util.ArrayList
import android.graphics.drawable.LayerDrawable
import android.os.Handler
import android.support.v4.widget.DrawerLayout
import com.think360.alexbloodbank.utils.Utils2
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


import com.think360.alexbloodbank.api.AppController
import com.think360.alexbloodbank.api.EventToRefresh
import com.think360.alexbloodbank.api.data.Responce
import com.think360.alexbloodbank.api.interfaces.ApiService
import com.think360.alexbloodbank.fragments.base.BaseActivity

import kotlinx.android.synthetic.main.nav_header_blood_center.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
class BloodCenterActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    @Inject
    internal lateinit var apiService: ApiService
   private lateinit var  drawerToggle: ActionBarDrawerToggle
    var  select = true
    private val compositeDisposable = CompositeDisposable()
    companion object {

       var bloodCenterActivity : BloodCenterActivity? = null

    }
   lateinit var icon : LayerDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ( this.applicationContext as AppController).getComponent().inject(this@BloodCenterActivity)
        compositeDisposable.add(( this.applicationContext as AppController).bus().toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { o ->

           if (o is EventToRefresh && o.body == 1000 ) {
               showProfile()
            }else if (o is EventToRefresh && o.body == 1002 ) {
               changeAward()
           }
        })
        setContentView(R.layout.blood_center_activity)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        bloodCenterActivity = this
        toolbar.setTitle("The Community Blood Center")

        drawerToggle = object : ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)
                isDrawerIndicatorEnabled = true
            }

            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
            }
        }

        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()


        nav_view.setNavigationItemSelectedListener(this)

        add(MainFragment.newInstance())

        showProfile()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.blood_center, menu)
        // Get the notifications MenuItem and LayerDrawable (layer-list)
        val item = menu.findItem(R.id.action_notifications)
         icon = item.icon as LayerDrawable

        // Update LayerDrawable's BadgeDrawable
        Utils2.setBadgeCount(this, icon, 0)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_notifications ->{
                if(select) {
                    add(NotificationFragment.newInstance())
                    select= false
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_edit_profile -> {

                toolbar.setTitle("Edit Profile")
                add(EditProfileFragment.newInstance())

            }
            R.id.nav_need_help -> {
                toolbar.setTitle("Need Help")
                add(NeedHelpFragment.newInstance())
            }
            R.id.nav_change_pwd -> {
                toolbar.setTitle("Change Password")
                add(ChangePasswordFragment.newInstance())

            }
            R.id.nav_logout -> {
                AppController.getSharedPref().edit().putBoolean("is_login",false).apply()

                            startActivity(Intent(applicationContext,SplashScreenActivity::class.java))
                            finish()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun getDrawer(): DrawerLayout {
        return drawer_layout
    }

    override fun getDrawerToggle(): ActionBarDrawerToggle {
        return drawerToggle
    }

    fun setTitle(title : String){
        toolbar.setTitle(title)
    }

   override  fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
       Log.d("","")
        when (intent.getIntExtra("TO_OPEN", 0)) {
            R.id.action_notifications -> {
                add(NotificationFragment.newInstance())
            }

        }

    }
   private fun showProfile() {

        apiService.showProfile(AppController.getSharedPref().getString("user_id","")).enqueue(object : Callback<Responce.ShowProfileResponce> {
            override fun onResponse(call: Call<Responce.ShowProfileResponce>, response: Response<Responce.ShowProfileResponce>) {
                if(response.body().getStatus()==1){


                    tvUserName.setText(response.body().getData().getFirst_name()+" "+response.body().getData().getLast_name())
                    tvEmail.setText(response.body().getData().getUseremail())
           /*       Picasso.with(applicationContext)
                            .load(response.body().getData().getImage())
                          .resize(120, 120)
                          .error(R.drawable.no_img)
                            .into(ivProfile)*/

              Glide.with(applicationContext)
     .load(response.body().getData().getImage())
     .apply( RequestOptions()
             .override(120,120)
                .placeholder(R.drawable.no_img)
                .centerCrop()
                .dontAnimate()
                .dontTransform())
                .into(ivProfile)
                }
            }
            override fun onFailure(call: Call<Responce.ShowProfileResponce>, t: Throwable) {
                Toast.makeText(applicationContext,""+t, Toast.LENGTH_SHORT).show()
            }
        })

    }
    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
   fun changeAward(){
    if(AppController.getSharedPref().getInt("getAward",0)==1){

        ffAchivedAward .setBackgroundColor(resources.getColor(R.color.award_color_sel))
        tvAchivedAward  .setCompoundDrawablesWithIntrinsicBounds( R.drawable.not_awad2, 0, 0, 0);
        tvAchivedAward.setTextColor(resources.getColor(R.color.award_color_white))
        tvAchivedAward.setText("ACHIEVED GALLON GARD AWARD")
    }else{
        AppController.getSharedPref().edit().putInt("getAward",0)
        ffAchivedAward .setBackgroundColor(resources.getColor(R.color.award_color_unsel))
        tvAchivedAward  .setCompoundDrawablesWithIntrinsicBounds( R.drawable.not_awad, 0, 0, 0);
        tvAchivedAward.setTextColor(resources.getColor(R.color.award_color_sel))
        tvAchivedAward.setText("NOT ACHIEVED GALLON GARD AWARD")
    }
}
}
