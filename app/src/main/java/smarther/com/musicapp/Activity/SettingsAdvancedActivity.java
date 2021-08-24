package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.SessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class SettingsAdvancedActivity extends AppCompatActivity {
   SessionManager sessionManager;
   LinearLayout background_of_layout;
   LinearLayout playlist_duplicate_layout;
   LinearLayout ask_first_layout;
   LinearLayout allow_layout;
   LinearLayout never_allow_layout;
   ImageView ask_first_img;
   ImageView allow_img;
   ImageView never_allow_img;
    LinearLayout navigate_back;
    TextView clear_cache;
    TextView playlist_duplicate;

    public MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_advanced);
        navigate_back=findViewById(R.id.navigate_back);
        navigate_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sessionManager=new SessionManager(SettingsAdvancedActivity.this);
        background_of_layout=findViewById(R.id.background_of_layout);
        clear_cache=findViewById(R.id.clear_cache);
        playlist_duplicate=findViewById(R.id.playlist_duplicate);
        playlist_duplicate_layout=findViewById(R.id.playlist_duplicate_layout);

        myReceiver = new MusicIntentReceiver();

        if(sessionManager.getIsDefaultThemeSelected())
        {
            background_of_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(SettingsAdvancedActivity.this,selectedImageUri));
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
        if (sessionManager.getDupilcatePlaylistSongId()==1){
            playlist_duplicate.setText("Allow");
        }else  if (sessionManager.getDupilcatePlaylistSongId()==2){
            playlist_duplicate.setText("Ask Always");
        }else{
            playlist_duplicate.setText("Never Allow");
        }




        playlist_duplicate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.duplicate_playlist_alert, null);
//                View alertLayout = LayoutInflater.from(SettingsAdvancedActivity.this).inflate(R.layout.duplicate_playlist_alert, null);
                background_of_layout = alertLayout.findViewById(R.id.background_of_layout);
                ask_first_layout = alertLayout.findViewById(R.id.ask_first_layout);
                ask_first_img = alertLayout.findViewById(R.id.ask_first_img);
                allow_layout = alertLayout.findViewById(R.id.allow_layout);
                allow_img = alertLayout.findViewById(R.id.allow_img);
                never_allow_layout = alertLayout.findViewById(R.id.never_allow_layout);
                never_allow_img = alertLayout.findViewById(R.id.never_allow_img);


                if (sessionManager.getIsDefaultThemeSelected()) {
                    background_of_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
                }
                else {
                    Drawable bg;
                    Uri selectedImageUri = Uri.parse(sessionManager.getFileManagerTheme());
                    try {

                        File f = new File(Global.getRealPathFromURI(SettingsAdvancedActivity.this, selectedImageUri));
                        bg = Drawable.createFromPath(f.getAbsolutePath());


//                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                encodeTobase64(bitmap);
                    } catch (Exception e) {
                        bg = ContextCompat.getDrawable(SettingsAdvancedActivity.this, R.drawable.theme_1);
                        e.printStackTrace();

                    }
//            Drawable dr = new BitmapDrawable(sessionManager.getFileManagerTheme());
                    background_of_layout.setBackground(bg);
//                    background_of_layout.setBackgroundResource(R.drawable.theme_1);

                }

                if (sessionManager.getDupilcatePlaylistSongId() == 1) {
                    allow_img.setImageResource(R.drawable.radio_button);
                    never_allow_img.setImageResource(R.drawable.radio_circle);
                    ask_first_img.setImageResource(R.drawable.radio_circle);
                } else if (sessionManager.getDupilcatePlaylistSongId() == 2) {
                    allow_img.setImageResource(R.drawable.radio_circle);
                    never_allow_img.setImageResource(R.drawable.radio_circle);
                    ask_first_img.setImageResource(R.drawable.radio_button);
                }

                else{

                    allow_img.setImageResource(R.drawable.radio_circle);
                    never_allow_img.setImageResource(R.drawable.radio_button);
                    ask_first_img.setImageResource(R.drawable.radio_circle);
                }





//                final AlertDialog.Builder alert = new AlertDialog.Builder(SettingsAdvancedActivity.this);
//                alert.setCancelable(true);
//                alert.setView(alertLayout);
//                final AlertDialog dialog = alert.create();
//                dialog.show();
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(SettingsAdvancedActivity.this);
                alert.setView(alertLayout);
//                alert.setCancelable(false);
                final android.app.AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ask_first_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sessionManager.setDupilcatePlaylistSongId(2);
                        playlist_duplicate.setText("Ask First");
                        dialog.dismiss();


                    }
                });
                never_allow_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sessionManager.setDupilcatePlaylistSongId(3);
                        playlist_duplicate.setText("Never Allow");
                        dialog.dismiss();

                    }
                });


                allow_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sessionManager.setDupilcatePlaylistSongId(1);
                        playlist_duplicate.setText("Allow");
                        dialog.dismiss();

                    }
                });




            }
        });


        clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCache(SettingsAdvancedActivity.this);
//                Global.show_toasts(SettingsAdvancedActivity.this,"cache cleared");

            }
        });

    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            System.out.println("aji file "+dir);
            boolean status = deleteDir(dir);
            if (status)
            {
                Toast.makeText(context,"cache cleared",Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                System.out.println("aji file "+i+" "+success);

                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
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
