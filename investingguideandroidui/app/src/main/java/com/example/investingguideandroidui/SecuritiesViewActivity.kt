package com.example.investingguideandroidui

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.investingguideandroidui.database.DBHandler
import com.example.investingguideandroidui.models.Security
import com.example.investingguideandroidui.recyclerviewadapters.SecurityAdapter
import com.example.investingguideandroidui.tabadapters.SecurityFragmentPagerAdapter
import com.example.investingguideandroidui.threadtasks.ReadSecuritiesFromTreasuryDirectWebsite
import com.example.investingguideandroidui.utilities.JsonParser
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import org.json.JSONException
import java.util.*


class SecuritiesViewActivity : AppCompatActivity(), View.OnClickListener, View.OnTouchListener {

    lateinit var startDate : String
    lateinit var endDate : String
    lateinit var dbHandler : DBHandler
    private var screenHeight : Int = 0
    private var screenWidth : Int = 0

    val BASE_URL : String = "https://www.treasurydirect.gov/TA_WS/securities"
    var searchRoute : String = MainActivity.AUCTIONED_ROUTE

    val format : String = "json"
    var dateFieldName : String = "issueDate"
    val issueDateFieldName : String = "issueDate"
    val auctionDateFieldName : String = "auctionDate"
    public var securityType : String = "Bond"
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
    lateinit var securityAdapter : SecurityAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var btnTop: Button
    private lateinit var btnBottom: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenWidth = Resources.getSystem( ).displayMetrics.widthPixels
        screenHeight = Resources.getSystem( ).displayMetrics.heightPixels
        // size of cards
        bw = (screenWidth.toFloat()*0.8).toInt()
        bh = (screenHeight/16).toInt()
        //set behavior of buttons top and bottom

        setContentView(R.layout.display_securities_view)
        btnTop = findViewById<Button>(R.id.return_search_menu_top_button)

        btnBottom  = findViewById<Button>(R.id.return_search_menu_bottom_button)
        btnTop.setOnClickListener(this)
        btnBottom.setOnClickListener(this)

        //progress bar update
        progressBar = findViewById(R.id.loadingDataFromInternetProgressBar)
        showProgressBar()

        var extras : Bundle = this.intent.extras!!
        startDate = extras.getString(MainActivity.INTER_ACTIVITY_START_DATE_KEY,"")
        endDate = extras.getString(MainActivity.INTER_ACTIVITY_END_DATE_KEY,"")
        val searchDateFieldNameBy : String = extras.getString(MainActivity.DATE_FIELD_NAME_SEARCH_BY_KEY,MainActivity.DEFAULT_DATE_FIELD_NAME_SEARCH_BY_VALUE)
        searchRoute = extras.getString(MainActivity.SEARCH_ROUTE_KEY,MainActivity.DEFAULT_SEARCH_ROUTE)


        // pull data from database and show to user
        dbHandler = DBHandler(this,this)
        val count : Int = dbHandler.countMatchingRows(startDate=startDate,endDate=endDate)
        if (count == 0 ) {
           dbHandler.getDataOnline()
        }else{
            securities = dbHandler.readAllBetween(
                startDate = startDate,
                endDate = endDate,
                dateFieldName = searchDateFieldNameBy
            )
            displaySecuritiesList(securities)
        }

    }

    fun displaySecuritiesList(secs : ArrayList<Security> = ArrayList<Security>()) {
        if (secs.size == 0 )
            return
        hideProgressBar()

        securities = secs
        securities_recycler_view = findViewById(R.id.securities_recycler_view)

        securityAdapter = SecurityAdapter(securities, this)

        securities_recycler_view.adapter = securityAdapter
        securities_recycler_view.layoutManager = LinearLayoutManager(this)
        Log.w(MainActivity.LOG_TAG_EXTERIOR,"securities size at time of display : ${securities.size}")

    }


    fun hideProgressBar() {
        progressBar.visibility = View.GONE

    }

    fun showProgressBar() {

        progressBar.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }


    fun saveWebResult(webResult : String ) {
        showProgressBar()
        if ( webResult == null || webResult.length == 0)
            return
        try {

            var jsonParser : JsonParser = JsonParser()
            securities = jsonParser.parseString(webResult)
            dbHandler.saveAllSecuritiesAsTransaction(securities)
            val count : Int = dbHandler.getCountAll()
            Log.w(MainActivity.LOG_TAG_EXTERIOR,"Count should be greater than 0 : ${count}")
            hideProgressBar()
            displaySecuritiesList(securities)
        } catch ( e : JSONException ) {
            Log.w(MainActivity.LOG_TAG_EXTERIOR,"Error while parsing json data : ${e.printStackTrace()}")
        }
    }
    override fun onClick(view: View?) {
        if( view == null )
            return


        Log.w(MainActivity.LOG_TAG_EXTERIOR, "Generated UUID: ${view.id}")
        Log.w(MainActivity.LOG_TAG_EXTERIOR, "Card clicked upon")

        if (view == btnTop || view == btnBottom) {
            finish()
            return
        }



    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (view == null || event == null )
            return false

        val action = event.action

        Log.w(MainActivity.LOG_TAG_EXTERIOR, "Generated UUID: ${view.id}")


        Log.w(MainActivity.LOG_TAG_EXTERIOR, "Card clicked upon")


        return true
    }


}