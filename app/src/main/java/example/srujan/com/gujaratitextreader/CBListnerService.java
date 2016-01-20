package example.srujan.com.gujaratitextreader;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Srujan on 11-11-2015.
 */
public class CBListnerService extends Service {

    int NOTIFICATION_ID = 101;
    int REQUEST_CODE = 1;

    ClipboardManager clipboard;
    SharedPreferences preferences;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (preferences.getBoolean("runForeground", false)) {
            startForeground(NOTIFICATION_ID, getNotification());
        } else {
            stopForeground(true);
        }

        if (preferences.getBoolean("serviceOn", false)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startService(new Intent(getBaseContext(), CBListnerService.class));
                }
            }, 3000);
        } else stopSelf();
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                if (preferences.getBoolean("serviceOn", true)) {
                    openFAB();
                } else {
                    stopSelf();
                }
            }
        });
        return START_STICKY;
    }

    private Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.notification_icon)
                .setContentTitle("Smart Gujarati Text Reader")
                .setTicker("Service started")
                .setContentText("Click to open the app.")
                .setWhen(System.currentTimeMillis());
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.notification_icon);
        builder.setLargeIcon(bm);
        Intent startIntent = new Intent(getApplicationContext(),
                MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, REQUEST_CODE, startIntent, 0);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        return notification;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void openFAB() {
        startService(new Intent(getBaseContext(), FloatingFaceBubbleService.class));
    }
}
