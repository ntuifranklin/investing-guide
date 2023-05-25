package com.example.investingguideandroidui

import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.example.investingguideandroidui.models.Security
import com.example.investingguideandroidui.threadtasks.ReadSecuritiesFromTreasuryDirectWebsite
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewSecurities : Button

    private var screenHeight : Int = 0
    private var screenWidth : Int = 0
    private var bh : Int = 0
    private var bw : Int = 0

    lateinit var startDate : String
    lateinit var endDate : String
    lateinit var formatter : SimpleDateFormat
    lateinit var daysDifference : Period
    lateinit var date : LocalDate
    val BASE_URL : String = "https://www.treasurydirect.gov/TA_WS/securities/auctioned"
    val format : String = "json"
    val dateFieldName : String = "issueDate"
    val securityType : String = "Bill"
    val LOG_TAG : String = "Investingguide"
    var APP_UNIQUE_ID : String = "cbhsuisnzdfui2378348347647641edsjhsh"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // set width and height of screen
        screenWidth = Resources.getSystem( ).displayMetrics.widthPixels
        screenHeight = Resources.getSystem( ).displayMetrics.heightPixels

        // size of cards
        bw = (screenWidth.toFloat()*0.8).toInt()
        bh = (screenHeight/16).toInt()
        // set button width at start

        daysDifference = Period.of(0, 0, 3)
        formatter = SimpleDateFormat("yyyy-MM-dd")
        var currentDate: Date = Date()
        date = LocalDate.of(currentDate.year, currentDate.month, currentDate.day)
        startDate  = date.minus(daysDifference).toString()
        endDate = date.plus(daysDifference).toString()
        Log.w(LOG_TAG, startDate + " : " + endDate)

        viewSecurities = findViewById<Button>(R.id.view_securities_button)
        viewSecurities.setOnClickListener(this)

    }


    fun displaySecuritiesList(secs : ArrayList<Security>) {
        if (secs.size < 1 )
            return

        var startTop : Int = (screenHeight/11).toInt()
        var verticalGap : Int = (screenHeight/7).toInt()
        var leftMargin : Int = (screenWidth/15).toInt()
        var rightMargin : Int = leftMargin
        var rl : RelativeLayout = RelativeLayout(this)
        var currentViewId : Int = 0
        var previousViewId : Int = 0
        var scrollView : ScrollView = ScrollView(this)
        var scrollViewParams : TableLayout.LayoutParams = TableLayout.LayoutParams(screenWidth, screenHeight)

        scrollView.layoutParams = scrollViewParams
        var no : Int = 1
        for ( s in secs ) {
            var top : Int = 0
            var increment : Int = 38
            currentViewId = View.generateViewId()

            var cv : CardView = CardView(this)
            var rv : RelativeLayout = RelativeLayout(this)
            var rp : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(bw, bh)
            rp.topMargin = bh * no
            cv.layoutParams =  rp

            cv.id = currentViewId
            //rv.setPadding(0, 10, 0, 0)

            var lparams : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
            lparams.leftMargin = leftMargin
            lparams.topMargin = 20


            no += 1
            if (previousViewId != 0 ) {
                lparams.addRule(RelativeLayout.BELOW, previousViewId)
            }

            var l : View = LayoutInflater.from(this).inflate(R.layout.security_card_view, null)

            l.id = currentViewId
            var cusipView : TextView = l.findViewById<TextView>(R.id.cusip)
            //cusipView.id = View.generateViewId()
            cusipView.text = "CUSIP: " + s.getCusip()
            var issueDate : TextView  = l.findViewById<TextView>(R.id.issueDate)
            issueDate.text = "Issue Date: " + s.getIssueDate().substring(0,10)

            var pricePer100 : TextView = l.findViewById<TextView>(R.id.pricePer100)
            pricePer100.text = "Price/100: " + s.getPricePer100().toString()

            var imageView : ImageView = l.findViewById<ImageView>(R.id.security_type_image)
            if (s.getSecurityType() == "Bill")
                imageView.setImageResource(R.drawable.bill)
            else if (s.getSecurityType() == "Bond")
                imageView.setImageResource(R.drawable.bond)
            else if (s.getSecurityType() == "Note")
                imageView.setImageResource(R.drawable.note)
            else if (s.getSecurityType() == "TIPPS")
                imageView.setImageResource(R.drawable.tipps)
            else if (s.getSecurityType() == "FRN")
                imageView.setImageResource(R.drawable.frn)
            else if (s.getSecurityType() == "CMB")
                imageView.setImageResource(R.drawable.cmb)
            else
                imageView.setImageResource(R.drawable.resource_default)
            rl.addView(l, lparams)

            previousViewId = currentViewId
        }
        scrollView.addView(rl)
        setContentView(scrollView, scrollViewParams)
    }

    override fun onClick(view: View?) {
        if (view == null )
            return


        if (view == viewSecurities) {
            //Log.w(LOG_TAG,"You clicked on the button")
            var taskThread : ReadSecuritiesFromTreasuryDirectWebsite = ReadSecuritiesFromTreasuryDirectWebsite(this)
            taskThread.start()
        }
    }

    companion object {
        /*
        *
        * */
        // const val url : String = "https://www.treasurydirect.gov/TA_WS/securities/search?format=json&startDate=2023-05-10&endDate=2023-05-23&dateFieldName=issueDate"
        //const val auction_url : String = "https://www.treasurydirect.gov/TA_WS/securities/auctioned?format=json&startDate=2023-05-21&endDate=2023-05-29"
        const val SAVED_WEB_RESULT_KEY : String = "SAVED_WEB_RESULT"
    }

}