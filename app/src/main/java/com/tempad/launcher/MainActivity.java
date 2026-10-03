package com.tempad.launcher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    private Handler timerHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.parseColor("#08090C"));
        root.setPadding(24, 30, 24, 20);

        LinearLayout headerBox = new LinearLayout(this);
        headerBox.setOrientation(LinearLayout.HORIZONTAL);
        headerBox.setGravity(Gravity.CENTER_VERTICAL);
        headerBox.setPadding(16, 16, 16, 16);
        headerBox.setBackgroundColor(Color.parseColor("#15181E"));

        TextView missMinutes = new TextView(this);
        missMinutes.setText("⏱️");
        missMinutes.setTextSize(36);
        missMinutes.setGravity(Gravity.CENTER);
        
        ScaleAnimation pulse = new ScaleAnimation(0.9f, 1.1f, 0.9f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        pulse.setDuration(800);
        pulse.setRepeatMode(Animation.REVERSE);
        pulse.setRepeatCount(Animation.INFINITE);
        missMinutes.startAnimation(pulse);
        headerBox.addView(missMinutes);

        LinearLayout consoleText = new LinearLayout(this);
        consoleText.setOrientation(LinearLayout.VERTICAL);
        consoleText.setPadding(20, 0, 0, 0);

        TextView tvaTitle = new TextView(this);
        tvaTitle.setText("TVA TEMPAD // MARK-21");
        tvaTitle.setTextColor(Color.parseColor("#FF9900"));
        tvaTitle.setTextSize(16);
        tvaTitle.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        consoleText.addView(tvaTitle);

        TextView timelineStatus = new TextView(this);
        timelineStatus.setTextColor(Color.parseColor("#00FF66"));
        timelineStatus.setTextSize(12);
        timelineStatus.setTypeface(Typeface.MONOSPACE);
        consoleText.addView(timelineStatus);
        headerBox.addView(consoleText);

        root.addView(headerBox);

        Runnable clockRunnable = new Runnable() {
            @Override
            public void run() {
                String time = new SimpleDateFormat("yyyy.MM.dd 'BRANCH' HH:mm:ss", Locale.US).format(new Date());
                timelineStatus.setText("SACRED TIMELINE: " + time);
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.post(clockRunnable);
