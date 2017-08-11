package cz.fely.weightedaverage.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;

import java.util.ArrayList;

import cz.fely.weightedaverage.abstracts.SmartFragmentStatePagerAdapter;
import cz.fely.weightedaverage.activities.MainActivity;
import cz.fely.weightedaverage.fragments.OverviewFragment;
import cz.fely.weightedaverage.fragments.SubjectTemplateFragment;
import cz.fely.weightedaverage.utils.ParseUtil;

import static cz.fely.weightedaverage.activities.MainActivity.tabLayout;

public class SubjectsStatePagerAdapter extends SmartFragmentStatePagerAdapter {
    private final MainActivity mActivity;
    private int nubmerOfSubjects;
    private SparseArray<Fragment> mFragList = new SparseArray<Fragment>();

    ArrayList<String> mTitleList = new ArrayList<>();

    public SubjectsStatePagerAdapter(MainActivity activity, FragmentManager fragmentManager){
        super(fragmentManager);
        addTitles();
        this.mActivity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new OverviewFragment();
        else
            return new SubjectTemplateFragment().newInstance(position);
    }

    public void addTitles(){
        for(int i = 0; i <= 14; i++)
            mTitleList.add(ParseUtil.getTabNames(i));
    }

    @Override
    public int getCount() {
        return mTitleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    public void changeTitles(){
        for(int i = 0; i <= 14; i++){
            if(tabLayout.getTabAt(i) != null) {
                tabLayout.getTabAt(i).setText(ParseUtil.getTabNames(i));
                mTitleList.set(i, ParseUtil.getTabNames(i));
            }
        }
    }
}
