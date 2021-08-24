package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.SessionManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    SessionManager sessionManager;
    public MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Integer.parseInt("nana");
        myReceiver = new MusicIntentReceiver();

        sessionManager=new SessionManager(SplashActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sessionManager.getLoginStatus())
                {
                    Intent i = new Intent(SplashActivity.this, HomeScreen.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(i);
                }

            }
        }, SPLASH_TIME_OUT);
    }
    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        super.onResume();
    }
    @Override public void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }
}
