package cz.fely.weightedaverage.subjects;

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
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.fely.weightedaverage.ListAdapter;
import cz.fely.weightedaverage.MainActivity;
import cz.fely.weightedaverage.R;

import static cz.fely.weightedaverage.MainActivity.cl;
import static cz.fely.weightedaverage.MainActivity.context;
import static cz.fely.weightedaverage.MainActivity.mDbAdapterStatic;
import static cz.fely.weightedaverage.MainActivity.man;
import static cz.fely.weightedaverage.MainActivity.subjectsStatePagerAdapter;
import static cz.fely.weightedaverage.MainActivity.tabLayout;
import static cz.fely.weightedaverage.MainActivity.tabPosition;
import static cz.fely.weightedaverage.MainActivity.tvAverage;
import static cz.fely.weightedaverage.MainActivity.tvCount;
import static cz.fely.weightedaverage.MainActivity.viewPager;

public class SubjectTemplateFragment extends Fragment {

    public static View subjectView;
    public static SubjectTemplateFragment fragment;
    private MainActivity mActivity;
    private String subjectId;
    int mHour, mMinute;
    String dateCompleted;

    public SubjectTemplateFragment newInstance(int page) {
        SubjectTemplateFragment frag = new SubjectTemplateFragment();
        Bundle args = new Bundle();
        args.putInt("subject_id", page);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null){
            this.subjectId = arguments.getString("subject_id");
        }
    }

    public void refreshViews(int position){
        EditText etMark, etWeight;
        AutoCompleteTextView etName;
        ListView lv;
        Cursor c = mDbAdapterStatic.getAllEntries(position);
        View v = viewPager.getFocusedChild();
        if(v != null) {
            lv = (ListView) v.findViewById(R.id.lvZnamky);
            etName = (AutoCompleteTextView) v.findViewById(R.id.etName);
            etMark = (EditText) v.findViewById(R.id.etMark);
            autoCompleteAuth(etName, context);
            lv.setAdapter(new ListAdapter(man, c, 0));
        }
        average(getContext(), position);
    }

    public SubjectTemplateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_main, container, false);
        MainActivity.checkSettings(view);
        fragment = SubjectTemplateFragment.this;
        subjectView = view;
        mActivity = man;
        refreshViews(tabLayout.getSelectedTabPosition());
        setClickListeners();
        return view;
    }

    private void setClickListeners() {
        setListViewItemClickListener();
        setBtnAddClickListener();
    }

    private void setBtnAddClickListener() {
        EditText etName, etMark, etWeight;
        etName = (EditText) subjectView.findViewById(R.id.etName);
        etMark = (EditText) subjectView.findViewById(R.id.etMark);
        etWeight = (EditText) subjectView.findViewById(R.id.etWeight);
        Button btnAdd = (Button) subjectView.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v ->
                addOrUpdateMark(subjectView, tabPosition, getContext(),
                etName.getText().toString(), etMark.getText().toString(),
                etWeight.getText().toString(), null, new long[0]));
    }

    private void setListViewItemClickListener() {
        ListView lvMarks = (ListView) subjectView.findViewById(R.id.lvZnamky);
        lvMarks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDialog(((TextView) view.findViewById(R.id.name)).getText().toString(), (
                        (TextView) view.findViewById(R.id.mark)).getText().toString(), ((TextView)
                        view.findViewById(R.id.weight)).getText().toString(), id);
            }
        });
    }

    private void autoCompleteAuth(AutoCompleteTextView etName, Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("autoComplete", true)) {
            String[] array = helpList().toArray(new String[0]);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_dropdown_item_1line, array);
            etName.setThreshold(0);
            etName.setAdapter(adapter);
        }
    }

    public void fillListView(int sub){
        ListView lv = (ListView) subjectView.findViewById(R.id.lvZnamky);
        lv.setAdapter(new ListAdapter(man, mDbAdapterStatic.getAllEntries(sub), 0));
    }

    private ArrayList<String> helpList() {
        Cursor cursor = MainActivity.mDbAdapterStatic.getFromNameEntries();
        ArrayList<String> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    if(list.contains(cursor.getString(i))
                            || list.contains(cursor.getString(i).trim()) && cursor.getString(i).endsWith(" ")
                            || list.contains(cursor.getString(i).trim()) && cursor.getString(i).startsWith(" ")
                            || list.contains(cursor.getString(i).trim()) && cursor.getString(i).endsWith(".")
                            || list.contains(cursor.getString(i).trim()) && cursor.getString(i).startsWith(".")
                            || list.contains(cursor.getString(i).trim()) && cursor.getString(i).endsWith(",")
                            || list.contains(cursor.getString(i).trim()) && cursor.getString(i).startsWith(",")
                            || list.contains(cursor.getString(i).trim()) && cursor.getString(i).endsWith("-")
                            || list.contains(cursor.getString(i).trim()) && cursor.getString(i).startsWith("-")){
                    }
                    else {
                        list.add(cursor.getString(i));
                    }
                }
            } while (cursor.moveToNext());
        }
        return list;
    }

    private void average(Context ctx, int posArg){
        Cursor cursor;
        cursor = mDbAdapterStatic.makeAverage(posArg);
        double sum = cursor.getDouble(cursor.getColumnIndex("average"));
        DecimalFormat formater = new DecimalFormat("0.00");
        String total = String.valueOf(formater.format(sum));
        tvAverage.setText(total);
        tvCount.setText(String.valueOf((mDbAdapterStatic.getAllEntries(posArg)).getCount()));
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
                addOrUpdateMark(subjectView, tabPosition, context, etNameDialog.getText()
                        .toString(), etMarkDialog.getText().toString(), etWeightDialog.getText()
                        .toString(), dateCompleted, id);
            }
        });
        adb.setNegativeButton(R.string.titleDelete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeMark(tabPosition, context, id);
            }
        });
        adb.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        adb.setView(v);
        adb.show();
    }

    public void removeMark(int posArg, Context ctx, long id) {
        mDbAdapterStatic.deleteMark(id, posArg);
        SubjectTemplateFragment fragment = (SubjectTemplateFragment) subjectsStatePagerAdapter.getItem(posArg);
        refreshViews(posArg);
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
            refreshViews(posArg);
        } catch (IllegalArgumentException e) {
            Snackbar.make(cl, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        //MainActivity.getViews(view);
        MainActivity.checkSettings(subjectView);
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