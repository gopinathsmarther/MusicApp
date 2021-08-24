package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.FolderModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.SessionManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static smarther.com.musicapp.Fragments.FoldersFragment.getAudioFileCount;

public class ScanMediaActivity extends AppCompatActivity {

    LinearLayout scan_media_background;
    LinearLayout back;
    SessionManager sessionManager;
    Button scan_start_btn;
    LinearLayout checkbox_layout,fetching_layout,after_fetching_layout;
    CheckBox scan_media_checkBox_1,scan_media_checkBox_2,scan_media_checkBox_3;
    TextView scan_scaning_text,files_added,scan_Done_text,path_text;
//    GifView pGif;
    GifImageView gifimageview;
    GifDrawable gifDrawable;
    LinearLayout scan_3dot_btn;
    public  List<FolderModel> fileList;
    public MusicIntentReceiver myReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_media);
        sessionManager=new SessionManager(ScanMediaActivity.this);
        scan_media_background=findViewById(R.id.scan_media_background);
        back=findViewById(R.id.back);
        scan_Done_text=findViewById(R.id.scan_Done_text);
        checkbox_layout=findViewById(R.id.checkbox_layout);
        scan_start_btn=findViewById(R.id.scan_start_btn);
        fetching_layout=findViewById(R.id.fetching_layout);
        after_fetching_layout=findViewById(R.id.after_fetching_layout);
        files_added=findViewById(R.id.files_added);
        scan_scaning_text=findViewById(R.id.scan_scaning_text);
        path_text=findViewById(R.id.path_text);
        scan_3dot_btn=findViewById(R.id.scan_3dot_btn);
        gifimageview = (GifImageView) findViewById(R.id.scan_media_imageView);
        myReceiver = new MusicIntentReceiver();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        pGif = (GifView) findViewById(R.id.scan_media_imageView);
