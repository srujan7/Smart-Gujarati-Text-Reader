package example.srujan.com.gujaratitextreader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Survey extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    int questionNum = 0;
    final static String[] questionArray = {"Did you understand how this app works?",
            "Did you go to any app and COPIED any Gujarati text?",
            "Were you able to see an icon like this?",
            "When you clicked on that icon, were you able to see a blue colour popup window like this?",
            "Seems everything is working fine, have some other problem?",
            "Many times the icon does not show up, and you have to click 'Start service' to start it again?",
            "Your problem isn't in our list.\nWould you like to contact us?"};
    final static String[] noteArray = {"Click 'No' to see the tutorial.",
            "You need to copy the Gujarati message if you want to read it. See tutorial",
            "A blue color icon under the top-left corner of your screen will appear when you copy a message",
            "null",
            "null",
            "Click yes if you have to click on 'Start service' too often",
            "Always there to solve your issues. Let us know",};
    final static String[] imageArray = {"null",
            "null",
            "icon_screenshot",
            "popup_screenshot",
            "null",
            "null",
            "null",};
    final static String[] button1 = {"Yes",
            "Yes",
            "Yes",
            "Yes",
            "Yes",
            "Yes",
            "Yes",};
    final static String[] button2 = {"No",
            "No",
            "No",
            "No",
            "No",
            "No",
            "No",};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        generateQuestion();
    }

    private void generateQuestion() {
        TextView questionNumCount = (TextView) findViewById(R.id.questionNumCount);
        TextView question = (TextView) findViewById(R.id.question);
        TextView note = (TextView) findViewById(R.id.note);
        ImageView image = (ImageView) findViewById(R.id.image1);
        Button but1 = (Button) findViewById(R.id.but1);
        Button but2 = (Button) findViewById(R.id.but2);

        //set QuestionNum
        questionNumCount.setText(questionNum+1+"/"+questionArray.length);
        //Set Question
        question.setText(questionArray[questionNum]);
        //Set Note
        if(!(noteArray[questionNum].equals("null"))){
            note.setText(noteArray[questionNum]);
        }else{
            note.setText("");
        }
        //Set Image (If any)
        if(!(imageArray[questionNum].equals("null"))){
            int resId = getResources().getIdentifier(imageArray[questionNum],"drawable",getPackageName());
            image.setImageResource(resId);
        }else{
            image.setImageDrawable(null);
        }
        //Set Buttons
        but1.setText(button1[questionNum]);
        but2.setText(button2[questionNum]);

    }

    public void onBClick(View view) {
        Boolean bool = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tip");
        builder.setCancelable(true);
        if(view.getId() == R.id.but1) bool = true;
        switch(questionNum){
            case 0:
                if (bool){
                    questionNum++;
                    generateQuestion();
                }else{
                    builder.setMessage("Click below to see the tutorial on how this app works.");
                    builder.setPositiveButton("Tutorial", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Survey.this, Tutorial.class));
                        }
                    });
                    builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                break;
            case 1:
                if (bool){
                    questionNum++;
                    generateQuestion();
                }else{
                    builder.setMessage("Open Whatsapp, or any other application, Find a gujarati message, select it and copy it.");
                    builder.setPositiveButton("Open Whatsapp", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Go home", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                        }
                    });
                    builder.show();
                }
                break;
            case 2:
                if (bool){
                    questionNum++;
                    generateQuestion();
                }else{
                    builder.setTitle("Tip");
                    builder.setMessage("Go to main page of this app, click on 'Start service' and try it again. " +
                            "If the problem persists contact us from the about page.");
                    builder.setPositiveButton("App home", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.setNegativeButton("Contact us", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Survey.this, About.class));
                        }
                    });
                    builder.show();
                }
                break;
            case 3:
                if (bool){
                    questionNum++;
                    generateQuestion();
                }else{
                    builder.setTitle("Oops");
                    builder.setMessage("We have noticed that Android phones running Android version 5.0+ are facing this issue. " +
                            "BUT, we can solve this issue by making necessary changes. " +
                            "You can revert the changes by going to 'Settings' menu.");
                    builder.setPositiveButton("Make necessary changes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putBoolean("launchApp", true);
                            editor.apply();
                            finish();
                            Toast.makeText(getBaseContext(), "Changes Applied.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
                break;
            case 4:
                if (bool){
                    questionNum++;
                    generateQuestion();
                }else{
                    builder.setTitle("Great");
                    builder.setMessage("For any queries, suggestions or words of appreciation, " +
                            "contact us as from the 'About' page.");
                    builder.setPositiveButton("About page", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Survey.this, About.class));
                            finish();
                        }
                    });
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });
                    }
                    builder.show();
                }
                break;
            case 5:
                if (bool){
                    builder.setTitle("Oops");
                    builder.setMessage("If you have the problem of starting the service every time you want to read read a Gujarati text, " +
                            "you can go to settings page and change the type of service to 'foreground service'. " +
                            "This will solve the problem to many extent. ");
                    builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Survey.this,Settings.class));
                        }
                    });
                    builder.show();
                }else{
                    questionNum++;
                    generateQuestion();
                }
                break;
            case 6:
                if (bool){
                    startActivity(new Intent(Survey.this,About.class));
                }else{
                    builder.setTitle("Help?");
                    builder.setMessage("Let us know if you are facing any other problems.");
                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.setNegativeButton("Retake survey", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Survey.this,Survey.class));
                            finish();
                        }
                    });
                    builder.show();
                }
        }
    }
}
