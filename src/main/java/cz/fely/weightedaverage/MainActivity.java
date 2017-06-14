package cz.fely.weightedaverage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hockeyapp.android.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.fely.weightedaverage.db.Database;
import cz.fely.weightedaverage.subjects.*;
import cz.fely.weightedaverage.utils.*;
import cz.fely.weightedaverage.utils.ExpandCollapseUtil.*;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        man = MainActivity.this;
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);
        firstRun();
        setContentView(R.layout.main_coordinator);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabPosition = tabLayout.getSelectedTabPosition();

        cl = (CoordinatorLayout) findViewById(R.id.coordinator);
        tvAverage = (TextView) findViewById(R.id.tvAverage);
        tvCount = (TextView) findViewById(R.id.tvMarkCount);
        tabRl = (RelativeLayout) findViewById(R.id.relativeTabInfo);

        mDbAdapterStatic = new Database(this);
        hockeyAppSet();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabPosition = position;
                View v;
                if(tabPosition != 0){
                        v = ParseUtil.getViewByTab();
                        getViews(v);
                        updateView(position, getApplicationContext());
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = viewPager.getCurrentItem();
                View v;
                if (tabPosition == 0){
                   OverviewFragment.ovf.onAttachFragment(ParseUtil.getSubjectFrag());
                }
                if (tabPosition != 0){
                    v = ParseUtil.getViewByTab();
                    getViews(v);
                }
                if(tabPosition != 0) {
                    updateView(tabPosition, getApplicationContext());
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ParseUtil.getTabNames(this);
        adapter.addFrag(new OverviewFragment(), ParseUtil.overview);
        adapter.addFrag(new SubjectOneFragment(), ParseUtil.one);
        adapter.addFrag(new SubjectTwoFragment(), ParseUtil.two);
        adapter.addFrag(new SubjectThreeFragment(), ParseUtil.three);
        adapter.addFrag(new SubjectFourFragment(), ParseUtil.four);
        adapter.addFrag(new SubjectFiveFragment(), ParseUtil.five);
        adapter.addFrag(new SubjectSixFragment(), ParseUtil.six);
        adapter.addFrag(new SubjectSevenFragment(), ParseUtil.seven);
        adapter.addFrag(new SubjectEightFragment(), ParseUtil.eight);
        adapter.addFrag(new SubjectNineFragment(), ParseUtil.nine);
        adapter.addFrag(new SubjectTenFragment(), ParseUtil.ten);
        adapter.addFrag(new SubjectElevenFragment(), ParseUtil.eleven);
        adapter.addFrag(new SubjectTwelveFragment(), ParseUtil.twelve);
        adapter.addFrag(new SubjectTtFragment(), ParseUtil.tt);
        adapter.addFrag(new SubjectFtFragment(), ParseUtil.ft);
        viewPager.setAdapter(adapter);
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
            if (tabPosition == 0) {
                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                        .coordinator);
                Snackbar.make(coordinatorLayout, R.string.errorSnackCannotDelete,
                        Snackbar.LENGTH_LONG)
                        .show();
            }
            else if(lv.getCount() == 0){
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
                adb.setPositiveButton(R.string.yes, (dialog, which) -> {
                    mDbAdapterStatic.deleteSubject(tabPosition);
                    View v;
                    v = ParseUtil.getViewByTab();
                    getViews(v);
                    updateView(tabPosition, getApplicationContext());
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

    public static void getViews(View v){
        if(v != null) {
            lv = (ListView) v.findViewById(R.id.lvZnamky);
            etName = (AutoCompleteTextView) v.findViewById(R.id.etName);
            EditText etMark = (EditText) v.findViewById(R.id.etMark);
            if(tabPosition != 0) {
                tabRl.setVisibility(View.VISIBLE);
                etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        boolean isNext = false;
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            etMark.requestFocus();
                            isNext = true;
                        }
                        return isNext;
                    }
                });
            }
        }
    }

    public static void autoCompleteAuth(){
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("autoComplete", true)){
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

    public static void updateView(int positionArg, Context ctx){
        Cursor cursor;
        getViews(ParseUtil.getViewByTab());
        cursor = mDbAdapterStatic.getAllEntries(positionArg);
        lv.setAdapter(new ListAdapter(man, cursor, 0));
        average(ctx, positionArg);
    }

    public static void average(Context ctx, int posArg){
        Cursor cursor;
        cursor = mDbAdapterStatic.makeAverage(posArg);
        double sum = cursor.getDouble(cursor.getColumnIndex("average"));
        DecimalFormat formater = new DecimalFormat("0.00");
        String total = String.valueOf(formater.format(sum));
        tvAverage.setText(total);
        tvCount.setText(String.valueOf((mDbAdapterStatic.getAllEntries(posArg)).getCount()));
    }

    public static void addOrUpdateMark(View v, int posArg, Context ctx, String name, String m,
                                       String w, long... id) {
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                Date notFormatedDate = new Date();
                String date = dateFormat.format(notFormatedDate);
                mDbAdapterStatic.addMark(posArg, name, mark, weight, date);
                etName.setText("");
                etMark.setText("");
                etWeight.setText("");
                etName.requestFocus();
            } else {
                mDbAdapterStatic.updateMark(posArg, name, mark, weight, id[0]);
            }
            updateView(posArg, context);
        } catch (IllegalArgumentException e) {
            Snackbar.make(cl, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void removeMark(int posArg, Context ctx, long id) {
        mDbAdapterStatic.deleteMark(id, posArg);
        updateView(posArg, context);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public void changeTitles(){
            ParseUtil.getTabNames(getApplicationContext());
            tabLayout.getTabAt(1).setText(ParseUtil.one);
            tabLayout.getTabAt(2).setText(ParseUtil.two);
            tabLayout.getTabAt(3).setText(ParseUtil.three);
            tabLayout.getTabAt(4).setText(ParseUtil.four);
            tabLayout.getTabAt(5).setText(ParseUtil.five);
            tabLayout.getTabAt(6).setText(ParseUtil.six);
            tabLayout.getTabAt(7).setText(ParseUtil.seven);
            tabLayout.getTabAt(8).setText(ParseUtil.eight);
            tabLayout.getTabAt(9).setText(ParseUtil.nine);
            tabLayout.getTabAt(10).setText(ParseUtil.ten);
            tabLayout.getTabAt(11).setText(ParseUtil.eleven);
            tabLayout.getTabAt(12).setText(ParseUtil.twelve);
            tabLayout.getTabAt(13).setText(ParseUtil.tt);
            tabLayout.getTabAt(14).setText(ParseUtil.ft);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onResume(){
        ThemeUtil.setTheme(this);
        ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
        adapter.changeTitles();
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
    private void unregisterManagers() {
        UpdateManager.unregister();
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
                //TODO
            }
        });
        builder.create().show();
    }

    public static void showEditDialog(String name, String mark, String weight,long id){
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
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(R.string.editMark);
        adb.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.addOrUpdateMark(ParseUtil.getViewByTab(), tabPosition, context, etNameDialog.getText()
                        .toString(), etMarkDialog.getText().toString(), etWeightDialog.getText()
                        .toString(), id);
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



/*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleUtils.updateConfig(getApplication(), newConfig);
    }
    */
}