package cz.fely.weightedaverage.subjects;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.utils.PreferencesUtil;

public class OverviewFragment extends Fragment {

     public static TextView tv1Title, tv2Title, tv3Title, tv4Title, tv5Title, tv6Title, tv7Title,
    tv8Title,
            tv9Title, tv10Title, tv1Mark, tv2Mark, tv3Mark, tv4Mark, tv5Mark, tv6Mark, tv7Mark, tv8Mark, tv9Mark,
            tv10Mark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState){

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
        updateOverView(getContext());
        return view;
    }

    public static void updateOverView(Context ctx) {
        PreferencesUtil.getTabNames(ctx);
        PreferencesUtil.getAverages(ctx);

        //Set Titles
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
}
