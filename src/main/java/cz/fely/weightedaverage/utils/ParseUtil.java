package cz.fely.weightedaverage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.Preference;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.View;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import javax.security.auth.SubjectDomainCombiner;

import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.subjects.*;

import static cz.fely.weightedaverage.MainActivity.mDbAdapterStatic;
import static cz.fely.weightedaverage.MainActivity.tabPosition;

public final class ParseUtil {
    private static SharedPreferences mPrefs;
    public static String overview;
    public static String one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, tt, ft;
    public static String oneAvg, twoAvg, threeAvg, fourAvg, fiveAvg, sixAvg, sevenAvg, eightAvg, nineAvg, tenAvg, elevenAvg, twelveAvg, ttAvg, ftAvg;
    public static double sum1, sum2, sum3, sum4, sum5, sum6, sum7, sum8, sum9, sum10, sum11, sum12, sum13, sum14;

    public static void reloadPref(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        //Weight
        boolean weight = mPrefs.getBoolean("pref_key_general_weight", true);
        if(SubjectOneFragment.view != null | SubjectTwoFragment.view != null | SubjectThreeFragment
                .view != null | SubjectFourFragment.view != null | SubjectFiveFragment.view !=
                null | SubjectSixFragment.view != null | SubjectSevenFragment.view != null |
                SubjectEightFragment.view != null | SubjectNineFragment.view != null |
                SubjectTenFragment.view != null) {
            if (weight) {
                SubjectOneFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectTwoFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectThreeFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectFourFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectFiveFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectSixFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectSevenFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectEightFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectNineFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectTenFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectElevenFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectTwelveFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectTtFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
                SubjectFtFragment.view.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
            } else {
                SubjectOneFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectTwoFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectThreeFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectFourFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectFiveFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectSixFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectSevenFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectEightFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectNineFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectTenFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectElevenFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectTwelveFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectTtFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
                SubjectFtFragment.view.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
            }
        }
    }

    public static void getTabNames(Context ctx){
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        //Names
        overview = ctx.getResources().getString(R.string.overView);
        one = mPrefs.getString("pref_key_subject_name_one", ctx.getResources().getString(R.string
                        .nameOneDefault));
        two = mPrefs.getString("pref_key_subject_name_two", ctx.getResources().getString(R.string
                        .nameTwoDefault));
        three = mPrefs.getString("pref_key_subject_name_three", ctx.getResources().getString(R.string
                        .nameThreeDefault));
        four = mPrefs.getString("pref_key_subject_name_four", ctx.getResources().getString(R.string
                        .nameFourDefault));
        five = mPrefs.getString("pref_key_subject_name_five", ctx.getResources().getString(R.string
                        .nameFiveDefault));
        six = mPrefs.getString("pref_key_subject_name_six", ctx.getResources().getString(R.string
                        .nameSixDefault));
        seven = mPrefs.getString("pref_key_subject_name_seven", ctx.getResources().getString(R.string
                .nameSevenDefault));
        eight = mPrefs.getString("pref_key_subject_name_eight", ctx.getResources().getString(R.string
                .nameEightDefault));
        nine = mPrefs.getString("pref_key_subject_name_nine", ctx.getResources().getString(R.string
                .nameNineDefault));
        ten = mPrefs.getString("pref_key_subject_name_ten", ctx.getResources().getString(R.string
                .nameTenDefault));
        eleven = mPrefs.getString("pref_key_subject_name_eleven", ctx.getResources().getString(R.string
                .nameElevenDefault));
        twelve = mPrefs.getString("pref_key_subject_name_twelve", ctx.getResources().getString(R.string
                .nameTwelveDefault));
        tt = mPrefs.getString("pref_key_subject_name_tt", ctx.getResources().getString(R.string
                .nameTtDefault));
        ft = mPrefs.getString("pref_key_subject_name_ft", ctx.getResources().getString(R.string
                .nameFtDefault));
    }

    public static void getTabAverages(){
        Cursor cursor;
        DecimalFormat formater;
        cursor = mDbAdapterStatic.makeAverage(1);
        sum1 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        oneAvg = String.valueOf(formater.format(sum1));

        cursor = mDbAdapterStatic.makeAverage(2);
        sum2 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        twoAvg = String.valueOf(formater.format(sum2));

        cursor = mDbAdapterStatic.makeAverage(3);
        sum3 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        threeAvg = String.valueOf(formater.format(sum3));

        cursor = mDbAdapterStatic.makeAverage(4);
        sum4 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        fourAvg = String.valueOf(formater.format(sum4));

        cursor = mDbAdapterStatic.makeAverage(5);
        sum5 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        fiveAvg = String.valueOf(formater.format(sum5));

        cursor = mDbAdapterStatic.makeAverage(6);
        sum6 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        sixAvg = String.valueOf(formater.format(sum6));

        cursor = mDbAdapterStatic.makeAverage(7);
        sum7 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        sevenAvg = String.valueOf(formater.format(sum7));

        cursor = mDbAdapterStatic.makeAverage(8);
        sum8 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        eightAvg = String.valueOf(formater.format(sum8));

        cursor = mDbAdapterStatic.makeAverage(9);
        sum9 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        nineAvg = String.valueOf(formater.format(sum9));

        cursor = mDbAdapterStatic.makeAverage(10);
        sum10 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        tenAvg = String.valueOf(formater.format(sum10));

        cursor = mDbAdapterStatic.makeAverage(11);
        sum11 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        elevenAvg = String.valueOf(formater.format(sum11));

        cursor = mDbAdapterStatic.makeAverage(12);
        sum12 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        twelveAvg = String.valueOf(formater.format(sum12));

        cursor = mDbAdapterStatic.makeAverage(13);
        sum13 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        ttAvg = String.valueOf(formater.format(sum13));

        cursor = mDbAdapterStatic.makeAverage(14);
        sum14 = cursor.getDouble(cursor.getColumnIndex("average"));
        formater = new DecimalFormat("0.00");
        ftAvg = String.valueOf(formater.format(sum14));
    }

