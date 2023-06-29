package com.example.investingguideandroidui

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.investingguideandroidui.models.Security
import com.example.investingguideandroidui.tabadapters.SecurityFragmentPagerAdapter
import com.example.investingguideandroidui.utilities.JsonParser
import com.example.investingguideandroidui.utilities.SecurityType
import com.google.android.material.tabs.TabLayout
import java.util.HashMap


class SecuritiesViewActivity : AppCompatActivity(), View.OnClickListener {


    private var screenHeight : Int = 0
    private var screenWidth : Int = 0
    private lateinit var pages : ViewPager
    private lateinit var securitiesTabs : TabLayout
    private lateinit var myTabAdapter : SecurityFragmentPagerAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit  var securities_recycler_view: RecyclerView
    private lateinit var securities: ArrayList<Security>
    private lateinit var myOnClickListener: View.OnClickListener
    private lateinit var removedItems: ArrayList<Int>
    private lateinit var webResult : String

    private lateinit var editor : SharedPreferences.Editor
    private lateinit var pref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var extras = this.intent


        setContentView(R.layout.securities_view_main_activity)
        //set onclick listener for return butons
        var topButton : Button = findViewById<Button>(R.id.securities_return_main_button_top)
        topButton.setOnClickListener(this)

        try {

            pref = getSharedPreferences(MainActivity.APP_UNIQUE_ID, Context.MODE_PRIVATE)


            webResult = pref.getString(MainActivity.SAVED_WEB_RESULT_KEY,"{}")!!
            securities = JsonParser().parseString(webResult!!)
            setPagerAndTabs()

        } catch(e : Exception) {
            Log.w(MainActivity.LOG_TAG_EXTERIOR,"Error parsning json object : ${e}. Returning to Previous ")
            //finish()
        }

    }



    fun setPagerAndTabs() {
        if (securities == null || securities.size == 0)
            return


        pages = findViewById<ViewPager>(R.id.pages)
        securitiesTabs = findViewById<TabLayout>(R.id.security_tab_layout)
        myTabAdapter = SecurityFragmentPagerAdapter(this.supportFragmentManager, this, securities)
        //first load the list of securities type into an array list of Strings
        var securityTypes : java.util.ArrayList<String> = java.util.ArrayList<String>()
        val hm : HashMap<String, Int> = SecurityType().securityTypeMapToInt()
        for ( (secType:String, secInt: Int) in hm ) {
            securityTypes.add(secType)
            var tabTitle: String = secType.uppercase()
            securitiesTabs.addTab(securitiesTabs.newTab().setText(tabTitle))
        }

        //Log.w(MainActivity.LOG_TAG_EXTERIOR,"All Security Types : ${securityTypes.toString()}")
        //securitiesTabs.setTabGravity(TabLayout.GRAVITY_FILL)
        pages.adapter = myTabAdapter
        pages.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(securitiesTabs))

        var tl : TabListener = TabListener()
        securitiesTabs.addOnTabSelectedListener( tl )
    }


    fun getFrameLayoutFromSecurities(context: Context, secs : java.util.ArrayList<Security>, secType : String? ) : ScrollView? {
        if (secs.size < 1 || context == null )
            return null

        screenWidth = Resources.getSystem( ).displayMetrics.widthPixels
        screenHeight = Resources.getSystem( ).displayMetrics.heightPixels
        var startTop : Int = (screenHeight/11).toInt()
        var verticalGap : Int = (screenHeight/7).toInt()
        var leftMargin : Int = (screenWidth/15).toInt()
        var rightMargin : Int = leftMargin
        var rl : RelativeLayout = RelativeLayout(context)
        var currentViewId : Int = 0
        var previousViewId : Int = 0
        var scrollView : ScrollView = ScrollView(context)
        var scrollViewParams : TableLayout.LayoutParams = TableLayout.LayoutParams(screenWidth, screenHeight)

        scrollView.layoutParams = scrollViewParams
        var no : Int = 1
        for ( s in secs ) {
            if ( secType != null && s.getSecurityType().lowercase() != secType.lowercase())
                continue
            var top : Int = 0
            var increment : Int = 38
            currentViewId = View.generateViewId()

            var lparams : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT)
            lparams.leftMargin = leftMargin
            lparams.topMargin = 10
            lparams.bottomMargin = 10
            no += 1
            if (previousViewId != 0 ) {
                lparams.addRule(RelativeLayout.BELOW, previousViewId)
            }

            var l : View = LayoutInflater.from(this).inflate(R.layout.security_card_view, null)

            l.id = currentViewId
            var cusipView : TextView = l.findViewById<TextView>(R.id.cusip)
            //cusipView.id = View.generateViewId()
            cusipView.text = "CUSIP: " + s.getCusip()
            var issueDate : TextView = l.findViewById<TextView>(R.id.issueDate)
            issueDate.text = "Issue Date: " + s.getIssueDate().substring(0,10)

            var pricePer100 : TextView = l.findViewById<TextView>(R.id.pricePer100)
            pricePer100.text = "PricePer100: " + s.getPricePer100().toString()


            rl.addView(l, lparams)

            previousViewId = currentViewId
        }
        scrollView.addView(rl)
        return scrollView

    }

    inner class TabListener : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            pages!!.currentItem = tab.position

        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }
    }

    override fun onClick(view: View?) {
        if( view == null )
            return

        finish()
    }


}