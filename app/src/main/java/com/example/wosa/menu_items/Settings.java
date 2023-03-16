

package com.example.wosa.menu_items;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.SeekBarPreference;

import com.example.wosa.Buzzer_tone;
import com.example.wosa.Home.Home_Page_Drawer;
import com.example.wosa.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;


public class Settings extends AppCompatActivity  {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.FittoSystem);

        super.onCreate(savedInstanceState);
        StatusBarColor();
        getFragmentManager().beginTransaction().replace(R.id.frame_fragment, new MypreferenceFragment()).commit();
        getLayoutInflater().inflate(R.layout.actual_navigation_actionbar, (ViewGroup) findViewById(android.R.id.content));

//        replace_fragment();
        Toolbar toolbar = findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Settings");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Home_Page_Drawer.class));
                finish();
            }
        });
    }

    // Preference Fragment
    public static class MypreferenceFragment extends PreferenceFragment {
        ListPreference listPreference;
        AudioManager audioManager;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Buzzer_tone buzzer_tone = new Buzzer_tone(getActivity());

            addPreferencesFromResource(R.xml.settings);

            listPreference = findPreference("buzzer_tones");

            audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

            listPreference.setSummary(buzzer_tone.getBuzzerTone());
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    listPreference.setSummary(newValue.toString());
                    buzzer_tone.setBuzzerTone(newValue.toString());

//                    Toast.makeText(getActivity(),buzzer_tone.getBuzzerTone(),Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            final Preference about = (Preference)findPreference("about");
            Preference feedback = (Preference)findPreference("feedback");
            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(),About.class));
                    getActivity().finish();
                    return false;
                }
            });
            feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(),Feedback.class));
                    getActivity().finish();
                    return false;
                }
            });

        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Home_Page_Drawer.class));
        finish();
    }

    public void StatusBarColor() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setTintColor(ContextCompat.getColor(this, R.color.titlebar));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(getResources().getColor(R.color.titlebar));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setFullVolume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setFullVolume();
    }
    private void setFullVolume(){
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
    }


}

