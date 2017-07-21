package cz.fely.weightedaverage;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cz.fely.weightedaverage.utils.ThemeUtil;

@SuppressWarnings("deprecation")
public class AboutFragment extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);
        initToolbar();
        PreferenceManager.getDefaultSharedPreferences(this);
        addPreferencesFromResource(R.xml.about_pref);

        String prefChangelog = "changelog";
        Preference changelog = (Preference) findPreference(prefChangelog);
        changelog.setOnPreferenceClickListener(preference -> {
            LayoutInflater inflater = LayoutInflater.from(AboutFragment.this);
            View view = inflater.inflate(R.layout.changelog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(AboutFragment.this);
            builder.setView(view).setTitle(getResources().getString(R.string.welcome_title))
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.show();
            return false;
        });
    }
    private void initToolbar() {
        Toolbar toolbar;

        ViewGroup root = (ViewGroup) findViewById(android.R.id.list).getParent().getParent().getParent();
        toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.tool_bar, root, false);
        root.addView(toolbar, 0);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}