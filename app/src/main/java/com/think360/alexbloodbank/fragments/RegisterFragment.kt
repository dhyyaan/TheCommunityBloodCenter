package com.think360.alexbloodbank.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.think360.alexbloodbank.BloodCenterActivity
import com.think360.alexbloodbank.LoginRegTabActivity
import com.think360.alexbloodbank.R
import com.think360.alexbloodbank.api.AppController
import com.think360.alexbloodbank.api.data.Responce
import com.think360.alexbloodbank.api.interfaces.ApiService
import com.think360.alexbloodbank.interfaces.OnRegistrationSuccess
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import kotlinx.android.synthetic.main.activity_loginregister.*
import kotlinx.android.synthetic.main.register_tab_fragment.*
import me.shaohui.advancedluban.Luban
import me.shaohui.advancedluban.OnMultiCompressListener


import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by think360 on 22/11/17.
 */

class RegisterFragment : Fragment() {
    @Inject
    internal lateinit var apiService: ApiService
    private var profile_pic: MultipartBody.Part? = null
    private var sms : Int = 0
    private var schId : String = ""

    lateinit var  spSelectSchool : Spinner
    private var schYear : String = ""
    lateinit var  yearSp : Spinner
    lateinit var  listSchId : ArrayList<String>
    lateinit var progressBar : ProgressBar
    private var selDate : String = ""
    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }
    lateinit var  fName : TextInputEditText
    lateinit var  lName : TextInputEditText
    lateinit var  uName : TextInputEditText
    lateinit var  dob : TextInputEditText
    lateinit var  email : TextInputEditText
    lateinit var  mobile : TextInputEditText
    lateinit var  pwd : TextInputEditText
    lateinit var  cPwd : TextInputEditText
    lateinit var  chTc : CheckBox
    lateinit var  etSchool : TextInputEditText
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.register_tab_fragment, container, false)
        progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)
        fName = rootView.findViewById<TextInputEditText>(R.id.etFName)
        lName = rootView.findViewById<TextInputEditText>(R.id.etLName)
        uName = rootView.findViewById<TextInputEditText>(R.id.etUName)
        dob = rootView.findViewById<TextInputEditText>(R.id.etDB)
        email = rootView.findViewById<TextInputEditText>(R.id.etEmail)
        mobile = rootView.findViewById<TextInputEditText>(R.id.etMobile)
        pwd = rootView.findViewById<TextInputEditText>(R.id.etPwd)
        cPwd = rootView.findViewById<TextInputEditText>(R.id.etCPassword)
        etSchool = rootView.findViewById<TextInputEditText>(R.id.etSchool)
        chTc = rootView.findViewById<CheckBox>(R.id.chTc)
        if(chTc.isChecked){
            sms = 1
        }else{
            sms = 0
        }
        chTc.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
            if(b){
                sms = 1
            }else{
                sms = 0
            }

        }
        val c = Calendar.getInstance()
      //  System.out.println("Current time => " + c.time)
        val df = SimpleDateFormat("MM/dd/yyyy")
        val formattedDate = df.format(c.time)
        dob.setText(formattedDate)

        val c1 = Calendar.getInstance()
      //  System.out.println("Current time => " + c.time)
        val df1 = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate1 = df1.format(c1.time)

        selDate=formattedDate1


        val cIvProfilePicw = rootView.findViewById<ImageView>(R.id.cIvProfilePic)
        cIvProfilePicw.setOnClickListener{
             val setup = PickSetup().setTitle("Choose")
                     .setTitleColor(R.color.colorAccent)
                     .setSystemDialog(false)

             PickImageDialog.build(setup) { r ->
                 Log.e("RESULT", r.path)
                 if (r.getError() == null) {
                     r.getBitmap()
                     r.getError()
                     //imagePath = r.uri
                  //   Log.e("RESULT", r.path)
                //  profile_pic =  prepareFilePart("profile_pic",File(r.path).absoluteFile)

                //     cIvProfilePicw.setImageBitmap( r.bitmap)
              Luban.compress(activity, File(r.getPath()))
                             .putGear(Luban.THIRD_GEAR)
                             .launch(object : OnMultiCompressListener {
                                 override fun onStart() {
                                     // Log.i(TAG, "start");
                                 }

                                 override fun onSuccess(fileList: List<File>) {
                                     profile_pic =  prepareFilePart("profile_pic",fileList.get(0))
                                     cIvProfilePic.setImageURI(Uri.fromFile(fileList.get(0)))
                                 }
                                 override fun onError(e: Throwable) {
                                     e.printStackTrace();
                                 }
                             })
                     //If you want the Bitmap.
                   //  editProfileBinding.ivUser.setImageBitmap(r.bitmap)

                 } else {
                     //Handle possible errors
                     Toast.makeText(activity, r.error.message, Toast.LENGTH_LONG).show()
                 }
             }.show(fragmentManager)

        }
       val  btnSubmit = rootView.findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            if(!TextUtils.isEmpty(etFName.text.toString())){
                if(!TextUtils.isEmpty(etLName.text.toString())){
                    if(!TextUtils.isEmpty(etUName.text.toString())){
                        if(!TextUtils.isEmpty(etDB.text.toString())){
                            if(!TextUtils.isEmpty(etEmail.text.toString())){
                                if(isValidEmail(etEmail.text.toString())){
                                    if(!TextUtils.isEmpty(etMobile.text.toString())){
                                        if(checkMobileNo(etMobile.text.toString())){
                                            if(!TextUtils.isEmpty(etPwd.text.toString())){
                                                if(!TextUtils.isEmpty(etCPassword.text.toString())){
                                                    if(etPwd.text.toString().equals(etCPassword.text.toString())){
                                                        LoginRegTabActivity.loginRegTabActivity!!.hideSoftKeyboard()

                                                            if(!schId.equals("-1")){
                                                                if(!yearSp.selectedItem.equals("Select Year")){
                                                                    regUser()
                                                                }else{
                                                                    Toast.makeText(activity,"Please select year",Toast.LENGTH_SHORT).show()
                                                                }
                                                            }else{
                                                                Toast.makeText(activity,"Please select school",Toast.LENGTH_SHORT).show()
                                                            }

                                                        /*   }else{
                                                               Toast.makeText(activity,"Please select profile image",Toast.LENGTH_SHORT).show()
                                                           }*/
                                                    }else{
                                                        Toast.makeText(activity,"Password don't match",Toast.LENGTH_SHORT).show()
                                                    }
                                                }else{
                                                    Toast.makeText(activity,"Confirm Password can't be empty",Toast.LENGTH_SHORT).show()
                                                }
                                            }else{
                                                Toast.makeText(activity,"Password can't be empty",Toast.LENGTH_SHORT).show()
                                            }
                                        }else{
                                            Toast.makeText(activity,"Mobile Number is incorrect",Toast.LENGTH_SHORT).show()
                                        }
                                    }else{
                                        Toast.makeText(activity,"Mobile Number can't be empty",Toast.LENGTH_SHORT).show()
                                    }

                                }else{
                                    Toast.makeText(activity,"Email is incorrect",Toast.LENGTH_SHORT).show()
                                }

                            }else{
                                Toast.makeText(activity,"Email can't be empty",Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(activity,"DOB can't be empty",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(activity,"User Name can't be empty",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(activity,"Last Name can't be empty",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(activity,"First Name can't be empty",Toast.LENGTH_SHORT).show()
            }
           }
        val years =  ArrayList<String>()
        years.add("Select Year")
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)

        for (i in 1..13) {
            years.add(Integer.toString(thisYear+i))
        }
         yearSp = rootView.findViewById<Spinner>(R.id.spSelectGraduationYear)
        val   yOJAdapter = ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, years)
        yearSp.adapter = yOJAdapter
        spSelectSchool = rootView.findViewById<Spinner>(R.id.spSelectSchool)

        yearSp.onItemSelectedListener = fonItemSelectedListener

        val  etDB = rootView.findViewById<EditText>(R.id.etDB)
        etDB.setOnClickListener {
            val mcurrentDate = Calendar.getInstance()
            val mYear = mcurrentDate.get(Calendar.YEAR)
            val mMonth = mcurrentDate.get(Calendar.MONTH)
            val mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH)

            val mDatePicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                selDate =  ""+selectedyear+ "-" + (selectedmonth + 1) + "-" +selectedday
                etDB.setText(""+(selectedmonth + 1) + "/" +selectedday.toString() + "/" + selectedyear) }, mYear, mMonth, mDay)
            mDatePicker.getDatePicker().setMaxDate(mcurrentDate.getTimeInMillis())
            mDatePicker.show()
        }
        getSchools()
        return rootView
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
    val fonItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            schYear = parent.getItemAtPosition(position) .toString()
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }
    private fun regUser() {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        progressBar.setClickable(false)
        val fname = RequestBody.create(MediaType.parse("text/plain"), etFName.text.toString())
        val lname = RequestBody.create(MediaType.parse("text/plain"), etLName.text.toString())
        val uname = RequestBody.create(MediaType.parse("text/plain"), etUName.text.toString())
        val dob = RequestBody.create(MediaType.parse("text/plain"),selDate)
        val email = RequestBody.create(MediaType.parse("text/plain"), etEmail.text.toString())
        val mobile = RequestBody.create(MediaType.parse("text/plain"), etMobile.text.toString())

        val sms = RequestBody.create(MediaType.parse("text/plain"), ""+sms)
        val password = RequestBody.create(MediaType.parse("text/plain"), etPwd.text.toString())
        val confirm_pass = RequestBody.create(MediaType.parse("text/plain"), etCPassword.text.toString())
        val school = RequestBody.create(MediaType.parse("text/plain"), schId)
        var etSchools = RequestBody.create(MediaType.parse("text/plain"), "")

        if(schId.equals("0")){
            etSchools = RequestBody.create(MediaType.parse("text/plain"), etSchool.text.toString())
        }

        val schoolyear = RequestBody.create(MediaType.parse("text/plain"), schYear)

          val deviceid = RequestBody.create(MediaType.parse("text/plain"), AppController.getSharedPref().getString("firebase_reg_token","null"))
          val devicetype = RequestBody.create(MediaType.parse("text/plain"), "android")

        //swipeLayout.setRefreshing(true)

        apiService.registration(fname,lname,uname,dob,email,mobile,sms,password,confirm_pass,
                school,schoolyear,profile_pic,deviceid,devicetype,etSchools).enqueue(object : Callback<Responce.RegistrationResponce> {
            override fun onResponse(call: Call<Responce.RegistrationResponce>, response: Response<Responce.RegistrationResponce>) {
                if(response.body().getStatus()==1){
                //    Toast.makeText(activity,""+response.body().getMessage(),Toast.LENGTH_SHORT).show()
                   etFName.setText("")
                      etLName.setText("")
                           etUName.setText("")
                               etDB.setText("")
                                  etEmail.setText("")
                                      etEmail.setText("")
                                            etMobile.setText("")
                                                etMobile.setText("")
                                                   etPwd.setText("")
                                                        etCPassword.setText("")
                                                           etPwd.setText("")
                    etCPassword.setText("")
                    cIvProfilePic.setImageResource(R.drawable.no_img)

                        chTc.setChecked(false)

                spSelectSchool.setSelection(0)

                yearSp.setSelection(0)


                    progressBar.visibility = View.GONE
                 //   LoginRegTabActivity.loginRegTabActivity!!.hideSoftKeyboard()
                    val dialog =  Dialog(context)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_thread_alarm)

                   // val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
                    //tvMsg.setText(response.body().message)

                    val btnNo = dialog.findViewById<Button>(R.id.btnNo)
                  //  btnNo.visibility = View.GONE
                    btnNo.setOnClickListener {
                        dialog.dismiss()
                    }
                    val btnYes = dialog.findViewById<Button>(R.id.btnYes)
                //    btnYes.setText("OK")
                    btnYes.setOnClickListener {
                        dialog.dismiss()
                        LoginRegTabActivity.loginRegTabActivity.tabs.getTabAt(0)!!.select()


                    }
                    dialog.show()


                }  else{
                        progressBar.visibility = View.GONE
                         Toast.makeText(activity,""+response.body().getMessage(),Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<Responce.RegistrationResponce>, t: Throwable) {

                progressBar.visibility = View.GONE

                Toast.makeText(activity,""+t,Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun prepareFilePart(param : String,file: File): MultipartBody.Part{
        return MultipartBody.Part.createFormData(param, file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
    }

   override fun onAttach(context: Context) {
       super.onAttach(context)
       ( context.applicationContext as AppController).getComponent().inject(this@RegisterFragment)
    }
    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun checkMobileNo(mobile: String): Boolean {
        return Pattern.matches("^[7-9][0-9]{9}$", mobile)
    }
    private fun getSchools() {
        apiService.getSchools().enqueue(object : Callback<Responce.CountryResponce> {
            override fun onResponse(call: Call<Responce.CountryResponce>, response: Response<Responce.CountryResponce>) {
                if(response.body().status==1){
                    listSchId = ArrayList<String>()
                    listSchId.add("-1")
                    val  listSchName = ArrayList<String>()
                    listSchName.add("Select School")
                    for( i in response.body().data.getSchoolList()){
                        listSchId.add(i.getId())
                        listSchName.add(i.getName())
                    }

                    val   yOJAdapter = ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listSchName)
                    spSelectSchool.adapter = yOJAdapter
                    spSelectSchool.onItemSelectedListener = sonItemSelectedListener
                }



            }

            override fun onFailure(call: Call<Responce.CountryResponce>, t: Throwable) {
                Toast.makeText(activity,""+t,Toast.LENGTH_SHORT).show()
            }
        })

    }
}