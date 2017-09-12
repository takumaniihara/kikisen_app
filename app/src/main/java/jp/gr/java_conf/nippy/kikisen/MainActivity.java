package jp.gr.java_conf.nippy.kikisen;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();

    TextView tvIP;
    EditText etSendString;
    EditText etIP;
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
        etIP = (EditText) findViewById(R.id.etIP);


        //SEND button
        Button btn = (Button) findViewById(R.id.btSend);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        bouyomi.talk("" + etSendString.getText().toString());
                    }
                }).start();
            }
        });

        // SKIP button
        Button btSkip = (Button) findViewById(R.id.btSkip);
        btSkip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Thread(new Runnable() {
                    public void run() {
                        bouyomi.skip();
                    }
                }).start();
            }
        });
        //START button
        Button btStart = (Button) findViewById(R.id.btStart);
        btStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                tvIP.setText("started on IP : " +  etIP.getText().toString());
                new Thread(new Runnable() {
                    public void run() {
                        bouyomi = new BouyomiChan4J(etIP.getText().toString(), 50001);
                    }
                }).start();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        bouyomi = new BouyomiChan4J("192.168.11.15", 50001);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.v(TAG,"onStop");
    }
}