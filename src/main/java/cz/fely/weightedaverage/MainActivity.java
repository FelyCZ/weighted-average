package cz.fely.weightedaverage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cz.fely.weightedaverage.db.DatabaseAdapter;
import cz.fely.weightedaverage.utils.ThemeUtil;

public class MainActivity extends AppCompatActivity{
    final String welcomeScreenShownPref = "welcomeScreenShown";
    ArrayList<Columns> listItems;
    Button btnAdd;
    SharedPreferences mPrefs;
    private EditText etName, etWeight, etMark;
    private Toolbar toolbar;
    private TextView tvAverage;
    private DatabaseAdapter mDbAdapter;
    private ListView lv;
    double weightsAmount, weightedMarks;
    Cursor cursor;
    boolean doubleBackToExitPressedOnce = false;
    public int mTheme = 0;

    private void getFields()
    {
        this.mDbAdapter = new DatabaseAdapter(this);
        this.lv = ((ListView)findViewById(R.id.lvZnamky));
        this.etName = ((EditText)findViewById(R.id.etName));
        this.etMark = ((EditText)findViewById(R.id.etMark));
        this.etWeight = ((EditText)findViewById(R.id.etWeight));
        this.tvAverage = ((TextView)findViewById(R.id.averageTV));
        this.btnAdd = ((Button)findViewById(R.id.btnAdd));
    }

    private void updateView(){
        cursor = this.mDbAdapter.getAllEntries1();
        startManagingCursor(cursor);
        this.lv.setAdapter(new ListAdapter(this, cursor, 0));
        cursor.moveToFirst();
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
                DecimalFormat formater = new DecimalFormat("#.##");
                String total = String.valueOf(formater.format(sum));
                tvAverage.setText(getResources().getString(R.string.prumer)+" "+total);
            } while (cursor.moveToNext());
        }
        if ((weightsAmount == 0.0 || weightedMarks == 0.0)) {
            tvAverage.setText(getResources().getString(R.string.prumer)+" "+"0.00");
        }
    }

    public void addMark(View v) {
        add(this.etName.getText().toString(), this.etMark.getText().toString(), this.etWeight.getText().toString(), new long[0]);
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
                this.mDbAdapter.addMark1(name, mark, weight);
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFields();
        this.lv.setAdapter(new ListAdapter(this, cursor, 0));
        updateView();
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //Welcome Message
        showWelcomeMessage();
        listItems = new ArrayList<Columns>();
        checkSettings();
        ThemeUtil.setAppTheme(this);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        if (id == R.id.action_feedback) {
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "kubfe@seznam.cz" });
            Email.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.SubjectFeedback));
            Email.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(Email, getResources().getString(R.string.ChooserTitle)));
            return true;
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
            return true;
        }
        if (id == R.id.action_changelog){
            showChangelog();
        }

        if (id == R.id.action_about){
            Intent i = new Intent(this, AboutFragment.class);
            startActivity(i);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void showWelcomeMessage() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);
        if (!welcomeScreenShown) {
            String whatsNewTitle = getResources().getString(R.string.welcome_title);
            String whatsNewText = getResources().getString(R.string.welcome_text);
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_new)
                    .setTitle(whatsNewTitle)
                    .setMessage(whatsNewText)
                    .setPositiveButton(
                    R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeScreenShownPref, true);
            editor.commit();
        }
    }

    private void showChangelog (){
        String whatsNewTitle = getResources().getString(R.string.welcome_title);
        String whatsNewText = getResources().getString(R.string.welcome_text);
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_new)
                .setTitle(whatsNewTitle)
                .setMessage(whatsNewText)
                .setPositiveButton(
                        R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    @Override
    public void onBackPressed() {
        if (!doubleBackToExitPressedOnce) {
            super.onBackPressed();

            System.exit(0);

            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        checkSettings();
        ThemeUtil.setAppTheme(this);
        this.recreate();
        super.onResume();
    }

    private void checkSettings () {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        //Vážení známek
        boolean weightValue = sp.getBoolean("pref_key_general_weight", false);
        if (weightValue){
            etWeight.setEnabled(true);
            etWeight.setVisibility(View.VISIBLE);
        } else {
            etWeight.setEnabled(false);
            etWeight.setVisibility(View.INVISIBLE);
        }
    }
}