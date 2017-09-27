package jp.gr.java_conf.nippy.kikisen;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by takuma on 17/09/22.
 */

/**
 * PreferenceFragmentを継承したクラス
 * 個々で定義したPreferenceのリソースを設定します
 */
public class SimplePreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

}