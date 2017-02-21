package cz.fely.weightedaverage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import net.hockeyapp.android.FeedbackManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.fely.weightedaverage.db.DatabaseAdapter;
import cz.fely.weightedaverage.utils.ThemeUtil;

public class MainActivityNew extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    int tabPosition;
    static DatabaseAdapter mDbAdapterStatic;
    private static MainActivityNew man;
    static ListView lv;
    static TextView tvAverage;

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
        man = MainActivityNew.this;

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                updateView(tabPosition, getApplicationContext());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                updateView(tabPosition, getApplicationContext());
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SubjectOneFragment(), "ONE");
        adapter.addFrag(new SubjectTwoFragment(), "TWO");
        adapter.addFrag(new SubjectThreeFragment(), "THREE");
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
            FeedbackManager.showFeedbackActivity(MainActivityNew.this);
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
                    if(tabPosition == 0){
                        mDbAdapterStatic.deleteAll();
                        updateView(0, getApplicationContext());
                        updateView(1, getApplicationContext());
                        updateView(2, getApplicationContext());
                    }
                    else if (tabPosition == 1){
                        mDbAdapterStatic.deleteAll2();
                        updateView(1, getApplicationContext());
                        updateView(0, getApplicationContext());
                        updateView(2, getApplicationContext());
                    }
                    else if (tabPosition == 2){
                        mDbAdapterStatic.deleteAll3();
                        updateView(2, getApplicationContext());
                        updateView(1, getApplicationContext());
                        updateView(0, getApplicationContext());
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

    public static void getViews (View v){
        lv = (ListView) v.findViewById(R.id.lvZnamky);
        tvAverage = (TextView) v.findViewById(R.id.averageTV);
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
        lv.setAdapter(new ListAdapter(man, cursor, 0));
        average(ctx, positionArg);
    }

    public static void average(Context ctx, int posArg){
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
}
