package com.think360.alexbloodbank.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.think360.alexbloodbank.BloodCenterActivity
import com.think360.alexbloodbank.R
import com.think360.alexbloodbank.api.AppController
import com.think360.alexbloodbank.api.EventToRefresh
import com.think360.alexbloodbank.api.data.Responce
import com.think360.alexbloodbank.api.interfaces.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by think360 on 16/11/17.
 */
class EnterBloodDriveInfoFragment : Fragment() {
    @Inject
    internal lateinit var apiService: ApiService
    lateinit var  etLocation : EditText
    lateinit var  etBloodDriveDate : EditText
    lateinit var  etCBCRecruiterName : EditText
    lateinit var progressBar : ProgressBar
    private var selDate : String = ""
    companion object {
        fun newInstance(): EnterBloodDriveInfoFragment {
            return EnterBloodDriveInfoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val  rootView = inflater!!.inflate(R.layout.blood_drive_info_fragment, container, false)
        BloodCenterActivity.bloodCenterActivity!!.select = true
        etLocation = rootView.findViewById<EditText>(R.id.etLocation)
        etBloodDriveDate = rootView.findViewById<EditText>(R.id.etBloodDriveDate)
        etCBCRecruiterName = rootView.findViewById<EditText>(R.id.etCBCRecruiterName)
        progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)
        val ffRecordThisAchievement = rootView.findViewById<FrameLayout>(R.id.ffRecordThisAchievement)
        ffRecordThisAchievement.setOnClickListener {
            if(!TextUtils.isEmpty(etLocation.text.toString())&&!TextUtils.isEmpty(etBloodDriveDate.text.toString())&& !TextUtils.isEmpty(etCBCRecruiterName.text.toString())){
                addhost(etLocation.text.toString() ,selDate ,etCBCRecruiterName.text.toString())
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
        //  System.out.println("Current time => " + c.time)
        val df = SimpleDateFormat("MM/dd/yyyy")
        val formattedDate = df.format(c.time)
        val  etBloodDriveDate = rootView.findViewById<EditText>(R.id.etBloodDriveDate)

        etBloodDriveDate.setText(formattedDate)

        etBloodDriveDate.setOnClickListener {
            val mcurrentDate = Calendar.getInstance()
            val mYear = mcurrentDate.get(Calendar.YEAR)
            val mMonth = mcurrentDate.get(Calendar.MONTH)
            val mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH)

            val mDatePicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                selDate =   ""+selectedyear+ "-" + (selectedmonth + 1) + "-" +selectedday
                etBloodDriveDate.setText(""+(selectedmonth + 1) + "/" +selectedday.toString() + "/" + selectedyear) }, mYear, mMonth, mDay)
            mDatePicker.getDatePicker()  .setMaxDate(System.currentTimeMillis())
           // mDatePicker.getDatePicker().setMinDate(mcurrentDate.getTimeInMillis())
            mDatePicker.show()
        }
        return rootView
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ( context.applicationContext as AppController).getComponent().inject(this@EnterBloodDriveInfoFragment)
    }
    private fun addhost(loc : String ,date : String,name : String) {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        progressBar.setClickable(false)
        apiService.addhost(AppController.getSharedPref().getString("user_id",""),loc,date,name).enqueue(object : Callback<Responce.BloodResponce> {
            override fun onResponse(call: Call<Responce.BloodResponce>, response: Response<Responce.BloodResponce>) {
                if(response.body().getStatus()==1){
                    progressBar.visibility = View.GONE
                    MainFragment.navigatione.selectedItemId = R.id.navigation_home
                    etLocation.setText("")
                    etBloodDriveDate .setText("")
                    etCBCRecruiterName.setText("")
                    ( activity.application as AppController).bus().send(EventToRefresh(1001))
                    BloodCenterActivity.bloodCenterActivity!!.hideSoftKeyboard()
                    Toast.makeText(activity,""+response.body().getMessage(), Toast.LENGTH_SHORT).show()
                }else{
                    progressBar.visibility = View.GONE
                    Toast.makeText(activity,""+response.body().message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Responce.BloodResponce>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
            }
        })

    }
}