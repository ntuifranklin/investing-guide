package com.example.investingguideandroidui.recyclerviewadapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.example.investingguideandroidui.MainActivity
import com.example.investingguideandroidui.R
import com.example.investingguideandroidui.models.Security
import java.util.*


class SecurityAdapter : RecyclerView.Adapter<SecurityAdapter.ViewHolder> {
    var securities : ArrayList<Security>

    constructor(securities : ArrayList<Security>) {

        this.securities = securities
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val cusipTv = itemView.findViewById<TextView>(R.id.cusip)
        val securityType = itemView.findViewById<TextView>(R.id.securityType)

        val issueDate = itemView.findViewById<TextView>(R.id.issueDate)
        val auctionDate = itemView.findViewById<TextView>(R.id.auctionDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.security_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        //Log.w(MainActivity.LOG_TAG_EXTERIOR,"item count returns ${securities.size}")
        return this.securities.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val security : Security = securities[position]
        holder.issueDate.text = "Issue Date : " + security.getIssueDate()
        holder.cusipTv.text =  "CUSIP : " + security.getCusip()
        holder.securityType.text =  "Security Type : " + security.getSecurityType()
        holder.auctionDate.text =  "Auction Date : " +security.getAuctionDate()

    }
}