package com.think360.alexbloodbank.fragments

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
import javax.inject.Inject

/**
 * Created by think360 on 16/11/17.
 */
class RecordRecruitingEffertsFragment  : Fragment() {
    @Inject
    internal lateinit var apiService: ApiService
    lateinit var  etDFName : EditText
    lateinit var  etDLName : EditText
    lateinit var progressBar : ProgressBar
    companion object {
        fun newInstance(): RecordRecruitingEffertsFragment {
            return RecordRecruitingEffertsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      val rootView =  inflater!!.inflate(R.layout.record_your_recruiting_efforts_fragment, container, false)
        BloodCenterActivity.bloodCenterActivity!!.select = true
        etDFName = rootView.findViewById<EditText>(R.id.etDFName)
        etDLName = rootView.findViewById<EditText>(R.id.etDLName)
        progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)
        val  ffRecordThisAchievement = rootView.findViewById<FrameLayout>(R.id.ffRecordThisAchievement)
        ffRecordThisAchievement.setOnClickListener {
            if(!TextUtils.isEmpty(etDFName.text.toString()) && !TextUtils.isEmpty(etDLName.text.toString())){
                addonation(etDFName.text.toString() ,etDLName.text.toString())
            }else{
                Toast.makeText(activity,"All boxes are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        return rootView
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ( context.applicationContext as AppController).getComponent().inject(this@RecordRecruitingEffertsFragment)
    }
    private fun addonation(fName : String ,lName : String) {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        progressBar.setClickable(false)
        apiService.addrecruit(AppController.getSharedPref().getString("user_id",""),fName,lName).enqueue(object : Callback<Responce.RecruitResponce> {
            override fun onResponse(call: Call<Responce.RecruitResponce>, response: Response<Responce.RecruitResponce>) {
                if(response.body().getStatus()==1){
                    progressBar.visibility = View.GONE
                    Toast.makeText(activity,""+response.body().getMessage(), Toast.LENGTH_SHORT).show()
                    MainFragment.navigatione.selectedItemId = R.id.navigation_home
                    etDFName.setText("")
                    etDLName.setText("")
                    ( activity.application as AppController).bus().send(EventToRefresh(1001))
                    BloodCenterActivity.bloodCenterActivity!!.hideSoftKeyboard()
                    Toast.makeText(activity,""+response.body().getMessage(), Toast.LENGTH_SHORT).show()
                }else{
                    progressBar.visibility = View.GONE
                    Toast.makeText(activity,""+response.body().message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Responce.RecruitResponce>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
            }
        })

    }
}