package cz.fely.weightedaverage.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.activities.MainActivity;

import static cz.fely.weightedaverage.activities.MainActivity.cl;
import static cz.fely.weightedaverage.activities.MainActivity.context;
import static cz.fely.weightedaverage.activities.MainActivity.mDbAdapterStatic;
import static cz.fely.weightedaverage.activities.MainActivity.man;
import static cz.fely.weightedaverage.activities.MainActivity.tabPosition;

public class SubjectTemplateFragment extends Fragment {

    public static SubjectTemplateFragment fragment;
    private MainActivity mActivity;
    int mHour, mMinute;
    int subjectId;
    String dateCompleted;
    static String argumentsKey = "subject_id";
    public static View view;

    public SubjectTemplateFragment newInstance(int page) {
        SubjectTemplateFragment frag = new SubjectTemplateFragment();
        Bundle args = new Bundle();
        args.putInt(argumentsKey, page);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null){
            this.subjectId = getArguments() != null ? getArguments().getInt(argumentsKey) : 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activity_main, container, false);
        MainActivity.checkSettings(view);
        fragment = SubjectTemplateFragment.this;
        EditText etMark, etWeight;
        AutoCompleteTextView etName;
        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        etName = (AutoCompleteTextView) view.findViewById(R.id.etName);
        etMark = (EditText) view.findViewById(R.id.etMark);
        etWeight = (EditText) view.findViewById(R.id.etWeight);
        ListView lv = (ListView) view.findViewById(R.id.lvZnamky);

        btnAdd.setOnClickListener(v ->
                addOrUpdateMark(view, tabPosition, getContext(),
                        etName.getText().toString(), etMark.getText().toString(),
                        etWeight.getText().toString(), null, new long[0]));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDialog(((TextView) view.findViewById(R.id.name)).getText().toString(), (
                        (TextView) view.findViewById(R.id.mark)).getText().toString(), ((TextView)
                        view.findViewById(R.id.weight)).getText().toString(), id);
            }
        });
        return view;
    }

    public void showEditDialog(String name, String mark, String weight, long id){
        LayoutInflater inflater = LayoutInflater.from(man);
        View v = inflater.inflate(R.layout.edit_dialog, null);
        EditText etNameDialog, etMarkDialog,etWeightDialog;
        TextView vDateDialog;
        etNameDialog = (EditText) v.findViewById(R.id.etNameDialog);
        etMarkDialog = (EditText) v.findViewById(R.id.etMarkDialog);
        etWeightDialog = (EditText) v.findViewById(R.id.etWeightDialog);
        vDateDialog = (TextView) v.findViewById(R.id.viewDateDialog);
        DatePicker dp = new DatePicker(context);
        etNameDialog.setText(name);
        etMarkDialog.setText(mark);
        etWeightDialog.setText(weight);
        Cursor c = mDbAdapterStatic.getDate(tabPosition, id);
        String date = c.getString(c.getColumnIndex("day"));
        vDateDialog.setText(date);
        vDateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear;
                int mMonth;
                int mDay;

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date_time = dayOfMonth + "." + (monthOfYear + 1) + "." + year;

                                //TIME SET
                                final Calendar c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);

                                // Launch Time Picker Dialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(
                                        SubjectTemplateFragment.this.getContext(), new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        mHour = hourOfDay;
                                        mMinute = minute;
                                        dateCompleted = date_time+" " + hourOfDay + ":" + minute;
                                        vDateDialog.setText(date_time+" " + hourOfDay + ":" + minute);
                                    }
                                }, mHour, mMinute, true);
                                timePickerDialog.show();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(R.string.editMark);
        adb.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addOrUpdateMark(view, tabPosition, context, etNameDialog.getText()
                        .toString(), etMarkDialog.getText().toString(), etWeightDialog.getText()
                        .toString(), dateCompleted, id);
            }
        });
        adb.setNegativeButton(R.string.titleDelete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeMark(tabPosition, id);
            }
        });
        adb.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.setView(v);
        adb.show();
    }

    public void removeMark(int posArg, long id) {
        mDbAdapterStatic.deleteMark(id, posArg);
        man.refreshViews(posArg);
    }

    public void addOrUpdateMark(View v, int posArg, Context ctx, String name, String m,
                                String w, @Nullable String date, long... id) {
        EditText etName, etMark, etWeight;
        etName = (EditText) v.findViewById(R.id.etName);
        etMark = (EditText) v.findViewById(R.id.etMark);
        etWeight = (EditText) v.findViewById(R.id.etWeight);
        try {
            double weight;
            double mark;
            if (w.equals("")) {
                weight = 1;
            } else {
                weight = Double.parseDouble(w);
            }
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(m) || m.equals("0")) {
                throw new IllegalArgumentException(ctx.getResources().getString(R.string
                        .illegalArgument));
            }
            else {
                mark = Double.parseDouble(m);
                if(mark > 5 || weight == 0 || mark < 1){
                    throw new IllegalArgumentException(ctx.getResources().getString(R.string
                            .invalidMarkWeight));
                }
            }
            if (id.length == 0) {
                if(date == null){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    Date notFormatedDate = new Date();
                    date = dateFormat.format(notFormatedDate);
                }
                mDbAdapterStatic.addMark(posArg, name, mark, weight, date);
                etName.setText("");
                etMark.setText("");
                etWeight.setText("");
                etName.requestFocus();
            } else {
                mDbAdapterStatic.updateMark(posArg, name, mark, weight, date, id[0]);
            }
            man.refreshViews(posArg);
        } catch (IllegalArgumentException e) {
            Snackbar.make(cl, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        //MainActivity.getViews(view);
        MainActivity.checkSettings(view);
        Log.i("SubjectFragment: ", "Fragment resumed");
        super.onResume();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        Log.i("SubjectFragment: ", "Fragment attached");
        //MainActivity.getViews(view);
        super.onAttachFragment(childFragment);
    }
}