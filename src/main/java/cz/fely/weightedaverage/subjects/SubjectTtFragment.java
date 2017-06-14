package cz.fely.weightedaverage.subjects;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import cz.fely.weightedaverage.MainActivity;
import cz.fely.weightedaverage.R;

public class SubjectTtFragment extends Fragment {

    Button btnAdd;
    EditText etMark, etWeight;
    TextView tvAverage;
    public static ListView lv;
    public static View view;
    public static SubjectTtFragment fragment;
    AutoCompleteTextView etName;

    public SubjectTtFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        etName = (AutoCompleteTextView) view.findViewById(R.id.etName);
        etMark = (EditText) view.findViewById(R.id.etMark);
        etWeight = (EditText) view.findViewById(R.id.etWeight);
        lv = (ListView) view.findViewById(R.id.lvZnamky);
        MainActivity.checkSettings(view);
        MainActivity.getViews(view);
        MainActivity.updateView(MainActivity.tabPosition, getContext());
        fragment = SubjectTtFragment.this;
        MainActivity.autoCompleteAuth();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.showEditDialog(((TextView) view.findViewById(R.id.name)).getText().toString(), (
                        (TextView) view.findViewById(R.id.mark)).getText().toString(), ((TextView)
                        view.findViewById(R.id.weight)).getText().toString(), id);

            }
        });

        btnAdd.setOnClickListener(v -> {
            MainActivity.getViews(view);
            MainActivity.addOrUpdateMark(view, MainActivity.tabPosition, getContext(), etName.getText().toString
                    (), etMark.getText().toString(), etWeight.getText().toString(), new
                    long[0]);
        });
        return view;
    }

    @Override
    public void onResume() {
        MainActivity.getViews(view);
        MainActivity.checkSettings(view);
        super.onResume();
    }
}