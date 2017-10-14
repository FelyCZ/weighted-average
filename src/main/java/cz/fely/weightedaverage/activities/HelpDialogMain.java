package cz.fely.weightedaverage.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;

import cz.fely.weightedaverage.R;

public class HelpDialogMain extends DialogFragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.help_dialog, container, false);
        getDialog().setTitle(R.string.help);
        WebView web = view.findViewById(R.id.web_help_dialog);
        web.loadUrl(getString(R.string.assets_path) + "help_main.html");
        Button btnOk = view.findViewById(R.id.help_dialog_btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("help_main", true).apply();
                getDialog().dismiss();
            }
        });
        return view;
    }
}