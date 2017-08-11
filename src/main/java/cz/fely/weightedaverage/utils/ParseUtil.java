package cz.fely.weightedaverage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.fragments.*;

import static cz.fely.weightedaverage.activities.MainActivity.mDbAdapterStatic;
import static cz.fely.weightedaverage.activities.MainActivity.tabPosition;

public final class ParseUtil {
    private static SharedPreferences mPrefs;
    public static String overview;
    public static String one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, tt, ft;
    public static String oneAvg, twoAvg, threeAvg, fourAvg, fiveAvg, sixAvg, sevenAvg, eightAvg, nineAvg, tenAvg, elevenAvg, twelveAvg, ttAvg, ftAvg;
    public static double sum1, sum2, sum3, sum4, sum5, sum6, sum7, sum8, sum9, sum10, sum11, sum12, sum13, sum14;

    public ParseUtil(Context ctx){

    }

    public static void reloadPref(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        //Weight
        boolean weight = mPrefs.getBoolean("pref_key_general_weight", true);
        if(SubjectTemplateFragment.view != null) {
            if (weight) {
                SubjectTemplateFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
            } else {
                SubjectTemplateFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
            }
        }
    }

    public static String getTabNames(int index){
        return mDbAdapterStatic.getTabTitle(index);
    }

    public static TextView overviewTVs(int index){

        OverviewFragment.tv1Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectOneTitleOverview);
        OverviewFragment.tv2Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectThreeTitleOverview);
        OverviewFragment.tv3Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectTwoTitleOverview);
        OverviewFragment.tv4Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectFourTitleOverview);
        OverviewFragment.tv5Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectFiveTitleOverview);
        OverviewFragment.tv6Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectSixTitleOverview);
        OverviewFragment.tv7Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectSevenTitleOverview);
        OverviewFragment.tv8Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectEightTitleOverview);
        OverviewFragment.tv9Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectNineTitleOverview);
        OverviewFragment.tv10Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectTenTitleOverview);
        OverviewFragment.tv11Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectElevenTitleOverview);
        OverviewFragment.tv12Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectTwelveTitleOverview);
        OverviewFragment.tv13Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectTtTitleOverview);
        OverviewFragment.tv14Title = (TextView) OverviewFragment.view.findViewById(R.id.SubjectFtTitleOverview);
        switch (index){
            case 1:
                return OverviewFragment.tv1Title;
            case 2:
                return OverviewFragment.tv2Title;
            case 3:
                return OverviewFragment.tv3Title;
            case 4:
                return OverviewFragment.tv4Title;
            case 5:
                return OverviewFragment.tv5Title;
            case 6:
                return OverviewFragment.tv6Title;
            case 7:
                return OverviewFragment.tv7Title;
            case 8:
                return OverviewFragment.tv8Title;
            case 9:
                return OverviewFragment.tv9Title;
            case 10:
                return OverviewFragment.tv10Title;
            case 11:
                return OverviewFragment.tv11Title;
            case 12:
                return OverviewFragment.tv12Title;
            case 13:
                return OverviewFragment.tv13Title;
            case 14:
                return OverviewFragment.tv14Title;
            default:
                return null;

        }//end switch
    }

    public static void getTabAverages(){
        Cursor cursor;
        DecimalFormat formatter;

        cursor = mDbAdapterStatic.makeAverage(1);
        sum1 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        oneAvg = String.valueOf(formatter.format(sum1));

        cursor = mDbAdapterStatic.makeAverage(2);
        sum2 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        twoAvg = String.valueOf(formatter.format(sum2));

        cursor = mDbAdapterStatic.makeAverage(3);
        sum3 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        threeAvg = String.valueOf(formatter.format(sum3));

        cursor = mDbAdapterStatic.makeAverage(4);
        sum4 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        fourAvg = String.valueOf(formatter.format(sum4));

        cursor = mDbAdapterStatic.makeAverage(5);
        sum5 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        fiveAvg = String.valueOf(formatter.format(sum5));

        cursor = mDbAdapterStatic.makeAverage(6);
        sum6 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        sixAvg = String.valueOf(formatter.format(sum6));

        cursor = mDbAdapterStatic.makeAverage(7);
        sum7 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        sevenAvg = String.valueOf(formatter.format(sum7));

        cursor = mDbAdapterStatic.makeAverage(8);
        sum8 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        eightAvg = String.valueOf(formatter.format(sum8));

        cursor = mDbAdapterStatic.makeAverage(9);
        sum9 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        nineAvg = String.valueOf(formatter.format(sum9));

        cursor = mDbAdapterStatic.makeAverage(10);
        sum10 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        tenAvg = String.valueOf(formatter.format(sum10));

        cursor = mDbAdapterStatic.makeAverage(11);
        sum11 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        elevenAvg = String.valueOf(formatter.format(sum11));

        cursor = mDbAdapterStatic.makeAverage(12);
        sum12 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        twelveAvg = String.valueOf(formatter.format(sum12));

        cursor = mDbAdapterStatic.makeAverage(13);
        sum13 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        ttAvg = String.valueOf(formatter.format(sum13));

        cursor = mDbAdapterStatic.makeAverage(14);
        sum14 = cursor.getDouble(cursor.getColumnIndex("average"));
        formatter = new DecimalFormat("0.00");
        ftAvg = String.valueOf(formatter.format(sum14));
    }

    public static Fragment getSubjectFrag(){
        if(tabPosition == 0){
            return OverviewFragment.ovf;
        }
        else
            return SubjectTemplateFragment.fragment;
    }

    public static double avgByIntReturnDouble (int subject){
        Cursor cursor;
        Double avg;

        switch (subject) {
            case 1:
                cursor = mDbAdapterStatic.makeAverage(1);
                sum1 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum1).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 2:
                cursor = mDbAdapterStatic.makeAverage(2);
                sum2 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum2).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 3:
                cursor = mDbAdapterStatic.makeAverage(3);
                sum3 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum3).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 4:
                cursor = mDbAdapterStatic.makeAverage(4);
                sum4 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum4).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 5:
                cursor = mDbAdapterStatic.makeAverage(5);
                sum5 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum5).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 6:
                cursor = mDbAdapterStatic.makeAverage(6);
                sum6 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum6).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 7:
                cursor = mDbAdapterStatic.makeAverage(7);
                sum7 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum7).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 8:
                cursor = mDbAdapterStatic.makeAverage(8);
                sum8 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum8).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 9:
                cursor = mDbAdapterStatic.makeAverage(9);
                sum9 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum9).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 10:
                cursor = mDbAdapterStatic.makeAverage(10);
                sum10 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum10).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 11:
                cursor = mDbAdapterStatic.makeAverage(11);
                sum11 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum11).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 12:
                cursor = mDbAdapterStatic.makeAverage(12);
                sum12 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum12).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 13:
                cursor = mDbAdapterStatic.makeAverage(13);
                sum13 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum13).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            case 14:
                cursor = mDbAdapterStatic.makeAverage(14);
                sum14 = cursor.getDouble(cursor.getColumnIndex("average"));
                avg = new BigDecimal(sum14).setScale(2, RoundingMode.HALF_UP).doubleValue();
                return avg;
            default:
                return 0.00;
        }
    }
}
