package com.example.wosa.menu_items;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.wosa.Home.Home_Page_Drawer;
import com.example.wosa.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class Self_Defense extends AppCompatActivity {
    Toolbar toolbar;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColor();
        setContentView(R.layout.activity_self_defense);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle("Self Defense");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home_Page_Drawer.class));
                finish();
            }
        });


        webView = findViewById(R.id.sd_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://brightside.me/inspiration-tips-and-tricks/7-self-defense-techniques-for-women-recommended-by-a-professional-441310/");
        WebViewClient webViewClient = new WebViewClient(Self_Defense.this);
        webView.setWebViewClient(webViewClient);

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
    public void onBackPressed() {
//        if (webView.canGoBack()){
//            webView.goBack();
//        }
//        else {
//            startActivity(new Intent(getApplicationContext(),Home_Page_Drawer.class));
//            finish();
//            super.onBackPressed();
//        }
        startActivity(new Intent(getApplicationContext(),Home_Page_Drawer.class));
        finish();
        super.onBackPressed();
    }
}
