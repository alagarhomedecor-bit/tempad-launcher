package com.tempad.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.*;
import android.os.*;
import android.speech.tts.TextToSpeech;
import android.view.*;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    private Handler handler = new Handler(Looper.getMainLooper());
    private TextToSpeech tts;
    private TextView timelineClock, consoleLog;
    private TimelineGraphView graphView;
    private MissMinutesCanvas missMinutesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
                tts.setPitch(1.35f);
                tts.setSpeechRate(0.92f);
                tts.speak("Hey y'all! Welcome to the TVA TemPad Mark 21. Sacred timeline monitored and locked.", TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        ScrollView scroller = new ScrollView(this);
        scroller.setBackgroundColor(Color.parseColor("#080705"));
        scroller.setFillViewport(true);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20, 24, 20, 24);
        scroller.addView(root);

        LinearLayout topBar = new LinearLayout(this);
        topBar.setOrientation(LinearLayout.HORIZONTAL);
        topBar.setBackgroundColor(Color.parseColor("#17120A"));
        topBar.setPadding(16, 12, 16, 12);

        TextView sysTitle = new TextView(this);
        sysTitle.setText("TVA // TEMPAD MK-IV");
        sysTitle.setTextColor(Color.parseColor("#FF9900"));
        sysTitle.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        sysTitle.setTextSize(14);
        topBar.addView(sysTitle);

        timelineClock = new TextView(this);
        timelineClock.setTextColor(Color.parseColor("#FFB833"));
        timelineClock.setTypeface(Typeface.MONOSPACE);
        timelineClock.setTextSize(11);
        timelineClock.setGravity(Gravity.END);
        LinearLayout.LayoutParams clkParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        timelineClock.setLayoutParams(clkParams);
        topBar.addView(timelineClock);
        root.addView(topBar);

        LinearLayout assistantRow = new LinearLayout(this);
        assistantRow.setOrientation(LinearLayout.HORIZONTAL);
        assistantRow.setBackgroundColor(Color.parseColor("#120E08"));
        assistantRow.setPadding(16, 16, 16, 16);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowParams.topMargin = 16;
        assistantRow.setLayoutParams(rowParams);

        missMinutesView = new MissMinutesCanvas(this);
        LinearLayout.LayoutParams mmParams = new LinearLayout.LayoutParams(160, 160);
        missMinutesView.setLayoutParams(mmParams);
        assistantRow.addView(missMinutesView);

        LinearLayout logPanel = new LinearLayout(this);
        logPanel.setOrientation(LinearLayout.VERTICAL);
        logPanel.setPadding(20, 0, 0, 0);

        TextView variantNotice = new TextView(this);
        variantNotice.setText("STATUS: TIMELINE NOMINAL");
        variantNotice.setTextColor(Color.parseColor("#00FF66"));
        variantNotice.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        variantNotice.setTextSize(12);
        logPanel.addView(variantNotice);

        consoleLog = new TextView(this);
        consoleLog.setText("Hey y'all! Don't let branches diverge. Tap me for briefing.");
        consoleLog.setTextColor(Color.parseColor("#D9822B"));
        consoleLog.setTypeface(Typeface.MONOSPACE);
        consoleLog.setTextSize(11);
        consoleLog.setPadding(0, 6, 0, 0);
        logPanel.addView(consoleLog);
        assistantRow.addView(logPanel);

        assistantRow.setOnClickListener(v -> {
            if (tts != null) {
                tts.speak("Temporal variance verified. All branches pruned under protocol 21. Stay sharp, Ram!", TextToSpeech.QUEUE_FLUSH, null, null);
                consoleLog.setText("Miss Minutes: Temporal variance verified. All protocols stable.");
            }
        });
        root.addView(assistantRow);

        TextView graphHeader = new TextView(this);
        graphHeader.setText("--- TIMELINE DISPERSION MONITOR ---");
        graphHeader.setTextColor(Color.parseColor("#805315"));
        graphHeader.setTypeface(Typeface.MONOSPACE);
        graphHeader.setTextSize(10);
        graphHeader.setPadding(0, 16, 0, 6);
        root.addView(graphHeader);

        graphView = new TimelineGraphView(this);
        LinearLayout.LayoutParams gParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        graphView.setLayoutParams(gParams);
        root.addView(graphView);

        TextView appHeader = new TextView(this);
        appHeader.setText("--- SACRED GATEWAYS (TIME DOORS) ---");
        appHeader.setTextColor(Color.parseColor("#805315"));
        appHeader.setTypeface(Typeface.MONOSPACE);
        appHeader.setTextSize(10);
        appHeader.setPadding(0, 16, 0, 10);
        root.addView(appHeader);

        GridView grid = new GridView(this);
        grid.setNumColumns(4);
        grid.setVerticalSpacing(12);
        grid.setHorizontalSpacing(12);
        grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        LinearLayout.LayoutParams gridParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800);
        grid.setLayoutParams(gridParams);

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);

        grid.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() { return pkgAppsList.size(); }
            @Override
            public Object getItem(int position) { return pkgAppsList.get(position); }
            @Override
            public long getItemId(int position) { return position; }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LinearLayout card = new LinearLayout(MainActivity.this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setGravity(Gravity.CENTER);
                card.setPadding(6, 12, 6, 12);
                card.setBackgroundColor(Color.parseColor("#15110B"));

                ImageView icon = new ImageView(MainActivity.this);
                icon.setImageDrawable(pkgAppsList.get(position).loadIcon(getPackageManager()));
                icon.setLayoutParams(new LinearLayout.LayoutParams(80, 80));

                TextView label = new TextView(MainActivity.this);
                label.setText(pkgAppsList.get(position).loadLabel(getPackageManager()));
                label.setTextColor(Color.parseColor("#E68A00"));
                label.setTextSize(9);
                label.setTypeface(Typeface.MONOSPACE);
                label.setSingleLine(true);
                label.setGravity(Gravity.CENTER);
                label.setPadding(0, 6, 0, 0);

                card.addView(icon);
                card.addView(label);
                return card;
            }
        });

        grid.setOnItemClickListener((parent, view, position, id) -> {
            ResolveInfo info = pkgAppsList.get(position);
            Intent launch = getPackageManager().getLaunchIntentForPackage(info.activityInfo.packageName);
            if (launch != null) startActivity(launch);
        });
        root.addView(grid);

        handler.post(new Runnable() {
            @Override
            public void run() {
                String time = new SimpleDateFormat("yyyy.MM.dd // HH:mm:ss", Locale.US).format(new Date());
                timelineClock.setText(time);
                graphView.invalidate();
                handler.postDelayed(this, 1000);
            }
        });

        setContentView(scroller);
    }

    static class MissMinutesCanvas extends View {
        private Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        public MissMinutesCanvas(Context ctx) { super(ctx); }

        @Override
        protected void onDraw(Canvas c) {
            super.onDraw(c);
            float cx = getWidth() / 2f, cy = getHeight() / 2f, r = Math.min(cx, cy) - 10;
            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.parseColor("#FF8C00"));
            c.drawCircle(cx, cy, r, p);

            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.parseColor("#4A2800"));
            p.setStrokeWidth(6);
            c.drawCircle(cx, cy, r, p);

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.parseColor("#261400"));
            c.drawCircle(cx - 15, cy - 10, 6, p);
            c.drawCircle(cx + 15, cy - 10, 6, p);

            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(4);
            RectF mouth = new RectF(cx - 16, cy - 5, cx + 16, cy + 18);
            c.drawArc(mouth, 0, 180, false, p);
        }
    }

    static class TimelineGraphView extends View {
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Path path = new Path();
        private Random rnd = new Random();

        public TimelineGraphView(Context ctx) {
            super(ctx);
            setBackgroundColor(Color.parseColor("#120E08"));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paint.setColor(Color.parseColor("#FF9900"));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);

            path.reset();
            float w = getWidth(), h = getHeight();
            path.moveTo(0, h / 2f);

            for (float x = 0; x <= w; x += 30) {
                float y = (h / 2f) + (rnd.nextFloat() * 40f - 20f);
                path.lineTo(x, y);
            }
            canvas.drawPath(path, paint);
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
