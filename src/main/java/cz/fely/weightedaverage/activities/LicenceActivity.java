package cz.fely.weightedaverage.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.webkit.WebView;

import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.utils.ThemeUtil;

public class LicenceActivity extends Activity {
    DialogInterface dialogInterface;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);

        //EditWeb!
        WebView web = (WebView) LayoutInflater.from(this).inflate(R.layout.licence_dialog, null);
        web.loadUrl("file:///android_asset/open_source_licenses.html");
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.action_licenses))
                .setView(web)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialogInterface = dialog;
                        dialog.dismiss();
                        LicenceActivity.this.finish();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed(){
        LicenceActivity.this.finish();
        super.onBackPressed();
    }
}