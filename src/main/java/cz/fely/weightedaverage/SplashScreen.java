package cz.fely.weightedaverage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import static cz.fely.weightedaverage.MainActivity.*;

import cz.fely.weightedaverage.db.Database;
import cz.fely.weightedaverage.utils.ThemeUtil;

public class SplashScreen extends AppCompatActivity {
    static final int TIMER_RUNTIME = 2100;
    boolean mbActive;
    boolean one, two, three;
    ProgressBar pb;
    TextView tvInfo;
    String loadingText;
    StringBuilder b = new StringBuilder();

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
        pb = (ProgressBar) findViewById(R.id.pbSplash);
        pb.setMax(TIMER_RUNTIME);
        tvInfo = (TextView) findViewById(R.id.tvSplashInfo);
        tvInfo.setText("");

        final Thread thread = new Thread(){
            @Override
            public void run(){
                        mbActive = true;
                        try {
                            int waited = 0;
                            while(mbActive && (waited < TIMER_RUNTIME)){
                                for(int i = 1; i < 3; i++){
                                    sleep(100);
                                    if(mbActive) {
                                        waited += 100;
                                        updateProgress(waited);
                                    }
                                }
                                sleep(100);
                                if(mbActive) {
                                    waited += 100;
                                    updateProgress(waited);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvInfo.setText(loadingText);
                                            loadingText = b.append("∙").toString();
                                        }
                                    });
                                }
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

    public void updateProgress(int waited) {
        pb.setProgress(waited);
        pb.animate();
    }

}