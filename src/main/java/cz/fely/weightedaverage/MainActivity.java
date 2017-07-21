package cz.fely.weightedaverage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.FeedbackManager;
import net.hockeyapp.android.LoginManager;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.UpdateManagerListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.fely.weightedaverage.db.Database;
import cz.fely.weightedaverage.subjects.OverviewFragment;
import cz.fely.weightedaverage.subjects.SubjectTemplateFragment;
import cz.fely.weightedaverage.utils.ParseUtil;
import cz.fely.weightedaverage.utils.ThemeUtil;

public class MainActivity extends AppCompatActivity{
    private Toolbar toolbar;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int tabPosition;
    public static Database mDbAdapterStatic;
    public static MainActivity man;
    static ListView lv;
    static AutoCompleteTextView etName;
    static TextView tvAverage, tvCount;
    private static final int PERIOD = 2000;
    private long lastPressedTime;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    public static Context context;
    public static CoordinatorLayout cl;
    public static MultiAutoCompleteTextView mactv;
    public static GestureDetector gestureDetector;
    public static RelativeLayout rl;
    public static GestureDetectorCompat detector;
    public static RelativeLayout tabRl;
    public static String date_time;
    public static int mHour;
    public static int mMinute;
    public static EditText etMark;
    public static String dateCompleted;
    private static ViewPagerAdapter viewPagerAdapter;

    //MAIN ACTIVITY

