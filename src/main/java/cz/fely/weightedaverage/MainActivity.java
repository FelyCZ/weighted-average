package cz.fely.weightedaverage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import cz.fely.weightedaverage.db.DatabaseAdapter;
import cz.fely.weightedaverage.db.DatabaseHelper;
import cz.fely.weightedaverage.utils.ThemeUtil;

public class MainActivity extends AppCompatActivity{
    final String welcomeScreenShownPref = "welcomeScreenShown";
    Button btnAdd;
    SharedPreferences mPrefs;
    private EditText etName, etWeight, etMark;
    private Toolbar toolbar;
    private TextView tvAverage;
    private DatabaseAdapter mDbAdapter;
    private DatabaseHelper mDbHelper;
    private ListView lv;
    double weightsAmount, weightedMarks;
    Cursor cursor;
    private long lastPressedTime;
    private static final int PERIOD = 2000;

    private void getFields()
    {
        mDbAdapter = new DatabaseAdapter(this);
        lv = ((ListView)findViewById(R.id.lvZnamky));
        etName = ((EditText)findViewById(R.id.btnAddPopUp));
        etMark = ((EditText)findViewById(R.id.etMarkPopUp));
        etWeight = ((EditText)findViewById(R.id.etWeightPopUp));
        tvAverage = ((TextView)findViewById(R.id.averageTV));
        btnAdd = ((Button)findViewById(R.id.btnAdd));
    }

    public void updateView(){
        cursor = mDbAdapter.getAllEntries();
        startManagingCursor(cursor);
        lv.setAdapter(new ListAdapter(this, cursor, 0));
        average();
    }

    void average() {
        weightsAmount = 0.0;
        weightedMarks = 0.0;
        if (cursor.moveToFirst()) {
            do {
                double weight = cursor.getDouble(cursor.getColumnIndex("weight"));
                double mark = cursor.getDouble(cursor.getColumnIndex("mark"));
                weightsAmount += weight;
                weightedMarks += mark * weight;
                double sum = weightedMarks/weightsAmount;
                DecimalFormat formater = new DecimalFormat("#.00");
                String total = String.valueOf(formater.format(sum));
                tvAverage.setText(getResources().getString(R.string.prumer)+" "+total);
            }
            while (cursor.moveToNext());
        }
        if ((weightsAmount == 0.0 || weightedMarks == 0.0)) {
            tvAverage.setText(getResources().getString(R.string.prumer)+" "+"0.00");
        }
    }

    public void addMark(View v) {
        add(etName.getText().toString(), etMark.getText().toString(), etWeight.getText().toString(), new long[0]);
    }

    void removeMark(long id) {
        this.mDbAdapter.deleteMark(id);
        updateView();
    }

    void add(String name, String m, String w, long... id) {
        try {
            double weight;
            if (w.equals("")) {
                weight = 1;
            } else {
                weight = Double.parseDouble(w);
            }
            if (name.equals("") || m.equals("") || weight <= (short) 0) {
                throw new IllegalArgumentException(getResources().getString(R.string.illegalArgument));
            }
            double mark = Double.parseDouble(m);
            if (id.length == 0) {
                this.mDbAdapter.addMark(name, mark, weight);
                etName.requestFocus();
            }
            updateView();
        } catch (IllegalArgumentException iae) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.adbError);
            builder.setMessage(String.valueOf(iae));
            builder.setNeutralButton(R.string.close, null);
            builder.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFields();
        ThemeUtil.setTheme(this);
        updateView();
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //Welcome Message
        firstRun();
        checkSettings();
        hockeyAppSet();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle(R.string.titleDelete);
                adb.setIcon(R.drawable.warning);
                adb.setMessage(R.string.areYouSure);
                adb.setNegativeButton(R.string.cancel, null);
                adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
                   @Override
                    public void onClick (DialogInterface dialogInterface, int which){
                       removeMark(id);
                       updateView();
                   }
                });
                adb.show();
                }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify header parent activity in AndroidManifest.xml.
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
                    MainActivity.this.mDbAdapter.deleteAll();
                    MainActivity.this.updateView();
                }
            });
            adb.show();
        }

        if (id == R.id.action_about){
            Intent i = new Intent(this, AboutFragment.class);
            startActivity(i);
        }

        if(id == R.id.action_pop){
            Intent i = new Intent(this, FloatingWindow.class);
            startActivity(i);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    private void firstRun() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        int currentVersionNumber = 0;

        int savedVersionNumber = mPrefs.getInt("version", 0);

        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersionNumber = pi.versionCode;
        } catch (Exception e) {}

        if (currentVersionNumber > savedVersionNumber) {
            showChangelog();

            SharedPreferences.Editor editor = mPrefs.edit();

            editor.putInt("version", currentVersionNumber);
            editor.commit();
        }

        //Settings
        if (!welcomeScreenShown) {
            showChangelog();
            //Settings
            mPrefs.edit().putBoolean(welcomeScreenShownPref,
                    true).commit();
            mPrefs.edit().putBoolean
                    ("pref_key_general_weight", true).commit();
            mPrefs.edit().putBoolean
                    ("pref_key_sound_vibrate", true).commit();
            mPrefs.edit().putString
                    ("pref_key_general_theme", "0").commit();
        }
    }

    private void showChangelog (){
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

    @Override
    protected void onResume() {
        checkSettings();
        ThemeUtil.reloadTheme(this);
        super.onResume();
    }

    private void checkSettings () {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        //Vážení známek
        boolean weightValue = sp.getBoolean("pref_key_general_weight", false);
        if (!weightValue){
            etWeight.setEnabled(false);
            etWeight.setVisibility(View.INVISIBLE);
        } else {
            etWeight.setEnabled(true);
            etWeight.setVisibility(View.VISIBLE);
        }

        //Vibrace
        boolean vibrationsValue = sp.getBoolean("pref_key_sound_vibrate", true);
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
    private void hockeyAppSet(){
        LoginManager.register(this, "2fccbcc477da9ab6b058daf97571ac77", LoginManager
                .LOGIN_MODE_ANONYMOUS);
        LoginManager.verifyLogin(this, getIntent());
        CrashManager.register(this);
        UpdateManager.register(this, "a6f2b12acd1a4e22a763dab9a356879f", new UpdateManagerListener() {
            @Override
            public void onUpdateAvailable() {
                boolean vibrationsValue = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .getBoolean("pref_key_sound_vibrate",
                        true);
                if(vibrationsValue){
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(400);
                }
                UpdateManager.register(MainActivity.this);
                super.onUpdateAvailable();
            }
        });
    }
}