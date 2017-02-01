package cz.fely.weightedaverage;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import cz.fely.weightedaverage.db.DatabaseAdapter;
import cz.fely.weightedaverage.utils.ThemeUtil;

public class FloatingWindow extends Activity {
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;
    private Button mButton;
    private EditText etNamePopUp, etMarkPopUp, etWeightPopUp;
    private PopupWindow mPopupWindow;
    private DatabaseAdapter mDbAdapter;
    private Cursor cursor;
    private ListView lv;
    private double weightsAmount, weightedMarks;
    private TextView tvAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = FloatingWindow.this;

        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        lv = ((ListView) findViewById(R.id.lvZnamky));
        tvAverage = ((TextView) findViewById(R.id.tvAveragePopUp));
        // Set a click listener for the text view

        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.floating_window_row, null);

        // Initialize a new instance of popup window
        mPopupWindow = new PopupWindow(
                customView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }

        // Get a reference for the custom view close button
        ImageButton closeButton = (ImageButton) customView.findViewById(R.id.btnClosePopUp);

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });

        Button btnAdd = (Button) findViewById(R.id.btnAddPopUp);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(etNamePopUp.getText().toString(), etMarkPopUp.getText().toString()
                        , etWeightPopUp.getText().toString(), new long[0]);
            }
        });

        // Finally, show the popup window at the center location of root relative layout
        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);


    }

    private void updateView() {
        cursor = mDbAdapter.getAllEntries();
        startManagingCursor(cursor);
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
                double sum = weightedMarks / weightsAmount;
                DecimalFormat formater = new DecimalFormat("#.00");
                String total = String.valueOf(formater.format(sum));
                tvAverage.setText(getResources().getString(R.string.prumer) + " " + total);
            }
            while (cursor.moveToNext());
        }
        if ((weightsAmount == 0.0 || weightedMarks == 0.0)) {
            tvAverage.setText(getResources().getString(R.string.prumer) + " " + "0.00");
        }
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
                mDbAdapter.addMark(name, mark, weight);
                etNamePopUp.requestFocus();
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
}