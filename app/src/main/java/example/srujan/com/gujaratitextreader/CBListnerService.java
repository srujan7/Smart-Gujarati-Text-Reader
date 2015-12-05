package example.srujan.com.gujaratitextreader;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Srujan on 11-11-2015.
 */
public class CBListnerService extends Service {

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
        if(preferences.getBoolean("serviceOn",false)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startService(new Intent(getBaseContext(), CBListnerService.class));
                }
            }, 3000);
        }
        else stopSelf();
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    if (preferences.getBoolean("serviceOn", true)) {
                        openFAB();
                    }
                    else{
                        stopSelf();
                    }
                }
            });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void openFAB() {
        startService(new Intent(getBaseContext(), FloatingFaceBubbleService.class));
    }
}
