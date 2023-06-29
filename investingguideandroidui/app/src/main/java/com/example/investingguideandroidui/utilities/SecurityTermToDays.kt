package com.example.investingguideandroidui.utilities

import android.util.Log
import com.example.investingguideandroidui.MainActivity


class SecurityTermToDays {
    private var securityTerm : String = "0-week-0-days"
    private var totalDays : Double = 0.0


    constructor(termStr : String ) {
        securityTerm = termStr
    }

    fun convert() : Double {
        totalDays = multiTermToDays(securityTerm)
        return totalDays
    }

    fun singleTermToDays(termStr : String ) : Double {

        /**
        Takes a string in the format of XX-TIME(Week,Day,Year)
        that can be seperated by spaces
        the convert that to days
         n-week(s) == n * 7 days
         n-months = n * 365 / 12 days
         ...
         */
        if (termStr == null || !isValidSecurityTerm(termStr)) return 0.0
        var days: Double = 0.0

        var ts : String = termStr.replace(" ", "")

        ts = ts.lowercase()

        var t : List<String> = ts.split("-")


        if (t == null || t.size != 2 ) {
            Log.w(MainActivity.LOG_TAG_EXTERIOR, "Improper security term string in ${Thread.currentThread().stackTrace[1].methodName}. recieved ${termStr} returned 0, might crash app" )
            return 0.0
        }
        var n: Double = t[0].toDouble()
        if ( n == 0.0)
            return 0.0
        var oneMonth : Double = (365.0 / 12.0).toDouble()
        //day/week/month/year
        var dayWeekMonthYear : String = t[1]
        if ( dayWeekMonthYear.contains("weeks", ignoreCase = true) || dayWeekMonthYear.contains("week", ignoreCase = true))
            n *= 7.0
        else if ( dayWeekMonthYear.contains("months", ignoreCase = true) || dayWeekMonthYear.contains("month", ignoreCase = true))
           n *= oneMonth
        else  if ( dayWeekMonthYear.contains("years", ignoreCase = true) || dayWeekMonthYear.contains("year", ignoreCase = true))
            n *= 365.0

        days += n

        return days
    }

    fun multiTermToDays(termStr : String) : Double {
        if (termStr == null )
            return 0.0
        if (! termStr.contains(" "))
            return singleTermToDays(termStr)
        var days : Double = 0.0

        var tokens : List<String> = termStr.split(" ")
        //Log.w(MainActivity.LOG_TAG_EXTERIOR,"tokens : ${tokens.toString()}")
        for ( token : String in tokens ){
            if (token == null || token.length == 0)
                continue
            days += singleTermToDays(token)
        }
        return days
    }

    fun isValidSecurityTerm(termStr: String) : Boolean{

        return true
    }


}