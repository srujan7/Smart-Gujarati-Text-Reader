package example.srujan.com.gujaratitextreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class CheckPopup extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_popup);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert");
            builder.setMessage("This test is only for the devices running Android version 5.0+. " +
                    "This app runs perfectly on other devices.");
            builder.setCancelable(false);
            builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
        }

        loading = (TextView) findViewById(R.id.loading);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    private void showTestPopup() {
        LinearLayout layoutOfPopup = new LinearLayout(getBaseContext());
        final PopupWindow popupMessage;
        TextView popupText = new TextView(getBaseContext());
        Button button = new Button(getBaseContext());
        final ViewGroup mRootView = (ViewGroup) findViewById(R.id.Lin).getRootView();
        String s = "જો તમે આ મેસેજા વાંચી રહ્યા છો તો, congratulations, " +
                "તમારો ફોન popup મેસેજ સપોર્ટ કરે છે. નીચેનું બટન દબાવી home જઇ શકશો.";

        button.setText("Dismiss");
        Typeface tf = Typeface.createFromAsset(getAssets(),"shruti.ttf");
        popupText.setTypeface(tf);
        popupText.setText(s);
        popupText.setTextSize(24);
        popupText.setTextColor(Color.parseColor("#ffffff"));
        popupText.setPadding(16, 16, 16, 20);
        popupText.setGravity(Gravity.CENTER);
        layoutOfPopup.setOrientation(LinearLayout.VERTICAL);
        layoutOfPopup.setBackgroundColor(Color.parseColor("#ee3F51B5"));
        layoutOfPopup.setGravity(Gravity.CENTER);
        layoutOfPopup.setPadding(16, 16, 16, 0);
        popupText.setPadding(0, 8, 0, 0);

        button.setPadding(16,16,16,16);

        layoutOfPopup.addView(popupText);
        layoutOfPopup.addView(button);

//        WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        popupMessage = new PopupWindow(layoutOfPopup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupMessage.setContentView(layoutOfPopup);

        popupMessage.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("testTaken", true);
                editor.putBoolean("supportsPopup",true);
                editor.apply();
//                finish();
                popupMessage.dismiss();
            }
        });
    }

    public void beginTest(View view) {
//        loading.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.VISIBLE);
                showLoading(0);
            }
        }, 5);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(CheckPopup.this, "Please complete the test.", Toast.LENGTH_LONG).show();
    }

    private void showLoading(int i) {
        final int j = i+1;
        switch (i){
            case 0:
                loading.setText("Testing.");
                break;
            case 1:
                loading.setText("Testing..");
                break;
            case 2:
                loading.setText("Loading.");
                break;
            case 3:
                loading.setText("Loading..");
                break;
            case 4:
                loading.setText("Generating preview.");
                break;
            case 5:
                loading.setText("Generating preview..");
                break;
            case 6:
                loading.setText("Generating preview...");
                break;
            case 7:
                loading.setVisibility(View.GONE);
                showTestPopup();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView) findViewById(R.id.sorryMessage);
                        textView.setVisibility(View.VISIBLE);
                        Typeface tf = Typeface.createFromAsset(getAssets(),"shruti.ttf");
                        textView.setTypeface(tf);
                        /*textView.setText("If you are reading this message, then your phone does NOT support popup messages. " +
                                "But do not worry, you can still use this app by making necessary changes. " +
                                "Click below button to make changes automatically.");*/
                        textView.setText("જો તમે આ મેસેજા વાંચી રહ્યા છો તેનો અર્થ, તમારો ફોન popup મેસેજ સપોર્ટ કરતો નથી. " +
                                "તેમ છતા પણ તમે ગુજરાતી મેસેજ વાંચવા માટે આ app નો ઉપિયોગ કરી શકશો. " +
                                "તેવુ કરવા app મા થોડા બદલાવ કરવા પડશે. ઑટોમૅટિક બદલાવ કરવા નીચેનું 'Make changes' બટન દબાઓ. ");
                        Button button = (Button) findViewById(R.id.makeChanges);
                        Button button2 = (Button) findViewById(R.id.info);
                        button.setVisibility(View.VISIBLE);
                        button2.setVisibility(View.VISIBLE);
                    }
                },400);
                break;
        }
        if (i!=7){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showLoading(j);
                }
            },500);
        }
    }

    public void makeChanges(View view) {
        Toast.makeText(CheckPopup.this, "Changes saved", Toast.LENGTH_LONG).show();
        editor.putBoolean("launchApp", true);
        editor.putBoolean("testTaken",true);
        editor.putBoolean("supportsPopup",false);
        editor.apply();
        finish();
    }

    public void info(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Changes:");
        builder.setMessage("Now that your phone is not supporting popup messages, " +
                "you will be able to read the Guajrati message withing the app itself. " +
                "Everything else will remain same.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
