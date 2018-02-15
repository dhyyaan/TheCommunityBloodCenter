package com.think360.alexbloodbank.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
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
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by think360 on 16/11/17.
 */
class RecordVolunteersHoursFragment : Fragment() {
    @Inject
    internal lateinit var apiService: ApiService
    lateinit var  etDonationDate : EditText
    lateinit var  etFacultyName : EditText
    lateinit var  etLocation : EditText

    lateinit var progressBar : ProgressBar
    lateinit var spHours : Spinner
    private var selDate : String = ""
    companion object {
        fun newInstance(): RecordVolunteersHoursFragment {
            return RecordVolunteersHoursFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val  rootView  = inflater!!.inflate(R.layout.record_your_volunteer_hours_fragment, container, false)
        BloodCenterActivity.bloodCenterActivity!!.select = true
        progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)
        etDonationDate = rootView.findViewById<EditText>(R.id.etDonationDate)
        etFacultyName = rootView.findViewById<EditText>(R.id.etFacultyName)
        etLocation = rootView.findViewById<EditText>(R.id.etLocation)

         spHours = rootView.findViewById<Spinner>(R.id.spHours) as Spinner



       val ffRecordThisAchievement = rootView.findViewById<FrameLayout>(R.id.ffRecordThisAchievement)
        ffRecordThisAchievement.setOnClickListener {
            if(!TextUtils.isEmpty(etDonationDate.text.toString())&&!TextUtils.isEmpty(etFacultyName.text.toString())&&
                    !TextUtils.isEmpty(etLocation.text.toString()) && !spHours.selectedItem.toString().equals("Select Hours")){
                recordVolunteerHours(selDate ,etFacultyName.text.toString() ,etLocation.text.toString())
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
        //   System.out.println("Current time => " + c.time)
        val df = SimpleDateFormat("MM/dd/yyyy")
        val formattedDate = df.format(c.time)

        val  etDonationDate = rootView.findViewById<EditText>(R.id.etDonationDate)
        etDonationDate.setText(formattedDate)

        etDonationDate.setOnClickListener {
            val mcurrentDate = Calendar.getInstance()
            val mYear = mcurrentDate.get(Calendar.YEAR)
            val mMonth = mcurrentDate.get(Calendar.MONTH)
            val mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH)

            val mDatePicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->

                selDate =  ""+selectedyear+ "-" + (selectedmonth + 1) + "-" +selectedday
                etDonationDate.setText(""+(selectedmonth + 1) + "/" +selectedday.toString() + "/" + selectedyear) }, mYear, mMonth, mDay)
            mDatePicker.getDatePicker()   .setMaxDate(System.currentTimeMillis())
           // mDatePicker.getDatePicker().setMinDate(mcurrentDate.getTimeInMillis())
            mDatePicker.show()
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ( context.applicationContext as AppController).getComponent().inject(this@RecordVolunteersHoursFragment)
    }
    private fun recordVolunteerHours(vDate : String ,fName : String ,loc : String) {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        progressBar.setClickable(false)
        apiService.addvolunteer(AppController.getSharedPref().getString("user_id",""),vDate, spHours.selectedItem.toString(),fName,loc).enqueue(object : Callback<Responce.AddVolunteerResponce> {
            override fun onResponse(call: Call<Responce.AddVolunteerResponce>, response: Response<Responce.AddVolunteerResponce>) {
                if(response.body().getStatus()==1){
                    progressBar.visibility = View.GONE
                    Toast.makeText(activity,""+response.body().getMessage(), Toast.LENGTH_SHORT).show()
                    MainFragment.navigatione.selectedItemId = R.id.navigation_home
                    etDonationDate.setText("")
                    etFacultyName .setText("")
                    etLocation.setText("")
                    selDate=""
                    spHours.setSelection(0)
                    ( activity.application as AppController).bus().send(EventToRefresh(1001))
                    BloodCenterActivity.bloodCenterActivity!!.hideSoftKeyboard()
                    Toast.makeText(activity,""+response.body().getMessage(), Toast.LENGTH_SHORT).show()
                }else{
                    progressBar.visibility = View.GONE
                    Toast.makeText(activity,""+response.body().message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Responce.AddVolunteerResponce>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
            }
        })

    }
}