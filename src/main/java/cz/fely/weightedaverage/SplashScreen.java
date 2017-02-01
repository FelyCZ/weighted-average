package cz.fely.weightedaverage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;


public class SplashScreen extends Activity {
    static final int TIMER_RUNTIME = 2500;
    boolean mbActive;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.pb_splash);
        progressBar.setMax(2500);
        final Thread thread = new Thread(){
            @Override
            public void run(){
                mbActive = true;
                try {
                    int waited = 0;
                    int waitedSec = 0;
                    while(mbActive && (waited < TIMER_RUNTIME)){
                        sleep(100);
                        if(mbActive){
                            waited += 100;
                            waitedSec += 122;
                            updateProgress(waited, waitedSec);
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
        final int progress = waited;
        progressBar.setProgress(progress);
        progressBar.setSecondaryProgress(waitedSec);
    }
}