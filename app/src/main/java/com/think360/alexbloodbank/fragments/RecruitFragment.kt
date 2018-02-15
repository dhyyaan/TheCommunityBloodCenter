package com.think360.alexbloodbank.fragments
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.think360.alexbloodbank.BloodCenterActivity
import com.think360.alexbloodbank.R
import com.think360.alexbloodbank.api.AppController
import com.think360.alexbloodbank.api.data.Responce
import com.think360.alexbloodbank.api.interfaces.ApiService
import com.think360.alexbloodbank.fragments.base.BaseFragment
import com.think360.alexbloodbank.utils.RecyclerItemClickListener
import com.think360.alexbloodbank.utils.Utils2
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
/**
 * Created by think360 on 23/11/17.
 */
class RecruitFragment : BaseFragment() {
    @Inject
    internal lateinit var apiService: ApiService
    internal lateinit var rv: RecyclerView
    lateinit var tvTitleNoData : TextView
    internal lateinit var swiperefresh: SwipeRefreshLayout
  //  lateinit var arrayLists :ArrayList<Responce.RecruitListResponce.Data>
    override fun getTitle(): String {
        return "Recruit"
    }

    companion object {
        fun newInstance(): RecruitFragment {
            return RecruitFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater!!.inflate(R.layout.recruit_fragment, container, false)
        BloodCenterActivity.bloodCenterActivity!!.select = true

        rv = view.findViewById<RecyclerView>(R.id.rv)
        swiperefresh = view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        tvTitleNoData = view.findViewById<TextView>(R.id.tvTitleNoData)
        swiperefresh.setOnRefreshListener{
            recruitlist()
        }

        recruitlist()

        return view
    }


    private fun recruitlist() {
        swiperefresh.setRefreshing(true)

        apiService.recruitlist(AppController.getSharedPref().getString("user_id","")).enqueue(object : Callback<Responce.RecruitListResponce> {
            override fun onResponse(call: Call<Responce.RecruitListResponce>, response: Response<Responce.RecruitListResponce>) {
                if(response.body().status==1){
                    tvTitleNoData.visibility = View.GONE
                    rv.visibility = View.VISIBLE
                    swiperefresh.setRefreshing(false)
                    val hostAdapter = HostAdapter(response.body().getRecruitList())
                    rv.adapter = hostAdapter
                    hostAdapter.notifyDataSetChanged()
                }else{
                    rv.visibility = View.GONE
                    tvTitleNoData.visibility = View.VISIBLE
                    swiperefresh.setRefreshing(false)
                }

            }

            override fun onFailure(call: Call<Responce.RecruitListResponce>, t: Throwable) {
                rv.visibility = View.GONE
                tvTitleNoData.visibility = View.VISIBLE
                swiperefresh.setRefreshing(false)
                Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
            }
        })
    }
    inner class HostAdapter(hostList: ArrayList<Responce.RecruitListResponce.Data>) : RecyclerView.Adapter<HostAdapter.MyViewHolder>() {
        var arrayList  : ArrayList<Responce.RecruitListResponce.Data>
        init {
            this.arrayList = ArrayList<Responce.RecruitListResponce.Data>()
            this.arrayList = hostList
        }
        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var tvUserVolunteerHours : TextView

            var ivVeri: ImageView
            var tvVerified: TextView

            var btnFb: TextView
            var btnTweet: TextView

            init {
                tvUserVolunteerHours = view.findViewById<TextView>(R.id.tvUserVolunteerHours) as TextView

                ivVeri = view.findViewById<ImageView>(R.id.ivVeri) as ImageView
                tvVerified = view.findViewById<TextView>(R.id.tvVerified) as TextView

                btnFb = view.findViewById<TextView>(R.id.btnFb) as TextView
                btnTweet = view.findViewById<TextView>(R.id.btnTweet) as TextView
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recruit_rv_list_item, parent, false)

            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val data = arrayList.get(position)
            holder.tvUserVolunteerHours.setText(data.getUserinfo())

            if(data.getPoststatus().equals("0")){
                holder. ivVeri.setImageResource(R.drawable.unverified)
                holder. tvVerified.setText("Verification Pending")
            }else{
                holder. ivVeri.setImageResource(R.drawable.verified)
                holder. tvVerified.setText("Verified")
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
        ( context.applicationContext as AppController).getComponent().inject(this@RecruitFragment)
    }
}