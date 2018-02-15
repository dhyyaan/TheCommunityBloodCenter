package com.think360.alexbloodbank.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.think360.alexbloodbank.BloodCenterActivity
import com.think360.alexbloodbank.R
import com.think360.alexbloodbank.api.AppController
import com.think360.alexbloodbank.api.EventToRefresh
import com.think360.alexbloodbank.api.data.Responce
import com.think360.alexbloodbank.api.interfaces.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

import java.text.SimpleDateFormat


/**
 * Created by think360 on 16/11/17.
 */
class AddNewDonationFragment : Fragment() {
    @Inject
    internal lateinit var apiService: ApiService
    lateinit var  bloodDonationDate : EditText
    lateinit var  bloodDonationLoc : EditText
    lateinit var progressBar : ProgressBar
    private var selDate : String = ""
 //   lateinit var  ffAchivedAward : FrameLayout
  //  lateinit var  tvAchivedAward : TextView
    companion object {
        fun newInstance(): AddNewDonationFragment {
            return AddNewDonationFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.add_new_donation_fragment, container, false)
        bloodDonationDate = rootView.findViewById<EditText>(R.id.etDonationDate)
        bloodDonationLoc = rootView.findViewById<EditText>(R.id.etDonationLocation)



        BloodCenterActivity.bloodCenterActivity!!.select = true
       val  ffRecordThisAchievement = rootView.findViewById<FrameLayout>(R.id.ffRecordThisAchievement)
        ffRecordThisAchievement.setOnClickListener {
            if(!TextUtils.isEmpty(bloodDonationDate.text.toString()) && !TextUtils.isEmpty(bloodDonationLoc.text.toString())){
                addonation(selDate ,bloodDonationLoc.text.toString())
               }else{
                Toast.makeText(activity,"All boxes are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        val c1 = Calendar.getInstance()
        //  System.out.println("Current time => " + c.time)
        val df1 = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate1 = df1.format(c1.time)
        selDate=formattedDate1

        val c = Calendar.getInstance()
        // System.out.println("Current time => " + c.time)
        val df = SimpleDateFormat("MM/dd/yyyy")
        val formattedDate = df.format(c.time)

        val  etDonationDate = rootView.findViewById<EditText>(R.id.etDonationDate)
        etDonationDate. setText(formattedDate)
        etDonationDate.setOnClickListener {
            val mcurrentDate = Calendar.getInstance()
            val mYear = mcurrentDate.get(Calendar.YEAR)
            val mMonth = mcurrentDate.get(Calendar.MONTH)
            val mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH)

            val mDatePicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                selDate =  ""+selectedyear+ "-" + (selectedmonth + 1) + "-" +selectedday
                etDonationDate.setText(""+(selectedmonth + 1) + "/" +selectedday.toString() + "/" + selectedyear) }, mYear, mMonth, mDay)

            mDatePicker.getDatePicker() .setMaxDate(System.currentTimeMillis())
            mDatePicker.show()
        }
        progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)
       // changeAward()
        return rootView
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ( context.applicationContext as AppController).getComponent().inject(this@AddNewDonationFragment)
    }
    private fun addonation(donationDate : String ,donationLoc : String) {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        progressBar.setClickable(false)
        apiService.addonation(AppController.getSharedPref().getString("user_id",""),donationLoc,donationDate).enqueue(object : Callback<Responce.AddDonationResponce> {
            override fun onResponse(call: Call<Responce.AddDonationResponce>, response: Response<Responce.AddDonationResponce>) {
                if(response.body().getStatus()==1){
                    progressBar.visibility = View.GONE
                    MainFragment.navigatione.selectedItemId = R.id.navigation_home
                    bloodDonationDate.setText("")
                    bloodDonationLoc.setText("")
                    ( activity.application as AppController).bus().send(EventToRefresh(1001))
                    BloodCenterActivity.bloodCenterActivity!!.hideSoftKeyboard()
                    Toast.makeText(activity,""+response.body().getMessage(), Toast.LENGTH_SHORT).show()
                }else{

                    progressBar.visibility = View.GONE

                    Toast.makeText(activity,""+response.body().message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Responce.AddDonationResponce>, t: Throwable) {

                progressBar.visibility = View.GONE

                Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
            }
        })

    }
/*    fun changeAward(){
        if(AppController.getSharedPref().getInt("getAward",0)==1){

            ffAchivedAward .setBackgroundColor(resources.getColor(R.color.award_color_sel))
            tvAchivedAward  .setCompoundDrawablesWithIntrinsicBounds( R.drawable.not_awad2, 0, 0, 0);
            tvAchivedAward.setTextColor(resources.getColor(R.color.award_color_white))
        }else{
            AppController.getSharedPref().edit().putInt("getAward",0)
            ffAchivedAward .setBackgroundColor(resources.getColor(R.color.award_color_unsel))
            tvAchivedAward  .setCompoundDrawablesWithIntrinsicBounds( R.drawable.not_awad, 0, 0, 0);
            tvAchivedAward.setTextColor(resources.getColor(R.color.award_color_sel))
        }
    }*/

}