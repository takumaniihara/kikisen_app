package jp.gr.java_conf.nippy.kikisen;

import android.os.Bundle;
import android.preference.PreferenceFragment;
/**
 * Created by ntkm9 on 2017/09/23.
 */

public class MainPreferenceFragment  extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_main_setting);
    }
}
