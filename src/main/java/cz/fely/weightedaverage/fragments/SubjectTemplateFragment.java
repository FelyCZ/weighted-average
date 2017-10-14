package cz.fely.weightedaverage.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.activities.MainActivity;

import static cz.fely.weightedaverage.activities.MainActivity.checkSettings;
import static cz.fely.weightedaverage.activities.MainActivity.tabPosition;

public class SubjectTemplateFragment extends Fragment {

    public static SubjectTemplateFragment fragment;
    private MainActivity mActivity;
    int mHour, mMinute;
    int subjectId;
    String dateCompleted;
    static String argumentsKey = "subject_id";
    public static View view;

    public SubjectTemplateFragment newInstance(int page) {
        SubjectTemplateFragment frag = new SubjectTemplateFragment();
        Bundle args = new Bundle();
        args.putInt(argumentsKey, page);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null){
            this.subjectId = getArguments() != null ? getArguments().getInt(argumentsKey) : 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activity_main, container, false);
        MainActivity.checkSettings(view);
        fragment = SubjectTemplateFragment.this;
        EditText etMark, etWeight;
        AutoCompleteTextView etName;
        Button btnAdd = view.findViewById(R.id.btnAdd);
        etName = view.findViewById(R.id.etName);
        etMark = view.findViewById(R.id.etMark);
        etWeight = view.findViewById(R.id.etWeight);
        ListView lv = view.findViewById(R.id.lvZnamky);
        mActivity = (MainActivity) getActivity();
        checkSettings(view);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.addOrUpdateMark(view, tabPosition, getContext(),
                        etName.getText().toString(), etMark.getText().toString(),
                        etWeight.getText().toString(), null, new long[0]);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mActivity.showEditDialog(view, ((TextView) view.findViewById(R.id.name)).getText().toString(), (
                        (TextView) view.findViewById(R.id.mark)).getText().toString(), ((TextView)
                        view.findViewById(R.id.weight)).getText().toString(), id);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        //MainActivity.getViews(view);
        MainActivity.checkSettings(view);
        Log.i("SubjectFragment: ", "Fragment resumed");
        super.onResume();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        Log.i("SubjectFragment: ", "Fragment attached");
        //MainActivity.getViews(view);
        super.onAttachFragment(childFragment);
    }
}