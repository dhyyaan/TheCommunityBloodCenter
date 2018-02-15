package com.think360.alexbloodbank.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.ParseException
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


import com.mikhaellopez.circularimageview.CircularImageView


import com.think360.alexbloodbank.BloodCenterActivity
import com.think360.alexbloodbank.LoginRegTabActivity
import com.think360.alexbloodbank.R

import com.think360.alexbloodbank.api.AppController
import com.think360.alexbloodbank.api.EventToRefresh
import com.think360.alexbloodbank.api.data.Responce
import com.think360.alexbloodbank.api.interfaces.ApiService
import com.think360.alexbloodbank.fragments.base.BaseFragment
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe.subscribe
import io.reactivex.internal.util.HalfSerializer.onNext
import kotlinx.android.synthetic.main.activity_loginregister.*
import kotlinx.android.synthetic.main.nav_header_blood_center.*
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

/**
 * Created by think360 on 17/11/17.
 */
class EditProfileFragment  : BaseFragment() {
    @Inject
    internal lateinit var apiService: ApiService
    private var profile_pic: MultipartBody.Part? = null
    lateinit var  cIvProfilePic : CircularImageView
    lateinit var  spSelectSchool : Spinner

    lateinit var  listSchId : ArrayList<String>
    lateinit var  listSchName : ArrayList<String>
    lateinit var  years : ArrayList<String>
  //  private var schId : String = ""
   // private var schYear : String = ""
    private var sms : String = "0"
    lateinit var  yearSp : Spinner

    lateinit var  fName : TextInputEditText
    lateinit var  lName : TextInputEditText
    lateinit var  dob : TextInputEditText
    lateinit var  tvBtnEdit : TextView
    lateinit var  chTc : CheckBox
    lateinit var progressBar : ProgressBar
    private var selDate : String = ""
    lateinit var  etSchool : TextInputEditText
    private var schId : String = ""
    override fun getTitle(): String {
        return "Edit Profile"
    }

    companion object {
        fun newInstance(): EditProfileFragment {
            return EditProfileFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.edit_profile_fragment, container, false)
        BloodCenterActivity.bloodCenterActivity!!.select = true
        progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)
        tvBtnEdit = rootView.findViewById<TextView>(R.id.tvBtnEdit)

