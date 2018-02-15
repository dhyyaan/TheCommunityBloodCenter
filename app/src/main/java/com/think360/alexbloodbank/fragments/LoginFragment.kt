package com.think360.alexbloodbank.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.think360.alexbloodbank.BloodCenterActivity
import com.think360.alexbloodbank.LoginRegTabActivity
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
 * Created by think360 on 22/11/17.
 */
class LoginFragment : BaseFragment() {
    lateinit var chTc : CheckBox
    lateinit var progressBar : ProgressBar
    override fun getTitle(): String {
       return "Login"
    }

    @Inject
    internal lateinit var apiService: ApiService
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       val  rootView = inflater!!.inflate(R.layout.login_tab_fragment, container, false)
        chTc = rootView.findViewById<CheckBox>(R.id.chTc)

        val etUserName = rootView.findViewById<TextInputEditText>(R.id.etUserName)
        val etPwd = rootView.findViewById<TextInputEditText>(R.id.etPwd)
        progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)


        if(!AppController.getSharedPref().getString("user_name","null").equals("null")){

            etUserName.setText(AppController.getSharedPref().getString("user_name","null"))
            etPwd.setText(AppController.getSharedPref().getString("password","null"))
        }



        val submit = rootView.findViewById<Button>(R.id.btnSubmit)

        submit.setOnClickListener{
if(!TextUtils.isEmpty(etUserName.text.toString())){
 if(!TextUtils.isEmpty(etPwd.text.toString())){
     login(etUserName.text.toString(),etPwd.text.toString())
 }else{
     Toast.makeText(activity,"Please password", Toast.LENGTH_SHORT).show()
 }

}else{
    Toast.makeText(activity,"Please enter username", Toast.LENGTH_SHORT).show()
}
         }
        val tvForget = rootView.findViewById<TextView>(R.id.tvForget)
        tvForget.setOnClickListener { add(ForgetFragment.newInstance()) }
        return rootView
    }
    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ( context.applicationContext as AppController).getComponent().inject(this@LoginFragment)
    }
    private fun login(uname: String, pwd: String) {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        progressBar.setClickable(false)
        apiService.login("android",AppController.getSharedPref().getString("firebase_reg_token","null"),uname,pwd).enqueue(object : Callback<Responce.LoginResponce> {
            override fun onResponse(call: Call<Responce.LoginResponce>, response: Response<Responce.LoginResponce>) {
                if(response.body().getStatus()==1)
                {
                    progressBar.visibility = View.GONE
                     AppController.getSharedPref().edit().putString("user_id",response.body().getData().getUserId()).apply()
                     AppController.getSharedPref().edit().putBoolean("is_login",true).apply()
                    if(chTc.isChecked){
                          AppController.getSharedPref().edit().putString("user_name",uname).apply()
                          AppController.getSharedPref().edit().putString("password",pwd).apply()
                      }
                   LoginRegTabActivity.loginRegTabActivity!!.hideSoftKeyboard()
                    startActivity(Intent(activity, BloodCenterActivity::class.java))
                    activity.finish()
                }else{
                    progressBar.visibility = View.GONE
                    Toast.makeText(activity,response.body().getMessage(), Toast.LENGTH_SHORT).show()
                }



            }

            override fun onFailure(call: Call<Responce.LoginResponce>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
            }
        })

    }
}