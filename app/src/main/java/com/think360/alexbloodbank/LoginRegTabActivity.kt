package com.think360.alexbloodbank

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.think360.alexbloodbank.api.AppController
import com.think360.alexbloodbank.api.data.Responce
import com.think360.alexbloodbank.api.interfaces.ApiService
import com.think360.alexbloodbank.fragments.HomeFragment
import com.think360.alexbloodbank.fragments.LoginFragment
import com.think360.alexbloodbank.fragments.RegisterFragment
import com.think360.alexbloodbank.fragments.base.BaseActivity
import kotlinx.android.synthetic.main.login_reg_tab_activity.*
import kotlinx.android.synthetic.main.login_tab_fragment.view.*

import me.shaohui.advancedluban.Luban
import me.shaohui.advancedluban.OnMultiCompressListener

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList
import javax.inject.Inject

class LoginRegTabActivity : RuntimePermissionsActivity() {

    companion object {
      lateinit var  loginRegTabActivity : LoginRegTabActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_reg_tab_activity)
        loginRegTabActivity =  this
        if (Build.VERSION.SDK_INT >= 23)
            permissions()
        setupViewPager(container)
        tabs.setupWithViewPager(container)

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(LoginFragment.newInstance(), "Login")
        adapter.addFragment(RegisterFragment.newInstance(), "Register")
        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }
    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(BaseActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
    override fun onBackPressed() {

        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()

        }
    }
    private fun permissions() {
        val REQUEST_PERMISSIONS = 20
        super@LoginRegTabActivity.requestAppPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), R.string.app_name, REQUEST_PERMISSIONS)
    }
}
