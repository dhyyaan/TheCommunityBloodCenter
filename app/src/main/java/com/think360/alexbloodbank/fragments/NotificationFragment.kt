package com.think360.alexbloodbank.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by think360 on 17/11/17.
 */
class NotificationFragment : BaseFragment() {
    @Inject
    internal lateinit var apiService: ApiService
    internal lateinit var rv: RecyclerView
    internal lateinit var swiperefresh: SwipeRefreshLayout
    lateinit var tvTitleNoData : TextView
    override fun getTitle(): String {
      return  "Notifications"
    }

    companion object {
        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater!!.inflate(R.layout.notification_fragment, container, false)
        BloodCenterActivity.bloodCenterActivity!!.select = false
        rv = view.findViewById<RecyclerView>(R.id.rv)
        swiperefresh = view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)

        tvTitleNoData = view.findViewById<TextView>(R.id.tvTitleNoData)

        swiperefresh.setOnRefreshListener{
            notilist()
        }
        notilist()
        return view
    }

    private fun notilist() {
        swiperefresh.setRefreshing(true)
            apiService.notificationlist(AppController.getSharedPref().getString("user_id",""),AppController.getSharedPref().getString("firebase_reg_token","null")).enqueue(object : Callback<Responce.NotificationListResponce> {
                override fun onResponse(call: Call<Responce.NotificationListResponce>, response: Response<Responce.NotificationListResponce>) {
                    if(response.body().getStatus()==1){
                        tvTitleNoData.visibility = View.GONE
                        rv.visibility = View.VISIBLE
                        swiperefresh.setRefreshing(false)
                        val hostAdapter = NotificationAdapter(response.body().getNotificationList())
                        rv.adapter = hostAdapter
                        hostAdapter.notifyDataSetChanged()
                        Utils2.setBadgeCount(BloodCenterActivity.bloodCenterActivity!!.applicationContext, BloodCenterActivity.bloodCenterActivity!!.icon, 0)
                    }else{
                        rv.visibility = View.GONE
                        tvTitleNoData.visibility = View.VISIBLE
                        swiperefresh.setRefreshing(false)
                    }
                }
                override fun onFailure(call: Call<Responce.NotificationListResponce>, t: Throwable) {
                    rv.visibility = View.GONE
                    tvTitleNoData.visibility = View.VISIBLE
                    swiperefresh.setRefreshing(false)
                    Toast.makeText(activity,""+t, Toast.LENGTH_SHORT).show()
                }
            })
        }
        inner class NotificationAdapter(hostList: ArrayList<Responce.NotificationListResponce.Data>) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {
            var arrayList  : ArrayList<Responce.NotificationListResponce.Data>
            init {
                this.arrayList = ArrayList<Responce.NotificationListResponce.Data>()
                this.arrayList = hostList
            }
            inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
                var tvNotTitle : TextView
                var tvdDays: TextView
                var tvdDate: TextView


                init {
                    tvNotTitle = view.findViewById<TextView>(R.id.tvNotTitle) as TextView
                    tvdDays = view.findViewById<TextView>(R.id.tvdDays) as TextView
                    tvdDate = view.findViewById<TextView>(R.id.tvdDate) as TextView

                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.notification_rv_item, parent, false)

                return MyViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
                val data = arrayList.get(position)
                holder.tvNotTitle.setText(data.getMessage())
                holder.tvdDays.setText(data.getGapwith_date())
                holder.tvdDate.setText(data.getNotification_date())



            }

            override fun getItemCount(): Int {
                return arrayList.size
            }
        }
        override fun onAttach(context: Context) {
            super.onAttach(context)
            ( context.applicationContext as AppController).getComponent().inject(this@NotificationFragment)
        }


}