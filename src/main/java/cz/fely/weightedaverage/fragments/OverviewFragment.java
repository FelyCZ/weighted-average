package cz.fely.weightedaverage.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.fely.weightedaverage.activities.MainActivity;
import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.activities.SettingsActivity;
import cz.fely.weightedaverage.utils.ParseUtil;

public class OverviewFragment extends Fragment {
    public static Fragment ovf;
    public static TextView tv1Title, tv2Title, tv3Title, tv4Title, tv5Title, tv6Title, tv7Title,
    tv8Title, tv9Title, tv10Title, tv11Title, tv12Title, tv13Title, tv14Title, tv1Mark, tv2Mark,
            tv3Mark, tv4Mark, tv5Mark, tv6Mark, tv7Mark, tv8Mark, tv9Mark,
            tv10Mark, tv11Mark, tv12Mark, tv13Mark, tv14Mark;
    static Context ctx = MainActivity.context;
    static int badMark;
    static int ffMark;
    static int okMark;
    public static View view;
    public static boolean initCompleted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState){
        ovf = OverviewFragment.this;
        badMark = getResources().getColor(R.color.badMark);
        ffMark = getResources().getColor(R.color.ffMark);
        okMark = getResources().getColor(R.color.okMark);

        view = inflater.inflate(R.layout.activity_overview, container, false);
        tv1Title = view.findViewById(R.id.SubjectOneTitleOverview);
        tv2Title = view.findViewById(R.id.SubjectTwoTitleOverview);
        tv3Title = view.findViewById(R.id.SubjectThreeTitleOverview);
        tv4Title = view.findViewById(R.id.SubjectFourTitleOverview);
        tv5Title = view.findViewById(R.id.SubjectFiveTitleOverview);
        tv6Title = view.findViewById(R.id.SubjectSixTitleOverview);
        tv7Title = view.findViewById(R.id.SubjectSevenTitleOverview);
        tv8Title = view.findViewById(R.id.SubjectEightTitleOverview);
        tv9Title = view.findViewById(R.id.SubjectNineTitleOverview);
        tv10Title = view.findViewById(R.id.SubjectTenTitleOverview);
        tv11Title = view.findViewById(R.id.SubjectElevenTitleOverview);
        tv12Title = view.findViewById(R.id.SubjectTwelveTitleOverview);
        tv13Title = view.findViewById(R.id.SubjectTtTitleOverview);
        tv14Title = view.findViewById(R.id.SubjectFtTitleOverview);

        tv1Mark = view.findViewById(R.id.SubjectOneMarkOverview);
        tv2Mark = view.findViewById(R.id.SubjectTwoMarkOverview);
        tv3Mark = view.findViewById(R.id.SubjectThreeMarkOverview);
        tv4Mark = view.findViewById(R.id.SubjectFourMarkOverview);
        tv5Mark = view.findViewById(R.id.SubjectFiveMarkOverview);
        tv6Mark = view.findViewById(R.id.SubjectSixMarkOverview);
        tv7Mark = view.findViewById(R.id.SubjectSevenMarkOverview);
        tv8Mark = view.findViewById(R.id.SubjectEightMarkOverview);
        tv9Mark = view.findViewById(R.id.SubjectNineMarkOverview);
        tv10Mark = view.findViewById(R.id.SubjectTenMarkOverview);
        tv11Mark = view.findViewById(R.id.SubjectElevenMarkOverview);
        tv12Mark = view.findViewById(R.id.SubjectTwelveMarkOverview);
        tv13Mark = view.findViewById(R.id.SubjectTtMarkOverview);
        tv14Mark = view.findViewById(R.id.SubjectFtMarkOverview);
        initCompleted = true;
        setNames();
        setAverages();

        return view;
    }

    public static void setNames(){
        if(initCompleted) {
            for (int i = 1; i < 15; i++) {
                updateNameTexts(i);
            }
        }
    }

    public static void setAverages() {
        if(initCompleted){
            for (int i = 1; i < 15; i++){
                updateAverageTexts(i);
            }
        }
    }

    public static void updateNameTexts (int pos){
        ParseUtil.overviewTVs(pos).setText(ParseUtil.getTabNames(pos));
    }

    public static void updateAverageTexts(int pos){
        OverviewFragment.setTextMarks(ParseUtil.overviewTVsAvgs(pos), pos);
        OverviewFragment.changeColor(ParseUtil.overviewTVsAvgs(pos), pos);
    }

    public static void setAvgColor(int pos){
        ParseUtil.overviewTVsAvgs(pos).setTextColor(MainActivity.context.getResources().getColor(android.R.color.tab_indicator_text));
    }

    public static void setTextMarks (TextView v, int subject){
        ParseUtil.getTabAverages();
        if (subject == 1) {
            v.setText(ParseUtil.oneAvg);
        }
        else if (subject == 2) {
            v.setText(ParseUtil.twoAvg);
        }
        else if (subject == 3) {
            v.setText(ParseUtil.threeAvg);
        }
        else if (subject == 4) {
            v.setText(ParseUtil.fourAvg);
        }
        else if (subject == 5) {
            v.setText(ParseUtil.fiveAvg);
        }
        else if (subject == 6) {
            v.setText(ParseUtil.sixAvg);
        }
        else if (subject == 7) {
            v.setText(ParseUtil.sevenAvg);
        }
        else if (subject == 8) {
            v.setText(ParseUtil.eightAvg);
        }
        else if (subject == 9) {
            v.setText(ParseUtil.nineAvg);
        }
        else if (subject == 10) {
            v.setText(ParseUtil.tenAvg);
        }
        else if (subject == 11) {
            v.setText(ParseUtil.elevenAvg);
        }
        else if (subject == 12) {
            v.setText(ParseUtil.twelveAvg);
        }
        else if (subject == 13) {
            v.setText(ParseUtil.ttAvg);
        }
        else if (subject == 14) {
            v.setText(ParseUtil.ftAvg);
        }
    }

    public static void changeColor(TextView v, int subject){
        double avg = ParseUtil.avgByIntReturnDouble(subject);
        double okMarkNum = SettingsActivity.getAvgFromPreference(SettingsActivity.prefOkMark);
        double okMarkNum2 = 0.49;
        double sum = okMarkNum+okMarkNum2;
        double ffMarkNum = SettingsActivity.getAvgFromPreference(SettingsActivity.prefFfMark);
        double ffMarkNum2 = 0.49;
        double sum2 = ffMarkNum+ffMarkNum2;
        if (avg == 0){
            v.setTextColor(v.getTextColors().getDefaultColor());
        }
        else if (avg > 0 && avg < sum){
            v.setTextColor(okMark);
        }
        else if (avg >= sum && avg <= sum2){
            v.setTextColor(ffMark);
        }
        else {
            v.setTextColor(badMark);
        }
        /*

        if (avg > SettingsActivity.getAvgFromPreference(SettingsActivity.prefFfMark)){
            v.setTextColor(badMark);
        }
        // If avg <= 3 && avg > 2.45
        if(avg <= SettingsActivity.getAvgFromPreference(SettingsActivity.prefFfMark)
                && avg > sum){
            v.setTextColor(ffMark);
        }
        // If avg <= 2 && != 0
        if (avg <= sum && avg != 0){
            v.setTextColor(okMark);
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }
}
