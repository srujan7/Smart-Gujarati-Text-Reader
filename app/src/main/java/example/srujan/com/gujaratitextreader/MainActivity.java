package example.srujan.com.gujaratitextreader;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.madx.updatechecker.lib.UpdateRunnable;

import org.w3c.dom.Text;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    ClipboardManager clipboard;
    float textSize;
    Runnable runnable;
    Handler zoomButtonVisibilityToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        zoomButtonVisibilityToggle = new Handler();
        setZoomButtonVisibility("VISIBLE");
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setZoomButtonVisibility("INVISIBLE");
            }
        };
        zoomButtonVisibilityToggle.postDelayed(runnable, 3000);

        Calendar now = Calendar.getInstance();
        Calendar lastAppOpenedDate = Calendar.getInstance();
        lastAppOpenedDate.setTimeInMillis(preferences.getLong("lastAppOpenedDateLong", now.getTimeInMillis()));
        if(lastAppOpenedDate.get(Calendar.WEEK_OF_YEAR ) != now.get(Calendar.WEEK_OF_YEAR)){
            Toast.makeText(MainActivity.this, "Weekly update check", Toast.LENGTH_SHORT).show();
            new UpdateRunnable(this, new Handler()).force(true).start();
        }
        editor.putLong("lastAppOpenedDateLong", now.getTimeInMillis());
        editor.apply();

        final TextView textView = (TextView) findViewById(R.id.textView);
        TextView textView1 = (TextView) findViewById(R.id.notice);
        Typeface tf = Typeface.createFromAsset(getAssets(), "shruti.ttf");
        textView.setTypeface(tf);
        textView1.setTypeface(tf);
        textSize = textView.getTextSize();
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                zoomButtonVisibilityToggle.removeCallbacks(runnable);
                setZoomButtonVisibility("VISIBLE");
                zoomButtonVisibilityToggle.postDelayed(runnable, 3000);
                return false;
            }
        });

        Button zoomIn = (Button) findViewById(R.id.zoomIn);
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize += 2.0;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        });
        Button zoomOut = (Button) findViewById(R.id.zoomOut);
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize -= 2.0;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        });

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                updateData();
            }
        });

        updateData();

        if(preferences.getBoolean("serviceOn",true)) {
            editor.putBoolean("serviceOn", true);
            editor.apply();
            startService(new Intent(getBaseContext(), CBListnerService.class));
        }
        if(preferences.getBoolean("appOpenedForFirstTime",true)){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Kem chho!");
            builder.setMessage("Welcome to Smart Gujarati Reader, before you proceed we would like to take you through a short tutorial.");
            builder.setPositiveButton("Tutorial", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(MainActivity.this, Tutorial.class));
                    editor.putBoolean("appOpenedForFirstTime", false);
                    editor.apply();
                }
            });
            builder.show();
        }
        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !(preferences.getBoolean("testTaken",false)))){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Oops");
            dialog.setMessage("Seems you are using Android 5.0 or plus. This app might not work as expected. " +
                    "Please take a small test before you proceed.");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Take test", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(MainActivity.this,CheckPopup.class));
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

    }

    private void setZoomButtonVisibility(String mode) {
        if(mode.equals("VISIBLE")){
            Button button = (Button) findViewById(R.id.zoomIn);
            button.setVisibility(View.VISIBLE);
            button = (Button) findViewById(R.id.zoomOut);
            button.setVisibility(View.VISIBLE);
        }
        else if(mode.equals("INVISIBLE")){
            Button button = (Button) findViewById(R.id.zoomIn);
            button.setVisibility(View.INVISIBLE);
            button = (Button) findViewById(R.id.zoomOut);
            button.setVisibility(View.INVISIBLE);

        }
        else if(mode.equals("GONE")){
            Button button = (Button) findViewById(R.id.zoomIn);
            button.setVisibility(View.GONE);
            button = (Button) findViewById(R.id.zoomOut);
            button.setVisibility(View.GONE);
        }
        else
            Toast.makeText(MainActivity.this, "String didnt match any criteria", Toast.LENGTH_SHORT).show();
    }

    private void updateData() {
        try {

            TextView textView = (TextView) findViewById(R.id.textView);
            clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            String s = "";
            try {
                s = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
            }catch (Exception e){
                s = (String) clipboard.getText();
            }
            if (s.length() != 0) textView.setText(s);
            else textView.setText("No text found in clipboard");
        }catch (Exception e) {}
    }


    public void buttonClick(View view) {
        if(view.getId() == R.id.button) {
            Intent intent = new Intent(this,ReadMessage.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.button2){
            Toast.makeText(this,"Service started",Toast.LENGTH_SHORT).show();
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("serviceOn", true);
            editor.apply();
            startService(new Intent(getBaseContext(), CBListnerService.class));
        }
        else if (view.getId() == R.id.button3){
            Toast.makeText(this,"Service stopped",Toast.LENGTH_SHORT).show();
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("serviceOn", false);
            editor.putBoolean("isDemoRunning",false);
            editor.apply();
            stopService(new Intent(getBaseContext(), CBListnerService.class));
        }
        else if (view.getId() == R.id.survey){
            Intent intent = new Intent(MainActivity.this,Survey.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.about){
            startActivity(new Intent(MainActivity.this,About.class));
        }
        if(id == R.id.problem){
            startActivity(new Intent(MainActivity.this,Survey.class));
        }
        if (id == R.id.test){
            startActivity(new Intent(MainActivity.this,CheckPopup.class));
        }
        if(id == R.id.example){
            startActivity(new Intent(MainActivity.this,Example.class));
        }
        if(id == R.id.settings){
            startActivity(new Intent(MainActivity.this,Settings.class));
        }
        return true;
    }
}
