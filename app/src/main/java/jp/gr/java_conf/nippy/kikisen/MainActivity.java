package jp.gr.java_conf.nippy.kikisen;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();

    TextView tvIP;
    EditText etSendString;
    EditText etIP;
    TextView etLog;
    BouyomiChan4J bouyomi;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvIP = (TextView) findViewById(R.id.tvIP);
        etSendString = (EditText) findViewById(R.id.etSendString);
        etSendString.setEnabled(true);
        etIP = (EditText) findViewById(R.id.etIP);
        etLog = (TextView) findViewById(R.id.etLog);
        etLog.setEnabled(false);


        //SEND button
        Button btn = (Button) findViewById(R.id.btSend);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ココらへんは下のkeylistenerと統合するべき TODO
                new Thread(new Runnable() {
                    public void run() {
                        bouyomi.talk("" + etSendString.getText().toString());
                    }
                }).start();
                if (!(etSendString.getText().toString().equals(""))) {
                    etLog.setText(etLog.getText().toString() + "\n" + etSendString.getText().toString());
                }
                etSendString.getEditableText().clear();
            }
        });
        etSendString.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && !(etSendString.getText().toString().equals(""))) {
                    //送信 この部分はきれいにする必要あり TODO
                    new Thread(new Runnable() {
                        public void run() {
                            bouyomi.talk("" + etSendString.getText().toString());
                        }
                    }).start();
                    etLog.setText(etLog.getText().toString() + "\n" + etSendString.getText().toString());
                    etSendString.getEditableText().clear();
                    return true;
                }
                return false;
            }
        });


        // SKIP button
        Button btSkip = (Button) findViewById(R.id.btSkip);
        btSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        bouyomi.skip();
                    }
                }).start();
            }
        });
        //START button
        Button btStart = (Button) findViewById(R.id.btStart);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvIP.setText("started on IP : " + etIP.getText().toString());
                new Thread(new Runnable() {
                    public void run() {
                        bouyomi = new BouyomiChan4J(etIP.getText().toString(), 50001);
                    }
                }).start();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        bouyomi = new BouyomiChan4J("192.168.11.15", 50001);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }
}