package cz.fely.weightedaverage.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.fely.weightedaverage.fragments.AboutFragment;
import cz.fely.weightedaverage.adapters.ListAdapter;
import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.adapters.SubjectsStatePagerAdapter;
import cz.fely.weightedaverage.db.Database;
import cz.fely.weightedaverage.fragments.OverviewFragment;
import cz.fely.weightedaverage.fragments.SubjectTemplateFragment;
import cz.fely.weightedaverage.utils.ParseUtil;
import cz.fely.weightedaverage.utils.ThemeUtil;

public class MainActivity extends AppCompatActivity{
    //GENERAL
    public static MainActivity man;
    public static Context context;
    private Toolbar toolbar;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static SubjectsStatePagerAdapter subjectsStatePagerAdapter;
    private static ListView lv;
    public static TextView tvAverage;
    public static TextView tvCount;
    public static CoordinatorLayout cl;
    public static RelativeLayout tabRl;
    //SUBJECTS
    public static EditText etMark, etWeight;
    public static AutoCompleteTextView etName;
    //DATABASE
    public static Database mDbAdapterStatic;
    //INTs
    public static int tabPosition;
    private static final int PERIOD = 2000;
    private long lastPressedTime;
    private int mHour, mMinute;
    int longClickTabPos;

    //BOOLEANs
    public boolean pagerAdapterInitialized = false;
    private ArrayList<Boolean> tabRefreshed = new ArrayList<>();

    //STRIINGs
    final String welcomeScreenShownPref = "welcomeScreenShown";
    private String dateCompleted;
    private final String TAB_POS = "TAB_POS";

    //******************
    /* END VARIABLES */
    //******************
    /* ------------------ */
    //******************
    //MAIN ACTIVITY
    //******************