    @Override
    public void onResume(){
        ThemeUtil.setTheme(this);
       // viewPagerAdapter.changeTitles();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(tabPosition == 0){
            menu.findItem(R.id.action_deletemarks).setVisible(false);
        }
        else {
            menu.findItem(R.id.action_deletemarks).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        if (id == R.id.action_feedback) {
            FeedbackManager.register(this);
            FeedbackManager.showFeedbackActivity(MainActivity.this);
        }
        if (id == R.id.action_deletemarks) {
            if(lv.getCount() == 0){
                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                        .coordinator);
                Snackbar.make(coordinatorLayout, R.string.errorSnackListSizeZero,
                        Snackbar.LENGTH_LONG)
                        .show();
            }
            else {
                AlertDialog.Builder adb;
                adb = new AlertDialog.Builder(this);
                adb.setTitle(R.string.vymazatZnamky);
                adb.setIcon(R.drawable.warning);
                adb.setMessage(R.string.deleteAllMes);
                adb.setNegativeButton(R.string.cancel, null);
                adb.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    mDbAdapterStatic.deleteSubject(tabPosition);
                    View v;
                    v = ParseUtil.getViewByTab();
                    refreshViews(this);
                });
                adb.show();
            }
        }

        if (id == R.id.action_about){
            Intent i = new Intent(this, AboutFragment.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void getViews(View v){
        if(v != null) {
            lv = (ListView) v.findViewById(R.id.lvZnamky);
            etName = (AutoCompleteTextView) v.findViewById(R.id.etName);
            EditText etMark = (EditText) v.findViewById(R.id.etMark);
            autoCompleteAuth();
            fillListView(tabPosition);
        }
        else {
            Log.e("void getViews: ", "Null view");
        }
        if(tabPosition != 0)
            tabRl.setVisibility(View.VISIBLE);
        Log.i("getViews: ", "Position = " + String.valueOf(tabPosition));
    }

    public static void checkSettings(View v){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean weight = mPrefs.getBoolean("pref_key_general_weight", true);
        String order = mPrefs.getString("pref_key_general_order", "0");
        if (weight) {
            v.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
        }
        else if (!weight) {
            v.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
        }
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }

    public static void autoCompleteAuth() {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("autoComplete", true)) {
            String[] array = helpList().toArray(new String[0]);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_dropdown_item_1line, array);
            etName.setThreshold(1);
            etName.setAdapter(adapter);
        }
    }

    public static ArrayList<String> helpList() {
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
                    }                }

            } while (cursor.moveToNext());
        }
        return list;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrags();
        viewPager.setAdapter(adapter);
    }

    public void hockeyAppSet(){
        LoginManager.register(this, "2fccbcc477da9ab6b058daf97571ac77", LoginManager
                .LOGIN_MODE_ANONYMOUS);
        LoginManager.verifyLogin(this, getIntent());
        CrashManager.register(this);
        UpdateManager.register(this, "a6f2b12acd1a4e22a763dab9a356879f", new UpdateManagerListener() {
            @Override
            public void onUpdateAvailable() {
                boolean vibrationsValue = android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .getBoolean("pref_key_sound_vibrate",
                                true);
                if (vibrationsValue) {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(400);
                }
                super.onUpdateAvailable();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if(tabPosition != 0){
                        tabLayout.getTabAt(0).select();
                        tabRl.setVisibility(View.INVISIBLE);
                    }
                    else {
                        if (event.getDownTime() - lastPressedTime < PERIOD) {
                            finish();
                        } else {
                            CoordinatorLayout cl = (CoordinatorLayout) findViewById(R.id.coordinator);
                            Snackbar.make(cl, getResources().getString(R.string.backToExit), Snackbar.LENGTH_SHORT).show();
                            lastPressedTime = event.getEventTime();
                        }
                    }
                    return true;
            }
        }
        return false;
    }

    private void firstRun() {
        SharedPreferences mPrefs = android.preference.PreferenceManager
                .getDefaultSharedPreferences(this);
        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        int currentVersionNumber = 0;

        int savedVersionNumber = mPrefs.getInt("version", 0);

        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersionNumber = pi.versionCode;
        } catch (Exception ignored) {}

        if (currentVersionNumber > savedVersionNumber) {
            showChangelog();

            SharedPreferences.Editor editor = mPrefs.edit();

            editor.putInt("version", currentVersionNumber);
            editor.apply();
        }

        //Settings
        if (!welcomeScreenShown) {
            //Settings
            mPrefs.edit().putBoolean(welcomeScreenShownPref,
                    true).apply();
            mPrefs.edit().putBoolean
                    ("pref_key_general_weight", true).apply();
            mPrefs.edit().putBoolean
                    ("pref_key_sound_vibrate", true).apply();
            mPrefs.edit().putString
                    ("pref_key_general_theme", "0").apply();
        }
    }

    public void showChangelog (){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.changelog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).setTitle(getResources().getString(R.string.welcome_title))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static void refreshViews (Context ctx){
        Cursor c;
        View v;

        FragmentPagerAdapter adapter = (FragmentPagerAdapter) viewPager.getAdapter();
        for (int i = 1; i < 15; i++) {
            c = mDbAdapterStatic.getAllEntries(i);
            SubjectTemplateFragment viewPagerFragment = (SubjectTemplateFragment) adapter.getItem(i);
            int page = viewPagerFragment.getArguments().getInt("page");
            if(page == i) {
                v = viewPagerFragment.getView();
                if (v != null) {
                    etMark = (EditText) v.findViewById(R.id.etMark);
                    lv = (ListView) v.findViewById(R.id.lvZnamky);
                    etName = (AutoCompleteTextView) v.findViewById(R.id.etName);
                    if (tabPosition == 0) {
                      tabRl.setVisibility(View.INVISIBLE);
                    } else {
                      tabRl.setVisibility(View.VISIBLE);
                    }
                    lv.setAdapter(new ListAdapter(man, c, 0));
                }
            }
            else Log.e("Void refreshViews: ", "Error, invalid page");
        }//end of for(;;)
        Log.d("Void refreshViews: ", String.valueOf(15) + " tabs loaded");
        average(ctx, tabPosition);
    }//end of refreshViews()

    public static void average(Context ctx, int posArg){
        Cursor cursor;
        cursor = mDbAdapterStatic.makeAverage(posArg);
        double sum = cursor.getDouble(cursor.getColumnIndex("average"));
        DecimalFormat formater = new DecimalFormat("0.00");
        String total = String.valueOf(formater.format(sum));
        tvAverage.setText(total);
        tvCount.setText(String.valueOf((mDbAdapterStatic.getAllEntries(posArg)).getCount()));
    }

    public static void removeMark(int posArg, Context ctx, long id) {
        mDbAdapterStatic.deleteMark(id, posArg);
        refreshViews(context);
    }

    public static void addOrUpdateMark(View v, int posArg, Context ctx, String name, String m,
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
                    String newDate = dateFormat.format(notFormatedDate);
                    mDbAdapterStatic.addMark(posArg, name, mark, weight, newDate);
                }
                else{
                    mDbAdapterStatic.addMark(posArg, name, mark, weight, date);
                }
                etName.setText("");
                etMark.setText("");
                etWeight.setText("");
                etName.requestFocus();
            } else {
                mDbAdapterStatic.updateMark(posArg, name, mark, weight, date, id[0]);
            }
            refreshViews(context);
        } catch (IllegalArgumentException e) {
            Log.e("AddMark Exception: ", e.getMessage());
            Snackbar.make(cl, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void showEditDialog(String name, String mark, String weight, long id){
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_time = dayOfMonth + "." + (monthOfYear + 1) + "." + year;

                                //TIME SET
                                final Calendar c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);

                                // Launch Time Picker Dialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(
                                        context, new TimePickerDialog.OnTimeSetListener() {
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
                MainActivity.addOrUpdateMark(ParseUtil.getViewByTab(), tabPosition, context, etNameDialog.getText()
                        .toString(), etMarkDialog.getText().toString(), etWeightDialog.getText()
                        .toString(), dateCompleted, id);
            }
        });
        adb.setNegativeButton(R.string.titleDelete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.removeMark(tabPosition, context, id);
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

    protected static View tabView(int index){
        View view;
        view = viewPager.getFocusedChild();
        return view;
    }

    private static void fillListView(int index){
        Cursor cursor = mDbAdapterStatic.getAllEntries(index);
        lv.setAdapter(new ListAdapter(man, cursor, 0));
    }

    private void setTabListeners (){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);
        context = this;
        man = MainActivity.this;
        firstRun();
        setContentView(R.layout.main_coordinator);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager, true);

        tabLayout.setupWithViewPager(viewPager);

        tabPosition = tabLayout.getSelectedTabPosition();

        cl = (CoordinatorLayout) findViewById(R.id.coordinator);

        tvAverage = (TextView) findViewById(R.id.tvAverage);

        tvCount = (TextView) findViewById(R.id.tvMarkCount);

        tabRl = (RelativeLayout) findViewById(R.id.relativeTabInfo);
        tabRl.setVisibility(View.INVISIBLE);

        mDbAdapterStatic = new Database(this);
        hockeyAppSet();
/*
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v;
                tabPosition = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    OverviewFragment.ovf.onAttachFragment(ParseUtil.getSubjectFrag());
                    tabRl.setVisibility(View.INVISIBLE);
                } else {
                    v = ParseUtil.getViewByTab();
                    tabRl.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        */

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tabPosition = tab.getPosition();
                if (tabPosition == 0) {
                    OverviewFragment.ovf.onAttachFragment(ParseUtil.getSubjectFrag());
                    tabRl.setVisibility(View.INVISIBLE);
                    Log.i("OnTabSelected: ", "Position = " + String.valueOf(tabPosition));
                } else {
                    tabRl.setVisibility(View.VISIBLE);
                    getViews(tabView(tabPosition));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });//end of onTabSelectedListener


        refreshViews(this);
    }//end of onCreate
}//end of MainActivity
