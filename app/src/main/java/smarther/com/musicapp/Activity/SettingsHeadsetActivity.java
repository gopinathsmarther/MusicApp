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

import java.io.File;

public class SettingsHeadsetActivity extends AppCompatActivity {
    SessionManager sessionManager;
    LinearLayout background_of_layout;
    LinearLayout navigate_back;
    SwitchCompat pause_on_disconnect;
    SwitchCompat resume_connect;
    public MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_headset);
        navigate_back=findViewById(R.id.navigate_back);
        navigate_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sessionManager=new SessionManager(SettingsHeadsetActivity.this);
        background_of_layout=findViewById(R.id.background_of_layout);
        pause_on_disconnect=findViewById(R.id.pause_on_disconnect);
        resume_connect=findViewById(R.id.resume_connect);
        myReceiver = new MusicIntentReceiver();
        if(sessionManager.getHeadsetPause()){
            pause_on_disconnect.setChecked(true);
            System.out.println("aji shake at first "+sessionManager.getShakeEnable());

        }else{
            pause_on_disconnect.setChecked(false);
            System.out.println("aji shake at first "+sessionManager.getShakeEnable());
        }


        if(sessionManager.getHeadsetResume()){
            resume_connect.setChecked(true);
            System.out.println("aji shake at first "+sessionManager.getShakeEnable());

        }else{
            resume_connect.setChecked(false);
            System.out.println("aji shake at first "+sessionManager.getShakeEnable());
        }


        if(sessionManager.getIsDefaultThemeSelected())
    {
        background_of_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
    }
        else
    {
        Drawable bg;
        Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
        try {

            File f = new File(Global.getRealPathFromURI(SettingsHeadsetActivity.this,selectedImageUri));
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


        pause_on_disconnect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    sessionManager.setHeadsetPause(true);
//                    shakeEnableSwitch.setChecked(false);
                    System.out.println("aji getHeadsetPause "+sessionManager.getHeadsetPause());
                }else{
                    sessionManager.setHeadsetPause(false);
//                    shakeEnableSwitch.setChecked(true);
                    System.out.println("aji getHeadsetPause "+sessionManager.getHeadsetPause());

                }

            }
        });



        resume_connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    sessionManager.setHeadsetResume(true);
//                    shakeEnableSwitch.setChecked(false);
                    System.out.println("aji getHeadsetResume "+sessionManager.getHeadsetResume());
                }else{
                    sessionManager.setHeadsetResume(false);
//                    shakeEnableSwitch.setChecked(true);
                    System.out.println("aji getHeadsetResume "+sessionManager.getHeadsetResume());

                }

            }
        });
}

    @Override
    public void onResume() {
//        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
//        registerReceiver(myReceiver, filter);
        super.onResume();
    }
    @Override public void onPause() {
        //unregisterReceiver(myReceiver);
        super.onPause();
    }
}
