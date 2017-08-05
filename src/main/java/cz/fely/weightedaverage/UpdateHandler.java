package cz.fely.weightedaverage;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;


public class UpdateHandler implements Runnable {

    private static final int THERE_IS_UPDATE = 999;
    private Handler timerHandler;
    private Handler updateEventHandler;
    private Context context;

    public UpdateHandler(Handler timerHandler, Context ctx){
        this.timerHandler = timerHandler;
        this.updateEventHandler = new UpdateEventHandler();
        this.context = ctx;
    }

    @Override
    public void run() {
        Message msg = new Message();
        msg.what = THERE_IS_UPDATE;
        updateEventHandler.sendMessage(msg);
        timerHandler.postDelayed(this, 2000
        );
    }

    @SuppressLint("HandlerLeak")
    private class UpdateEventHandler extends Handler {

        @Override
        public void handleMessage(Message msg){
            if(msg.what == THERE_IS_UPDATE) {
                PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.drawable.warning);
                mBuilder.setContentTitle("Test Update");
                mBuilder.setContentText("Text ......");
                mBuilder.setContentIntent(pi);

                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(001, mBuilder.build());
            }
        }
    }
}
