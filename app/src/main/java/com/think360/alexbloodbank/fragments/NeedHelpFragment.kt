package com.think360.alexbloodbank.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
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
class NeedHelpFragment : BaseFragment() {
    @Inject
    internal lateinit var apiService: ApiService
    lateinit var  spSelectSchool : Spinner
    lateinit var  listSchId : ArrayList<String>
    lateinit var listSchNameList : ArrayList<String>

    lateinit var  fName : TextInputEditText
    lateinit var  lName : TextInputEditText
    lateinit var  email : TextInputEditText
    lateinit var  etSchool : TextInputEditText
    private var schId : String = ""
    lateinit var  etQCBox : TextInputEditText

    lateinit var progressBar : ProgressBar
    override fun getTitle(): String {
       return "Need Help"
    }

    companion object {
        fun newInstance(): NeedHelpFragment {
            return NeedHelpFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.need_help_fragment, container, false)
        BloodCenterActivity.bloodCenterActivity!!.select = true
        progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)
        spSelectSchool = rootView.findViewById<Spinner>(R.id.spSelectSchool)
        getSchools()
        spSelectSchool.onItemSelectedListener = sonItemSelectedListener

        fName = rootView.findViewById<TextInputEditText>(R.id.etFName)
        lName = rootView.findViewById<TextInputEditText>(R.id.etLName)
        email = rootView.findViewById<TextInputEditText>(R.id.etEmail)
        etSchool = rootView.findViewById<TextInputEditText>(R.id.etSchool)
        etQCBox = rootView.findViewById<TextInputEditText>(R.id.etQCBox)
       val btnSubmit = rootView.findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            if (!TextUtils.isEmpty(fName.text.toString()) && !TextUtils.isEmpty(lName.text.toString()) &&
                    !TextUtils.isEmpty(email.text.toString()) && !TextUtils.isEmpty(etQCBox.text.toString())&& !schId.equals("-1")) {
                needHelp(fName.text.toString(),lName.text.toString(),email.text.toString(),schId,etQCBox.text.toString(),etSchool.text.toString())
            }else{
                Toast.makeText(activity,"All boxes are mandatory", Toast.LENGTH_SHORT).show()
            }
        }
        return rootView
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ( context.applicationContext as AppController).getComponent().inject(this@NeedHelpFragment)
    }
    val sonItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            schId = listSchId.get(position)
            if(schId.equals("0")){
                etSchool.visibility = View.VISIBLE

            }else{
                etSchool.visibility = View.GONE
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }
    private fun needHelp(fname: String, lname: String, email: String, schId: String, comments: String, otherschool: String) {

        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        progressBar.setClickable(false)
        apiService.needhelp(AppController.getSharedPref().getString("user_id",""),fname,lname,email,schId,comments,otherschool).enqueue(object : Callback<Responce.NeedHelpResponce> {
            override fun onResponse(call: Call<Responce.NeedHelpResponce>, response: Response<Responce.NeedHelpResponce>) {
                if(response.body().getStatus()==1){
                    progressBar.visibility = View.GONE
                    BloodCenterActivity.bloodCenterActivity!!.hideSoftKeyboard()
                    Toast.makeText(activity,""+response.body().getMessage(), Toast.LENGTH_SHORT).show()
                    activity.onBackPressed()
                }
            }
            override fun onFailure(call: Call<Responce.NeedHelpResponce>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getSchools() {

        apiService.getSchools().enqueue(object : Callback<Responce.CountryResponce> {
            override fun onResponse(call: Call<Responce.CountryResponce>, response: Response<Responce.CountryResponce>) {
                if(response.body().status==1){
                    listSchId = ArrayList<String>()
                    listSchId.add("-1")
                    listSchNameList = ArrayList<String>()
                    listSchNameList.add("Select School")
                    for( i in response.body().data.getSchoolList()){
                        listSchId.add(i.getId())
                        listSchNameList.add(i.getName())
                    }

                    val   yOJAdapter = ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listSchNameList)
                    spSelectSchool.adapter = yOJAdapter
                    showProfile()
                }



            }

            override fun onFailure(call: Call<Responce.CountryResponce>, t: Throwable) {
                Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun showProfile() {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        progressBar.setClickable(false)
        apiService.showProfile(AppController.getSharedPref().getString("user_id","")).enqueue(object : Callback<Responce.ShowProfileResponce> {
            override fun onResponse(call: Call<Responce.ShowProfileResponce>, response: Response<Responce.ShowProfileResponce>) {
                if(response.body().getStatus()==1){
              fName .setText(response.body().getData().getFirst_name())
               lName .setText(response.body().getData().getLast_name())
               email.setText(response.body().getData().getUseremail())
                    schId = response.body().getData().getSchool()


                    if(schId.equals("0")){
                        etSchool.visibility = View.VISIBLE
                        val schooName = response.body().getData().getSchoolName()
                        etSchool.setText(schooName)
                        spSelectSchool.setSelection(listSchNameList.indexOf("Other"))
                    }else{
                        spSelectSchool.setSelection(listSchNameList.indexOf(response.body().getData().getSchoolName()))
                    }
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<Responce.ShowProfileResponce>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
            }
        })

    }

}