package cz.fely.weightedaverage.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.utils.ThemeUtil;

public class SplashScreen extends AppCompatActivity {
    static final int TIMER_RUNTIME = 2100;
    boolean mbActive;
    StringBuilder b = new StringBuilder();
    int versionCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pi.versionCode;
            TextView tvCode = findViewById(R.id.tv_code);
            tvCode.setText("("+String.valueOf(versionCode)+")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        final Thread thread = new Thread(){
            @Override
            public void run(){
                        mbActive = true;
                        try {
                            int waited = 0;
                            while(mbActive && (waited < TIMER_RUNTIME)){
                                sleep(TIMER_RUNTIME);
                                waited = TIMER_RUNTIME;
                            }//end of while()
                        }//end of try{]
                        catch (InterruptedException e){
                            Log.e("Splash: ", e.toString());
                        }
                        finally {
                            startActivity(new Intent(SplashScreen.this, MainActivity.class));
                            finish();
                        }
                    }
        }; thread.start(); //end of Thread

    }
}