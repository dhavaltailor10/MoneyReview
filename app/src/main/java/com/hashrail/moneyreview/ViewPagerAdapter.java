package com.hashrail.moneyreview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by new-3 on 5/20/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int noOfTab;

    public ViewPagerAdapter(FragmentManager fm, int noOfTab) {
        super(fm);
        this.noOfTab = noOfTab;
    }


    @Override
    public int getCount() {
        return noOfTab;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                HomeFragment tab1=new HomeFragment();
                return tab1;
            case 1:
                SumFragment tab2=new SumFragment();
                return tab2;
            case 2:
                ReviewFragment tab3=new ReviewFragment();
                return tab3;
            case 3:
                ReportFragment tab4=new ReportFragment();
                return tab4;
            default:
                return null;

        }

    }
}