    @Override
    public void onResume(){
        ThemeUtil.setTheme(this);
        initMainVariables();
        initBooleansList();
        if(pagerAdapterInitialized)
            subjectsStatePagerAdapter.changeTitles();
        setTabListeners();
        viewPager.invalidate();
        refreshViews(viewPager.getCurrentItem());
        super.onResume();
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
        if (id == R.id.action_deletemarks) {
            if(lv.getCount() == 0){
                CoordinatorLayout coordinatorLayout = findViewById(
                        R.id.coordinator);
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
                    tabRefreshed.set(tabPosition, false);
                    OverviewFragment.setAvgColor(tabPosition);
                    refreshViews(tabPosition);
                });
                adb.show();
            }
        }

        if (id == R.id.action_about){
            Intent i = new Intent(this, AboutFragment.class);
            startActivity(i);
        }

        if(id == R.id.action_help){
            int index = viewPager.getCurrentItem();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("help_main", false).apply();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("help_overview", false).apply();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("help_subject", false).apply();
            if (index == 0){
                new HelpDialogOverview().show(getSupportFragmentManager(), "help_overview");
            }
            else {
                new HelpDialogSubject().show(getSupportFragmentManager(), "help_subject");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_tab, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit_title:
                int pos = longClickTabPos;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.tab_title_change, null);
                EditText etTitle = dialogView.findViewById(R.id.tab_title_change_et);
                etTitle.setHint(ParseUtil.getTabNames(pos));
                dialogBuilder.setTitle(R.string.change_tab_title);
                dialogBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newTitle = etTitle.getText().toString();
                    mDbAdapterStatic.updateTabTitle(pos, newTitle);
                    subjectsStatePagerAdapter.changeTitles();
                    OverviewFragment.updateNameTexts(pos);
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.clean)
                            .setMessage(R.string.you_want_clean)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDbAdapterStatic.deleteSubject(pos);
                                    MainActivity.this.tabRefreshed.set(pos, false);
                                    MainActivity.this.refreshViews(pos);
                                }
                            })
                            .setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                   }
                });
                dialogBuilder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogBuilder.setView(dialogView);
                dialogBuilder.show();

                setTabListeners();
                break;
            case R.id.action_clean_subject:
                if(lv.getCount() == 0){
                    CoordinatorLayout coordinatorLayout = findViewById(
                            R.id.coordinator);
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
                        mDbAdapterStatic.deleteSubject(longClickTabPos);
                        tabRefreshed.set(longClickTabPos, false);
                        OverviewFragment.setAvgColor(longClickTabPos);
                        refreshViews(longClickTabPos);
                    });
                    adb.show();
                }

                setTabListeners();
                break;
        }
        return super.onContextItemSelected(item);
    }

    public static void checkSettings(View v){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean weight = mPrefs.getBoolean("pref_key_general_weight", true);
        if (weight) {
            v.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
        }
        else if (!weight) {
            v.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        subjectsStatePagerAdapter = new SubjectsStatePagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(subjectsStatePagerAdapter.getCount());
        viewPager.setAdapter(subjectsStatePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(TabSelectedListener);
        pagerAdapterInitialized = true;
    }
    TabLayout.OnTabSelectedListener TabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                viewPager.setCurrentItem(tabPosition, true);
                if (tabPosition == 0) {
                    View current = getCurrentFocus();
                    if (current != null)
                        current.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(current.getWindowToken(), 0);
                    tabRl.setVisibility(View.INVISIBLE);
                    showHelpIfPrefs("help_overview");

                } else {
                    refreshViews(tabPosition);
                    showHelpIfPrefs("help_subject");
                }
                invalidateOptionsMenu();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

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
                            CoordinatorLayout cl = findViewById(R.id.coordinator);
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

        mPrefs.edit().putBoolean("pref_db_changed_titles", true).apply();

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

            if(currentVersionNumber == 22){
                mPrefs.edit().putBoolean("help_overview", false).apply();
                mPrefs.edit().putBoolean("help_main", false).apply();
                mPrefs.edit().putBoolean("help_subject", false).apply();
            }
        }

        //Settings
        if (!welcomeScreenShown) {
            //Settings
            mPrefs.edit().putBoolean(welcomeScreenShownPref,
                    true).apply();
            mPrefs.edit().putBoolean
                    ("pref_key_general_weight", true).apply();
            mPrefs.edit().putString
                    ("pref_key_general_theme", "0").apply();

        }

        showHelpIfPrefs("help_main");
    }

    private void showHelp(boolean updatePrefs, String key){
        if(updatePrefs){
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(key, false).apply();
        }
        switch (key){
            case "help_overview":
                new HelpDialogOverview().show(getSupportFragmentManager(), key);
                break;
            case "help_subject":
                new HelpDialogSubject().show(getSupportFragmentManager(), key);
                break;
            case "help_main":
                new HelpDialogMain().show(getSupportFragmentManager(), key);
        }
    }

    private void showHelpIfPrefs(String key){
        boolean showed =
                PreferenceManager.getDefaultSharedPreferences(this).getBoolean(key, false);
        if(showed == false){
            showHelp(false, key);
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

    private void autoCompleteAuth(AutoCompleteTextView etName, Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("autoComplete", true)) {
            String[] array = helpList().toArray(new String[0]);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_dropdown_item_1line, array);
            etName.setThreshold(0);
            etName.setAdapter(adapter);
        }
    }
    ArrayList<String> helpList() {
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
                    }//end if(contains?)
                    else {
                        list.add(cursor.getString(i));
                    }//end else
                }//end for()
            } while (cursor.moveToNext());
        }//end if(boolean)
        return list;
    }

    @Deprecated
    public static void getViews(View v){
        if(v != null && v.findViewById(R.id.lvZnamky) != null) {
            lv = v.findViewById(R.id.lvZnamky);
            EditText etMark = v.findViewById(R.id.etMark);
            //       autoCompleteAuth();
            lv.setAdapter(new ListAdapter(man, mDbAdapterStatic.getAllEntries(tabPosition), 0));
        }//end if()
        else {
            Log.e("void getViews: ", "Null view");
        }//end else
        if(tabPosition != 0)
            tabRl.setVisibility(View.VISIBLE);
        Log.i("getViews: ", "Position = " + String.valueOf(tabPosition));
        //average(context, tabPosition);
    }

    public void changeColor(TextView tv, double avg) {
        double okMarkNum = SettingsActivity.getAvgFromPreference(SettingsActivity.prefOkMark);
        double okMarkNum2 = 0.49;
        double sum = okMarkNum + okMarkNum2;
        double ffMarkNum = SettingsActivity.getAvgFromPreference(SettingsActivity.prefFfMark);
        double ffMarkNum2 = 0.49;
        double sum2 = ffMarkNum + ffMarkNum2;
        int okMark, ffMark, badMark;
        okMark = ContextCompat.getColor(this, R.color.okMark);
        ffMark = ContextCompat.getColor(this, R.color.ffMark);
        badMark = ContextCompat.getColor(this, R.color.badMark);
        if (avg == 0) {
            tv.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        } else if (avg > 0 && avg < sum) {
            tv.setTextColor(okMark);
        } else if (avg >= sum && avg <= sum2) {
            tv.setTextColor(ffMark);
        } else {
            tv.setTextColor(badMark);
        }
    }

    private void infoTabUpdate(int pos){
        if(tabPosition != 0)
            tabRl.setVisibility(View.VISIBLE);

        Cursor cursor;
        cursor = mDbAdapterStatic.makeAverage(pos);
        double sum = cursor.getDouble(cursor.getColumnIndex("average"));
        DecimalFormat formatter = new DecimalFormat("0.00");
        String total = String.valueOf(formatter.format(sum));
        if (lv.getCount() == 0) {
            tvAverage.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
            OverviewFragment.setAvgColor(pos);
            tvAverage.setText(total);
            tvCount.setText(String.valueOf(lv.getCount()));
        }
        else {
            changeColor(tvAverage, sum);
            tvAverage.setText(total);
            tvCount.setText(String.valueOf(lv.getCount()));
        }
    }

    public void refreshViews(int pos){
        if (pos != 0) {
            boolean refreshed = tabRefreshed.get(pos);
            SubjectTemplateFragment fragment = (SubjectTemplateFragment)
                    subjectsStatePagerAdapter.getRegisteredFragment(pos);
            if(fragment == null) {
                tabRefreshed.set(pos, false);
            }
            else {
                View v = fragment.getView();
                if (v != null) {
                    Cursor c = mDbAdapterStatic.getAllEntries(pos);
                    lv = v.findViewById(R.id.lvZnamky);
                    etName = v.findViewById(R.id.etName);
                    etMark = v.findViewById(R.id.etMark);
                    etWeight = v.findViewById(R.id.etWeight);
                    etName.requestFocus();
                    autoCompleteAuth(etName, context);
                    checkSettings(v);

                    if (!refreshed) {
                        lv.setAdapter(new ListAdapter(man, c, 0));
                        tabRefreshed.set(pos, true);
                        OverviewFragment.updateAverageTexts(pos);
                    }
                }
                infoTabUpdate(pos);
            }
        }
    }

    private void initViewActions(){

    }

    protected static boolean dbTableExist(String table){
        SQLiteDatabase mDatabase = context.openOrCreateDatabase("AppDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Cursor c = null;
        boolean tableExists = false;

        try {
            c = mDatabase.query(table, null,
                    null, null, null, null, null);
            tableExists = true;
        }//end try
        catch (Exception e) {
            Log.e("Check Db exist: ", e.getMessage());
        }//end catch

        return tableExists;
    }

    private void setTabListeners() {
        LinearLayout tabStrip = (LinearLayout) tabLayout.getChildAt(0);
        for (int i = 1; i < tabStrip.getChildCount(); i++) {
            int position = i;
            tabStrip.getChildAt(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    registerForContextMenu(v);
                    longClickTabPos = position;
                    openContextMenu(v);
                    return true;
                }//end onLongClick(View)
            });//end setOnLongClickListener()
        }//end for(;;)
    }

    public static void newTabListTable(Context ctx){
        SQLiteDatabase mDatabase = ctx.openOrCreateDatabase("AppDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        mDatabase.execSQL(Database.SQL_TABLE2_CREATE);
        String[] titles = new String[]{ctx.getResources().getString(R.string.overview), ctx.getResources().getString(R.string.tab1), ctx.getResources().getString(R.string.tab2),
                ctx.getResources().getString(R.string.tab3), ctx.getResources().getString(R.string.tab4), ctx.getResources().getString(R.string.tab5),
                ctx.getResources().getString(R.string.tab6), ctx.getResources().getString(R.string.tab7), ctx.getResources().getString(R.string.tab8),
                ctx.getResources().getString(R.string.tab9), ctx.getResources().getString(R.string.tab10), ctx.getResources().getString(R.string.tab11),
                ctx.getResources().getString(R.string.tab12), ctx.getResources().getString(R.string.tab13), ctx.getResources().getString(R.string.tab14)};
        mDbAdapterStatic = new Database(ctx);
        mDbAdapterStatic.newTabListTable(titles);
    }

    private void initBooleansList(){
        this.tabRefreshed.clear();
        this.tabRefreshed.add(0, null);
        for(int i = 1; i <= 14; i++){
            this.tabRefreshed.add(i, false);
        }//end for(;;)
    }

    public void removeMark(int posArg, long id) {
        mDbAdapterStatic.deleteMark(id, posArg);
        this.tabRefreshed.set(posArg, false);
        man.refreshViews(posArg);
    }

    public void showEditDialog(View fragView, String name, String mark, String weight, long id){
        LayoutInflater inflater = LayoutInflater.from(man);
        View v = inflater.inflate(R.layout.edit_dialog, null);
        EditText etNameDialog, etMarkDialog,etWeightDialog;
        TextView vDateDialog;
        etNameDialog = v.findViewById(R.id.etNameDialog);
        etMarkDialog = v.findViewById(R.id.etMarkDialog);
        etWeightDialog = v.findViewById(R.id.etWeightDialog);
       // vDateDialog = v.findViewById(R.id.viewDateDialog);  //TODO: DATE CHANGE DIALOG
        DatePicker dp = new DatePicker(context);
        etNameDialog.setText(name);
        etMarkDialog.setText(mark);
        etWeightDialog.setText(weight);
        /*Cursor c = mDbAdapterStatic.getDate(tabPosition, id);
        //String date = c.getString(c.getColumnIndex("day"));
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
        });*/
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(R.string.editMark);
        adb.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addOrUpdateMark(fragView, tabLayout.getSelectedTabPosition(), context, etNameDialog.getText()
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

    public void addOrUpdateMark(View v, int posArg, Context ctx, String name, String m,
                                String w, @Nullable String date, long... id) {
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
                etName.requestFocus();
                etName.setText("");
                etMark.setText("");
                etWeight.setText("");
            }else
                mDbAdapterStatic.updateMark(posArg, name, mark, weight, date, id[0]);
            this.tabRefreshed.set(posArg, false);
            man.refreshViews(posArg);
        } catch (IllegalArgumentException e) {
            Snackbar.make(cl, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void showToast(String text, boolean isLong){
        if (isLong)
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        else
            showToast(text);
    }
    public static void showToast(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    private void initMainVariables(){
        viewPager = findViewById(R.id.viewpager);
        tvAverage = findViewById(R.id.tvAverage);
        tvCount = findViewById(R.id.tvMarkCount);
        tabLayout = findViewById(R.id.tabs);
        tabRl = findViewById(R.id.relativeTabInfo);
        cl = findViewById(R.id.coordinator);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);
        context = this;
        man = MainActivity.this;
        setContentView(R.layout.main_coordinator);

        initMainVariables();
        initBooleansList();
        setSupportActionBar(toolbar);
        if(MainActivity.dbTableExist(Database.TABLE2_NAME) == false){
            MainActivity.newTabListTable(this);
        }
        else
            mDbAdapterStatic = new Database(this);
        firstRun();
        setupViewPager(viewPager);
        tabPosition = 0;
        tabRl.setVisibility(View.INVISIBLE);
        setTabListeners();
    }
}