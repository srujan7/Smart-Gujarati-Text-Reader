package example.srujan.com.gujaratitextreader;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Tutorial extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        final SharedPreferences.Editor editor = preferences.edit();
        /*
        ImageView imageView = (ImageView) findViewById(R.id.testservice);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("isDemoRunning", true);
                editor.apply();
                AlertDialog.Builder dialog = new AlertDialog.Builder(Tutorial.this);
                dialog.setCancelable(false);
                dialog.setTitle("Copy text");
                final String msg = "Below is the text in Gujarati.\n" +
                        "તમે ગુજરાતી સંદેશ વાંચવમા સફળ રહ્યા છો. આ રીતે કોઈ પણ અપ્લિકેશન મા ગુજરાતી સંદેશ કૉપી કરી વાંચી શકશો. હૅપી રીડિંગ." +
                        "\nCan't read it?\nCopy to read it.";
                dialog.setMessage(msg);
                dialog.setPositiveButton("Copy text", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(Tutorial.this,"<-- Click on the icon on the left",Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL,0,200);
                                toast.show();
                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                clipboardManager.setText(msg);
                            }
                        }, 500);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });*/
    }

    @Override
    protected void onDestroy() {
//        Toast.makeText(this,"Tutorial destroyed",Toast.LENGTH_SHORT).show();
//        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean("isDemoRunning",false);
//        editor.apply();
//        stopService(new Intent(getBaseContext(), FloatingFaceBubbleService.class));
        super.onDestroy();
    }

    public void openExample(View view) {
        Intent intent = new Intent(Tutorial.this,Example.class);
        startActivity(intent);
        finish();
    }
}
