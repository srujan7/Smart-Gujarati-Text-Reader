package example.srujan.com.gujaratitextreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.madx.updatechecker.lib.UpdateRunnable;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView shareImage = (ImageView) findViewById(R.id.shareImage);
        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });

        ImageView whatssappicon = (ImageView) findViewById(R.id.whatsappicon);
        whatssappicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsappCM();
            }
        });

        ImageView gmailicon = (ImageView) findViewById(R.id.gmailicon);
        gmailicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmailCM();
            }
        });

        TextView version = (TextView) findViewById(R.id.appVersion);
        version.setText("Current Version: "+ getResources().getString(R.string.versionName));

    }

    private void gmailCM() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "srujanbarai75@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Smart Gujarati text reader. (Version "+ getResources().getString(R.string.versionName) +")");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send email via"));
        }catch (Exception e){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "srujanbarai75@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Smart Gujarati text reader (Version " + getResources().getString(R.string.versionName) + ")");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            String[] addresses = {"srujanbarai75@gmail.com"};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses); // String[] addresses
            startActivity(Intent.createChooser(emailIntent, "Send email via"));
        }
    }

    private void whatsappCM() {
        Uri mUri = Uri.parse("smsto:+919429097971");
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
        mIntent.setPackage("com.whatsapp");
        mIntent.putExtra(Intent.EXTRA_SUBJECT, "The text goes here");
        mIntent.putExtra("chat",true);
        startActivity(mIntent);
    }

    private void shareIt() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String string = "Hey, not able to read Gujarati text in phone? Download \"Smart Gujarati Reader\" FREE app and read gujarati messages without closing any application, smartly. (Size: 2MB)"
                + " https://play.google.com/store/apps/details?id=example.srujan.com.gujaratitextreader";
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,string);
        startActivity(Intent.createChooser(shareIntent,"Share via"));
    }

    public void checkUpdate(View view) {
        new UpdateRunnable(this, new Handler()).force(true).start();
    }
}
