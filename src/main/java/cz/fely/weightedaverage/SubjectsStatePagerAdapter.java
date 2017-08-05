package cz.fely.weightedaverage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.security.PrivilegedAction;
import java.util.ArrayList;

import cz.fely.weightedaverage.subjects.OverviewFragment;
import cz.fely.weightedaverage.subjects.SubjectTemplateFragment;
import cz.fely.weightedaverage.utils.ParseUtil;

import static cz.fely.weightedaverage.MainActivity.tabLayout;

public class SubjectsStatePagerAdapter extends FragmentStatePagerAdapter {
    private final MainActivity mActivity;
    private int nubmerOfSubjects;
    ArrayList<Fragment> mFragList = new ArrayList<>();
    ArrayList<String> mTitleList = new ArrayList<>();

    public SubjectsStatePagerAdapter(MainActivity activity, FragmentManager fragmentManager){
        super(fragmentManager);
        this.mActivity = activity;
    }

    @Override
    public Fragment getItem(int position){
        /*if(position == 0){
            return new OverviewFragment();
        }else {
            String name = ParseUtil.getTabNames(position);
            return SubjectTemplateFragment.newInstance(String.valueOf(position));
        }*/
        return mFragList.get(position);
    }

    public void addFrags(){
        mFragList.add(new OverviewFragment());
        mTitleList.add(ParseUtil.getTabNames(0));
        for (int i = 0; i <= 14; i++){
            mFragList.add(new SubjectTemplateFragment().newInstance(i));
            mTitleList.add(ParseUtil.getTabNames(i));
        }
    }

    @Override
    public int getCount() {
        return 14;
    }

    public void changeTitles(){
        for(int i = 0; i <= 14; i++){
            if(tabLayout.getTabAt(i) != null)
            tabLayout.getTabAt(i).setText(ParseUtil.getTabNames(i));
        }
    }
}