    public static View getViewByTab(){
         if(tabPosition == 1){
             return SubjectOneFragment.view;
         }
        else if(tabPosition == 2) {
             return SubjectTwoFragment.view;
         }
        else if(tabPosition == 3) {
             return SubjectThreeFragment.view;
         }
        else if(tabPosition == 4) {
             return SubjectFourFragment.view;
         }
        else if(tabPosition == 5) {
             return SubjectFiveFragment.view;
         }
        else if(tabPosition == 6) {
             return SubjectSixFragment.view;
         }
        else if(tabPosition == 7) {
             return SubjectSevenFragment.view;
         }
        else if(tabPosition == 8) {
             return SubjectEightFragment.view;
         }
         else if(tabPosition == 9) {
             return SubjectNineFragment.view;
         }
         else if(tabPosition == 10) {
             return SubjectTenFragment.view;
         }
         else if(tabPosition == 11) {
             return SubjectElevenFragment.view;
         }
         else if(tabPosition == 12) {
             return SubjectTwelveFragment.view;
         }
         else if(tabPosition == 13) {
             return SubjectTtFragment.view;
         }
         else if(tabPosition == 14) {
             return SubjectFtFragment.view;
         }
        else {
             return null;
         }
    }

    public static View getViewByPos(int pos){
        if(pos == 1){
            return SubjectOneFragment.view;
        }
        else if(pos == 2) {
            return SubjectTwoFragment.view;
        }
        else if(pos == 3) {
            return SubjectThreeFragment.view;
        }
        else if(pos == 4) {
            return SubjectFourFragment.view;
        }
        else if(pos == 5) {
            return SubjectFiveFragment.view;
        }
        else if(pos == 6) {
            return SubjectSixFragment.view;
        }
        else if(pos == 7) {
            return SubjectSevenFragment.view;
        }
        else if(pos == 8) {
            return SubjectEightFragment.view;
        }
        else if(pos == 9) {
            return SubjectNineFragment.view;
        }
        else if(pos == 10) {
            return SubjectTenFragment.view;
        }
        else if(pos == 11) {
            return SubjectElevenFragment.view;
        }
        else if(pos == 12) {
            return SubjectTwelveFragment.view;
        }
        else if(pos == 13) {
            return SubjectTtFragment.view;
        }
        else if(pos == 14) {
            return SubjectFtFragment.view;
        }
        else {
            return null;
        }
    }

    public static Fragment getSubjectFrag(){
        if(tabPosition == 1){
            return SubjectOneFragment.fragment;
        }
        else if(tabPosition == 2) {
            return SubjectTwoFragment.fragment;
        }
        else if(tabPosition == 3) {
            return SubjectThreeFragment.fragment;
        }
        else if(tabPosition == 4) {
            return SubjectFourFragment.fragment;
        }
        else if(tabPosition == 5) {
            return SubjectFiveFragment.fragment;
        }
        else if(tabPosition == 6) {
            return SubjectSixFragment.fragment;
        }
        else if(tabPosition == 7) {
            return SubjectSevenFragment.fragment;
        }
        else if(tabPosition == 8) {
            return SubjectEightFragment.fragment;
        }
        else if(tabPosition == 9) {
            return SubjectNineFragment.fragment;
        }
        else if(tabPosition == 10) {
            return SubjectTenFragment.fragment;
        }
        else if(tabPosition == 11) {
            return SubjectElevenFragment.fragment;
        }
        else if(tabPosition == 12) {
            return SubjectTwelveFragment.fragment;
        }
        else if(tabPosition == 13) {
            return SubjectTtFragment.fragment;
        }
        else if(tabPosition == 14) {
            return SubjectFtFragment.fragment;
        }
        else {
            return null;
        }
    }

    public static double avgByIntReturnDouble (int subject){
        Cursor cursor;
        Double avg;

        if(subject == 1) {
            cursor = mDbAdapterStatic.makeAverage(1);
            sum1 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum1).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 2) {
            cursor = mDbAdapterStatic.makeAverage(2);
            sum2 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum2).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 3) {
            cursor = mDbAdapterStatic.makeAverage(3);
            sum3 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum3).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 4) {
            cursor = mDbAdapterStatic.makeAverage(4);
            sum4 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum4).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 5) {
            cursor = mDbAdapterStatic.makeAverage(5);
            sum5 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum5).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 6) {
            cursor = mDbAdapterStatic.makeAverage(6);
            sum6 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum6).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 7) {
            cursor = mDbAdapterStatic.makeAverage(7);
            sum7 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum7).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 8) {
            cursor = mDbAdapterStatic.makeAverage(8);
            sum8 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum8).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 9) {
            cursor = mDbAdapterStatic.makeAverage(9);
            sum9 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum9).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 10) {
            cursor = mDbAdapterStatic.makeAverage(10);
            sum10 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum10).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 11) {
            cursor = mDbAdapterStatic.makeAverage(11);
            sum11 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum11).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 12) {
            cursor = mDbAdapterStatic.makeAverage(12);
            sum12 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum12).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 13) {
            cursor = mDbAdapterStatic.makeAverage(13);
            sum13 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum13).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }

        else if(subject == 14) {
            cursor = mDbAdapterStatic.makeAverage(14);
            sum14 = cursor.getDouble(cursor.getColumnIndex("average"));
            avg = new BigDecimal(sum14).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return avg;
        }
        else return 0;
    }
}
