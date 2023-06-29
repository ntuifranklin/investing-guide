package com.example.investingguideandroidui.tabadapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.example.investingguideandroidui.MainActivity
import com.example.investingguideandroidui.R
import com.example.investingguideandroidui.SecuritiesViewActivity
import com.example.investingguideandroidui.models.Security
import com.example.investingguideandroidui.utilities.SecurityType

class BillFragment : Fragment {

    public final lateinit var fromActivity: SecuritiesViewActivity

    public lateinit var secures : ArrayList<Security>
    constructor(from: SecuritiesViewActivity, secs: ArrayList<Security>) {
        fromActivity = from
        secures = secs

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var ly : FrameLayout =
            inflater.inflate(R.layout.fragment_bill, container, false) as FrameLayout
        var secType : String = ""
        var hm : HashMap<String,Int> = SecurityType().securityTypeMapToInt()
        for ( (secT:String,secId:Int) in hm ){
            if (secId == MainActivity.BILL)
                secType = secT
        }

        var billsView : ScrollView? = fromActivity.getFrameLayoutFromSecurities(fromActivity,secures,secType)
        ly!!.addView(billsView!!)
        return ly
    }


}