package com.think360.alexbloodbank.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.RecyclerView
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
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by think360 on 24/11/17.
 */
class ForgetFragment : BaseFragment() {
    @Inject
    internal lateinit var apiService: ApiService
    lateinit var progressBar : ProgressBar
    lateinit var etEmail : TextInputEditText
    override fun getTitle(): String {
        return "Forget Username or Password"
    }

    companion object {
        fun newInstance(): ForgetFragment {
            return ForgetFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView  = inflater!!.inflate(R.layout.forget_fragment, container, false)

         etEmail =  rootView.findViewById<TextInputEditText>(R.id.etEmail)
         progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)
        val ivBack =  rootView.findViewById<ImageView>(R.id.ivBack)
         val btnSubmit =  rootView.findViewById<Button>(R.id.btnSubmit)


        btnSubmit.setOnClickListener {
            if(!TextUtils.isEmpty(etEmail.text.toString())){
                getForget(etEmail.text.toString())
            }else{
                Toast.makeText(LoginRegTabActivity.loginRegTabActivity, "please enter email", Toast.LENGTH_SHORT).show()
            }
        }
        ivBack.setOnClickListener {
            LoginRegTabActivity.loginRegTabActivity!!.hideSoftKeyboard()
            LoginRegTabActivity.loginRegTabActivity!!.onBackPressed()
        }

        return rootView
    }


    private fun getForget(email : String) {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        progressBar.setClickable(false)
        apiService.forgetPassword(email).enqueue(object : Callback<Responce.ForgetPasswordResponce> {
            override fun onResponse(call: Call<Responce.ForgetPasswordResponce>, response: Response<Responce.ForgetPasswordResponce>) {
                if (response.body().getStatus() == 1) {
                    progressBar.visibility = View.GONE
                    etEmail.setText("")
                    LoginRegTabActivity.loginRegTabActivity!!.hideSoftKeyboard()
                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show()
                    LoginRegTabActivity.loginRegTabActivity!!.   onBackPressed()
                }else{
                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }


            }

            override fun onFailure(call: Call<Responce.ForgetPasswordResponce>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as AppController).getComponent().inject(this@ForgetFragment)
    }
}