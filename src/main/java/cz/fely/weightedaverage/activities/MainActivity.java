package cz.fely.weightedaverage.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.FeedbackManager;
import net.hockeyapp.android.LoginManager;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.UpdateManagerListener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
    private Toolbar toolbar;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int tabPosition;
    public static Database mDbAdapterStatic;
    public static MainActivity man;
    static ListView lv;
    public static TextView tvAverage;
    public static TextView tvCount;
    private static final int PERIOD = 2000;
    private long lastPressedTime;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    public static Context context;
    public static CoordinatorLayout cl;
    public static GestureDetector gestureDetector;
    public static RelativeLayout rl;
    public static GestureDetectorCompat detector;
    public static RelativeLayout tabRl;
    public static EditText etMark;
    public static SubjectsStatePagerAdapter subjectsStatePagerAdapter;
    private PowerManager.WakeLock wakeLock;
    int longClickTabPos;
    public boolean pagerAdapterInitialized = false;
    private ArrayList<Boolean> tabRefreshed = new ArrayList<>();

    //MAIN ACTIVITY

    @Override
    public void onResume(){
        ThemeUtil.setTheme(this);
        if(pagerAdapterInitialized)
            subjectsStatePagerAdapter.changeTitles();
        tabPosition = tabLayout.getSelectedTabPosition();
        tabRefreshed.set(tabPosition, false);
        refreshViews(tabPosition);
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
                    tabRefreshed.set(tabPosition, false);
                    refreshViews(tabPosition);
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
                EditText etTitle = (EditText) dialogView.findViewById(R.id.tab_title_change_et);
                etTitle.setHint(ParseUtil.getTabNames(pos));
                dialogBuilder.setTitle(R.string.change_tab_title);
                dialogBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                      String newTitle = etTitle.getText().toString();
                       mDbAdapterStatic.updateTabTitle(pos, newTitle);
                       subjectsStatePagerAdapter.changeTitles();
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
                break;
            case R.id.action_clean_subject:
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
                        tabRefreshed.set(tabPosition, false);
                        refreshViews(tabPosition);
                    });
                    adb.show();
                }
                break;
        }
        return super.onContextItemSelected(item);
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

    public void setupViewPager(ViewPager viewPager) {
        subjectsStatePagerAdapter = new SubjectsStatePagerAdapter(this, getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(subjectsStatePagerAdapter.getCount());
        viewPager.setAdapter(subjectsStatePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(TabSelectedListener);
    }
    TabLayout.OnTabSelectedListener TabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                viewPager.setCurrentItem(tabPosition, true);
                if (tabPosition == 0) {
                    OverviewFragment.setNames(context);
                    tabRl.setVisibility(View.INVISIBLE);
                    Log.d("OnTabSelected: ", "Position = " + String.valueOf(tabPosition));
                } else {
                    refreshViews(tabPosition);
                }
                invalidateOptionsMenu();

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    }; //end of onTabSelectedListener


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

        mPrefs.edit().putBoolean("pref_db_changed_titles", true).apply();

        int currentVersionNumber = 0;

        int savedVersionNumber = mPrefs.getInt("version", 0);

        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersionNumber = pi.versionCode;
        } catch (Exception ignored) {}

        if (currentVersionNumber > savedVersionNumber) {
            if(currentVersionNumber == 16){
                mDbAdapterStatic.reChangeTabInt();
                mPrefs.edit().putBoolean("pref_db_changed_titles", false).apply();
            }
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
    }//end helpList()

 /*   public void refreshViews(){
        Cursor c;
        for(int i = 1; i <= 14; i++){
            viewPager.setCurrentItem(i);
            c = mDbAdapterStatic.getAllEntries(i);
            View v = viewPager.getFocusedChild();
            if (v != null) {
                EditText etMark, etWeight;
                AutoCompleteTextView etName;
                ListView lv;
                if (v != null) {
                    lv = (ListView) v.findViewById(R.id.lvZnamky);
                    etName = (AutoCompleteTextView) v.findViewById(R.id.etName);
                    etMark = (EditText) v.findViewById(R.id.etMark);
                    autoCompleteAuth(etName, context);
                    lv.setAdapter(new ListAdapter(man, c, 0));
                }
                average(context, i);
            }
        }
        viewPager.setCurrentItem(0);
    }//end of refreshViews()
*/

    @Deprecated
    public static void getViews(View v){
        if(v != null && v.findViewById(R.id.lvZnamky) != null) {
            lv = (ListView) v.findViewById(R.id.lvZnamky);
            EditText etMark = (EditText) v.findViewById(R.id.etMark);
            //       autoCompleteAuth();
            lv.setAdapter(new ListAdapter(man, mDbAdapterStatic.getAllEntries(tabPosition), 0));
        }//end if()
        else {
            Log.e("void getViews: ", "Null view");
        }//end else
        if(tabPosition != 0)
            tabRl.setVisibility(View.VISIBLE);
        Log.i("getViews: ", "Position = " + String.valueOf(tabPosition));
        average(context, tabPosition);
    }//end of @Deprecated getViews(View)

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
            tv.setTextColor(tv.getTextColors().getDefaultColor());
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
        DecimalFormat formater = new DecimalFormat("0.00");
        String total = String.valueOf(formater.format(sum));
        if(cursor.getCount() == 0)
            tvAverage.setTextColor(tvAverage.getTextColors().getDefaultColor());
        else
            changeColor(tvAverage, sum);
        tvAverage.setText(total);
        tvCount.setText(String.valueOf((mDbAdapterStatic.getAllEntries(pos)).getCount()));
    }

    public void refreshViews(int pos){
        boolean refreshed = tabRefreshed.get(pos);
            if (tabPosition != 0) {
                SubjectTemplateFragment fragment = (SubjectTemplateFragment)
                        subjectsStatePagerAdapter.getRegisteredFragment(pos);
                View v = fragment.getView();
                if (v != null) {
                    EditText etMark, etWeight;
                    AutoCompleteTextView etName;
                    Cursor c = mDbAdapterStatic.getAllEntries(pos);
                    lv = (ListView) v.findViewById(R.id.lvZnamky);
                    etName = (AutoCompleteTextView) v.findViewById(R.id.etName);
                    etMark = (EditText) v.findViewById(R.id.etMark);
                    autoCompleteAuth(etName, context);
                    if(!refreshed) {
                        lv.setAdapter(new ListAdapter(man, c, 0));
                        tabRefreshed.set(pos, true);
                    }
                }//end if(boolean)
            }//end if(tabPostition != 0)
        infoTabUpdate(pos);
    }//end of refreshViews(int)


    private static void average(Context ctx, int posArg){

    }//end of average(Context, int)

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
    }//end of dbTableExist(String)

    private void renameDb(){
        File database = getApplicationContext().getDatabasePath("MarksV2.db");
        if (database.exists()) {
            File newPath = getApplicationContext().getDatabasePath("AppDB.db");
            database.renameTo(newPath);
        }//end if()
    }//end of renameDb()

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
    }//end of setTabListeners()

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
    }//end of newTabListTable

    private void initBooleansList(){
        this.tabRefreshed.add(0, null);
        for(int i = 1; i <= 14; i++){
            this.tabRefreshed.add(i, false);
        }//end for(;;)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);
        context = this;
        man = MainActivity.this;

        //startActivity(new Intent(this, SplashScreen.class));

        renameDb();
        setContentView(R.layout.main_coordinator);
        initBooleansList();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(MainActivity.dbTableExist(Database.TABLE2_NAME) == false){
            MainActivity.newTabListTable(this);
        }//end of if()
        else
            mDbAdapterStatic = new Database(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        firstRun();

        tvAverage = (TextView) findViewById(R.id.tvAverage);

        tvCount = (TextView) findViewById(R.id.tvMarkCount);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupViewPager(viewPager);

        tabPosition = 0;

        cl = (CoordinatorLayout) findViewById(R.id.coordinator);

        tabRl = (RelativeLayout) findViewById(R.id.relativeTabInfo);

        tabRl.setVisibility(View.INVISIBLE);

        hockeyAppSet();

        setTabListeners();

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MainWakelockLog");
        wakeLock.acquire();
        //   registerReceiver(new UpdateReceiver(), new IntentFilter(Intent.ACTION_TIME_TICK));
    }//end of onCreate
}//end of the Class