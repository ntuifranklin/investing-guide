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
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class BondFragment : Fragment {
    public final lateinit var fromActivity: SecuritiesViewActivity
    public lateinit var secures : ArrayList<Security>
    lateinit var lseries : LineGraphSeries<DataPoint>
    lateinit var dpoints : Array<DataPoint>

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
            inflater.inflate(R.layout.fragment_bond, container, false)  as FrameLayout

        var secType : String = ""
        var hm : HashMap<String,Int> = SecurityType().securityTypeMapToInt()
        for ( (secT:String,secId:Int) in hm ){
            if (secId == MainActivity.BOND)
                secType = secT
        }

        var bondView : ScrollView? = fromActivity.getFrameLayoutFromSecurities(fromActivity,secures,secType)
        /*
        var dps : Array<DataPoint> = getDataPoints()

        if (ly != null && dps != null && dps.size > 0) {

            var graph : GraphView = ly.findViewById(R.id.bondGraphview)
            var lgs : LineGraphSeries<DataPoint> = LineGraphSeries(dps)
            if (lgs != null )
                graph.addSeries(lgs)
            graph.title = "Graph of Bills"
        }
        */
        ly!!.addView(bondView!!)
        return ly
    }


    fun getDataPoints() : Array<DataPoint> {
        var ds = Array<DataPoint>(secures.size, {
                i -> DataPoint(secures[i].getPricePerDay(), secures[i].getSecurityTermInDays())
        })
        return ds
    }
}