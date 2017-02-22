package cz.fely.weightedaverage;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SubjectThreeFragment extends Fragment{

    Button btnAdd;
    EditText etName, etMark, etWeight;
    TextView tvAverage;
    ListView lv;
    public static View view;

    public SubjectThreeFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState){
        view = inflater.inflate(R.layout.activity_main, container, false);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        etName = (EditText) view.findViewById(R.id.etName);
        etMark = (EditText) view.findViewById(R.id.etMark);
        etWeight = (EditText) view.findViewById(R.id.etWeight);
        tvAverage = (TextView) view.findViewById(R.id.averageTV);
        lv = (ListView) view.findViewById(R.id.lvZnamky);
        checkSettings();
        MainActivity.getViews(view);
        MainActivity.updateView(2, getContext());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDialog(((TextView) view.findViewById(R.id.name)).getText().toString(), (
                        (TextView) view.findViewById(R.id.mark)).getText().toString(), ((TextView)
                        view.findViewById(R.id.weight)).getText().toString(), id);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getViews(view);
                MainActivity.addOrUpdateMark(view, 2, getContext(), etName.getText().toString
                        (), etMark.getText().toString(), etWeight.getText().toString(), new
                        long[0]);
            }
        });
        return view;
    }

    public void showEditDialog(String name, String mark, String weight, long id){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.edit_dialog, null);
        EditText etNameDialog, etMarkDialog,etWeightDialog;
        etNameDialog = (EditText) v.findViewById(R.id.etNameDialog);
        etMarkDialog = (EditText) v.findViewById(R.id.etMarkDialog);
        etWeightDialog = (EditText) v.findViewById(R.id.etWeightDialog);
        etNameDialog.setText(name);
        etMarkDialog.setText(mark);
        etWeightDialog.setText(weight);
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
        adb.setTitle(R.string.editMark);
        adb.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.getViews(view);
                MainActivity.addOrUpdateMark(view, 2, getContext(), etNameDialog.getText()
                        .toString(), etMarkDialog.getText().toString(), etWeightDialog.getText()
                        .toString(), id);
            }
        });
        adb.setNegativeButton(R.string.titleDelete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.getViews(view);
                MainActivity.removeMark(2, getContext(), id);
                MainActivity.updateView(2, getContext());
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

    public void checkSettings(){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        //Weight marks
        boolean weightValue = mPrefs.getBoolean("pref_key_general_weight", true);
        if(weightValue){
            etWeight.setVisibility(View.VISIBLE);
        }
        else {
            etWeight.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        MainActivity.getViews(view);
        super.onResume();
    }
}
