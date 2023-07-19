package com.example.investingguideandroidui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.example.investingguideandroidui.models.Security
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*


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
    val BASE_URL : String = "https://www.treasurydirect.gov/TA_WS/securities"
    var searchRoute : String = AUCTIONED_ROUTE
    val format : String = "json"
    var dateFieldName : String = "issueDate"
    val issueDateFieldName : String = "issueDate"
    val auctionDateFieldName : String = "auctionDate"

    var LOG_TAG : String = "SUPPOSED_TO_BE_LOGGED_TAGGED"
    private lateinit var securities : ArrayList<Security>
    private lateinit var editor : SharedPreferences.Editor
    private lateinit var pref : SharedPreferences
    private lateinit var searchButton : Button
    private lateinit var startDatePicker: DatePicker
    private lateinit var endDatePicker: DatePicker
    private lateinit var webResult : String
    private lateinit var securitiesViewIntent : Intent
    private lateinit var securityTypeSpinner : Spinner

    /*
    *  Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //test(" UpperCase and Lowercase transformations ")

        LOG_TAG = resources.getString(R.string.log_tag)
        LOG_TAG_EXTERIOR = LOG_TAG
        // set width and height of screen
        screenWidth = Resources.getSystem( ).displayMetrics.widthPixels
        screenHeight = Resources.getSystem( ).displayMetrics.heightPixels

        // size of cards
        bw = (screenWidth.toFloat()*0.8).toInt()
        bh = (screenHeight/16).toInt()
        // set button width at start

        setContentView(R.layout.activity_main)
        createAd()

        searchButton = findViewById(R.id.search_treasury_direct)
        searchButton.setOnClickListener(this)

        startDatePicker = findViewById(R.id.startDate)
        endDatePicker = findViewById(R.id.endDate)


    }



    override fun onClick(view: View?) {
        if (view != null && view == searchButton ) {

            searchRepositoryAndLoadResult()
        }

    }
    fun searchRepositoryAndLoadResult() {

        var startYear : String = startDatePicker.year.toString()
        var startMonth : String = (startDatePicker.month + 1).toString()
        var startDay : String = startDatePicker.dayOfMonth.toString()

        var endYear : String = endDatePicker.year.toString()
        var endMonth : String = (endDatePicker.month + 1 ).toString()
        var endDay : String = endDatePicker.dayOfMonth.toString()

        startDate  = "${startYear}-${startMonth}-${startDay}"
        endDate = "${endYear}-${endMonth}-${endDay}"
        formatter = SimpleDateFormat("yyyy-MM-dd")
        startDate = formatter.format(formatter.parse(startDate))
        endDate = formatter.format(formatter.parse(endDate))



        //Log.w(MainActivity.LOG_TAG_EXTERIOR,"start date : ${startDate} end date : ${endDate}")

        var auctionOrSearchBtn : RadioGroup = findViewById(R.id.auctionOrSearch)
        if (auctionOrSearchBtn == null )
            return

        var checkedButtonId : Int = auctionOrSearchBtn.checkedRadioButtonId
        var btnChecked : Button = findViewById(checkedButtonId)
        if (btnChecked == null)
            return

        if (btnChecked.id == R.id.auctionedChecked){
            dateFieldName = auctionDateFieldName
            searchRoute = AUCTIONED_ROUTE
        }
        else if (btnChecked.id == R.id.upComingChecked){
            dateFieldName = issueDateFieldName
            searchRoute = DEFAULT_SEARCH_ROUTE

        }
        securitiesViewIntent = Intent( this, SecuritiesViewActivity::class.java )
        securitiesViewIntent.putExtra("$INTER_ACTIVITY_START_DATE_KEY",startDate)
        securitiesViewIntent.putExtra("$INTER_ACTIVITY_END_DATE_KEY",endDate)
        securitiesViewIntent.putExtra("$DATE_FIELD_NAME_SEARCH_BY_KEY",dateFieldName)
        securitiesViewIntent.putExtra("$SEARCH_ROUTE_KEY", searchRoute)

        startActivity(securitiesViewIntent)


    }

    fun test(title : String = "Testing "){

        var t : String? = LOG_TAG_EXTERIOR!!
        if ( t == null )
            return
        Log.w(t, "########### Testing : $title ############## ")
        var s : String = "note"
        //Log.w(t, "s.uppercase : ${s.uppercase()}, s : $s")
        //Log.w(t, "s.toUpperCase : ${s.toUpperCase()}, s : $s")


        Log.w(t, "########### End of Testing ############## ")

    }

    fun createAd( ) : Unit {
        var adView : AdView = findViewById<AdView>(R.id.googleAdvertisement)


        var adRequestBuilder :AdRequest.Builder = AdRequest.Builder( )
        /*
        adRequestBuilder.addKeyword("TreasuryDirect")
        adRequestBuilder.addKeyword("Treasury Bond")
        adRequestBuilder.addKeyword("Treasury Bill")
        adRequestBuilder.addKeyword("Treasury TIPS")
        adRequestBuilder.addKeyword("Treasury Bond")

         */
        adRequestBuilder.addKeyword("Bonds")
        adRequestBuilder.addKeyword("Savings")
        adRequestBuilder.addKeyword("Investment")
        adRequestBuilder.addKeyword("Stocks")
        adRequestBuilder.addKeyword("Notes")


        var adRequest : AdRequest = adRequestBuilder.build()

        try {
            adView.loadAd(adRequest)
        } catch( e : Exception) {
            Log.w(MainActivity.LOG_TAG_EXTERIOR, "Error loading the google ad")
        }
    }


    companion object {
        /*
        *
        * */
        // const val url : String = "https://www.treasurydirect.gov/TA_WS/securities/search?format=json&startDate=2023-05-10&endDate=2023-05-23&dateFieldName=issueDate"
        //const val auction_url : String = "https://www.treasurydirect.gov/TA_WS/securities/auctioned?format=json&startDate=2023-05-21&endDate=2023-05-29"
        const val SAVED_WEB_RESULT_KEY : String = "SAVED_WEB_RESULT"
        var LOG_TAG_EXTERIOR : String = "InvestingGuideLOG_TAG_NOT_SET"
        val NOTE : Int = 0
        val BILL : Int = 1
        val BOND : Int = 2
        val FRN : Int = 3
        val TIPS : Int = 4
        val CMB : Int = 5
        val AUCTIONED_ROUTE : String = "auctioned"
        val DEFAULT_SEARCH_ROUTE : String = "search"
        val SEARCH_ROUTE_KEY : String = "searchRoute"
        val WEB_RESULT_KEY : String = "webResult"
        val APP_UNIQUE_ID : String = "cbhsuisnzdfui2378348347647641edsjhsh"
        val INTER_ACTIVITY_START_DATE_KEY = "startDate"
        val INTER_ACTIVITY_END_DATE_KEY = "endDate"

        val DATE_FIELD_NAME_SEARCH_BY_KEY = "dateFieldSearchBy"

        val DEFAULT_DATE_FIELD_NAME_SEARCH_BY_VALUE = "issueDate"
        val SECURITY_TYPE_KEY = "securityType"
        val DEFAULT_SECURITY_TYPE = "Bill"

    }

}