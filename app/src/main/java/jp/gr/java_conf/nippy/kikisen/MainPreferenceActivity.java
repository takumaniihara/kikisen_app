package jp.gr.java_conf.nippy.kikisen;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by ntkm9 on 2017/09/23.
 */

public class MainPreferenceActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("設定");
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MainPreferenceFragment()).commit();

    }
}
