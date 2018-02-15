package com.think360.alexbloodbank.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.think360.alexbloodbank.BloodCenterActivity
import com.think360.alexbloodbank.R
import com.think360.alexbloodbank.api.AppController
import com.think360.alexbloodbank.api.data.Responce
import com.think360.alexbloodbank.api.interfaces.ApiService
import com.think360.alexbloodbank.fragments.base.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by think360 on 17/11/17.
 */
class ChangePasswordFragment : BaseFragment() {
    @Inject
    internal lateinit var apiService: ApiService
    lateinit var  etPwd : TextInputEditText
    lateinit var  etCPwd : TextInputEditText
    lateinit var progressBar : ProgressBar

    override fun getTitle(): String {
        return "Change Password"
    }

    companion object {
        fun newInstance(): ChangePasswordFragment {
            return ChangePasswordFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.change_pwd_fragment, container, false)

        etPwd = rootView.findViewById<TextInputEditText>(R.id.etPwd)
        etCPwd = rootView.findViewById<TextInputEditText>(R.id.etCPwd)
        progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)

        BloodCenterActivity.bloodCenterActivity!!.select = true
        val  btnSubmit = rootView.findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            if(!TextUtils.isEmpty(etPwd.text.toString())){

                if(!TextUtils.isEmpty(etCPwd.text.toString())){

                    if(etPwd.text.toString().equals(etCPwd.text.toString())){
                       changepassword(etPwd.text.toString() ,etCPwd.text.toString())
                    }else{
                        Toast.makeText(activity,"Please enter same password!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(activity,"Please enter confirm password!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(activity,"Please enter password to change!", Toast.LENGTH_SHORT).show()
            }
        }

        return rootView
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ( context.applicationContext as AppController).getComponent().inject(this@ChangePasswordFragment)
    }
    private fun changepassword(pwd : String ,cpwd : String) {
        progressBar.visibility = View.VISIBLE
        progressBar.setClickable(false)
        apiService.changepassword(AppController.getSharedPref().getString("user_id",""),pwd,cpwd).enqueue(object : Callback<Responce.ChangePasswordResponce> {
            override fun onResponse(call: Call<Responce.ChangePasswordResponce>, response: Response<Responce.ChangePasswordResponce>) {
                if(response.body().getStatus()==1){
                    progressBar.visibility = View.GONE
                    BloodCenterActivity.bloodCenterActivity!!.hideSoftKeyboard()
                    Toast.makeText(activity,""+response.body().getMessage(), Toast.LENGTH_SHORT).show()
                    activity.onBackPressed()
                }else{

                    progressBar.visibility = View.GONE

                    Toast.makeText(activity,""+response.body().getMessage(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Responce.ChangePasswordResponce>, t: Throwable) {

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