package cz.fely.weightedaverage.subjects;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.fely.weightedaverage.MainActivity;
import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.SettingsActivity;
import cz.fely.weightedaverage.utils.ParseUtil;

public class OverviewFragment extends Fragment {
    public static Fragment ovf;
    public static TextView tv1Title, tv2Title, tv3Title, tv4Title, tv5Title, tv6Title, tv7Title,
    tv8Title, tv9Title, tv10Title, tv11Title, tv12Title, tv13Title, tv14Title, tv1Mark, tv2Mark,
            tv3Mark, tv4Mark, tv5Mark, tv6Mark, tv7Mark, tv8Mark, tv9Mark,
            tv10Mark, tv11Mark, tv12Mark, tv13Mark, tv14Mark;
    static Context ctx = MainActivity.context;
    static int badMark = ContextCompat.getColor(ctx, R.color.badMark);
    static int ffMark = ContextCompat.getColor(ctx, R.color.ffMark);
    static int okMark = ContextCompat.getColor(ctx, R.color.okMark);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState){
        ovf = OverviewFragment.this;

        View view = inflater.inflate(R.layout.activity_overview, container, false);
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
        tv11Title = (TextView) view.findViewById(R.id.SubjectElevenTitleOverview);
        tv12Title = (TextView) view.findViewById(R.id.SubjectTwelveTitleOverview);
        tv13Title = (TextView) view.findViewById(R.id.SubjectTtTitleOverview);
        tv14Title = (TextView) view.findViewById(R.id.SubjectFtTitleOverview);

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
        tv11Mark = (TextView) view.findViewById(R.id.SubjectElevenMarkOverview);
        tv12Mark = (TextView) view.findViewById(R.id.SubjectTwelveMarkOverview);
        tv13Mark = (TextView) view.findViewById(R.id.SubjectTtMarkOverview);
        tv14Mark = (TextView) view.findViewById(R.id.SubjectFtMarkOverview);
        setNames(getContext());

        OverviewFragment.changeColor(OverviewFragment.tv1Mark, 1);
        OverviewFragment.changeColor(OverviewFragment.tv2Mark, 2);
        OverviewFragment.changeColor(OverviewFragment.tv3Mark, 3);
        OverviewFragment.changeColor(OverviewFragment.tv4Mark, 4);
        OverviewFragment.changeColor(OverviewFragment.tv5Mark, 5);
        OverviewFragment.changeColor(OverviewFragment.tv6Mark, 6);
        OverviewFragment.changeColor(OverviewFragment.tv7Mark, 7);
        OverviewFragment.changeColor(OverviewFragment.tv8Mark, 8);
        OverviewFragment.changeColor(OverviewFragment.tv9Mark, 9);
        OverviewFragment.changeColor(OverviewFragment.tv10Mark, 10);
        OverviewFragment.changeColor(OverviewFragment.tv11Mark, 11);
        OverviewFragment.changeColor(OverviewFragment.tv12Mark, 12);
        OverviewFragment.changeColor(OverviewFragment.tv13Mark, 13);
        OverviewFragment.changeColor(OverviewFragment.tv14Mark, 14);

        OverviewFragment.setTextMarks(OverviewFragment.tv1Mark, 1);
        OverviewFragment.setTextMarks(OverviewFragment.tv2Mark, 2);
        OverviewFragment.setTextMarks(OverviewFragment.tv3Mark, 3);
        OverviewFragment.setTextMarks(OverviewFragment.tv4Mark, 4);
        OverviewFragment.setTextMarks(OverviewFragment.tv5Mark, 5);
        OverviewFragment.setTextMarks(OverviewFragment.tv6Mark, 6);
        OverviewFragment.setTextMarks(OverviewFragment.tv7Mark, 7);
        OverviewFragment.setTextMarks(OverviewFragment.tv8Mark, 8);
        OverviewFragment.setTextMarks(OverviewFragment.tv9Mark, 9);
        OverviewFragment.setTextMarks(OverviewFragment.tv10Mark, 10);
        OverviewFragment.setTextMarks(OverviewFragment.tv11Mark, 11);
        OverviewFragment.setTextMarks(OverviewFragment.tv12Mark, 12);
        OverviewFragment.setTextMarks(OverviewFragment.tv13Mark, 13);
        OverviewFragment.setTextMarks(OverviewFragment.tv14Mark, 14);

        MainActivity.tabRl.setVisibility(View.INVISIBLE);
        return view;
    }

    public static void setNames (Context ctx){
        ParseUtil.getTabNames(ctx);
        tv1Title.setText(ParseUtil.one);
        tv2Title.setText(ParseUtil.two);
        tv3Title.setText(ParseUtil.three);
        tv4Title.setText(ParseUtil.four);
        tv5Title.setText(ParseUtil.five);
        tv6Title.setText(ParseUtil.six);
        tv7Title.setText(ParseUtil.seven);
        tv8Title.setText(ParseUtil.eight);
        tv9Title.setText(ParseUtil.nine);
        tv10Title.setText(ParseUtil.ten);
        tv11Title.setText(ParseUtil.eleven);
        tv12Title.setText(ParseUtil.twelve);
        tv13Title.setText(ParseUtil.tt);
        tv14Title.setText(ParseUtil.ft);
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
        setNames(getContext());
        super.onResume();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        setNames(getContext());
        MainActivity.tabRl.setVisibility(View.INVISIBLE);
        super.onAttachFragment(childFragment);
    }
}
