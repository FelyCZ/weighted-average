package cz.fely.weightedaverage;

import android.support.design.internal.ParcelableSparseArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cz.fely.weightedaverage.subjects.OverviewFragment;
import cz.fely.weightedaverage.subjects.SubjectTemplateFragment;
import cz.fely.weightedaverage.utils.ParseUtil;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentTitleList.size();
    }


/*Old usage -->
    public void addFrag2(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        Log.i("Adding tab: ", "Tab " + title + " added");
    }
    <-- */

    public void addFrags() {
        //Overview Fragment
        mFragmentList.add(new OverviewFragment());
        Log.i("Adding tab: ", "Tab " + ParseUtil.getTabNames(MainActivity.context, 0) + " loaded");
        mFragmentTitleList.add(ParseUtil.getTabNames(MainActivity.context, 0));

        //Subject Template Fragments
        for(int i = 1; i < 15; i++){
            new SubjectTemplateFragment();
            mFragmentList.add(SubjectTemplateFragment.newInstance(i));
            Log.i("Adding tab: ", "Tab " + ParseUtil.getTabNames(MainActivity.context, i) + " loaded");
            mFragmentTitleList.add(ParseUtil.getTabNames(MainActivity.context, i));
        }
    }

    public void removeFrag(int index){
        mFragmentTitleList.remove(index);
        mFragmentList.remove(index);
        Log.i("Removing tab: ", "Tab " + getPageTitle(index) + " removed");
    }

    public void changeTitles(){
        for(int i = 1; i < 15; i++){
            if(mFragmentTitleList.size() != 0)
                mFragmentTitleList.set(i, ParseUtil.getTabNames(MainActivity.context, i));
            else {
                Log.e("Title list size: ", String.valueOf(mFragmentTitleList.size()));
            }
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
