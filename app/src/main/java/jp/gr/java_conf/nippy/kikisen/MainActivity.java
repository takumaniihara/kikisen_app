package jp.gr.java_conf.nippy.kikisen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    BouyomiChan4J bouyomi;
    SharedPreferences pref;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        tvIP = (TextView) findViewById(R.id.tvIP);
        etSendString = (EditText) findViewById(R.id.etSendString);
        btSend = (Button) findViewById(R.id.btSend);
        etSendString.setEnabled(true);
        btNo = (Button) findViewById(R.id.btNo);
        btYes = (Button) findViewById(R.id.btYes);
        btEnemy = (Button) findViewById(R.id.btEnemy);
        btDirection = (Button) findViewById(R.id.btDirection);
        btDistance = (Button) findViewById(R.id.btDistance);
        btNumber = (Button) findViewById(R.id.btNumber);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

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
        btYes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                talk("ろんぐくりっく");
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
                FireMissilesDialogFragment hoge = new FireMissilesDialogFragment();
                hoge.show(getFragmentManager(), "test");
            }
        });
        //Distance button
        btDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //talk("きょりにゅうりょくがめんよてい");
                //TODO enter distance
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
                alertDialog.setTitle("hi");
                alertDialog.setMessage("comming soon (tm)");

                alertDialog.setButton("Continue..", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                    }
                });
                alertDialog.show();
            }
        });
        //Number button
        btNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //talk("にんずうにゅうりょくがめんよてい");
                //TODO enter number
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
                alertDialog.setTitle("hi");
                alertDialog.setMessage("comming soon (tm)");

                alertDialog.setButton("Continue..", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                    }
                });
                alertDialog.show();
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
                            str);
                }
            }).start();
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