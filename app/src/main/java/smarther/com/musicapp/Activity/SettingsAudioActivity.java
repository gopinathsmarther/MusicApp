package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.SessionManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.io.File;

public class SettingsAudioActivity extends AppCompatActivity {
    SessionManager sessionManager;
    LinearLayout background_of_layout;
    LinearLayout navigate_back;
    LinearLayout shakeEnable;
    SwitchCompat shakeEnableSwitch;
    SwitchCompat remember_shuffle;
    public MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_audio);
        navigate_back=findViewById(R.id.navigate_back);
        navigate_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sessionManager=new SessionManager(SettingsAudioActivity.this);
        background_of_layout=findViewById(R.id.background_of_layout);
        shakeEnableSwitch=findViewById(R.id.shakeEnableSwitch);
        shakeEnable=findViewById(R.id.shakeEnable);
        remember_shuffle=findViewById(R.id.remember_shuffle);
        myReceiver = new MusicIntentReceiver();
        if(sessionManager.getShakeEnable()){
            shakeEnableSwitch.setChecked(true);
            System.out.println("aji shake at first "+sessionManager.getShakeEnable());

        }else{
            shakeEnableSwitch.setChecked(false);
            System.out.println("aji shake at first "+sessionManager.getShakeEnable());
        }
        if(sessionManager.getRememberShuffle()){
            remember_shuffle.setChecked(true);
            System.out.println("aji getRememberShuffle at first "+sessionManager.getRememberShuffle());

        }else{
            remember_shuffle.setChecked(false);
            System.out.println("aji getRememberShuffle at first "+sessionManager.getRememberShuffle());
        }
        shakeEnableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    sessionManager.setShakeEnable(true);
//                    shakeEnableSwitch.setChecked(false);
                    System.out.println("aji shake at audio "+sessionManager.getShakeEnable());
                }else{
                    sessionManager.setShakeEnable(false);
//                    shakeEnableSwitch.setChecked(true);
                    System.out.println("aji shake at audio "+sessionManager.getShakeEnable());

                }

            }
        });

        remember_shuffle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    sessionManager.setRememberShuffle(true);
//                    shakeEnableSwitch.setChecked(false);
                    System.out.println("aji getRememberShuffle at audio "+sessionManager.getRememberShuffle());
                }else{
                    sessionManager.setRememberShuffle(false);
//                    shakeEnableSwitch.setChecked(true);
                    System.out.println("aji getRememberShuffle at audio "+sessionManager.getRememberShuffle());

                }

            }
        });
//        shakeEnable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("aji shake at audio entered listener");
//
//                if(shakeEnableSwitch.isChecked()){
//                    sessionManager.setShakeEnable(false);
//                    shakeEnableSwitch.setChecked(false);
//                    System.out.println("aji shake at audio "+sessionManager.getShakeEnable());
//                }else{
//                    sessionManager.setShakeEnable(true);
//                    shakeEnableSwitch.setChecked(true);
//                    System.out.println("aji shake at audio "+sessionManager.getShakeEnable());
//
//                }
//
//            }
//        });


        if(sessionManager.getIsDefaultThemeSelected())
        {
            background_of_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(SettingsAudioActivity.this,selectedImageUri));
                bg= Drawable.createFromPath(f.getAbsolutePath());


//                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                encodeTobase64(bitmap);
            }
            catch (Exception e)
            {
                bg = ContextCompat.getDrawable(this, R.drawable.theme_1);
                e.printStackTrace();
            }
//            Drawable dr = new BitmapDrawable(sessionManager.getFileManagerTheme());
            background_of_layout.setBackground(bg);
        }
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
