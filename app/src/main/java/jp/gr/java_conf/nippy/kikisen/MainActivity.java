package jp.gr.java_conf.nippy.kikisen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import jp.gr.java_conf.nippy.kikisen.dialog.DirectionDialogFragment;
import jp.gr.java_conf.nippy.kikisen.dialog.DistanceDialogFragment;
import jp.gr.java_conf.nippy.kikisen.dialog.NumberDialogFragment;
import jp.gr.java_conf.nippy.kikisen.dialog.YesDialogFragment;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();

    TextView tvIP;
    EditText etSendString;
    Button btSend;
    Button btNo;
    Button btYes;
    Button btEnemy;
    Button btDirection;
    Button btDistance;
    Button btNumber;
    Button btTimerStart;
    Button btTimerEnd;
    TextView tvTimer;
    BouyomiChan4J bouyomi;
    SharedPreferences pref;

    //タイマー用の時間リスト
    final long circle_update[] = {2 * 60, 12 * 60, 17 * 60 + 40, 21 * 60 + 40, 24 * 60 + 40, 27 * 60 + 20, 29 * 60 + 20, 31 * 60 + 20}; //円更新される時間
    final long circle_shrink_start[] = {0, 7 * 60, 15 * 60 + 20, 20 * 60 + 10, 23 * 60 + 40, 26 * 60 + 40, 28 * 60 + 50, 30 * 60 + 50}; //円の縮小が開始される時間
    final long total_time = circle_update[circle_update.length - 1] - 5;//-5はカウントダウンが終わってから飛行機が動き出すまでの時間(sec)

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //UI
        tvIP = (TextView) findViewById(R.id.tvIP);
        etSendString = (EditText) findViewById(R.id.etSendString);
        btSend = (Button) findViewById(R.id.btSend);
        btNo = (Button) findViewById(R.id.btNo);
        btYes = (Button) findViewById(R.id.btYes);
        btEnemy = (Button) findViewById(R.id.btEnemy);
        btDirection = (Button) findViewById(R.id.btDirection);
        btDistance = (Button) findViewById(R.id.btDistance);
        btNumber = (Button) findViewById(R.id.btNumber);
        btTimerStart = (Button) findViewById(R.id.btTimerStart);
        btTimerEnd = (Button) findViewById(R.id.btTimerEnd);
        tvTimer = (TextView) findViewById(R.id.tvTimer);

        //preference
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        tvTimer.setText("press start");

        Log.v(TAG, "total time " + total_time);
        // CountDownTimer(long millisInFuture, long countDownInterval)
        // 3分= 3x60x1000 = 180000 msec
        final CountDown countDown = new CountDown(total_time * 1000, 100);

        //SEND button
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talk(etSendString.getText().toString());
                etSendString.getEditableText().clear();
            }
        });
        //enter pressed
        etSendString.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && !(etSendString.getText().toString().equals(""))) {
                    talk(etSendString.getText().toString());
                    etSendString.getEditableText().clear();
                    return true;
                }
                return false;
            }
        });

        //No button
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talk("いいえ");
            }
        });
        //Yes button
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talk("はい");
            }
        });
        //Yes button Long click
        btYes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                YesDialogFragment yesDialogFragment = new YesDialogFragment();
                yesDialogFragment.show(getFragmentManager(), "yes");
                return true;
            }
        });
        //Enemy button
        btEnemy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talk("てき");
            }
        });
        //Direction button
        btDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //talk("ほういにゅうりょくがめんよてい");
                //TODO enter direction
                DirectionDialogFragment directionDialogFragment = new DirectionDialogFragment();
                directionDialogFragment.show(getFragmentManager(), "yes");
            }
        });
        //Distance button
        btDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //talk("きょりにゅうりょくがめんよてい");
                //TODO enter distance
                DistanceDialogFragment distanceDialogFragment = new DistanceDialogFragment();
                distanceDialogFragment.show(getFragmentManager(), "yes");
            }
        });
        //Number button
        btNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //talk("にんずうにゅうりょくがめんよてい");
                //TODO enter number
                NumberDialogFragment numberDialogFragment = new NumberDialogFragment();
                numberDialogFragment.show(getFragmentManager(), "yes");
            }
        });

        // SKIP button
        Button btSkip = (Button) findViewById(R.id.btSkip);
        btSkip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        bouyomi.clear();
                        bouyomi.skip();
                    }
                }).start();
            }
        });

        //タイマー関係
        btTimerStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                countDown.start();
                talk_auto("しあいかいし");
            }
        });
        btTimerEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                countDown.cancel();
                tvTimer.setText("press start");
            }
        });


    }

    //send string to bouyomi-chan
    private void talk(final String str) {
        if (!(str.equals(""))) {
            new Thread(new Runnable() {
                public void run() {
                    bouyomi.talk(Integer.parseInt(pref.getString("list_preference_volume", "50")),
                            Integer.parseInt(pref.getString("list_preference_speed", "100")),
                            Integer.parseInt(pref.getString("list_preference_interval", "100")),
                            Integer.parseInt(pref.getString("list_preference_type", "0")),
                            pref.getString("edit_text_preference_command", " ") + str);
                }
            }).start();
        }
    }

    //talk for timer
    private void talk_auto(final String str) {
        if (!(str.equals(""))) {
            new Thread(new Runnable() {
                public void run() {
                    bouyomi.talk(Integer.parseInt(pref.getString("list_preference_volume", "50")),
                            Integer.parseInt(pref.getString("list_preference_speed", "100")),
                            Integer.parseInt(pref.getString("list_preference_interval", "100")),
                            Integer.parseInt(pref.getString("list_preference_type_auto", "0")),
                            pref.getString("edit_text_preference_command_auto", "") + str);
                }
            }).start();
        }
    }

    class CountDown extends CountDownTimer {

        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 完了
            tvTimer.setText("0:00.000");
        }

        long old_time = 0;

        @Override
        public void onTick(long millisUntilFinished) {
            final long time_now = total_time * 1000 - millisUntilFinished;

            for (int i = 0; i < circle_shrink_start.length; i++) {
                if (old_time < circle_shrink_start[i] * 1000 && circle_shrink_start[i] * 1000 < time_now) {
                    talk_auto("だい" + (i - 1) + "回 円縮小始まります");
                }
            }
            for (int i = 0; i < circle_shrink_start.length; i++) {
                if (old_time < (circle_shrink_start[i] - 30) * 1000 && (circle_shrink_start[i] - 30) * 1000 < time_now) {
                    talk_auto("だい" + (i - 1) + "回 円縮小 30秒前です");
                }
            }
            for (int i = 0; i < circle_shrink_start.length; i++) {
                if (old_time < (circle_shrink_start[i] - 60) * 1000 && (circle_shrink_start[i] - 60) * 1000 < time_now) {
                    talk_auto("だい" + (i - 1) + "回 円縮小 1分前です");
                }
            }
            //TODO 設定からどの通知を行うか選択できるように
            //if(pref.getBoolean("multi_select_list_preference_alert_time",false) == false){
            for (int i = 0; i < circle_shrink_start.length; i++) {
                if (old_time < (circle_shrink_start[i] - 120) * 1000 && (circle_shrink_start[i] - 120) * 1000 < time_now) {
                    talk_auto("だい" + (i - 1) + "回 円縮小 2分前です");
                }
            }
            //}


            for (int i = 0; i < circle_update.length; i++) {
                if (old_time < circle_update[i] * 1000 && circle_update[i] * 1000 < time_now) {
                    if (i != 0) talk_auto("円更新");
                    if (i == 0) talk_auto("最初の円がマップにマークされます");
                }
            }

            old_time = time_now;

            // 残り時間を分、秒、ミリ秒に分割
            long mm = millisUntilFinished / 1000 / 60;
            long ss = millisUntilFinished / 1000 % 60;
            long ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60;
            //tvTimer.setText(String.format("%1$02d:%2$02d.%3$03d", mm, ss, ms));
            tvTimer.setText(String.format(" " + (int) (time_now / 1000) + "sec"));
        }
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionsMenu_01:
                Intent intent1 = new android.content.Intent(this, MainPreferenceActivity.class);
                startActivity(intent1);
                return true;
            case R.id.optionsMenu_02:
                Intent intent2 = new android.content.Intent(this, HowToUseActivity.class);
                startActivity(intent2);
                return true;
            case R.id.optionsMenu_03:
                Intent intent3 = new android.content.Intent(this, AboutThisAppActivity.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.v(TAG, "onStop");
        super.onStop();
        bouyomi = null;
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        new Thread(new Runnable() {
            public void run() {
                bouyomi = new BouyomiChan4J(pref.getString("edit_text_preference_ip", "127.0.0.1"), Integer.parseInt(pref.getString("edit_text_preference_port", "50001")));
            }
        }).start();
        tvIP.setText("開始しました \nip:" + pref.getString("edit_text_preference_ip", "127.0.0.1") + "\nport:" + pref.getString("edit_text_preference_port", "50001")
                + "\nvolume:" + pref.getString("list_preference_volume", "50") + "\nspeed:" + pref.getString("list_preference_speed", "100")
                + "\ninterval:" + pref.getString("list_preference_interval", "100") + "\nvoice type:" + pref.getString("list_preference_type:", "0"));
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
        super.onPause();
    }
}
