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

        setTextMarks(tv1Mark, 1);
        setTextMarks(tv2Mark, 2);
        setTextMarks(tv3Mark, 3);
        setTextMarks(tv4Mark, 4);
        setTextMarks(tv5Mark, 5);
        setTextMarks(tv6Mark, 6);
        setTextMarks(tv7Mark, 7);
        setTextMarks(tv8Mark, 8);
        setTextMarks(tv9Mark, 9);
        setTextMarks(tv10Mark, 10);
        setTextMarks(tv11Mark, 11);
        setTextMarks(tv12Mark, 12);
        setTextMarks(tv13Mark, 13);
        setTextMarks(tv14Mark, 14);

        changeColor(tv1Mark, 1);
        changeColor(tv2Mark, 2);
        changeColor(tv3Mark, 3);
        changeColor(tv4Mark, 4);
        changeColor(tv5Mark, 5);
        changeColor(tv6Mark, 6);
        changeColor(tv7Mark, 7);
        changeColor(tv8Mark, 8);
        changeColor(tv9Mark, 9);
        changeColor(tv10Mark, 10);
        changeColor(tv11Mark, 11);
        changeColor(tv12Mark, 12);
        changeColor(tv13Mark, 13);
        changeColor(tv14Mark, 14);

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

    public static void setAverages (Context ctx){
        ParseUtil.getTabAverages();

        tv1Mark.setText(ParseUtil.oneAvg);
        tv2Mark.setText(ParseUtil.twoAvg);
        tv3Mark.setText(ParseUtil.threeAvg);
        tv4Mark.setText(ParseUtil.fourAvg);
        tv5Mark.setText(ParseUtil.fiveAvg);
        tv6Mark.setText(ParseUtil.sixAvg);
        tv7Mark.setText(ParseUtil.sevenAvg);
        tv8Mark.setText(ParseUtil.eightAvg);
        tv9Mark.setText(ParseUtil.nineAvg);
        tv10Mark.setText(ParseUtil.tenAvg);
        tv11Mark.setText(ParseUtil.elevenAvg);
        tv12Mark.setText(ParseUtil.twelveAvg);
        tv13Mark.setText(ParseUtil.ttAvg);
        tv14Mark.setText(ParseUtil.ftAvg);

        if (ParseUtil.sum1 >= 3){
            tv1Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum1 >= 2.5 && ParseUtil.sum1 < 3){
            tv1Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum1 == 0){
            tv1Mark.setTextColor(tv1Mark.getTextColors().getDefaultColor());
        }
        else{
            tv1Mark.setTextColor(okMark);
        }


        if (ParseUtil.sum2 >= 3){
            tv2Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum2 >= 2.5 && ParseUtil.sum2 < 3){
            tv2Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum2 == 0){
            tv2Mark.setTextColor(tv2Mark.getTextColors().getDefaultColor());
        }
        else{
            tv2Mark.setTextColor(okMark);
        }
        if (ParseUtil.sum3 >= 3){
            tv3Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum3 >= 2.5 && ParseUtil.sum3 < 3){
            tv3Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum3 == 0){
            tv3Mark.setTextColor(tv3Mark.getTextColors().getDefaultColor());
        }
        else{
            tv3Mark.setTextColor(okMark);
        }

        if (ParseUtil.sum4 >= 3){
            tv4Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum4 >= 2.5 && ParseUtil.sum4 < 3){
            tv4Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum4 == 0){
            tv4Mark.setTextColor(tv4Mark.getTextColors().getDefaultColor());
        }
        else{
            tv4Mark.setTextColor(okMark);
        }

        if (ParseUtil.sum5 >= 3){
            tv5Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum5 >= 2.5 && ParseUtil.sum5 < 3){
            tv5Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum5 == 0){
            tv5Mark.setTextColor(tv5Mark.getTextColors().getDefaultColor());
        }
        else{
            tv5Mark.setTextColor(okMark);
        }

        if (ParseUtil.sum6 >= 3){
            tv6Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum6 >= 2.5 && ParseUtil.sum6 < 3){
            tv6Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum6 == 0){
            tv6Mark.setTextColor(tv6Mark.getTextColors().getDefaultColor());
        }
        else{
            tv6Mark.setTextColor(okMark);
        }

        if (ParseUtil.sum7 >= 3){
            tv7Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum7 >= 2.5 && ParseUtil.sum7 < 3){
            tv7Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum7 == 0){
            tv7Mark.setTextColor(tv7Mark.getTextColors().getDefaultColor());
        }
        else{
            tv7Mark.setTextColor(okMark);
        }

        if (ParseUtil.sum8 >= 3){
            tv8Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum8 >= 2.5 && ParseUtil.sum8 < 3){
            tv8Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum8 == 0){
            tv8Mark.setTextColor(tv8Mark.getTextColors().getDefaultColor());
        }
        else{
            tv8Mark.setTextColor(okMark);
        }

        if (ParseUtil.sum9 >= 3){
            tv9Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum9 >= 2.5 && ParseUtil.sum9 < 3){
            tv9Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum9 == 0){
            tv9Mark.setTextColor(tv9Mark.getTextColors().getDefaultColor());
        }
        else{
            tv9Mark.setTextColor(okMark);
        }

        if (ParseUtil.sum10 >= 3){
            tv10Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum10 >= 2.5 && ParseUtil.sum10 < 3){
            tv10Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum10 == 0){
            tv10Mark.setTextColor(tv10Mark.getTextColors().getDefaultColor());
        }
        else{
            tv10Mark.setTextColor(okMark);
        }

        if (ParseUtil.sum11 >= 3){
            tv11Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum11 >= 2.5 && ParseUtil.sum11 < 3){
            tv11Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum11 == 0){
            tv11Mark.setTextColor(tv11Mark.getTextColors().getDefaultColor());
        }
        else{
            tv11Mark.setTextColor(okMark);
        }

        if (ParseUtil.sum12 >= 3){
            tv12Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum12 >= 2.5 && ParseUtil.sum12 < 3){
            tv12Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum12 == 0){
            tv12Mark.setTextColor(tv12Mark.getTextColors().getDefaultColor());
        }
        else{
            tv12Mark.setTextColor(okMark);
        }

        if (ParseUtil.sum13 >= 3){
            tv13Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum13 >= 2.5 && ParseUtil.sum13 < 3){
            tv13Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum13 == 0){
            tv13Mark.setTextColor(tv13Mark.getTextColors().getDefaultColor());
        }
        else{
            tv13Mark.setTextColor(okMark);
        }

        if (ParseUtil.sum14 >= 3){
            tv14Mark.setTextColor(badMark);
        }
        else if(ParseUtil.sum14 >= 2.5 && ParseUtil.sum14 < 3){
            tv14Mark.setTextColor(ffMark);
        }
        else if (ParseUtil.sum14 == 0){
            tv14Mark.setTextColor(tv14Mark.getTextColors().getDefaultColor());
        }
        else{
            tv14Mark.setTextColor(okMark);
        }



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
      /*  if (avg <= SettingsActivity.getAvgFromPreference(SettingsActivity.prefOkMark)
                && avg != 0) {
            v.setTextColor(okMark);
        }
        else if (avg > SettingsActivity.getAvgFromPreference(SettingsActivity.prefOkMark)
                && avg <= SettingsActivity.getAvgFromPreference(SettingsActivity.prefFfMark) ){
            v.setTextColor(ffMark);
        }
        else if (avg > SettingsActivity.getAvgFromPreference(SettingsActivity.prefFfMark)){
            v.setTextColor(badMark);
        }
        else {
            v.setTextColor(v.getTextColors().getDefaultColor());
        }*/

        // If avg > 3
        double okMarkNum = SettingsActivity.getAvgFromPreference(SettingsActivity.prefOkMark);
        double okMarkNum2 = 45 / 100;
        if (avg > SettingsActivity.getAvgFromPreference(SettingsActivity.prefFfMark)){
            v.setTextColor(badMark);
        }
        // If avg <= 3 && avg > 2.45
        else if(avg <= SettingsActivity.getAvgFromPreference(SettingsActivity.prefFfMark)
                && avg > (okMarkNum+okMarkNum2)){
            v.setTextColor(ffMark);
        }
        // If avg <= 2 && != 0
        else if (avg <= (okMarkNum+okMarkNum2) && avg != 0){
            v.setTextColor(okMark);
        }
        // If avg = 0
        else{
            v.setTextColor(v.getTextColors().getDefaultColor());
        }
    }

    @Override
    public void onResume() {
        changeColor(tv1Mark, 1);
        changeColor(tv2Mark, 2);
        changeColor(tv3Mark, 3);
        changeColor(tv4Mark, 4);
        changeColor(tv5Mark, 5);
        changeColor(tv6Mark, 6);
        changeColor(tv7Mark, 7);
        changeColor(tv8Mark, 8);
        changeColor(tv9Mark, 9);
        changeColor(tv10Mark, 10);
        changeColor(tv11Mark, 11);
        changeColor(tv12Mark, 12);
        changeColor(tv13Mark, 13);
        changeColor(tv14Mark, 14);

        setTextMarks(tv1Mark, 1);
        setTextMarks(tv2Mark, 2);
        setTextMarks(tv3Mark, 3);
        setTextMarks(tv4Mark, 4);
        setTextMarks(tv5Mark, 5);
        setTextMarks(tv6Mark, 6);
        setTextMarks(tv7Mark, 7);
        setTextMarks(tv8Mark, 8);
        setTextMarks(tv9Mark, 9);
        setTextMarks(tv10Mark, 10);
        setTextMarks(tv11Mark, 11);
        setTextMarks(tv12Mark, 12);
        setTextMarks(tv13Mark, 13);
        setTextMarks(tv14Mark, 14);

        setNames(getContext());
        super.onResume();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        changeColor(tv1Mark, 1);
        changeColor(tv2Mark, 2);
        changeColor(tv3Mark, 3);
        changeColor(tv4Mark, 4);
        changeColor(tv5Mark, 5);
        changeColor(tv6Mark, 6);
        changeColor(tv7Mark, 7);
        changeColor(tv8Mark, 8);
        changeColor(tv9Mark, 9);
        changeColor(tv10Mark, 10);
        changeColor(tv11Mark, 11);
        changeColor(tv12Mark, 12);
        changeColor(tv13Mark, 13);
        changeColor(tv14Mark, 14);

        setTextMarks(tv1Mark, 1);
        setTextMarks(tv2Mark, 2);
        setTextMarks(tv3Mark, 3);
        setTextMarks(tv4Mark, 4);
        setTextMarks(tv5Mark, 5);
        setTextMarks(tv6Mark, 6);
        setTextMarks(tv7Mark, 7);
        setTextMarks(tv8Mark, 8);
        setTextMarks(tv9Mark, 9);
        setTextMarks(tv10Mark, 10);
        setTextMarks(tv11Mark, 11);
        setTextMarks(tv12Mark, 12);
        setTextMarks(tv13Mark, 13);
        setTextMarks(tv14Mark, 14);

        setNames(getContext());
        MainActivity.tabRl.setVisibility(View.INVISIBLE);
        super.onAttachFragment(childFragment);
    }
}
