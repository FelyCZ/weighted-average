package cz.fely.weightedaverage.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cz.fely.weightedaverage.services.UpdateService;


public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, UpdateService.class);
        if(intent.getAction().compareTo(Intent.ACTION_BOOT_COMPLETED) == 0) {
            Log.d("Receiver: ", "Boot Completed");
            context.startService(service);
        }
        else if(intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0){
            Log.d("Receiver: ", "TimeTick");
            context.startService(service);
        }

    }
}
