package com.example.investingguideandroidui.database
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.investingguideandroidui.MainActivity
import com.example.investingguideandroidui.SecuritiesViewActivity
import com.example.investingguideandroidui.models.Security
import com.example.investingguideandroidui.threadtasks.ReadSecuritiesFromTreasuryDirectWebsite
import java.util.*


class DBHandler  : SQLiteOpenHelper{
    private lateinit var context : Context
    private lateinit var fromActivity: SecuritiesViewActivity

    constructor(context: Context?, fromActivity: SecuritiesViewActivity) : super(context, DB_NAME, null, DB_VERSION)  {
        this.context = context!!
        this.fromActivity = fromActivity
    }
    // below method is for creating a database by running a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        //create security table
        val query_security = ("CREATE TABLE IF NOT EXISTS `$SECURITY_TABLE_NAME` ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ISSUE_DATE_COL + " DATE, "
                + AUCTION_DATE_COL + " DATE, "
                + SECURITY_JSON_DATA + " TEXT,"
                + CUSIP_COL + " TEXT)")

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query_security)
    }

    // this method is use to add new security to our sqlite database.
    fun saveSecurity(
        cusip: String?,
        issueDate: String,
        auctionDate: String,
        jsonData: String?
    ) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        val db = this.writableDatabase

        // on below line we are creating a
        // variable for content values.
        val values = ContentValues()

        values.put(CUSIP_COL, cusip)
        values.put(ISSUE_DATE_COL, issueDate)
        values.put(AUCTION_DATE_COL, auctionDate)
        values.put(SECURITY_JSON_DATA, jsonData)
        // after adding all values we are passing
        // content values to our table.
        db.insert("`$SECURITY_TABLE_NAME`", null, values)

        // at last we are closing our
        // database after adding database.
        db.close()
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS `$SECURITY_TABLE_NAME`")
        onCreate(db)
    }

    fun getCount(fieldValue: String, fieldName: String = CUSIP_COL) : Int {
        var c : Int = 0

        val db = this.readableDatabase
        var countSqlQuery : String  = (" SELECT COUNT(*) FROM ${SECURITY_TABLE_NAME} "
        + " WHERE ${fieldName} = '$fieldValue'")
        var countCursor : Cursor = db.rawQuery(countSqlQuery, null)

        if (countCursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.

                c = countCursor.getInt(0)
                break

            } while (countCursor.moveToNext())
            // moving our cursor to next.
        }
        return c
    }

    fun getCountAll() : Int {
        var c : Int = 0

        val db = this.readableDatabase
        var countSqlQuery : String  = (" SELECT COUNT(*) FROM ${SECURITY_TABLE_NAME} ")
        var countCursor : Cursor = db.rawQuery(countSqlQuery, null)

        if (countCursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.

                c = countCursor.getInt(0)
                break

            } while (countCursor.moveToNext())
            // moving our cursor to next.
        }
        return c
    }

    fun readAll() : ArrayList<Security> {
        var secures : ArrayList<Security> = ArrayList<Security>()
        val db = this.readableDatabase
        var cursorSecurity : Cursor = db.rawQuery(" SELECT * FROM ${SECURITY_TABLE_NAME}", null)


        if (cursorSecurity.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.

                var security: Security = Security()
                security.setJsonRawObject(cursorSecurity.getString(3))
                security.parseJsonObject()
                security.setCusip(cursorSecurity.getString(2))
                secures.add(security)
            } while (cursorSecurity.moveToNext())
            // moving our cursor to next.
        }

        return secures
    }

    fun readAllBetween(startDate: String, endDate:String, dateFieldName: String = ISSUE_DATE_COL) : ArrayList<Security> {
        var secures : ArrayList<Security> = ArrayList<Security>()
        val db = this.readableDatabase

        // first count number of rows whose dateFieldName(issue or auctionDate) matches the given entry
        // for that convert the issue date or auction date in sqlite to yyyy-mm-dd format and compare
        var dateMatchesSql = ("select count(*) from ${SECURITY_TABLE_NAME} where strftime('%Y-%m-%d', $ISSUE_DATE_COL) = strftime('%Y-%m-%d', '$startDate') || strftime('%Y-%m-%d', $ISSUE_DATE_COL) = strftime('%Y-%m-%d', '$endDate') ")
        if (dateFieldName != ISSUE_DATE_COL )
            dateMatchesSql = ("select count(*) from ${SECURITY_TABLE_NAME} where strftime('%Y-%m-%d', $AUCTION_DATE_COL) = strftime('%Y-%m-%d', '$startDate') || strftime('%Y-%m-%d', $AUCTION_DATE_COL) = strftime('%Y-%m-%d', '$endDate') ")

        Log.w(MainActivity.LOG_TAG_EXTERIOR,"running count sql : $dateMatchesSql")
        val countSql : String = dateMatchesSql!!
        var c : Int = getCountAll()
        if ( c == 0 ) {
            // pull data online and save to database while saving to array list to return
            getDataOnline()
            Log.w(MainActivity.LOG_TAG_EXTERIOR,"We do not have data yet, pulling online")
            return secures

        }
        Log.w(MainActivity.LOG_TAG_EXTERIOR,"We already have local data, loading locally")


        val timeBoundSql : String = (" SELECT * FROM ${SECURITY_TABLE_NAME}"
                + " WHERE $dateFieldName >= '$startDate' and $dateFieldName <= '$endDate'" )
        var cursorSecurity : Cursor = db.rawQuery(timeBoundSql, null)

        if (cursorSecurity.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.

                var security: Security = Security()
                security.setJsonRawObject(cursorSecurity.getString(3))
                security.parseJsonObject()
                security.setCusip(cursorSecurity.getString(2))
                secures.add(security)
            } while (cursorSecurity.moveToNext())
            // moving our cursor to next.
        }

        return secures
    }

    fun getDataOnline() {

        var taskThread : ReadSecuritiesFromTreasuryDirectWebsite =
            ReadSecuritiesFromTreasuryDirectWebsite(fromActivity, search_route=fromActivity.searchRoute)
        taskThread.start()
    }
    companion object {
        // creating a constant variables for our database.
        // below variable is for our database name.
        private const val DB_NAME = "TreasurySecurities.DB"

        // below int is our database version
        private const val DB_VERSION = 1

        // below variable is for our table name.
        private const val SECURITY_TABLE_NAME = "Securities"


        // below variable is for our id column in security table.
        private const val ID_COL = "id"

        // below variable id for our security cusip.
        public const val CUSIP_COL = "cusip"

        // below variable is for our security json data
        public const val SECURITY_JSON_DATA = "jsonData"

        // for the auction date
        public const val AUCTION_DATE_COL = "auctionDate"

        // for the issue date
        public const val ISSUE_DATE_COL = "issueDate"


    }
}
