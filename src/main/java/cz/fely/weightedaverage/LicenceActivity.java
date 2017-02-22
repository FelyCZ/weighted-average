package cz.fely.weightedaverage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.webkit.WebView;

import cz.fely.weightedaverage.utils.ThemeUtil;

public class LicenceActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);

        WebView web = (WebView) LayoutInflater.from(this).inflate(R.layout.licence_dialog, null);
        web.loadUrl("file:///android_asset/open_source_licenses.html");
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.action_licenses))
                .setView(web)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(LicenceActivity.this, AboutFragment.class).addFlags
                                (Intent
                                .FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .show();

    }
}