//        pGif.setImageResource(R.drawable.scanner);
        try {
            gifDrawable = new GifDrawable(getResources(), R.raw.scannergif );
            gifDrawable.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gifimageview.setImageDrawable(gifDrawable);


        if(sessionManager.getIsDefaultThemeSelected())
        {
            scan_media_background.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(ScanMediaActivity.this,selectedImageUri));
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
            scan_media_background.setBackground(bg);
        }

        scan_3dot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ScanMediaActivity.this, scan_3dot_btn);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.scan_popup_window, popup.getMenu());
                popup.setGravity(90);

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String title = (String) item.getTitle();
                        if(title.equalsIgnoreCase("Scan Download"))
                        {
                            scan(3,-10);
                            Toast.makeText(ScanMediaActivity.this, "You Clicked ane entered: " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        }
                        else  if(title.equalsIgnoreCase("Scan Sdcard"))
                        {
                            String s = getExternalStoragePath();
                            System.out.println("aji sd card "+s);
                            Toast.makeText(ScanMediaActivity.this, "You Clicked ane entered: " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        }
                        else  if(title.equalsIgnoreCase("Choose Directory"))
                        {
                            String root_sd = Environment.getExternalStorageDirectory().toString();
                            String root_sd_card = Environment.getExternalStorageDirectory().getAbsolutePath();
                            String root_sd_car = Environment.getRootDirectory().toString();
                            System.out.println("aji External "+root_sd);

                            System.out.println("aji get... "+root_sd_card);
                            System.out.println("aji getRootDirectory "+root_sd_car);

                            File file = new File(root_sd);
                            System.out.println("aji root_sd "+root_sd);
                            System.out.println("aji file "+file);
                            load_folder(file);


                            Toast.makeText(ScanMediaActivity.this, "You Clicked ane entered: " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        }
                       // Toast.makeText(ScanMediaActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu

            }


//                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.scan_popup_window, null, false),100,100, true);
//
//                pw.showAtLocation(this.findViewById(R.id.main), Gravity.CENTER, 0, 0);
//            }

        });

        scan_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               scan(1,-10);

            }
        });



        scan_Done_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ScanMediaActivity.this, HomeScreen.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


    }

    public  void load_folder(File file)
    {
        File[] list = file.listFiles(new Global.AudioFilter());
//        List<String> foldername = null;
        fileList = new ArrayList<>();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ScanMediaActivity.this, android.R.layout.select_dialog_item);
        fileList.clear();
        for (int i = 0; i < list.length; i++) {
            String name=list[i].getName();
            int count = getAudioFileCount(list[i].getAbsolutePath());

            FolderModel folderModel=new FolderModel();
            folderModel.setName(name);
            folderModel.setSongs(String.valueOf(count));
            folderModel.setPath(list[i].getAbsolutePath());
            System.out.println("aji name "+name);
            System.out.println("aji list[i].getAbsolutePath() "+list[i].getAbsolutePath());


            if (count != 0)
            {
                arrayAdapter.add(name);
                fileList.add(folderModel);
            }


        }
        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(ScanMediaActivity.this);
        alert.setTitle(file.toString());
        alert.setCancelable(false);
        alert.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                    partner_district.setText(DropDownTable.call_required_listdata("8").get(which));
                scan(12,which);
            }
        });
        alert.setNegativeButton("scan directory", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                scan(1,-10);
            }
        });
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();

    }
    public int getAudioFileCount(final String dirPath) {

        String selection = MediaStore.Audio.Media.DATA + " like ?";
        String[] projection = {MediaStore.Audio.Media.DATA};
        String[] selectionArgs = {dirPath + "%"};
        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor != null){
            int temp=cursor.getCount();
            cursor.close();
            return temp;
        }
        else {
            return 0;
        }
    }
    void scan(final int pos,final int which){

        fetching_layout.setVisibility(View.VISIBLE);
        checkbox_layout.setVisibility(View.GONE);
        after_fetching_layout.setVisibility(View.GONE);
        scan_scaning_text.setVisibility(View.VISIBLE);
        scan_start_btn.setVisibility(View.GONE);
        scan_Done_text.setVisibility(View.GONE);
        path_text.setText("Fetching");
        gifDrawable.start();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                final List<AudioModel> tempAudioList = new ArrayList<>();
                String s = "";

                if (pos == 1) {

                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.AudioColumns.ARTIST_ID, MediaStore.Audio.ArtistColumns.ARTIST, "title", "_id"};

//        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST,"title"};
//        Cursor c = getActivity().getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%yourFolderName%"}, null);
                Cursor c = getContentResolver().query(uri, projection, null, null, null);
                if (c != null) {
                    while (c.moveToNext()) {

                        AudioModel audioModel = new AudioModel();
                        String path = c.getString(0);
                        audioModel.setaPath(path);
                        path_text.setText(path);
                        System.out.println("COMPLETE  ---" + path);
                        System.out.println("<<<<<<<<<<<<<<<<<<<<<<COMPLETE Scaning...>>>>>>>>>>>>>>>>>>>>>>>>>" + tempAudioList.size());
                        tempAudioList.add(audioModel);
                    }
                    c.close();
                }
                     s = "" + tempAudioList.size();

                }
                else if(pos == 2){
                    int count = getAudioFileCount(fileList.get(which).getPath());
                    System.out.println("aji dir count "+count);
                    s = "" + count;

                }
                else{
                    int count = getAudioFileCount("/storage/emulated/0/Download");
                    System.out.println("aji count "+count);
                    s = "" + count;
                }
                        fetching_layout.setVisibility(View.GONE);
                        checkbox_layout.setVisibility(View.GONE);
                        after_fetching_layout.setVisibility(View.VISIBLE);
                        scan_scaning_text.setVisibility(View.GONE);
                        scan_start_btn.setVisibility(View.GONE);
                        scan_Done_text.setVisibility(View.VISIBLE);
                        files_added.setText(s);
                        gifDrawable.stop();
                        System.out.println("COMPLETED Scan>>>>>>>>>>>>>>>>>>>>>>>>>" + tempAudioList.size());

                    }



        }, 3000);

    }
    public String getExternalStoragePath() {

        String internalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String[] paths = internalPath.split("/");
        String parentPath = "/";
        for (String s : paths) {
            if (s.trim().length() > 0) {
                parentPath = parentPath.concat(s);
                break;
            }
        }
        File parent = new File(parentPath);
        if (parent.exists()) {
            File[] files = parent.listFiles();
            for (File file : files) {
                String filePath = file.getAbsolutePath();
                System.out.println("aji filepath"+filePath);
                if (filePath.equals(internalPath)) {
                    continue;
                } else if (filePath.toLowerCase().contains("sdcard")) {
                    return filePath;
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        if (Environment.isExternalStorageRemovable(file)) {
                            return filePath;
                        }
                    } catch (RuntimeException e) {
                        System.out.println("aji error");
                    }
                }
            }

        }
        return null;
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
