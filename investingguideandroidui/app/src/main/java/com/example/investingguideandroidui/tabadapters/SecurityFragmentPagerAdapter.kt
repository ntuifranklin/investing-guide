package com.example.investingguideandroidui.tabadapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.investingguideandroidui.MainActivity
import com.example.investingguideandroidui.SecuritiesViewActivity
import com.example.investingguideandroidui.models.Security
import com.example.investingguideandroidui.utilities.SecurityType

class SecurityFragmentPagerAdapter : FragmentPagerAdapter {

    val hm : HashMap<String, Int> = SecurityType().securityTypeMapToInt()

    lateinit var securitys : ArrayList<Security>
    lateinit var fromActivity : SecuritiesViewActivity
    var bills : ArrayList<Security> = ArrayList<Security>()
    var bonds : ArrayList<Security> = ArrayList<Security>()
    var cmbs : ArrayList<Security> = ArrayList<Security>()
    var frns : ArrayList<Security> = ArrayList<Security>()
    var notes : ArrayList<Security> = ArrayList<Security>()
    var tips : ArrayList<Security> = ArrayList<Security>()

    constructor(fm: FragmentManager, from: SecuritiesViewActivity, secs: ArrayList<Security>) : super(fm) {

        fromActivity = from
        securitys = secs


        for ( s in securitys ) {
            if (s.getSecurityType().lowercase() == "bill")
                bills.add(s)
            else if (s.getSecurityType().lowercase() == "bond")
                bonds.add(s)
            else if (s.getSecurityType().lowercase() == "cmb")
                cmbs.add(s)
            else if (s.getSecurityType().lowercase() == "frn")
                frns.add(s)
            else if (s.getSecurityType().lowercase() == "note")
                notes.add(s)
            else
                tips.add(s)

            /*
            bills.sortBy { it.getPricePerDay() }
            bonds.sortBy { it.getPricePerDay() }
            cmbs.sortBy { it.getPricePerDay() }
            frns.sortBy { it.getPricePerDay() }
            notes.sortBy { it.getPricePerDay() }
            tips.sortBy { it.getPricePerDay() }
            */
        }

    }
    override fun getCount(): Int {
        return SecurityType().securityTypeMapToInt().size
    }

    override fun getItem(position: Int): Fragment {

        if ( position == MainActivity.BILL)
            return BillFragment(fromActivity,bills)
        else if ( position == MainActivity.BOND)
            return BondFragment(fromActivity,bonds)
        else if ( position == MainActivity.CMB )
            return CmbFragment(fromActivity,cmbs)
        else if (position == MainActivity.FRN)
            return FrnFragment(fromActivity,frns)
        else if (position  == MainActivity.TIPS)
            return TipsFragment(fromActivity,tips)
        else
            return NoteFragment(fromActivity,notes)


    }

    override fun getPageTitle(position: Int): CharSequence? {

        for ( (secType, secId) in hm  )
            if ( position == secId )
                return secType
        return "SecurityTypeNotFound"
    }

    fun setSecurities(secs : ArrayList<Security>) {
        securitys = secs
    }

}