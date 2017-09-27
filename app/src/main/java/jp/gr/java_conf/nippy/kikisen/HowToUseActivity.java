package jp.gr.java_conf.nippy.kikisen;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * Created by ntkm9 on 2017/09/23.
 */

public class HowToUseActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_howtouse);
        setTitle("使い方");

    }
}
