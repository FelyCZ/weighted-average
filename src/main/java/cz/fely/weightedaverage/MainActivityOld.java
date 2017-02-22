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
import android.text.TextUtils;
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
import cz.fely.weightedaverage.utils.ThemeUtil;

public class MainActivityOld extends AppCompatActivity{
    private static final int PERIOD = 2000;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    Button btnAdd;
    SharedPreferences mPrefs;
    double weightsAmount, weightedMarks;
    Cursor cursor;
    EditText etName, etWeight, etMark;
    private Toolbar toolbar;
    private TextView tvAverage;
    public DatabaseAdapter mDbAdapter;
    private ListView lv;
    private long lastPressedTime;

    private void getFields()
    {
        mDbAdapter = new DatabaseAdapter(this);
        lv = ((ListView)findViewById(R.id.lvZnamky));
        etName = ((EditText)findViewById(R.id.etName));
        etMark = ((EditText)findViewById(R.id.etMark));
        etWeight = ((EditText)findViewById(R.id.etWeight));
        tvAverage = ((TextView)findViewById(R.id.averageTV));
        btnAdd = ((Button)findViewById(R.id.btnAdd));
    }

    public void updateView(){
        cursor = mDbAdapter.getAllEntries();
   //     lv.setAdapter(new ListAdapter(MainActivityOld.this, cursor, 0));
        average();
    }

    void average() {
        cursor = mDbAdapter.averageFromMarks();
        double sum =  cursor.getDouble(cursor.getColumnIndex("average"));
        DecimalFormat formater = new DecimalFormat("0.00");
        String total = String.valueOf(formater.format(sum));
        tvAverage.setText(getResources().getString(R.string.prumer)+" "+total);
    }

    public void addMark(View v) {
        addOrUpdateMark(etName.getText().toString(), etMark.getText().toString(), etWeight.getText()
                .toString(), new long[0]);
    }

    void removeMark(long id) {
        mDbAdapter.deleteMark(id);
        updateView();
    }

    void addOrUpdateMark(String name, String m, String w, long... id) {
        try {
            double weight;
            double mark;
            if (w.equals("")) {
                weight = 1;
            } else {
                weight = Double.parseDouble(w);
            }
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(m) || m.equals("0")) {
                throw new IllegalArgumentException(getResources().getString(R.string
                            .illegalArgument));
            }
            else {
                mark = Double.parseDouble(m);
                if(mark > 5 || weight == 0){
                    throw new IllegalArgumentException(getResources().getString(R.string.invalidMarkWeight));
                }
            }
            if (id.length == 0) {
                mDbAdapter.addMark(name, mark, weight);
                etName.requestFocus();
                etName.setText("");
                etMark.setText("");
                etWeight.setText("");
            } else {
                mDbAdapter.updateMark(name, mark, weight, id[0]);
            }
            updateView();
        } catch (IllegalArgumentException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(String.valueOf(e));
            builder.setTitle(R.string.adbError);
            builder.setNeutralButton(android.R.string.ok, null);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Welcome Message
        firstRun();
        checkSettings();
        hockeyAppSet();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                showEditDialog(((TextView) view.findViewById(R.id.name)).getText().toString(), (
                        (TextView) view.findViewById(R.id.mark)).getText().toString(),((TextView)
                        view.findViewById(R.id.weight)).getText().toString(), id);
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        if (id == R.id.action_feedback) {
            FeedbackManager.register(this);
            FeedbackManager.showFeedbackActivity(MainActivityOld.this);
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
                    mDbAdapter.deleteAll();
                    MainActivityOld.this.updateView();
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

    private void firstRun() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
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
        updateView();
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


    public void hockeyAppSet(){
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
                if (vibrationsValue) {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(400);
                }
                UpdateManager.register(MainActivityOld.this);
                super.onUpdateAvailable();
            }
        });
    }

    public void showEditDialog(String name, String mark, String weight, long id)
    {
        View view = getLayoutInflater().inflate(R.layout.edit_dialog, null);
        EditText etNameDialog = (EditText)view.findViewById(R.id.etNameDialog);
        EditText etMarkDialog = (EditText)view.findViewById(R.id.etMarkDialog);
        EditText etWeightDialog = (EditText)view.findViewById(R.id.etWeightDialog);
        etNameDialog.setText(name);
        etMarkDialog.setText(mark);
        etWeightDialog.setText(weight);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.editMark);
        adb.setPositiveButton(R.string.save, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                addOrUpdateMark(etNameDialog.getText().toString(), etMarkDialog.getText().toString(),
                        etWeightDialog.getText().toString(), id);
            }
        });
        adb.setNegativeButton(R.string.titleDelete, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivityOld.this);
                adb.setTitle(R.string.titleDelete);
                adb.setIcon(R.drawable.warning);
                adb.setMessage(R.string.areYouSure);
                adb.setNegativeButton(R.string.cancel, null);
                adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        removeMark(id);
                        updateView();
                    }
                });
                adb.show();
                dialog.dismiss();
            }
        });
        adb.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.setView(view);
        adb.show();
    }
}