package cz.fely.weightedaverage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import cz.fely.weightedaverage.utils.ThemeUtil;


public class SplashScreen extends Activity {
    static final int TIMER_RUNTIME = 2100;
    boolean mbActive;
    boolean one, two, three;
    ProgressBar pb;
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
        pb = (ProgressBar) findViewById(R.id.pbSplash);
        pb.setMax(TIMER_RUNTIME);
        tvInfo = (TextView) findViewById(R.id.tvSplashInfo);

        one = false;
        two = false;
        three = false;

        final Thread thread = new Thread(){
            @Override
            public void run(){
                        mbActive = true;
                        try {
                            int waited = 0;
                            while(mbActive && (waited < TIMER_RUNTIME)){
                                sleep(100);
                                if(mbActive){
                                    waited += 100;
                                    updateProgress(waited);
                                }
                                sleep(100);
                                if(mbActive) {
                                    waited += 100;
                                    updateProgress(waited);
                                    int finalWaited = waited;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (one == false) {
                                                tvInfo.setText("∙");
                                                one = true;
                                                Log.d("Splash Loading: ", "One Point");
                                            } else if (one == true && two == false) {
                                                tvInfo.setText("∙∙");
                                                two = true;
                                                Log.d("Splash Loading: ", "Two Point");
                                            } else if (one == true && two == true && three == false) {
                                                tvInfo.setText("∙∙∙");
                                                three = true;
                                                Log.d("Splash Loading: ", "Three Point");
                                            } else {
                                                tvInfo.setText("");
                                                one = false;
                                                two = false;
                                                three = false;
                                                Log.d("Splash Loading: ", "Zero Point");
                                            }
                                        }//end of void run()
                                    });//end of runOnUiThread
                                }//end of if(mbActive)
                            }//end of while()
                        }//end of try{]
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
        }; thread.start(); //end of Thread

    }

    public void updateProgress(int waited) {
        pb.setProgress(waited);
        pb.animate();
    }
}