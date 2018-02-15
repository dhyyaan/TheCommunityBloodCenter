package com.think360.alexbloodbank.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.think360.alexbloodbank.BloodCenterActivity
import com.think360.alexbloodbank.R
import com.think360.alexbloodbank.fragments.base.BackButtonSupportFragment
import com.think360.alexbloodbank.fragments.base.BaseFragment
import java.util.ArrayList

/**
 * Created by think360 on 20/11/17.
 */
class MainFragment : BaseFragment() , BackButtonSupportFragment {
    lateinit var  viewPager : ViewPager
    lateinit var  navigation : BottomNavigationView

    override fun getTitle(): String {
        return "Donors"
    }
    private var consumingBackPress = false
    private var toast: Toast? = null
    override fun onBackPressed(): Boolean {
        if (consumingBackPress) {

            BloodCenterActivity.bloodCenterActivity!!.finish()
        }

        this.consumingBackPress = true
        toast = Toast.makeText(activity, "Press back once more to quit the application", Toast.LENGTH_SHORT)
        toast!!.show()

        Handler().postDelayed({ consumingBackPress = false }, 1000)

            return  true


    }
    companion object {
        lateinit var  mOnNavigationItemSelectedListeners : BottomNavigationView.OnNavigationItemSelectedListener
        lateinit var navigatione : BottomNavigationView
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.main_fragment, container, false)
        navigation = view.findViewById(R.id.navigation)
        //bottum Navigation
        viewPager = view.findViewById(R.id.viewPager)
        viewPager!!.setAdapter(PagerAdapter(childFragmentManager, getFragmentArrrayList()))
        viewPager!!.setOffscreenPageLimit(5)
        disableShiftMode(navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        mOnNavigationItemSelectedListeners = mOnNavigationItemSelectedListener
        navigatione = navigation
        return view
    }
    //navigation code

    private val  mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

              BloodCenterActivity.bloodCenterActivity!!.  setTitle("The Community Blood Center")
                viewPager.setCurrentItem(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_donation -> {

                BloodCenterActivity.bloodCenterActivity!!. setTitle("Add New Donation")
                viewPager.setCurrentItem(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_hours -> {

                BloodCenterActivity.bloodCenterActivity!!.  setTitle("Record Your Volunteer Hours")
                viewPager.setCurrentItem(2)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_efforts -> {

                BloodCenterActivity.bloodCenterActivity!!.  setTitle("Record Your Recruiting Efforts")
                viewPager.setCurrentItem(3)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_blood -> {

                BloodCenterActivity.bloodCenterActivity!!. setTitle("Enter Your Blood Drive Information")
                viewPager.setCurrentItem(4)

                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }
    @SuppressLint("RestrictedApi")
    fun disableShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0..menuView.childCount - 1) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                item.setShiftingMode(false)
                // set once again checked value, so view will be updated
                item.setChecked(item.itemData.isChecked)
            }
        } catch (e: NoSuchFieldException) {
            Log.e("BNVHelper", "Unable to get shift mode field", e)
        } catch (e: IllegalAccessException) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e)
        }

    }
    private fun getFragmentArrrayList(): ArrayList<Fragment> {
        val fragmentSparseArray = ArrayList<Fragment>()
        fragmentSparseArray.add(HomeFragment.newInstance())
        fragmentSparseArray.add(AddNewDonationFragment.newInstance())
        fragmentSparseArray.add(RecordVolunteersHoursFragment.newInstance())
        fragmentSparseArray.add(RecordRecruitingEffertsFragment.newInstance())
        fragmentSparseArray.add(EnterBloodDriveInfoFragment.newInstance())

        return fragmentSparseArray

    }
    private inner class PagerAdapter(fm: FragmentManager, fragmentSparseArray: ArrayList<Fragment>) : FragmentPagerAdapter(fm) {

        private var fragmentSparseArray = ArrayList<Fragment>()

        init {
            this.fragmentSparseArray = fragmentSparseArray
        }

        override fun getItem(position: Int): Fragment {
            return fragmentSparseArray[position]
        }

        override fun getCount(): Int {
            return fragmentSparseArray.size
        }
    }

}