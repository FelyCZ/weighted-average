package cz.fely.weightedaverage.subjects;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.utils.PreferencesUtil;

public class OverviewFragment extends Fragment {
    public static Fragment ovf;
    public static TextView tv1Title, tv2Title, tv3Title, tv4Title, tv5Title, tv6Title, tv7Title,
    tv8Title,
            tv9Title, tv10Title, tv1Mark, tv2Mark, tv3Mark, tv4Mark, tv5Mark, tv6Mark, tv7Mark, tv8Mark, tv9Mark,
            tv10Mark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState){
        ovf = OverviewFragment.this;

        View view = inflater.inflate(R.layout.activity_overview, container, false);

        //Titles fields
        tv1Title = (TextView) view.findViewById(R.id.SubjectOneTitleOverview);
        tv2Title = (TextView) view.findViewById(R.id.SubjectTwoTitleOverview);
        tv3Title = (TextView) view.findViewById(R.id.SubjectThreeTitleOverview);
        tv4Title = (TextView) view.findViewById(R.id.SubjectFourTitleOverview);
        tv5Title = (TextView) view.findViewById(R.id.SubjectFiveTitleOverview);
        tv6Title = (TextView) view.findViewById(R.id.SubjectSixTitleOverview);
        tv7Title = (TextView) view.findViewById(R.id.SubjectSevenTitleOverview);
        tv8Title = (TextView) view.findViewById(R.id.SubjectEightTitleOverview);
        tv9Title = (TextView) view.findViewById(R.id.SubjectNineTitleOverview);
        tv10Title = (TextView) view.findViewById(R.id.SubjectTenTitleOverview);

        //Marks fields
        tv1Mark = (TextView) view.findViewById(R.id.SubjectOneMarkOverview);
        tv2Mark = (TextView) view.findViewById(R.id.SubjectTwoMarkOverview);
        tv3Mark = (TextView) view.findViewById(R.id.SubjectThreeMarkOverview);
        tv4Mark = (TextView) view.findViewById(R.id.SubjectFourMarkOverview);
        tv5Mark = (TextView) view.findViewById(R.id.SubjectFiveMarkOverview);
        tv6Mark = (TextView) view.findViewById(R.id.SubjectSixMarkOverview);
        tv7Mark = (TextView) view.findViewById(R.id.SubjectSevenMarkOverview);
        tv8Mark = (TextView) view.findViewById(R.id.SubjectEightMarkOverview);
        tv9Mark = (TextView) view.findViewById(R.id.SubjectNineMarkOverview);
        tv10Mark = (TextView) view.findViewById(R.id.SubjectTenMarkOverview);
        setNames(getContext());
        setAverages(getContext());
        return view;
    }

    public static void setNames (Context ctx){
        PreferencesUtil.getTabNames(ctx);
        tv1Title.setText(PreferencesUtil.one);
        tv2Title.setText(PreferencesUtil.two);
        tv3Title.setText(PreferencesUtil.three);
        tv4Title.setText(PreferencesUtil.four);
        tv5Title.setText(PreferencesUtil.five);
        tv6Title.setText(PreferencesUtil.six);
        tv7Title.setText(PreferencesUtil.seven);
        tv8Title.setText(PreferencesUtil.eight);
        tv9Title.setText(PreferencesUtil.nine);
        tv10Title.setText(PreferencesUtil.ten);
    }

    public static void setAverages (Context ctx){
        PreferencesUtil.getTabAverages();
        int badMark = ContextCompat.getColor(ctx, R.color.badMark);
        int ffMark = ContextCompat.getColor(ctx, R.color.ffMark);
        int okMark = ContextCompat.getColor(ctx, R.color.okMark);

        if (PreferencesUtil.sum1 >= 3){
            tv1Mark.setTextColor(badMark);
        }
        else if(PreferencesUtil.sum1 >= 2.5 && PreferencesUtil.sum1 < 3){
            tv1Mark.setTextColor(ffMark);
        }
        else if (PreferencesUtil.sum1 == 0){
            tv1Mark.setTextColor(tv1Mark.getTextColors().getDefaultColor());
        }
        else{
            tv1Mark.setTextColor(okMark);
        }

        if (PreferencesUtil.sum2 >= 3){
            tv2Mark.setTextColor(badMark);
        }
        else if(PreferencesUtil.sum2 >= 2.5 && PreferencesUtil.sum2 < 3){
            tv2Mark.setTextColor(ffMark);
        }
        else if (PreferencesUtil.sum2 == 0){
            tv2Mark.setTextColor(tv3Mark.getTextColors().getDefaultColor());
        }
        else{
            tv2Mark.setTextColor(okMark);
        }
        if (PreferencesUtil.sum3 >= 3){
            tv3Mark.setTextColor(badMark);
        }
        else if(PreferencesUtil.sum3 >= 2.5 && PreferencesUtil.sum3 < 3){
            tv3Mark.setTextColor(ffMark);
        }
        else if (PreferencesUtil.sum3 == 0){
            tv3Mark.setTextColor(tv3Mark.getTextColors().getDefaultColor());
        }
        else{
            tv3Mark.setTextColor(okMark);
        }

        if (PreferencesUtil.sum4 >= 3){
            tv4Mark.setTextColor(badMark);
        }
        else if(PreferencesUtil.sum4 >= 2.5 && PreferencesUtil.sum4 < 3){
            tv4Mark.setTextColor(ffMark);
        }
        else if (PreferencesUtil.sum4 == 0){
            tv4Mark.setTextColor(tv4Mark.getTextColors().getDefaultColor());
        }
        else{
            tv4Mark.setTextColor(okMark);
        }

        if (PreferencesUtil.sum5 >= 3){
            tv5Mark.setTextColor(badMark);
        }
        else if(PreferencesUtil.sum5 >= 2.5 && PreferencesUtil.sum5 < 3){
            tv5Mark.setTextColor(ffMark);
        }
        else if (PreferencesUtil.sum5 == 0){
            tv5Mark.setTextColor(tv5Mark.getTextColors().getDefaultColor());
        }
        else{
            tv5Mark.setTextColor(okMark);
        }

        if (PreferencesUtil.sum6 >= 3){
            tv6Mark.setTextColor(badMark);
        }
        else if(PreferencesUtil.sum6 >= 2.5 && PreferencesUtil.sum6 < 3){
            tv6Mark.setTextColor(ffMark);
        }
        else if (PreferencesUtil.sum6 == 0){
            tv6Mark.setTextColor(tv6Mark.getTextColors().getDefaultColor());
        }
        else{
            tv6Mark.setTextColor(okMark);
        }

        if (PreferencesUtil.sum7 >= 3){
            tv7Mark.setTextColor(badMark);
        }
        else if(PreferencesUtil.sum7 >= 2.5 && PreferencesUtil.sum7 < 3){
            tv7Mark.setTextColor(ffMark);
        }
        else if (PreferencesUtil.sum7 == 0){
            tv7Mark.setTextColor(tv7Mark.getTextColors().getDefaultColor());
        }
        else{
            tv7Mark.setTextColor(okMark);
        }

        if (PreferencesUtil.sum8 >= 3){
            tv8Mark.setTextColor(badMark);
        }
        else if(PreferencesUtil.sum8 >= 2.5 && PreferencesUtil.sum8 < 3){
            tv8Mark.setTextColor(ffMark);
        }
        else if (PreferencesUtil.sum8 == 0){
            tv8Mark.setTextColor(tv8Mark.getTextColors().getDefaultColor());
        }
        else{
            tv8Mark.setTextColor(okMark);
        }

        if (PreferencesUtil.sum9 >= 3){
            tv9Mark.setTextColor(badMark);
        }
        else if(PreferencesUtil.sum9 >= 2.5 && PreferencesUtil.sum9 < 3){
            tv9Mark.setTextColor(ffMark);
        }
        else if (PreferencesUtil.sum9 == 0){
            tv9Mark.setTextColor(tv9Mark.getTextColors().getDefaultColor());
        }
        else{
            tv9Mark.setTextColor(okMark);
        }

        if (PreferencesUtil.sum10 >= 3){
            tv10Mark.setTextColor(badMark);
        }
        else if(PreferencesUtil.sum10 >= 2.5 && PreferencesUtil.sum10 < 3){
            tv10Mark.setTextColor(ffMark);
        }
        else if (PreferencesUtil.sum10 == 0){
            tv10Mark.setTextColor(tv10Mark.getTextColors().getDefaultColor());
        }
        else{
            tv10Mark.setTextColor(okMark);
        }

        tv1Mark.setText(PreferencesUtil.oneAvg);
        tv2Mark.setText(PreferencesUtil.twoAvg);
        tv3Mark.setText(PreferencesUtil.threeAvg);
        tv4Mark.setText(PreferencesUtil.fourAvg);
        tv5Mark.setText(PreferencesUtil.fiveAvg);
        tv6Mark.setText(PreferencesUtil.sixAvg);
        tv7Mark.setText(PreferencesUtil.sevenAvg);
        tv8Mark.setText(PreferencesUtil.eightAvg);
        tv9Mark.setText(PreferencesUtil.nineAvg);
        tv10Mark.setText(PreferencesUtil.tenAvg);
    }

    @Override
    public void onResume() {
        setAverages(getContext());
        setNames(getContext());
        super.onResume();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        setAverages(getContext());
        setNames(getContext());
        super.onAttachFragment(childFragment);
    }
}
