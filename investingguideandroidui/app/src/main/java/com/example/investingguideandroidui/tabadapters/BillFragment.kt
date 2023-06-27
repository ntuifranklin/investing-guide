package com.example.investingguideandroidui.tabadapters

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.example.investingguideandroidui.MainActivity
import com.example.investingguideandroidui.R
import com.example.investingguideandroidui.models.Security
import com.example.investingguideandroidui.utilities.SecurityType
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class BillFragment : Fragment {

    public final lateinit var fromActivity: MainActivity

    public lateinit var secures : ArrayList<Security>
    constructor(from: MainActivity, secs: ArrayList<Security>) {
        fromActivity = from
        secures = secs
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        lateinit var lseries : LineGraphSeries<DataPoint>
        lateinit var dpoints : Array<DataPoint>
        var ly : FrameLayout =
            inflater.inflate(R.layout.fragment_bill, container, false) as FrameLayout
        var hm : HashMap<String,Int> = SecurityType().securityTypeMapToInt()


        var billsView : ScrollView? = fromActivity.getFrameLayoutFromSecurities(fromActivity,secures)

        /*
        var dps : Array<DataPoint> = getDataPoints()

        if (ly != null && dps != null && dps.size > 0) {

            var graph : GraphView = ly.findViewById(R.id.billGraphview)
            var lgs : LineGraphSeries<DataPoint> = LineGraphSeries(dps)
            if (lgs != null )
                graph.addSeries(lgs)
            graph.title = "Graph of Bills"
        }
        */

        ly!!.addView(billsView!!)
        return ly
    }

    fun getDataPoints() : Array<DataPoint> {
        var ds = Array<DataPoint>(secures.size, {
            i -> DataPoint(secures[i].getPricePerDay(), secures[i].getSecurityTermInDays())
        })


        return ds
    }


}