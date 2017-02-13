package cz.fely.weightedaverage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.LoginManager;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.UpdateManagerListener;

import java.sql.Time;
import java.util.Timer;

import cz.fely.weightedaverage.utils.ThemeUtil;


public class SplashScreen extends Activity {
    static final int TIMER_RUNTIME = 2000;
    boolean mbActive;
    ProgressBar progressBar;
    TextView tvInfo;

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
        progressBar = (ProgressBar) findViewById(R.id.pb_splash);
        progressBar.setMax(TIMER_RUNTIME);
        final Thread thread = new Thread(){
            @Override
            public void run(){

                        mbActive = true;
                        try {
                            int waited = 0;
                            int waitedSec = 0;
                            while(mbActive && (waited < TIMER_RUNTIME)){
                                if (waited == 1000) {
                                    sleep(500);
                                    if (mbActive) {
                                        waited += 500;
                                        waitedSec += 522;
                                        updateProgress(waited, waitedSec);
                                    }
                                }
                                else{
                                    sleep(100);
                                    if(mbActive) {
                                        waited += 100;
                                        waitedSec += 122;
                                        updateProgress(waited, waitedSec);
                                    }
                                }
                            }
                        }
                        catch (InterruptedException e){
                            Log.e("Splash: ", e.toString());
                        }
                        finally {
                            Intent i = new Intent(SplashScreen.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                            finish();
                        }
                    }
        };
        thread.start();
    }

    public void updateProgress(int waited, int waitedSec) {
        progressBar.setProgress(waited);
    }
}