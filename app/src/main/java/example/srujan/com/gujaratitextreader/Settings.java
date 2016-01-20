package example.srujan.com.gujaratitextreader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    Boolean launchApp;
    Boolean runForeground;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    RadioButton a1;
    RadioButton a2;
    RadioButton b1;
    RadioButton b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        launchApp = preferences.getBoolean("launchApp",false);
        runForeground = preferences.getBoolean("runForeground",false);

        a1 = (RadioButton) findViewById(R.id.a1);
        a2 = (RadioButton) findViewById(R.id.a2);
        b1 = (RadioButton) findViewById(R.id.b1);
        b2 = (RadioButton) findViewById(R.id.b2);
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a2.setChecked(false);
            }
        });
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a1.setChecked(false);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b2.setChecked(false);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1.setChecked(false);

            }
        });

        if(launchApp){
            b2.setChecked(true);
        }else{
            b1.setChecked(true);
        }
        if (runForeground){
            a1.setChecked(true);
        }else{
            a2.setChecked(true);
        }
    }

    public void helpClick(View view) {
        int id = view.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if(id == R.id.help1){
            builder.setTitle("Foreground service");
            builder.setMessage("Some people wrote to us that they had to click on 'Start service' too often. (2-3 time a day) " +
                    "If you are facing the same issue, use Foreground service.");
        }
        if(id == R.id.help2){
            builder.setTitle("Background service");
            builder.setMessage("This is the default service. If you do not need to use the Foreground service, use this.");
        }
        if(id == R.id.help3){
            builder.setTitle("Open popup");
            builder.setMessage("Whenever you copy a Gujarati message, you see an icon on the top left corner. On clicking that icon, " +
                    "a popup message will appear which will contain that Gujarati message. On some new Android device, this popup does not show up. " +
                    "If you are unable to see that popup, " +
                    "you should select the 'Launch app' option. ");
        }
        if(id == R.id.help4){
            builder.setTitle("Launch app");
            builder.setMessage("Whenever you copy a Gujarati message, you see an icon on the top left corner. On clicking that icon, " +
                    "a popup message will appear which will contain that Gujarati message. If you are unable to see that popup, " +
                    "you should select this 'Launch app' option. This will open the app directly where you can read your Gujarati message ");
        }
        builder.show();
    }

    void closeActivity(){
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        if(!(launchApp == b2.isChecked() && runForeground == a1.isChecked())){
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Save?");
            builder.setMessage("Do you want to save the changes?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.putBoolean("launchApp", b2.isChecked());
                    editor.putBoolean("runForeground", a1.isChecked());
                    editor.commit();
                    startService(new Intent(getBaseContext(), CBListnerService.class));
                    closeActivity();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    closeActivity();
                }
            });
            builder.show();
        }else {
            super.onBackPressed();
        }
    }
}
