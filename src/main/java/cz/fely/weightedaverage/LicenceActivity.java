package cz.fely.weightedaverage;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import cz.fely.weightedaverage.utils.ThemeUtil;

public class LicenceActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);
        displayLicensesDialogFragment();
    }
    private void displayLicensesDialogFragment() {
        FragmentManager fm = getFragmentManager();
        LicensesDialogFragment newFragment = new LicensesDialogFragment();
        newFragment.show(fm, "licenses");
    }
}