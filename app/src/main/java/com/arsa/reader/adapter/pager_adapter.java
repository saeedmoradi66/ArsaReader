package com.arsa.reader.adapter;
import com.arsa.reader.tab.tab_Books;
import com.arsa.reader.tab.tab_Description;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class pager_adapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public pager_adapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tab_Books tab1 = new tab_Books();
                return tab1;
            case 1:
                tab_Description tab2 = new tab_Description();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
