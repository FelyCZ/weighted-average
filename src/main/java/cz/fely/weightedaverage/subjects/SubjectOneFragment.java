package cz.fely.weightedaverage.subjects;

import android.content.DialogInterface;
import android.os.Bundle;
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

import cz.fely.weightedaverage.MainActivity;
import cz.fely.weightedaverage.R;

public class SubjectOneFragment extends Fragment {

    Button btnAdd;
    EditText etName, etMark, etWeight;
    TextView tvAverage;
    ListView lv;
    public static View view;

    public SubjectOneFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        etName = (EditText) view.findViewById(R.id.etName);
        etMark = (EditText) view.findViewById(R.id.etMark);
        etWeight = (EditText) view.findViewById(R.id.etWeight);
        tvAverage = (TextView) view.findViewById(R.id.averageTV);
        lv = (ListView) view.findViewById(R.id.lvZnamky);
        MainActivity.checkSettings(view);
        MainActivity.getViews(view);
        MainActivity.updateView(1, getContext());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDialog(((TextView) view.findViewById(R.id.name)).getText().toString(), (
                        (TextView) view.findViewById(R.id.mark)).getText().toString(), ((TextView)
                        view.findViewById(R.id.weight)).getText().toString(), id);

            }
        });

        btnAdd.setOnClickListener(v -> {
            MainActivity.getViews(view);
            MainActivity.addOrUpdateMark(view, 1, getContext(), etName.getText().toString
                    (), etMark.getText().toString(), etWeight.getText().toString(), new
                    long[0]);
        });
        return view;
    }

    public void showEditDialog(String name, String mark, String weight, long id) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.edit_dialog, null);
        EditText etNameDialog = (EditText) v.findViewById(R.id.etNameDialog);
        EditText etMarkDialog = (EditText) v.findViewById(R.id.etMarkDialog);
        EditText etWeightDialog = (EditText) v.findViewById(R.id.etWeightDialog);
        etNameDialog.setText(name);
        etMarkDialog.setText(mark);
        etWeightDialog.setText(weight);
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
        adb.setTitle(R.string.editMark);
        adb.setPositiveButton(R.string.save, (dialog, which) -> {
            MainActivity.getViews(view);
            MainActivity.addOrUpdateMark(view, 1, getContext(), etNameDialog.getText()
                         .toString(), etMarkDialog.getText().toString(), etWeightDialog
                         .getText().toString(), id);
        });
        adb.setNegativeButton(R.string.titleDelete, (dialog, which) -> {
            AlertDialog.Builder adb1 = new AlertDialog.Builder(getContext());
            adb1.setTitle(R.string.titleDelete);
            adb1.setIcon(R.drawable.warning);
            adb1.setMessage(R.string.areYouSure);
            adb1.setNegativeButton(R.string.cancel, null);
            adb1.setPositiveButton(R.string.yes, (dialogInterface, which1) -> {
                MainActivity.getViews(view);
                MainActivity.removeMark(1, getContext(), id);
                MainActivity.updateView(1, getContext());
            });
            adb1.show();
            dialog.dismiss();
        });
        adb.setNeutralButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        adb.setView(v);
        adb.show();
    }

    @Override
    public void onResume() {
        MainActivity.getViews(view);
        MainActivity.checkSettings(view);
        super.onResume();
    }
}