package cz.fely.weightedaverage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cz.fely.weightedaverage.utils.ThemeUtil;


public class SplashScreen extends Activity {
    static final int TIMER_RUNTIME = 2000;
    boolean mbActive;

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
        final Thread thread = new Thread(){
            @Override
            public void run(){
                        mbActive = true;
                        try {
                            int waited = 0;
                            while(mbActive && (waited < TIMER_RUNTIME)){
                                    sleep(100);
                                    if(mbActive) {
                                        waited += 100;

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
    }
}