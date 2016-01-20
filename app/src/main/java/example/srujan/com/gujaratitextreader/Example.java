package example.srujan.com.gujaratitextreader;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class Example extends AppCompatActivity {

    Boolean textSelected = false;
    final String string = "તમે ગુજરાતી સંદેશ વાંચવમા સફળ રહ્યા છો. આ રીતે કોઈ પણ અપ્લિકેશન મા ગુજરાતી સંદેશ કૉપી કરી વાંચી શકશો. હૅપી રીડિંગ.";
    TextSwitcher mSwitcher;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        if(!preferences.getBoolean("serviceOn",false)){
            AlertDialog.Builder dialog = new AlertDialog.Builder(Example.this);
            dialog.setTitle("Alert");
            dialog.setMessage("Service is off, Switch on the service to see the example.");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Switch on", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.putBoolean("serviceOn", true);
                    editor.apply();
                    Toast.makeText(Example.this, "Service started", Toast.LENGTH_SHORT).show();
                    startService(new Intent(getBaseContext(), CBListnerService.class));
                }
            });
            dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.show();
        }

        Button button = (Button) findViewById(R.id.copyText);
        button.setTextColor(Color.parseColor("#44000000"));

        EditText text = (EditText) findViewById(R.id.editText);
        text.setText(string);
        text.setTextSize(25);

        mSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);

        mSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
//                TextView myText = (TextView) findViewById(R.id.textView3);
                TextView myText = new TextView(Example.this);
                myText.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(36);
                myText.setTextColor(Color.BLUE);
                return myText;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);

        mSwitcher.setText("Click \"Select text\"");
    }

    public void buttonClick(View view) {
        if(view.getId() == R.id.selectText) {
            EditText editText = (EditText) findViewById(R.id.editText);
            editText.selectAll();
            Button button = (Button) findViewById(R.id.copyText);
            button.setTextColor(Color.parseColor("#000000"));
            textSelected = true;
            mSwitcher.setText("Now copy text.");
        }
        else if(view.getId() == R.id.copyText){
            if(textSelected){
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setText(string);
                mSwitcher.setText("Click on the icon.");
                preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isDemoRunning", true);
                editor.apply();
                Toast toast = Toast.makeText(Example.this, "<-- Click on the icon on the left", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL, 0, 200);
                toast.show();
                clipboardManager.setText("તમે ગુજરાતી સંદેશ વાંચવમા સફળ રહ્યા છો. આ રીતે કોઈ પણ અપ્લિકેશન મા ગુજરાતી સંદેશ કૉપી કરી વાંચી શકશો. હૅપી રીડિંગ.");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
                        textSwitcher.setVisibility(View.GONE);
                        TextView view1 = (TextView) findViewById(R.id.confirmation);
                        view1.setVisibility(View.VISIBLE);
                        LinearLayout layout = (LinearLayout) findViewById(R.id.buttonContainer2);
                        layout.setVisibility(View.VISIBLE);
                    }
                },5000);
            }
            else{
//                Toast.makeText(this,"First select text",Toast.LENGTH_SHORT).show();
                mSwitcher.setText("First select text.");
            }
        }
        else if(view.getId() == R.id.Yes){
            Toast.makeText(Example.this, "This app is working with your device. Happy reading", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.No){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Oops");
            dialog.setMessage("Seems this app is not working perfectly with your device. " +
                    "To compensate, this app will be launched instead of popup whenever you click on the icon. " +
                    "Make necessary changes?");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("launchApp", true);
                    editor.putBoolean("isDemoRunning",false);
                    editor.apply();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            dialog.setNegativeButton("Try once again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("launchApp",false);
                    editor.apply();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

    }

    @Override
    public void onBackPressed() {
//        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        if(preferences.getBoolean("isDemoRunning",false)){
//            Toast.makeText(this,"Press OK to complete the tutorial",Toast.LENGTH_LONG).show();
//        }else {
            super.onBackPressed();
//        }
    }

    @Override
    protected void onDestroy() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isDemoRunning",false);
        editor.apply();
        super.onDestroy();
    }
}
