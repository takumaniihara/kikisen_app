package jp.gr.java_conf.nippy.kikisen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
    BouyomiChan4J bouyomi;
    SharedPreferences pref;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvIP = (TextView) findViewById(R.id.tvIP);
        etSendString = (EditText) findViewById(R.id.etSendString);
        btSend = (Button) findViewById(R.id.btSend);
        etSendString.setEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        //SEND button
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str = etSendString.getText().toString();
                new Thread(new Runnable() {
                    public void run() {
                        talk(str);
                    }
                }).start();
                etSendString.getEditableText().clear();
            }
        });
        etSendString.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //enter pressed
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && !(etSendString.getText().toString().equals(""))) {
                    final String str = etSendString.getText().toString();
                    new Thread(new Runnable() {
                        public void run() {
                            talk(str);
                        }
                    }).start();
                    etSendString.getEditableText().clear();
                    return true;
                }
                return false;
            }
        });

        // SKIP button
        Button btSkip = (Button) findViewById(R.id.btSkip);
        btSkip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        bouyomi.skip();
                    }
                }).start();
            }
        });
    }

    private void talk(String str) {
        //TODO 設定から速度とかの反映を行う 棒読みを起動する前にアプリを起動すると落ちる？
        if (!(str.equals(""))) {
            //volume speed tone voice message
            bouyomi.talk(Integer.parseInt(pref.getString("list_preference_volume","-1")), Integer.parseInt(pref.getString("list_preference_speed","-1")), Integer.parseInt(pref.getString("list_preference_interval","-1")), Integer.parseInt(pref.getString("list_preference_type","0")), str);
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
        tvIP.setText("started \nip:" + pref.getString("edit_text_preference_ip", "127.0.0.1") + "\nport:" + pref.getString("edit_text_preference_port", "50001")
                + "\nvolume:" + pref.getString("list_preference_volume","-1") + "\nspeed:"+ pref.getString("list_preference_speed","-1")
                + "\ninterval:"+ pref.getString("list_preference_interval","-1") + "\nvoice type:" + pref.getString("list_preference_type:","0"));
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
        super.onPause();
    }
}