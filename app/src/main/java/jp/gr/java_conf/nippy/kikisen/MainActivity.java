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

    TextView tv;
    EditText et;
    BouyomiChan4J bouyomi;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textView);
        et = (EditText) findViewById(R.id.edittext);


        //SEND button
        Button btn = (Button) findViewById(R.id.btSend);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("pressed");
                final String hoge = et.getText().toString();
                new Thread(new Runnable() {
                    public void run() {
                        bouyomi.talk("" + hoge);
                    }
                }).start();
            }
        });

        // SKIP button
        Button btnSkip = (Button) findViewById(R.id.button);
        btnSkip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Thread(new Runnable() {
                    public void run() {
                        bouyomi.skip();
                    }
                }).start();
            }
        });



    }

    @Override
    public void onStart(){
        super.onStart();
        tv.setText("started ");
        bouyomi = new BouyomiChan4J("192.168.11.150", 50001);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.v(TAG,"onStop");
    }
}