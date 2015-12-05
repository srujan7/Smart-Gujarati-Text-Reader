package example.srujan.com.gujaratitextreader;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class FloatingFaceBubbleService extends Service {
    private WindowManager windowManager;
    private ImageView floatingFaceBubble;
    boolean isShowingPopup = false;
    ClipboardManager clipboard;
    SharedPreferences preferences;


    public void onCreate() {
        super.onCreate();
        floatingFaceBubble = new ImageView(this);
        floatingFaceBubble.setImageResource(R.drawable.floatingbutton);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        //a face floating bubble as imageView

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //here is all the science of params
        final LayoutParams myParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_PHONE,
                LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x = 0;
        myParams.y = 200;
        myParams.height = 80;
        myParams.width = 80;
        // add a floatingfacebubble icon in window
        windowManager.addView(floatingFaceBubble, myParams);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getBaseContext(),"Trying to stopSelf",Toast.LENGTH_SHORT).show();
                stop(floatingFaceBubble);
            }
        }, 5000);

        try {
            //for moving the picture on touch and slide
            floatingFaceBubble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (preferences.getBoolean("launchApp", false)) {
                        Log.d("TAG A", "launchApp block");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        windowManager.removeViewImmediate(floatingFaceBubble);
                        stopSelf();
                    } else {

                        LinearLayout layoutOfPopup;
                        LinearLayout buttonContainer;
                        final PopupWindow popupMessage;
                        Button insidePopupButton, insidePopupButton2;
                        TextView popupText;
                        ImageView divider = new ImageView(getBaseContext());

                        insidePopupButton = new Button(getBaseContext());
                        insidePopupButton2 = new Button(getBaseContext());
                        popupText = new TextView(getBaseContext());
                        layoutOfPopup = new LinearLayout(getBaseContext());
                        buttonContainer = new LinearLayout(getBaseContext());

                        insidePopupButton.setText("OK");
                        insidePopupButton2.setText("Read full");
                        LinearLayout.LayoutParams lp =
                                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2);
                        lp.setMargins(10, 10, 10, 10);
                        divider.setLayoutParams(lp);
                        divider.setBackgroundColor(Color.WHITE);
                        Typeface tf = Typeface.createFromAsset(getAssets(), "shruti.ttf");
                        popupText.setTypeface(tf);
                        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        String s = "";
                        s = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                        popupText.setText(s);
                        popupText.setTextColor(Color.parseColor("#ffffff"));
//                    popupText.setText("This is Popup Window.press OK to dismiss it.");
                        popupText.setPadding(0, 0, 0, 20);
                        layoutOfPopup.setOrientation(LinearLayout.VERTICAL);
                        buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
                        layoutOfPopup.setBackgroundColor(Color.parseColor("#dd3F51B5"));
                        layoutOfPopup.setPadding(16, 16, 16, 0);
                        popupText.setPadding(0, 8, 0, 0);
                        buttonContainer.addView(insidePopupButton);
                        buttonContainer.addView(insidePopupButton2);
                        buttonContainer.setWeightSum(3);
                        insidePopupButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                        insidePopupButton2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2.0f));
                        layoutOfPopup.addView(buttonContainer);
                        layoutOfPopup.addView(divider);
                        layoutOfPopup.addView(popupText);

//                    popupMessage = new PopupWindow(layoutOfPopup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
                        Display display = wm.getDefaultDisplay();
                        Point size = new Point();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            display.getSize(size);
//                        int height = ViewGroup.LayoutParams.WRAP_CONTENT > size.y ? size.y : ViewGroup.LayoutParams.WRAP_CONTENT;
                            popupMessage = new PopupWindow(layoutOfPopup, size.x - 40, ViewGroup.LayoutParams.WRAP_CONTENT);
                        } else
                            popupMessage = new PopupWindow(layoutOfPopup, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        popupMessage.setContentView(layoutOfPopup);
                        if (!isShowingPopup) {
                            popupMessage.showAsDropDown(floatingFaceBubble, 20, 0);
                            isShowingPopup = true;
                        } else {
                            popupMessage.dismiss();
//                        isShowingPopup = false;
                        }

                        insidePopupButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupMessage.dismiss();
                                isShowingPopup = false;
                                if (preferences.getBoolean("isDemoRunning", true)) {
                                    editor.putBoolean("isDemoRunning", false);
                                    editor.apply();
                                }
                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        stop(floatingFaceBubble);
                                    }
                                }, 2000);
                            }
                        });
                        insidePopupButton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                windowManager.removeViewImmediate(floatingFaceBubble);
                                stopSelf();
                            }
                        });

                    }
                }
        });
        floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {
            LayoutParams paramsT = myParams;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private long touchStartTime = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //remove face bubble on long press
//                    Toast.makeText(getBaseContext(),"move",Toast.LENGTH_SHORT).show();
                if (System.currentTimeMillis() - touchStartTime > ViewConfiguration.getLongPressTimeout() && initialTouchX == event.getX()) {
                    windowManager.removeView(floatingFaceBubble);
                    stopSelf();
                    return false;

                }
                switch (event.getAction()) {


                    case MotionEvent.ACTION_DOWN:
                        touchStartTime = System.currentTimeMillis();
                        initialX = myParams.x;
                        initialY = myParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:

                        myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(v, myParams);
                        break;
                }
                return false;
            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void stop(final ImageView floatingFaceBubble) {
//        Toast.makeText(this,"Stop executed",Toast.LENGTH_SHORT).show();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!isShowingPopup && !(preferences.getBoolean("isDemoRunning",false))) {
            try {
//                TranslateAnimation anim = new TranslateAnimation(floatingFaceBubble.getX(),-100,floatingFaceBubble.getY(),floatingFaceBubble.getY());
//                anim.setDuration(5000);
//                anim.setFillAfter(true);
//                floatingFaceBubble.startAnimation(anim);
                windowManager.removeView(floatingFaceBubble);
            } catch (Exception e) {
            }
            stopSelf();
        }
//        else{
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    stop(floatingFaceBubble);
//                }
//            },5000);
//        }
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(getBaseContext(),"Serice stopped",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}