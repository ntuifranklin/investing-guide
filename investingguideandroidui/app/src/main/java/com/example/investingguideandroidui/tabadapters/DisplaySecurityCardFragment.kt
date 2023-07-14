package com.example.investingguideandroidui.tabadapters


import android.os.Bundle
import android.util.Log
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

class DisplaySecurityCardFragment : Fragment {

    public final lateinit var fromActivity: SecuritiesViewActivity
    var securityId: Int = 0
    public lateinit var secures : ArrayList<Security>
    lateinit var secType : String
    constructor(from: SecuritiesViewActivity, secs: ArrayList<Security>, secId: Int) {
        fromActivity = from
        secures = secs
        securityId = secId
        //Log.w(MainActivity.LOG_TAG_EXTERIOR,"Received these in DisplayFragment ${secures}")

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var ly : FrameLayout =
            inflater.inflate(R.layout.fragment_bill, container, false) as FrameLayout
        secType = ""
        if (securityId == MainActivity.BILL)
        {
            secType = "bill"
        } else if  (securityId == MainActivity.NOTE)
        {
            secType = "note"
        } else if  (securityId == MainActivity.TIPS)
        {
            secType = "tips"
        } else if  (securityId == MainActivity.CMB)
        {
            secType = "cmb"
        } else if  (securityId == MainActivity.FRN)
        {
            secType = "frn"
        } else if  (securityId == MainActivity.BOND)
        {
            secType = "bond"
        }


        return ly
    }


}