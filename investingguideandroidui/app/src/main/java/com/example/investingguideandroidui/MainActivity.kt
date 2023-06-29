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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.investingguideandroidui.models.Security
import com.example.investingguideandroidui.tabadapters.SecurityFragmentPagerAdapter
import com.example.investingguideandroidui.threadtasks.ReadSecuritiesFromTreasuryDirectWebsite
import com.example.investingguideandroidui.utilities.JsonParser
import com.example.investingguideandroidui.utilities.SecurityType
import com.google.android.material.tabs.TabLayout
import org.json.JSONException
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
    val format : String = "json"
    val dateFieldName : String = "issueDate"
    val issueDateFieldName : String = "issueDate"
    val auctionDateFieldName : String = "auctionDate"

    val securityType : String = "Bill"
    var LOG_TAG : String = "SUPPOSED_TO_BE_LOGGED_TAGGED"
    private lateinit var securities : ArrayList<Security>
    private lateinit var editor : SharedPreferences.Editor
    private lateinit var pref : SharedPreferences
    private lateinit var searchButton : Button
    private lateinit var startDatePicker: DatePicker
    private lateinit var endDatePicker: DatePicker
    private lateinit var webResult : String
    private lateinit var securitiesViewIntent : Intent

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

        //identify the button to be clicked to search for securities
        searchButton = findViewById(R.id.search_treasury_direct)
        searchButton.setOnClickListener(this)

        startDatePicker = findViewById(R.id.startDate)
        endDatePicker = findViewById(R.id.endDate)

        //retrieve any saved data


    }

    fun loadSavedResult() {

        if (pref == null )
            pref = getSharedPreferences(APP_UNIQUE_ID, Context.MODE_PRIVATE)

        var oldWebResult : String? = pref.getString(MainActivity.SAVED_WEB_RESULT_KEY,"")
        if (oldWebResult != null && oldWebResult!!.length > 0 )  {
            Log.w(LOG_TAG,"Using old data saved")
            saveWebResult(oldWebResult!!)

        }
    }


    override fun onClick(view: View?) {
        if (view != null && view == searchButton ) {
            Log.w(LOG_TAG_EXTERIOR, "Clicked on search button")

            //go online and get securities in JSON
            searchTreasuryDirect()
        }

    }
    fun searchTreasuryDirect() {

        var startYear : String = startDatePicker.year.toString()
        var startMonth : String = (startDatePicker.month + 1).toString()
        var startDay : String = startDatePicker.dayOfMonth.toString()

        var endYear : String = endDatePicker.year.toString()
        var endMonth : String = (endDatePicker.month + 1 ).toString()
        var endDay : String = endDatePicker.dayOfMonth.toString()

        startDate  = "${startYear}-${startMonth}-${startDay}"
        endDate = "${endYear}-${endMonth}-${endDay}"

        var grpBtn : RadioGroup = findViewById(R.id.auctionOrSearch)
        if (grpBtn == null )
            return

        var checkedButtonId : Int = grpBtn.checkedRadioButtonId
        var btnChecked : Button = findViewById(checkedButtonId)
        if (btnChecked == null)
            return
        var searchRoute : String = AUCTIONED_ROUTE
        if (btnChecked.id == R.id.auctionedChecked)
            searchRoute = AUCTIONED_ROUTE
        else if (btnChecked.id == R.id.upComingChecked)
            searchRoute = SEARCH_ROUTE

        var taskThread : ReadSecuritiesFromTreasuryDirectWebsite =
            ReadSecuritiesFromTreasuryDirectWebsite(this, search_route=searchRoute)
        taskThread.start()


    }

    fun test(title : String = "Testing "){

        var t : String? = LOG_TAG_EXTERIOR!!
        if ( t == null )
            return
        Log.w(t, "########### Testing : $title ############## ")
        var s : String = "note"
        Log.w(t, "s.uppercase : ${s.uppercase()}, s : $s")
        Log.w(t, "s.toUpperCase : ${s.toUpperCase()}, s : $s")


        Log.w(t, "########### End of Testing ############## ")

    }

    fun setSecuritiesList(securities : ArrayList<Security>) {
        if ( securities != null )
            this.securities = securities
    }


    fun saveWebResult(webResult : String ) {

        if ( webResult == null && webResult.length < 2)
            return
        try {
            var jsonParser : JsonParser = JsonParser()
            this.webResult = webResult

            pref = getSharedPreferences(APP_UNIQUE_ID, Context.MODE_PRIVATE)
            editor = pref.edit()
            editor.putString(SAVED_WEB_RESULT_KEY, this.webResult)
            editor.commit()


            Log.w(LOG_TAG_EXTERIOR,"Received From The Internet : ${webResult}")
            goToSecuritiesViewActivities()
        }  catch ( e: JSONException) {

            Log.w(LOG_TAG,"JsonParser failing for ${webResult}")
            finish()
        }

    }


    fun goToSecuritiesViewActivities( ) {
        securitiesViewIntent = Intent( this, SecuritiesViewActivity::class.java )
        startActivity(securitiesViewIntent)

    }


    inner class CustomFragment : Fragment {

        constructor() {

        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            var frameLayout : FrameLayout = FrameLayout(requireContext())

            return frameLayout

        }


    }
    inner class TabAdapter: FragmentPagerAdapter {
        private lateinit var context : Context
        private var totalTabs : Int = 0

        constructor (c : Context, fm : FragmentManager, totalTabs : Int) : super(fm) {
            context = c
            this.totalTabs = totalTabs
        }

        override fun getCount(): Int {
            return totalTabs
        }

        override fun getItem(position: Int): Fragment {
            return CustomFragment()
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
        val SEARCH_ROUTE : String = "search"
        val WEB_RESULT_KEY : String = "webResult"
        val APP_UNIQUE_ID : String = "cbhsuisnzdfui2378348347647641edsjhsh"


    }

}