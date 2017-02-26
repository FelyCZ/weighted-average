package cz.fely.weightedaverage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.FeedbackManager;
import net.hockeyapp.android.LoginManager;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.UpdateManagerListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.fely.weightedaverage.db.DatabaseAdapter;
import cz.fely.weightedaverage.subjects.OverviewFragment;
import cz.fely.weightedaverage.subjects.SubjectEightFragment;
import cz.fely.weightedaverage.subjects.SubjectFiveFragment;
import cz.fely.weightedaverage.subjects.SubjectFourFragment;
import cz.fely.weightedaverage.subjects.SubjectNineFragment;
import cz.fely.weightedaverage.subjects.SubjectOneFragment;
import cz.fely.weightedaverage.subjects.SubjectSevenFragment;
import cz.fely.weightedaverage.subjects.SubjectSixFragment;
import cz.fely.weightedaverage.subjects.SubjectTenFragment;
import cz.fely.weightedaverage.subjects.SubjectThreeFragment;
import cz.fely.weightedaverage.subjects.SubjectTwoFragment;
import cz.fely.weightedaverage.utils.PreferencesUtil;
import cz.fely.weightedaverage.utils.ThemeUtil;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private static TabLayout tabLayout;
    private ViewPager viewPager;
    int tabPosition;
    public static DatabaseAdapter mDbAdapterStatic;
    private static MainActivity man;
    static ListView lv;
    static TextView tvAverage;
    private static final int PERIOD = 2000;
    private long lastPressedTime;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_coordinator);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabPosition = tabLayout.getSelectedTabPosition();

        mDbAdapterStatic = new DatabaseAdapter(this);
        man = MainActivity.this;
        context = this;

        if (tabPosition == 0) {
            MenuItem deleteAll = (MenuItem) findViewById(R.id.action_deletemarks);
            deleteAll.setVisible(false);
        }
        firstRun();
        hockeyAppSet();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabPosition = tabLayout.getSelectedTabPosition();
                View v;
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        v = SubjectOneFragment.view;
                        getViews(v);
                        break;
                    case 2:
                        v = SubjectTwoFragment.view;
                        getViews(v);
                        break;
                    case 3:
                        v = SubjectThreeFragment.view;
                        getViews(v);
                        break;
                    case 4:
                        v = SubjectFourFragment.view;
                        getViews(v);
                        break;
                    case 5:
                        v = SubjectFiveFragment.view;
                        getViews(v);
                        break;
                    case 6:
                        v = SubjectSixFragment.view;
                        getViews(v);
                        break;
                    case 7:
                        v = SubjectSevenFragment.view;
                        getViews(v);
                        break;
                    case 8:
                        v = SubjectEightFragment.view;
                        getViews(v);
                        break;
                    case 9:
                        v = SubjectNineFragment.view;
                        getViews(v);
                        break;
                    case 10:
                        v = SubjectTenFragment.view;
                        getViews(v);
                        break;
                }
                updateView(position, getApplicationContext());
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                View v;
                if (tabPosition == 0) {
                } else if (tabPosition == 1) {
                    v = SubjectOneFragment.view;
                    getViews(v);

                } else if (tabPosition == 2) {
                    v = SubjectTwoFragment.view;
                    getViews(v);

                } else if (tabPosition == 3) {
                    v = SubjectThreeFragment.view;
                    getViews(v);

                } else if (tabPosition == 4) {
                    v = SubjectFourFragment.view;
                    getViews(v);

                } else if (tabPosition == 5) {
                    v = SubjectFiveFragment.view;
                    getViews(v);

                } else if (tabPosition == 6) {
                    v = SubjectSixFragment.view;
                    getViews(v);

                } else if (tabPosition == 7) {
                    v = SubjectSevenFragment.view;
                    getViews(v);

                } else if (tabPosition == 8) {
                    v = SubjectEightFragment.view;
                    getViews(v);

                } else if (tabPosition == 9) {
                    v = SubjectNineFragment.view;
                    getViews(v);

                } else if (tabPosition == 10) {
                    v = SubjectTenFragment.view;
                    getViews(v);
                }
                updateView(tabPosition, getApplicationContext());
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
        PreferencesUtil.getTabNames(this);
        adapter.addFrag(new OverviewFragment(), PreferencesUtil.overview);
        adapter.addFrag(new SubjectOneFragment(), PreferencesUtil.one);
        adapter.addFrag(new SubjectTwoFragment(), PreferencesUtil.two);
        adapter.addFrag(new SubjectThreeFragment(), PreferencesUtil.three);
        adapter.addFrag(new SubjectFourFragment(), PreferencesUtil.four);
        adapter.addFrag(new SubjectFiveFragment(), PreferencesUtil.five);
        adapter.addFrag(new SubjectSixFragment(), PreferencesUtil.six);
        adapter.addFrag(new SubjectSevenFragment(), PreferencesUtil.seven);
        adapter.addFrag(new SubjectEightFragment(), PreferencesUtil.eight);
        adapter.addFrag(new SubjectNineFragment(), PreferencesUtil.nine);
        adapter.addFrag(new SubjectTenFragment(), PreferencesUtil.ten);
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
            AlertDialog.Builder adb;
            adb = new AlertDialog.Builder(this);
            adb.setTitle(R.string.titleDeleteAll);
            adb.setIcon(R.drawable.warning);
            adb.setMessage(R.string.areYouSure);
            adb.setNegativeButton(R.string.cancel, null);
            adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                {
                    if (tabPosition == 1) {
                        mDbAdapterStatic.deleteAll();
                        updateView(0, getApplicationContext());

                    } else if (tabPosition == 2) {
                        mDbAdapterStatic.deleteAll2();
                        updateView(1, getApplicationContext());

                    } else if (tabPosition == 3) {
                        mDbAdapterStatic.deleteAll3();
                        updateView(2, getApplicationContext());

                    } else if (tabPosition == 4) {
                        mDbAdapterStatic.deleteAll4();
                        updateView(3, getApplicationContext());

                    } else if (tabPosition == 5) {
                        mDbAdapterStatic.deleteAll5();
                        updateView(4, getApplicationContext());

                    } else if (tabPosition == 6) {
                        mDbAdapterStatic.deleteAll6();
                        updateView(5, getApplicationContext());

                    } else if (tabPosition == 7) {
                        mDbAdapterStatic.deleteAll7();
                        updateView(6, getApplicationContext());

                    } else if (tabPosition == 8) {
                        mDbAdapterStatic.deleteAll8();
                        updateView(7, getApplicationContext());

                    } else if (tabPosition == 9) {
                        mDbAdapterStatic.deleteAll9();
                        updateView(8, getApplicationContext());

                    } else if (tabPosition == 10) {
                        mDbAdapterStatic.deleteAll10();
                        updateView(9, getApplicationContext());
                    }
                    if(tabPosition != 0){
                        MenuItem deleteAll = (MenuItem) findViewById(R.id.action_deletemarks);
                        deleteAll.setVisible(true);
                    }
                }
            });
            adb.show();
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
        if (weight) {
            v.findViewById(R.id.etWeight).setVisibility(View.VISIBLE);
        }
        else if (!weight) {
            v.findViewById(R.id.etWeight).setVisibility(View.INVISIBLE);
        }
    }

    public static void getViews (View v){
        if(v != null) {
            lv = (ListView) v.findViewById(R.id.lvZnamky);
            tvAverage = (TextView) v.findViewById(R.id.averageTV);
        }
    }

    public static void updateView(int positionArg, Context ctx){
        DatabaseAdapter mDbAdapter = mDbAdapterStatic;
        Cursor cursor = null;

        if(positionArg == 0){
            cursor = mDbAdapter.getAllEntries();
        }
        else if (positionArg == 1){
            cursor = mDbAdapter.getAllEntries2();
        }
        else if (positionArg == 2){
            cursor = mDbAdapter.getAllEntries3();
        }
        else if(positionArg == 3){
            cursor = mDbAdapter.getAllEntries4();
        }
        else if(positionArg == 4){
            cursor = mDbAdapter.getAllEntries5();
        }
        else if(positionArg == 5){
            cursor = mDbAdapter.getAllEntries6();
        }
        else if (positionArg == 6){
            cursor = mDbAdapter.getAllEntries7();
        }
        else if(positionArg == 7){
            cursor = mDbAdapter.getAllEntries8();
        }
        else if(positionArg == 8){
            cursor = mDbAdapter.getAllEntries9();
        }
        else if(positionArg == 9){
            cursor = mDbAdapter.getAllEntries10();
        }
        lv.setAdapter(new ListAdapter(man, cursor, 0));
        average(ctx, positionArg);
        OverviewFragment.updateOverView(context);
    }

    public static void average(Context ctx, int posArg){
        SharedPreferences.Editor mPrefsEdit = PreferenceManager.getDefaultSharedPreferences(ctx)
                .edit();
        Cursor cursor = null;
        if(posArg == 0){
            cursor = mDbAdapterStatic.averageFromMarks();
        }
        else if(posArg == 1){
            cursor = mDbAdapterStatic.averageFromMarks2();
        }
        else if (posArg == 2){
            cursor = mDbAdapterStatic.averageFromMarks3();
        }
        else if(posArg == 3){
            cursor = mDbAdapterStatic.averageFromMarks4();
        }
        else if(posArg == 4){
            cursor = mDbAdapterStatic.averageFromMarks5();
        }
        else if(posArg == 5){
            cursor = mDbAdapterStatic.averageFromMarks6();
        }
        else if (posArg == 6){
            cursor = mDbAdapterStatic.averageFromMarks7();
        }
        else if(posArg == 7){
            cursor = mDbAdapterStatic.averageFromMarks8();
        }
        else if(posArg == 8){
            cursor = mDbAdapterStatic.averageFromMarks9();
        }
        else if(posArg == 9){
            cursor = mDbAdapterStatic.averageFromMarks10();
        }
        double sum = cursor.getDouble(cursor.getColumnIndex("average"));
        DecimalFormat formater = new DecimalFormat("0.00");
        String total = String.valueOf(formater.format(sum));
        tvAverage.setText(ctx.getResources().getString(R.string.prumer)+" "+total);
    }

    public static void addOrUpdateMark(View v, int posArg, Context ctx, String name, String m,
                                       String w,
                                        long... id) {
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
                if(mark > 5 || weight == 0){
                    throw new IllegalArgumentException(ctx.getResources().getString(R.string
                            .invalidMarkWeight));
                }
            }
            if (id.length == 0) {
                if(posArg == 0){
                    mDbAdapterStatic.addMark(name, mark, weight);
                }
                else if(posArg == 1){
                    mDbAdapterStatic.addMark2(name, mark, weight);
                }
                else if(posArg == 2){
                    mDbAdapterStatic.addMark3(name, mark, weight);
                }
                else if(posArg == 3){
                    mDbAdapterStatic.addMark4(name, mark, weight);
                }
                else if(posArg == 4){
                    mDbAdapterStatic.addMark5(name, mark, weight);
                }
                else if(posArg == 5){
                    mDbAdapterStatic.addMark6(name, mark, weight);
                }
                else if (posArg == 6){
                    mDbAdapterStatic.addMark7(name, mark, weight);
                }
                else if(posArg == 7){
                    mDbAdapterStatic.addMark8(name, mark, weight);
                }
                else if(posArg == 8){
                    mDbAdapterStatic.addMark9(name, mark, weight);
                }
                else if(posArg == 9){
                    mDbAdapterStatic.addMark10(name, mark, weight);
                }
                etName.setText("");
                etMark.setText("");
                etWeight.setText("");
                etName.requestFocus();
            } else {
                if(posArg == 0){
                    mDbAdapterStatic.updateMark(name, mark, weight, id[0]);
                }
                else if(posArg == 1){
                    mDbAdapterStatic.updateMark2(name, mark, weight, id[0]);
                }
                else if(posArg == 2){
                    mDbAdapterStatic.updateMark3(name, mark, weight, id[0]);
                }
                else if(posArg == 3){
                    mDbAdapterStatic.updateMark4(name, mark, weight, id[0]);
                }
                else if(posArg == 4){
                    mDbAdapterStatic.updateMark5(name, mark, weight, id[0]);
                }
                else if(posArg == 5){
                    mDbAdapterStatic.updateMark6(name, mark, weight, id[0]);
                }
                else if (posArg == 6){
                    mDbAdapterStatic.updateMark7(name, mark, weight, id[0]);
                }
                else if(posArg == 7){
                    mDbAdapterStatic.updateMark8(name, mark, weight, id[0]);
                }
                else if(posArg == 8){
                    mDbAdapterStatic.updateMark9(name, mark, weight, id[0]);
                }
                else if(posArg == 9){
                    mDbAdapterStatic.updateMark10(name, mark, weight, id[0]);
                }
            }
            if(posArg == 0){
                updateView(0, ctx);
            }
            else if(posArg == 1){
                updateView(1, ctx);
            }
            else if(posArg == 2){
                updateView(2, ctx);
            }
            else if(posArg == 3){
                updateView(3, ctx);
            }
            else if(posArg == 4){
                updateView(4, ctx);
            }
            else if(posArg == 5){
                updateView(5, ctx);
            }
            else if (posArg == 6){
                updateView(6, ctx);
            }
            else if(posArg == 7){
                updateView(7, ctx);
            }
            else if(posArg == 8){
                updateView(8, ctx);
            }
            else if(posArg == 9){
                updateView(9, ctx);
            }
        } catch (IllegalArgumentException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setMessage(String.valueOf(e));
            builder.setTitle(R.string.adbError);
            builder.setNeutralButton(android.R.string.ok, null);
            builder.show();
        }
    }

    public static void removeMark(int posArg, Context ctx, long id) {
        if(posArg == 0){
            mDbAdapterStatic.deleteMark(id);
            updateView(0, ctx);
        }
        else if(posArg == 1){
            mDbAdapterStatic.deleteMark2(id);
            updateView(1, ctx);
        }
        else if(posArg == 2){
            mDbAdapterStatic.deleteMark3(id);
            updateView(2, ctx);
        }
        else if(posArg == 3){
            mDbAdapterStatic.deleteMark4(id);
            updateView(3, ctx);
        }
        else if(posArg == 4){
            mDbAdapterStatic.deleteMark5(id);
            updateView(4, ctx);
        }
        else if(posArg == 5){
            mDbAdapterStatic.deleteMark6(id);
            updateView(5, ctx);
        }
        else if (posArg == 6){
            mDbAdapterStatic.deleteMark7(id);
            updateView(6, ctx);
        }
        else if(posArg == 7){
            mDbAdapterStatic.deleteMark8(id);
            updateView(7, ctx);
        }
        else if(posArg == 8){
            mDbAdapterStatic.deleteMark9(id);
            updateView(8, ctx);
        }
        else if(posArg == 9){
            mDbAdapterStatic.deleteMark10(id);
            updateView(9, ctx);
        }
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

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onResume(){
        ThemeUtil.reloadTheme(this);
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
                UpdateManager.register(MainActivity.this);
                super.onUpdateAvailable();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.backToExit,
                                Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
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
                });
        builder.create().show();
    }
/*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleUtils.updateConfig(getApplication(), newConfig);
    }
    */
}