package com.example.investingguideandroidui.models

import com.example.investingguideandroidui.utilities.SecurityTermToDays


class Security {
    private lateinit var jsonRawObject : String
    private var issueDate : String = ""
    private var pricePer100 : Double = 0.0
    private var cusip : String = ""
    private var securityType : String = ""
    private var pricePerWeek : Double = 0.0
    private var pricePerDay : Double = 0.0
    private var securityTerm : String = "0-Week"


    constructor() : this("1970-01-01", 99.65, "{}") {

    }

    constructor(issueDate: String, pricePer100: Double, jsonRawObject: String ) {
        setIssueDate(issueDate)
        setPricePer100(pricePer100)
        setJsonRawObject(jsonRawObject)
        setPricePerWeek(0.0)
        pricePerDay = 0.0
    }


    fun setSecurityType(securityType: String){
        this.securityType = securityType
    }


    fun setCusip(c: String) {
        cusip = c
    }


    fun setJsonRawObject(rawJson: String) {
        jsonRawObject = rawJson

    }
    fun setIssueDate(issueDate: String ) {
        this.issueDate = issueDate
    }

    fun setPricePer100(pp100: Double ) {
        this.pricePer100 = pp100
    }

    fun setPricePer100(pp100Str: String ) {
        var pp100 : Double = 0.0
        if (pp100Str != null )
            pp100 = pp100Str.toDouble()
        setPricePer100(pp100)
    }

    fun setPricePerWeek(ppw: Double){
        if (ppw != null )
            pricePerWeek = ppw

    }

    fun setSecurityTerm(termStr: String) {
        securityTerm = termStr
        var s : SecurityTermToDays  = SecurityTermToDays(securityTerm)
        var totalDays : Double = s.convert()
        if (totalDays > 0.0 && pricePer100 > 0.0 ) {
            pricePerDay = pricePer100 / totalDays
            pricePerWeek = pricePerDay * 7.0
        }
    }


    fun getSecurityType() : String {
        return securityType
    }

    fun getCusip() : String {
        return cusip
    }

    fun getIssueDate() : String {
        return issueDate
    }

    fun getPricePer100() : Double  {
       return pricePer100
    }

    fun getPricePerWeek() : Double{

        return  pricePerWeek

    }

    fun getJsonRawObject() : String {
        return jsonRawObject
    }

    fun computePricePerWeek() : Double {
        var s  : SecurityTermToDays = SecurityTermToDays(securityTerm)
        var denominator : Double = 0.0
        denominator = s.convert()
        if (denominator == 0.0 )
            pricePerWeek = 0.0
        else
            pricePerWeek = pricePer100 / denominator
        return pricePerWeek
    }

    override fun toString() : String {
        return "${jsonRawObject}"
    }


}