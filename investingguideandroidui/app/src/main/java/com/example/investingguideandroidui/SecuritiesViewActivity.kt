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
import androidx.cardview.widget.CardView
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
    public lateinit var securitiesTabs : TabLayout
    private lateinit var myTabAdapter : SecurityFragmentPagerAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit  var securities_recycler_view: RecyclerView
    private lateinit var securities: ArrayList<Security>
    private lateinit var myOnClickListener: View.OnClickListener
    private lateinit var removedItems: ArrayList<Int>
    private lateinit var webResult : String

    private var bh : Int = 0
    private var bw : Int = 0
    private lateinit var editor : SharedPreferences.Editor
    private lateinit var pref : SharedPreferences
    public lateinit var securityTypes : java.util.ArrayList<String>
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

        setContentView(R.layout.securities_view_main_activity)
        //set onclick listener for return butons
        var topButton : Button = findViewById<Button>(R.id.securities_return_main_button_top)
        topButton.setOnClickListener(this)

        try {

            pref = getSharedPreferences(MainActivity.APP_UNIQUE_ID, Context.MODE_PRIVATE)


            webResult = pref.getString(MainActivity.SAVED_WEB_RESULT_KEY,"{}")!!
            securities = JsonParser().parseString(webResult!!)


            displaySecuritiesList(securities)


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


        //rl.addView(b_top, lparams_top_button)

        scrollView.layoutParams = scrollViewParams
        var no : Int = 1
        for ( s in secs ) {
            var top : Int = 2
            var increment : Int = 38
            currentViewId = View.generateViewId()

            var cv : CardView = CardView(this)
            var rv : RelativeLayout = RelativeLayout(this)
            var rp : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(bw, bh)
            rp.topMargin = bh * no
            cv.layoutParams =  rp
            cv.id = currentViewId
            //rv.setPadding(0, 10, 0, 0)


            var cusip : TextView = TextView(this)
            cusip.setText("CUSIP : " + s.getCusip())
            var cusipRL :  RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(bw,bh)
            cusipRL.topMargin = top

            cv.addView(cusip,cusipRL)
            var securityType : TextView = TextView(this)
            securityType.setText("Security Type : " + s.getSecurityType())

            var secRL : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(bw,bh)
            top += increment
            secRL.topMargin = top
            cv.addView(securityType, secRL)

            var issueDateView : TextView = TextView(this)
            issueDateView.id = View.generateViewId()
            issueDateView.setText("Date Issued : " + s.getIssueDate())
            var tempRL : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(bw, bh)
            tempRL.addRule(RelativeLayout.BELOW, securityType.id)
            top += increment
            tempRL.topMargin = top
            cv.addView(issueDateView, tempRL)


            var pricePer100 : TextView = TextView(this)
            pricePer100.id = View.generateViewId()
            pricePer100.setText("Price Per 100 :  " + s.getPricePer100().toString())

            var pp100params : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(bw, bh)
            pp100params.addRule(RelativeLayout.BELOW, issueDateView.id)
            top += increment
            pp100params.topMargin = top
            cv.addView(pricePer100, pp100params)

            var lparams : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
            lparams.leftMargin = leftMargin
            lparams.topMargin = 2


            no += 1
            if (previousViewId != 0 ) {
                lparams.addRule(RelativeLayout.BELOW, previousViewId)
            }


            cv.setRadius(15f)
            cv.preventCornerOverlap = true
            cv.cardElevation = 18f
            cv.useCompatPadding = true

            rl.addView(cv, lparams)

            previousViewId = currentViewId

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