package cz.fely.weightedaverage;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

public class LicenceActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayLicensesDialogFragment();
        finish();
    }
    private void displayLicensesDialogFragment() {
        FragmentManager fm = getFragmentManager();
        LicensesDialogFragment newFragment = new LicensesDialogFragment();
        newFragment.show(fm, "licenses");
    }
}