        fName = rootView.findViewById<TextInputEditText>(R.id.etFName)
        lName = rootView.findViewById<TextInputEditText>(R.id.etLName)
        dob = rootView.findViewById<TextInputEditText>(R.id.etDob)
        etSchool = rootView.findViewById<TextInputEditText>(R.id.etSchool)
        chTc = rootView.findViewById<CheckBox>(R.id.cbSmsCapabilities)
        chTc.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
            if(b){
                sms = "1"
            }else{
                sms = "0"
            }

        }

        val btnProfileIEdit = rootView.findViewById<Button>(R.id.ivProfileImageEdit)

        cIvProfilePic = rootView.findViewById<CircularImageView>(R.id.cIvProfilePic)
        btnProfileIEdit.setOnClickListener{
            val setup = PickSetup().setTitle("Choose")
                    .setTitleColor(R.color.colorAccent)
                    .setSystemDialog(false)

            PickImageDialog.build(setup) { r ->
                Log.e("RESULT", r.getPath())
                if (r.getError() == null) {
                    r.getBitmap()
                    r.getError();
                  //  profile_pic =  prepareFilePart("profile_pic",File(r.path).absoluteFile)

                  //  cIvProfilePic.setImageBitmap( r.bitmap)
                  Luban.compress(activity, File(r.getPath()).absoluteFile)
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

                        if(!TextUtils.isEmpty(dob.text.toString())){
                            editUser()

                        }else{
                            Toast.makeText(activity,"DOB can't be empty",Toast.LENGTH_SHORT).show()
                        }

                }else{
                    Toast.makeText(activity,"Last Name can't be empty",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(activity,"First Name can't be empty",Toast.LENGTH_SHORT).show()
            }
        }
        years =  ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)

        for (i in 1..13) {
            years.add(Integer.toString(thisYear+i))
        }
        yearSp = rootView.findViewById<Spinner>(R.id.spGraYear)

        val   yOJAdapter = ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, years)
        yearSp.adapter = yOJAdapter



        spSelectSchool = rootView.findViewById<Spinner>(R.id.spSelectSchool)
        spSelectSchool.setEnabled(false)
        yearSp.setEnabled(false)
        tvBtnEdit.setOnClickListener {
            fName.isEnabled = true
            lName.isEnabled = true
            dob.isEnabled = true
            chTc.setEnabled(true)
            btnProfileIEdit.setEnabled(true)
            spSelectSchool.setEnabled(true)
            yearSp.setEnabled(true)
            etSchool.setEnabled(true)
            btnSubmit.setEnabled(true)
            btnSubmit.setBackgroundResource(R.drawable.custom_button)

        }

        dob.setOnClickListener {
            val mcurrentDate = Calendar.getInstance()
            val mYear = mcurrentDate.get(Calendar.YEAR)
            val mMonth = mcurrentDate.get(Calendar.MONTH)
            val mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH)

            val mDatePicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                selDate =  ""+selectedyear+ "-" + (selectedmonth + 1) + "-" +selectedday
                dob.setText(""+(selectedmonth + 1) + "/" +selectedday.toString() + "/" + selectedyear )}, mYear, mMonth, mDay)

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
    private fun editUser() {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        progressBar.setClickable(false)

        val useridBD = RequestBody.create(MediaType.parse("text/plain"), AppController.getSharedPref().getString("user_id",""))
        val fname = RequestBody.create(MediaType.parse("text/plain"), etFName.text.toString())
        val lname = RequestBody.create(MediaType.parse("text/plain"), etLName.text.toString())
        val dob = RequestBody.create(MediaType.parse("text/plain"), selDate)


        val sms = RequestBody.create(MediaType.parse("text/plain"), ""+sms)
        val pos = spSelectSchool.selectedItemPosition
        val id =   listSchId.get(pos)
        val school = RequestBody.create(MediaType.parse("text/plain"), id)
        val schYear = yearSp.selectedItem.toString()
        val schoolyear = RequestBody.create(MediaType.parse("text/plain"), schYear)
        var etSchools = RequestBody.create(MediaType.parse("text/plain"), "")

        if(schId.equals("0")){
            etSchools = RequestBody.create(MediaType.parse("text/plain"), etSchool.text.toString())
        }

        apiService.editprofile(useridBD,fname,lname,dob,sms,
                school,schoolyear,profile_pic,etSchools).enqueue(object : Callback<Responce.EditProfileResponce> {
            override fun onResponse(call: Call<Responce.EditProfileResponce>, response: Response<Responce.EditProfileResponce>) {
                if(response.body().getStatus()==1){
                    (activity!!.applicationContext as AppController).bus().send(EventToRefresh(1000))
                    progressBar.visibility = View.GONE
                    BloodCenterActivity.bloodCenterActivity!!.hideSoftKeyboard()
                    Toast.makeText(activity,""+response.body().getMessage(),Toast.LENGTH_SHORT).show()
                    activity!!.onBackPressed()

                }  else{

                    progressBar.visibility = View.GONE

                    Toast.makeText(activity,""+response.body().getMessage(),Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<Responce.EditProfileResponce>, t: Throwable) {

                progressBar.visibility = View.GONE

                Toast.makeText(activity,""+t,Toast.LENGTH_SHORT).show()
            }
        })

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ( context.applicationContext as AppController).getComponent().inject(this@EditProfileFragment)
    }

    private fun getSchools() {

        apiService.getSchools().enqueue(object : Callback<Responce.CountryResponce> {
            override fun onResponse(call: Call<Responce.CountryResponce>, response: Response<Responce.CountryResponce>) {
                if(response.body().getStatus()==1){
                    listSchId = ArrayList<String>()
                    listSchName = ArrayList<String>()
                    for( i in response.body().data.getSchoolList()){
                        listSchId.add(i.getId())
                        listSchName.add(i.getName())
                    }

                    val   yOJAdapter = ArrayAdapter<String>(BloodCenterActivity.bloodCenterActivity, android.R.layout.simple_list_item_1, listSchName)
                    spSelectSchool.adapter = yOJAdapter
                    yOJAdapter.notifyDataSetChanged()

                    showProfile()
                }



            }

            override fun onFailure(call: Call<Responce.CountryResponce>, t: Throwable) {
                Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun prepareFilePart(param : String,file: File): MultipartBody.Part{
        return MultipartBody.Part.createFormData(param, file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
    }
    private fun showProfile() {

        apiService.showProfile(AppController.getSharedPref().getString("user_id","")).enqueue(object : Callback<Responce.ShowProfileResponce> {
            override fun onResponse(call: Call<Responce.ShowProfileResponce>, response: Response<Responce.ShowProfileResponce>) {
                if(response.body().getStatus()==1){

                    fName.setText(response.body().getData().getFirst_name())
                    lName.setText(response.body().getData().getLast_name())

                  val date_after = formateDateFromstring("yyyy-MM-dd", "MM/dd/yyyy", response.body().getData().getDob())
                    //val date = dateFormat.parse(response.body().getData().getDob())

                    dob.setText(date_after)

                    selDate=response.body().getData().getDob()

                            Glide.with(activity.applicationContext)
                            .load(response.body().getData().getImage())
                            .apply( RequestOptions()
                                    .override(120,120)
                                    .placeholder(R.drawable.no_img)
                                    .centerCrop()
                                    .dontAnimate()
                                    .dontTransform())
                            .into(cIvProfilePic)


                     schId  = response.body().getData().getSchool()
if(schId.equals("0")){
    etSchool.visibility = View.VISIBLE
    val schooName = response.body().getData().getSchoolName()
    etSchool.setText(schooName)
    spSelectSchool.setSelection(listSchName.indexOf("Other"))
}else{
    spSelectSchool.setSelection(listSchName.indexOf(response.body().getData().getSchoolName()))
}

                    // schYear  = response.body().getData().getSchoolYear()
                    sms = response.body().getData().getSms()
                    if(response.body().getData().getSms().equals("1")){
                        chTc.setChecked(true)
                    }else{
                        chTc.setChecked(false)
                    }


                   yearSp.setSelection(years.indexOf(response.body().getData().getSchoolYear()))
                    spSelectSchool.onItemSelectedListener = sonItemSelectedListener
                }



            }

            override fun onFailure(call: Call<Responce.ShowProfileResponce>, t: Throwable) {
                Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun formateDateFromstring(inputFormat: String, outputFormat: String, inputDate: String): String {

        var parsed: Date? = null
        var outputDate = ""

        val df_input = SimpleDateFormat(inputFormat, java.util.Locale.getDefault())
        val df_output = SimpleDateFormat(outputFormat, java.util.Locale.getDefault())

        try {
            parsed = df_input.parse(inputDate)
            outputDate = df_output.format(parsed)

        } catch (e: ParseException) {
            //LOGE(TAG, "ParseException - dateFormat")
        }

        return outputDate

    }
}