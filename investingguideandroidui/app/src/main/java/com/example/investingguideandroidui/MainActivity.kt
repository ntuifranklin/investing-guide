package com.example.investingguideandroidui

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {


    private var screenHeight : Int = 0
    private var screenWidth : Int = 0
    private var bh : Int = 0
    private var bw : Int = 0
    lateinit var startDate : String
    lateinit var endDate : String
    lateinit var formatter : SimpleDateFormat
    val BASE_URL : String = "https://www.treasurydirect.gov/TA_WS/securities"
    var searchRoute : String = AUCTIONED_ROUTE
    val format : String = "json"
    var dateFieldName : String = "issueDate"
    val issueDateFieldName : String = "issueDate"
    val auctionDateFieldName : String = "auctionDate"

    var LOG_TAG : String = "SUPPOSED_TO_BE_LOGGED_TAGGED"
    private lateinit var searchButton : Button
    private lateinit var startDatePicker: DatePicker
    private lateinit var endDatePicker: DatePicker
    private lateinit var securitiesViewIntent : Intent

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
        bh = (screenHeight/16)
        // set button width at start

        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.search_treasury_direct)
        searchButton.setOnClickListener(this)
        startDatePicker = findViewById(R.id.startDate)
        endDatePicker = findViewById(R.id.endDate)
        //createAd()
    }

    override fun onClick(view: View?) {
        if (view != null && view == searchButton ) {

            searchRepositoryAndLoadResult()
        }

    }
    fun searchRepositoryAndLoadResult() {

        val startYear : String = startDatePicker.year.toString()
        val startMonth : String = (startDatePicker.month + 1).toString()
        val startDay : String = startDatePicker.dayOfMonth.toString()

        val endYear : String = endDatePicker.year.toString()
        val endMonth : String = (endDatePicker.month + 1 ).toString()
        val endDay : String = endDatePicker.dayOfMonth.toString()

        startDate  = "${startYear}-${startMonth}-${startDay}"
        endDate = "${endYear}-${endMonth}-${endDay}"
        formatter = SimpleDateFormat("yyyy-MM-dd")
        startDate = formatter.format(formatter.parse(startDate)!!)
        endDate = formatter.format(formatter.parse(endDate)!!)



        //Log.w(MainActivity.LOG_TAG_EXTERIOR,"start date : ${startDate} end date : ${endDate}")

        val auctionOrSearchBtn : RadioGroup = findViewById(R.id.auctionOrSearch)


        val checkedButtonId : Int = auctionOrSearchBtn.checkedRadioButtonId
        val btnChecked : Button = findViewById(checkedButtonId)


        if (btnChecked.id == R.id.auctionedChecked){
            dateFieldName = auctionDateFieldName
            searchRoute = AUCTIONED_ROUTE
        }
        else if (btnChecked.id == R.id.upComingChecked){
            dateFieldName = issueDateFieldName
            searchRoute = DEFAULT_SEARCH_ROUTE

        }
        securitiesViewIntent = Intent( this, SecuritiesViewActivity::class.java )
        securitiesViewIntent.putExtra(INTER_ACTIVITY_START_DATE_KEY,startDate)
        securitiesViewIntent.putExtra(INTER_ACTIVITY_END_DATE_KEY,endDate)
        securitiesViewIntent.putExtra(DATE_FIELD_NAME_SEARCH_BY_KEY,dateFieldName)
        securitiesViewIntent.putExtra(SEARCH_ROUTE_KEY, searchRoute)

        startActivity(securitiesViewIntent)


    }

    fun test(title : String = "Testing "){

        val t : String = LOG_TAG_EXTERIOR!!

        Log.w(t, "########### Testing : $title ############## ")


        Log.w(t, "########### End of Testing ############## ")

    }

    fun createAd( ) {
        val adView : AdView = findViewById<AdView>(R.id.googleAdvertisement)


        val adRequestBuilder :AdRequest.Builder = AdRequest.Builder( )
        var adRequest : AdRequest = adRequestBuilder.build()

        try {
            adView.loadAd(adRequest)
        } catch( e : Exception) {
            Log.w(LOG_TAG_EXTERIOR, "Error loading the google ad")
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
        const val NOTE : Int = 0
        const val BILL : Int = 1
        const val BOND : Int = 2
        const val FRN : Int = 3
        const val TIPS : Int = 4
        const val CMB : Int = 5
        const val AUCTIONED_ROUTE : String = "auctioned"
        const val DEFAULT_SEARCH_ROUTE : String = "search"
        const val SEARCH_ROUTE_KEY : String = "searchRoute"
        const val WEB_RESULT_KEY : String = "webResult"
        const val APP_UNIQUE_ID : String = "cbhsuisnzdfui2378348347647641edsjhsh"
        const val INTER_ACTIVITY_START_DATE_KEY = "startDate"
        const val INTER_ACTIVITY_END_DATE_KEY = "endDate"

        const val DATE_FIELD_NAME_SEARCH_BY_KEY = "dateFieldSearchBy"

        const val DEFAULT_DATE_FIELD_NAME_SEARCH_BY_VALUE = "issueDate"
        const val SECURITY_TYPE_KEY = "securityType"
        const val DEFAULT_SECURITY_TYPE = "Bill"

    }

}