package com.think360.alexbloodbank.fragments
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.think360.alexbloodbank.BloodCenterActivity
import com.think360.alexbloodbank.R
import com.think360.alexbloodbank.api.AppController
import com.think360.alexbloodbank.api.data.Responce
import com.think360.alexbloodbank.api.interfaces.ApiService
import com.think360.alexbloodbank.fragments.base.BaseFragment
import com.think360.alexbloodbank.utils.Utils2
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


/**
 * Created by think360 on 23/11/17.
 */
class DonationFragment : BaseFragment() {
    @Inject
    internal lateinit var apiService: ApiService
    internal lateinit var rv: RecyclerView
    internal lateinit var swiperefresh: SwipeRefreshLayout
    lateinit var tvTitleNoData : TextView
    override fun getTitle(): String {
        return "Donors"
    }

    companion object {
        fun newInstance(): DonationFragment {
            return DonationFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.donors_fragment, container, false)
        BloodCenterActivity.bloodCenterActivity!!.select = true
        rv = view.findViewById<RecyclerView>(R.id.rv)
        swiperefresh = view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)

        tvTitleNoData = view.findViewById<TextView>(R.id.tvTitleNoData)

        swiperefresh.setOnRefreshListener{
            donorslist()
        }

        donorslist()
        /*    rv.addOnItemTouchListener(
                     RecyclerItemClickListener(activity, RecyclerItemClickListener.OnItemClickListener { view, position ->
                         // TODO Handle item click
                         Toast.makeText(activity,""+arrayLists.get(position).getShare_url(),Toast.LENGTH_SHORT).show()
                     })
             )*/
        return view
    }


    private fun donorslist() {

       swiperefresh.setRefreshing(true)
        apiService.donorslist(AppController.getSharedPref().getString("user_id","")).enqueue(object : Callback<Responce.DonorsListResponce> {
            override fun onResponse(call: Call<Responce.DonorsListResponce>, response: Response<Responce.DonorsListResponce>) {
                if (response.body().status == 1) {
                    tvTitleNoData.visibility = View.GONE
                    rv.visibility = View.VISIBLE
                    swiperefresh.setRefreshing(false)
                    val donorsAdapter = DonorsAdapter(response.body().getDonorsList())
                    rv.adapter = donorsAdapter
                    donorsAdapter.notifyDataSetChanged()
                }else{
                    rv.visibility = View.GONE
                    tvTitleNoData.visibility = View.VISIBLE
                    swiperefresh.setRefreshing(false)
                }


            }

            override fun onFailure(call: Call<Responce.DonorsListResponce>, t: Throwable) {
                rv.visibility = View.GONE
                tvTitleNoData.visibility = View.VISIBLE
                swiperefresh.setRefreshing(false)
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
            }
        })
    }

    inner class DonorsAdapter(donorsList: ArrayList<Responce.DonorsListResponce.Data>) : RecyclerView.Adapter<DonorsAdapter.MyViewHolder>() {
     var arrayList: ArrayList<Responce.DonorsListResponce.Data>

        init {
            this.arrayList = ArrayList<Responce.DonorsListResponce.Data>()
            this.arrayList = donorsList
        }

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var tvUserVolunteerHours: TextView
            var tvDate: TextView
            var ivVeri: ImageView
            var tvVerified: TextView
            var btnFb : TextView
            var btnTweet : TextView
            init {
                tvUserVolunteerHours = view.findViewById<TextView>(R.id.tvUserVolunteerHours) as TextView

                tvDate = view.findViewById<TextView>(R.id.tvDate) as TextView
                ivVeri = view.findViewById<ImageView>(R.id.ivVeri) as ImageView
                tvVerified = view.findViewById<TextView>(R.id.tvVerified) as TextView

                btnFb = view.findViewById<TextView>(R.id.btnFb) as TextView
                btnTweet = view.findViewById<TextView>(R.id.btnTweet) as TextView
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.donors_rv_list_item, parent, false)

            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val data = arrayList.get(position)
            holder.tvUserVolunteerHours.setText(data.getUserinfo())
            holder.tvDate.setText(data.getUserpostdate())
            if (data.getPoststatus().equals("0")) {
                holder.ivVeri.setImageResource(R.drawable.unverified)
                holder.tvVerified.setText("Verification Pending")
            } else {
                holder.ivVeri.setImageResource(R.drawable.verified)
                holder.tvVerified.setText("Verified")
            }
            holder.btnFb.setOnClickListener(View.OnClickListener {
               val url =  "https://www.facebook.com/sharer/sharer.php?u="+data.getShare_url()
                Utils2. shareAppLinkViaFacebook(activity.applicationContext,data.getShare_url())
            })
            holder.btnTweet.setOnClickListener(View.OnClickListener {
                val url =  "https://twitter.com/share"+data.getShare_url()
                Utils2. getTwitterIntent(activity,url)
            })

        }

        override fun getItemCount(): Int {
            return arrayList.size
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as AppController).getComponent().inject(this@DonationFragment)
    }


}