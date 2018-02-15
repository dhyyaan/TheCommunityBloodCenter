package com.think360.alexbloodbank.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
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
import com.think360.alexbloodbank.fragments.base.BackButtonSupportFragment
import com.think360.alexbloodbank.fragments.base.BaseFragment
import com.think360.alexbloodbank.utils.Utils2
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.blood_center_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by think360 on 15/11/17.
 */
class HomeFragment : BaseFragment() {

    @Inject
    internal lateinit var apiService: ApiService
    lateinit var tvDonation : TextView
    lateinit var tvVolunteer : TextView
    lateinit var tvRecruit : TextView
    lateinit var tvHost : TextView
    lateinit var progressBar : ProgressBar
    lateinit var tvDCompleted : TextView
    lateinit var tvVCompleted : TextView
    lateinit var tvRCompleted : TextView
    lateinit var tvHCompleted : TextView

    lateinit var ctx : Context
    private var fb =""
    lateinit var card_viewe : CardView
    lateinit var tvUserVolunteerHours : TextView
    lateinit var tvDate : TextView
    lateinit var tvVerified : TextView
    private val compositeDisposable = CompositeDisposable()
    override fun getTitle(): String {
       return "The Community Blood Center"
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view  = inflater!!.inflate(R.layout.home_fragment, container, false)
        BloodCenterActivity.bloodCenterActivity!!.select = true
        compositeDisposable.add(( ctx.applicationContext as AppController).bus().toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { o ->

            if (o is EventToRefresh && o.body == 1001 ) {
                userCounts()
            }
        })

        tvDonation = view.findViewById(R.id.tvDonation)
        tvVolunteer = view.findViewById(R.id.tvVolunteer)
        tvRecruit = view.findViewById(R.id.tvRecruit)
        tvHost = view.findViewById(R.id.tvHost)
        //
        tvDCompleted = view.findViewById(R.id.tvDCompleted)
        tvVCompleted = view.findViewById(R.id.tvVCompleted)
        tvRCompleted = view.findViewById(R.id.tvRCompleted)
        tvHCompleted = view.findViewById(R.id.tvHCompleted)

        tvUserVolunteerHours = view.findViewById(R.id.tvUserVolunteerHours)

        card_viewe = view.findViewById(R.id.card_view)

        tvDate  = view.findViewById(R.id.tvDate)
        tvVerified  = view.findViewById(R.id.tvVerified)

        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

     val   btnFb = view.findViewById<TextView>(R.id.btnFb)
      val  btnTweet = view.findViewById<TextView>(R.id.btnTweet)
        val flDonation  = view.findViewById<FrameLayout>(R.id.flDonation)
        flDonation.setOnClickListener {

            add(DonationFragment.newInstance())

        }


        val flVolunteer  = view.findViewById<FrameLayout>(R.id.flVolunteer)
        flVolunteer.setOnClickListener{
            add(VolunteersFragment.newInstance())

        }

        val flRecruit  = view.findViewById<FrameLayout>(R.id.flRecruit)
        flRecruit.setOnClickListener{
            add(RecruitFragment.newInstance())

        }

        val flHost  = view.findViewById<FrameLayout>(R.id.flHost)
        flHost.setOnClickListener{
            add(HostFragment.newInstance())

        }

        val tvAddNewDonation  = view.findViewById<TextView>(R.id.tvAddNewDonation)
        tvAddNewDonation.setOnClickListener{

            MainFragment.navigatione.selectedItemId = R.id.navigation_donation
        }

        val tvVolunteerHours  = view.findViewById<TextView>(R.id.tvVolunteerHours)
        tvVolunteerHours.setOnClickListener{
            MainFragment.navigatione.selectedItemId = R.id.navigation_hours
        }

        val tvAddRecruitingEfferts  = view.findViewById<TextView>(R.id.tvAddRecruitingEfferts)
        tvAddRecruitingEfferts.setOnClickListener{
            MainFragment.navigatione.selectedItemId = R.id.navigation_efforts
        }

        val tvbloodDriveInfo  = view.findViewById<TextView>(R.id.tvbloodDriveInfo)
        tvbloodDriveInfo.setOnClickListener{
            MainFragment.navigatione.selectedItemId = R.id.navigation_blood
        }

       btnFb.setOnClickListener(View.OnClickListener {
            val url =  "https://www.facebook.com/sharer/sharer.php?u="+fb
            Utils2. shareAppLinkViaFacebook(activity.applicationContext,url)
        })
        btnTweet.setOnClickListener(View.OnClickListener {
            val url =  "https://twitter.com/share"+fb
            Utils2. getTwitterIntent(activity,url)
        })

        userCounts()
        return view
    }
    private fun userCounts() {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        progressBar.setClickable(false)
        apiService.userCounts(AppController.getSharedPref().getString("user_id","")).enqueue(object : Callback<Responce.UserCountsResponce> {
            override fun onResponse(call: Call<Responce.UserCountsResponce>, response: Response<Responce.UserCountsResponce>) {
                if(response.body().getStatus()==1){
                    progressBar.visibility = View.GONE
                    tvDonation.setText(response.body().getData().getDonation())
                    tvVolunteer .setText(response.body().getData().getVoluteer())
                    tvRecruit .setText(response.body().getData().getRecruit())
                    tvHost.setText(response.body().getData().getHost())
                    fb= response.body().getData().getShare_url()

                    if(response.body().getData().getDonation_count().toInt()>=3){
                        tvDCompleted.visibility = View.VISIBLE
                        tvDCompleted.setText("Complete")
                    }else{
                        tvDCompleted.visibility = View.GONE
                    }
                    if(response.body().getData().getVoluteer_count().toInt()>=5){
                        tvVCompleted.visibility = View.VISIBLE
                        tvVCompleted.setText("Complete")
                    }else{
                        tvVCompleted.visibility = View.GONE
                    }
                    if(response.body().getData().getRecruit_count().toInt()>=5){
                        tvRCompleted.visibility = View.VISIBLE
                        tvRCompleted.setText("Complete")
                    }else{
                        tvRCompleted.visibility = View.GONE
                    }
                    if(response.body().getData().getHost_count().toInt()>=1){
                        tvHCompleted.visibility = View.VISIBLE
                        tvHCompleted.setText("Complete")
                    }else{
                        tvHCompleted.visibility = View.GONE
                    }
                    tvUserVolunteerHours .setText(response.body().getData().getUserinfo())
                    tvDate .setText(response.body().getData().getUserpostdate())
                    if(!response.body().getData().getPoststatus().equals("")) {
                        card_viewe.visibility = View.VISIBLE
                        if (response.body().getData().getPoststatus().equals("0")) {
                            tvVerified.setText("Unverified")
                            tvVerified.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unverified, 0, 0, 0);
                        } else {
                            tvVerified.setText("Verified")
                            tvVerified.setCompoundDrawablesWithIntrinsicBounds(R.drawable.verified, 0, 0, 0);
                        }
                    }else{
                        card_viewe.visibility = View.GONE
                    }
                    Utils2.setBadgeCount(ctx.applicationContext, BloodCenterActivity.bloodCenterActivity!!.icon, response.body().getData().getNotificationcount().toInt())
                    AppController.getSharedPref().edit().putInt("getAward",response.body().getData().getCountAward()).apply()
                    (ctx.applicationContext as AppController).bus().send(EventToRefresh(1002))
                }else{
                    progressBar.visibility = View.GONE
                    //Toast.makeText(ctx.applicationContext,response.body().getMessage(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Responce.UserCountsResponce>, t: Throwable) {
                progressBar.visibility = View.GONE
               // Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
            }
        })

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
        ( context.applicationContext as AppController).getComponent().inject(this@HomeFragment)
    }

}