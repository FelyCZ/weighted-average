package cz.fely.weightedaverage;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import net.hockeyapp.android.LoginManager;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.UpdateManagerListener;

public class UpdateActivity extends AppCompatActivity {
    private Handler handlerTimer;
    private Runnable updateHandler;

  /*  public UpdateActivity(){
        LoginManager.register(this, "2fccbcc477da9ab6b058daf97571ac77", LoginManager
                .LOGIN_MODE_ANONYMOUS);

        UpdateManager.register(this, "a6f2b12acd1a4e22a763dab9a356879f", listener);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        handlerTimer = new Handler();
        updateHandler = new UpdateHandler(handlerTimer, this);
        handlerTimer.removeCallbacks(updateHandler);
        Log.w("UpdateActivity: ", "onCreate");
        Toast.makeText(this, "T", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onPause() {
        handlerTimer.removeCallbacks(updateHandler);
        super.onPause();
    }

    @Override
    protected void onResume() {
        handlerTimer.postDelayed(updateHandler, 2000);
        super.onResume();
    }
}