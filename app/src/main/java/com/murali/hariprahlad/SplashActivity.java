package com.murali.hariprahlad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.murali.hariprahlad.walletbaba.R;

import services.SessionManager;

public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(i);
            SplashActivity.this.finish();
        }
    };
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            addShortcut();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("first_time", false))
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first_time", true);
            editor.commit();
            Intent i = new Intent(SplashActivity.this, SplashActivity.class);
            this.startActivity(i);
            this.finish();
        }
        else
        {
            this.setContentView(R.layout.activity_splash);
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    }
    private void addShortcut() {
        //Adding shortcut for com.murali.hariprahlad.MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(), SplashActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "WalletbabShortcut");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.drawable.menupic));

        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }
}
