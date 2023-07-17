package com.example.investingguideandroidui

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.investingguideandroidui.models.Security
import com.example.investingguideandroidui.recyclerviewadapters.SecurityAdapter
import com.example.investingguideandroidui.tabadapters.SecurityFragmentPagerAdapter
import com.example.investingguideandroidui.utilities.JsonParser
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import java.util.*


class SecuritiesViewActivity : AppCompatActivity(), View.OnClickListener {


    private var screenHeight : Int = 0
    private var screenWidth : Int = 0
    private lateinit var pages : ViewPager
    public lateinit var securitiesTabs : TabLayout
    private lateinit var myTabAdapter : SecurityFragmentPagerAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var securities_recycler_view : RecyclerView
    private lateinit var securities: ArrayList<Security>
    private lateinit var myOnClickListener: View.OnClickListener
    private lateinit var removedItems: ArrayList<Int>
    private lateinit var webResult : String
    private var bh : Int = 0
    private var bw : Int = 0
    private lateinit var editor : SharedPreferences.Editor
    private lateinit var pref : SharedPreferences
    public lateinit var securityTypes : java.util.ArrayList<String>
    lateinit var securityAdapter : SecurityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenWidth = Resources.getSystem( ).displayMetrics.widthPixels
        screenHeight = Resources.getSystem( ).displayMetrics.heightPixels

        // size of cards
        bw = (screenWidth.toFloat()*0.8).toInt()
        bh = (screenHeight/16).toInt()
        var extras = this.intent
        if (extras == null ) {
            Log.w(MainActivity.LOG_TAG_EXTERIOR, "In ${localClassName} Previous Activity is " +
                    "supposed to pass data to this Activity. Failed, returning ")
            finish()
        }



        try {

            pref = getSharedPreferences(MainActivity.APP_UNIQUE_ID, Context.MODE_PRIVATE)


            webResult = pref.getString(MainActivity.SAVED_WEB_RESULT_KEY,"{}")!!
            securities = JsonParser().parseString(webResult!!)

            setContentView(R.layout.display_securities_view)
            // set onclick listener for return buttons
            securities_recycler_view = findViewById(R.id.securities_recycler_view)
            securities_recycler_view.layoutManager = LinearLayoutManager(this)
            securityAdapter = SecurityAdapter(securities)
            securities_recycler_view.adapter = securityAdapter


        } catch(e : Exception) {
            Log.w(MainActivity.LOG_TAG_EXTERIOR,"Error parsing json object : ${e.printStackTrace()}. Returning to Previous ")
            finish()
        }

    }





    fun displaySecuritiesList(secs : ArrayList<Security>) {
        if (secs.size == 0 )
            return

        var startTop : Int = (screenHeight/11).toInt()
        var verticalGap : Int = (screenHeight/7).toInt()
        var leftMargin : Int = (screenWidth/15).toInt()
        var rightMargin : Int = leftMargin
        var rl : RelativeLayout = RelativeLayout(this)
        var currentViewId : Int = 0
        var previousViewId : Int = 0
        var scrollView : ScrollView = ScrollView(this)
        scrollView.id = View.generateViewId()
        //add go back button at top
        var scrollViewParams : TableLayout.LayoutParams = TableLayout.LayoutParams(screenWidth, screenHeight)

        var layoutInflater : LayoutInflater = LayoutInflater.from(this)

        previousViewId = -1
        scrollView.layoutParams = scrollViewParams
        var no : Int = 1
        for ( s in secs ) {
            currentViewId = View.generateViewId()
            var lineLayout : LinearLayout = LinearLayout(this)

            var cv : MaterialCardView = MaterialCardView(ContextThemeWrapper(this, R.style.SecurityCardView))
            cv.id = currentViewId
            var relativeLayoutParams : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
            var cusip : TextView = TextView(this)
            //cusip.layoutParams = linearLayoutParams
            cusip.setText("CUSIP : " + s.getCusip())
            cusip.id = View.generateViewId()

            var cusiplp : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)


            cv.addView(cusip, cusiplp)


            var securityType : TextView = TextView(this)
            securityType.id = View.generateViewId()
            securityType.setText("Security Type : " + s.getSecurityType())

            var securityTypelp : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
             securityTypelp.addRule(RelativeLayout.BELOW, cusip.id)
            //securityType.layoutParams = linearLayoutParams

            cv.addView(securityType, securityTypelp)


            var issueDatelp : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)

            var issueDateView : TextView = TextView(this)
            issueDateView.id = View.generateViewId()
            issueDateView.setText("Date Issued : " + s.getIssueDate())
            issueDatelp.addRule(RelativeLayout.BELOW,securityType.id)
            //issueDateView.layoutParams = linearLayoutParams

            cv.addView(issueDateView, issueDatelp)
            if (previousViewId != -1 ) {
                relativeLayoutParams.addRule(RelativeLayout.BELOW,previousViewId)

            }
            previousViewId = currentViewId
            relativeLayoutParams.topMargin = 2
            rl.addView(cv,relativeLayoutParams)
        }


        scrollView.addView(rl)
        setContentView(scrollView, scrollViewParams)
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