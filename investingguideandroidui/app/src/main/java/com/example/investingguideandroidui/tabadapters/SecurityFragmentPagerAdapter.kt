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

    constructor(fm: FragmentManager, from: SecuritiesViewActivity, secs: ArrayList<Security>) : super(fm) {
        fromActivity = from
        securitys = secs
    }
    override fun getCount(): Int {
        return SecurityType().securityTypeMapToInt().size
    }

    override fun getItem(position: Int): Fragment {

        if ( position == MainActivity.BILL)
            return BillFragment(fromActivity,securitys)
        else if ( position == MainActivity.BOND)
            return BondFragment(fromActivity,securitys)
        else if ( position == MainActivity.CMB )
            return CmbFragment(fromActivity,securitys)
        else if (position == MainActivity.FRN)
            return FrnFragment(fromActivity,securitys)
        else if (position  == MainActivity.TIPS)
            return TipsFragment(fromActivity,securitys)
        else
            return NoteFragment(fromActivity,securitys)


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