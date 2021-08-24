package smarther.com.musicapp.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import smarther.com.musicapp.Adapter.AlbumSongsAdapter;
import smarther.com.musicapp.Adapter.DuplicatePlaylistAdapter;
import smarther.com.musicapp.Adapter.PlaylistAddAdapter;
import smarther.com.musicapp.Adapter.QueueSongAdapter;
import smarther.com.musicapp.Adapter.SongsAdapter;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.Model.PlaylistModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.MusicUtils;
import smarther.com.musicapp.Utils.RecyclerItemTouchHelper;
import smarther.com.musicapp.Utils.SessionManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TopTrackActivity extends AppCompatActivity {
    public MusicIntentReceiver myReceiver;


    List<AudioModel> playing_audio_list;


    LinearLayout background_of_layout;
    TextView add_to_playlist_add;
    TextView add_to_playlist_cancel;
    Realm music_database;
    Realm playlist_database;
    RecyclerView playlist_alert_recyclerView;
    PlaylistAddAdapter playlistAddAdapter;
    List<PlaylistAudioModel> playlistAudioModel =new ArrayList<>();
    List<PlaylistModel> playlistModelList =new ArrayList<>();
    List<AudioModel> audioModelList;
    TextView add_all;
    TextView skip_all;
    RecyclerView duplicate_recycler;
    DuplicatePlaylistAdapter duplicatePlaylistAdapter;


    LinearLayout queue_background;
    LinearLayout back_option;
    LinearLayout scan_3dot_btn;
    LinearLayout folder_plus;
    TextView activity_title;
    TextView song_duration;
    RecyclerView queue_song_recycler;
    SessionManager sessionManager;
    int millSecond=0;
    int songPosition =0;
    String title;
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_track);
        final ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        queue_background=(LinearLayout)findViewById(R.id.queue_background);
        back_option=(LinearLayout)findViewById(R.id.back_option);
        scan_3dot_btn=(LinearLayout)findViewById(R.id.scan_3dot_btn);
        folder_plus=(LinearLayout)findViewById(R.id.folder_plus);
        activity_title=(TextView) findViewById(R.id.activity_title);
        song_duration=(TextView) findViewById(R.id.song_duration);
        queue_song_recycler = (RecyclerView)findViewById(R.id.queue_song_recycler);
        queue_song_recycler.setLayoutManager(new GridLayoutManager(TopTrackActivity.this, 1, LinearLayoutManager.VERTICAL, false));
        queue_song_recycler.setHasFixedSize(true);
        queue_song_recycler.setNestedScrollingEnabled(false);
        sessionManager=new SessionManager(TopTrackActivity.this);
        myReceiver = new MusicIntentReceiver();

        Intent intent=getIntent();
         title=intent.getStringExtra("title");
        activity_title.setText(title);
        if(sessionManager.getIsDefaultThemeSelected())
        {
            queue_background.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(TopTrackActivity.this,selectedImageUri));
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
            queue_background.setBackground(bg);
        }
        back_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(TopTrackActivity.this,HomeScreen.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.enter_intent,R.anim.exit_intent);
            }
        });

        final Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
        audioModelList = new GsonBuilder().create().fromJson(sessionManager.getTopTrackList(), type);
        int size = audioModelList.size();
        songPosition = sessionManager.getSongPosition()+1;
        folder_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
                List<AudioModel> arrayList = new GsonBuilder().create().fromJson(sessionManager.getTopTrackList(), type);
                playing_audio_list = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
                playing_audio_list.addAll(arrayList);
                Gson gson_list = new GsonBuilder().create();
                JsonArray song_array = gson_list.toJsonTree(playing_audio_list).getAsJsonArray();
                sessionManager.setSong_JsonList(song_array.toString());
                Toast.makeText(TopTrackActivity.this, arrayList.size()+" songs Added to playing Queue", Toast.LENGTH_SHORT).show();
            }
        });
        scan_3dot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(TopTrackActivity.this, R.style.popupMenuStyle);
//                PopupMenu popup = new PopupMenu(wrapper, view);
                PopupMenu popup = new PopupMenu(wrapper, scan_3dot_btn);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.top_track_top_menu_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.play:
                                Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
                                List<AudioModel> arrayList = new GsonBuilder().create().fromJson(sessionManager.getTopTrackList(), type);
                                if(arrayList.size()!=0) {
                                    playing_audio_list = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
                                    playing_audio_list = (arrayList);
                                    System.out.println("aji toptrack play " + arrayList.size());
                                    Gson gson_list = new GsonBuilder().create();
                                    JsonArray song_array = gson_list.toJsonTree(playing_audio_list).getAsJsonArray();
                                    sessionManager.setSong_JsonList(song_array.toString());
                                    AudioModel audioModel = arrayList.get(0);
                                    Gson gson = new Gson();
                                    String json = gson.toJson(audioModel);
                                    sessionManager.setSong_Json(json);
                                    sessionManager.setSongPosition(0);
                                    // Global.intent_method(context, SongPlayingActivity.class);
                                    Intent intent = new Intent(TopTrackActivity.this, SongPlayingActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("value", 2);
                                    startActivity(intent);
                                }
                                break;
                            case R.id.play_next:
                                  type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
                                arrayList = new GsonBuilder().create().fromJson(sessionManager.getTopTrackList(), type);
                               // List<AudioModel> arrayList = MusicUtils.getSongsForAlbum(TopTrackActivity.this, sessionManager.getAlbumId());
                                if(arrayList.size()!=0) {
                                    playing_audio_list = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
                                    playing_audio_list.addAll(sessionManager.getSongPosition() + 1, arrayList);
                                    Gson gson_list = new GsonBuilder().create();
                                    JsonArray song_array = gson_list.toJsonTree(playing_audio_list).getAsJsonArray();
                                    sessionManager.setSong_JsonList(song_array.toString());
                                }
                                break;

                            case R.id.add_to_playlist:
                                type = new TypeToken<ArrayList<AudioModel>>() {}.getType();
                                audioModelList = new GsonBuilder().create().fromJson(sessionManager.getTopTrackList(), type);
//                                audioModelList = MusicUtils.getSongsForAlbum(AlbumDetailsActivity.this, sessionManager.getAlbumId());
                                if (audioModelList.size()!=0)
                                add_to_playlist_alert(TopTrackActivity.this);
                                //all_playlist(context);
                                break;
                            case R.id.share:
//                                sessionManager.setAlbumId(albumModelList.get(position).getId());
                                type = new TypeToken<ArrayList<AudioModel>>() {}.getType();
                                List<AudioModel> audioModelList1 = new GsonBuilder().create().fromJson(sessionManager.getTopTrackList(), type);
//                                 audioModelList1 = MusicUtils.getSongsForAlbum(TopTrackActivity.this ,sessionManager.getAlbumId());
                                if(audioModelList1.size()!=0)
                                shareFile(audioModelList1);
                                break;
                            case R.id.add_to_home_screen:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    add_add_short();
                                }
                                break;

                        }
                        return true;
                    }
                });
                popup.show(); //sho
            }
        });


        for(int i=songPosition-1;i<audioModelList.size();i++)
        {
            try {
                Uri uri = Uri.parse(audioModelList.get(i).getaPath());
                System.out.println("aji play q path "+audioModelList.get(i).getaPath());
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(TopTrackActivity.this,uri);
                String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                millSecond =millSecond+Integer.parseInt(durationStr);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        song_duration.setText("Up next . "+songPosition+"/"+size+" . "+String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millSecond), TimeUnit.MILLISECONDS.toMinutes(millSecond) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millSecond)), TimeUnit.MILLISECONDS.toSeconds(millSecond) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millSecond))));
        AlbumSongsAdapter songsAdapter=new AlbumSongsAdapter(TopTrackActivity.this,audioModelList);
        queue_song_recycler.setAdapter(songsAdapter);

    }
    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                TopTrackActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "HelloWorldShortcut");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.drawable.app_logo));
        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void add_add_short() {
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

//Create a ShortcutInfo object that defines all the shortcut’s characteristics//
        Intent intent = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, TopTrackActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("title",title);

        ShortcutInfo shortcut = new ShortcutInfo.Builder(this, title)
                .setShortLabel(title)
                .setLongLabel(title)
                .setIcon(Icon.createWithResource(this, R.drawable.app_logo))
                .setIntent(intent)
                .build();

        shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));

//Check that the device's default launcher supports pinned shortcuts//

        if (shortcutManager.isRequestPinShortcutSupported()) {
            ShortcutInfo pinShortcutInfo = new ShortcutInfo
                    .Builder(TopTrackActivity.this,title)
                    .build();
            Intent pinnedShortcutCallbackIntent =
                    shortcutManager.createShortcutResultIntent(pinShortcutInfo);

//Get notified when a shortcut is pinned successfully//

            PendingIntent successCallback = PendingIntent.getActivity(TopTrackActivity.this, 0,
                    pinnedShortcutCallbackIntent, 0);
            shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.getIntentSender());

//            Intent profileupdate = new Intent(TopTrackActivity.this, TopTrackActivity.class);
//            profileupdate.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            profileupdate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            PendingIntent profileupdateview = PendingIntent.getActivity(this, 0, profileupdate,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
        }



}

    public void add_to_playlist_alert(final Context context){



//        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = LayoutInflater.from(context).inflate(R.layout.add_to_playlist_alert, null);

        Realm.init(context);
        RealmConfiguration main_data_config = new RealmConfiguration.Builder().name("music.realm").deleteRealmIfMigrationNeeded().build();
        music_database = Realm.getInstance(main_data_config);
//        final Dialog alertDialog = new Dialog(context);
//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        alertDialog.setContentView(R.layout.add_to_playlist_alert);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //  alertDialog.setCancelable(false);
        playlist_alert_recyclerView = alertLayout.findViewById(R.id.playlist_alert_recyclerView);
        background_of_layout = alertLayout.findViewById(R.id.background_of_layout);
        add_to_playlist_add = alertLayout.findViewById(R.id.add_to_playlist_add);
        add_to_playlist_cancel = alertLayout.findViewById(R.id.add_to_playlist_cancel);
//        playlist_alert_recyclerView = alertDialog.findViewById(R.id.playlist_alert_recyclerView);
//        background_of_layout = alertDialog.findViewById(R.id.background_of_layout);
//        add_to_playlist_add = alertDialog.findViewById(R.id.add_to_playlist_add);
//        add_to_playlist_cancel = alertDialog.findViewById(R.id.add_to_playlist_cancel);
        sessionManager=new SessionManager(context);
        if(sessionManager.getIsDefaultThemeSelected())
        {
            background_of_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(context,selectedImageUri));
                bg= Drawable.createFromPath(f.getAbsolutePath());


//                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                encodeTobase64(bitmap);
            }
            catch (Exception e)
            {
                bg = ContextCompat.getDrawable(context, R.drawable.theme_1);
                e.printStackTrace();

            }
//            Drawable dr = new BitmapDrawable(sessionManager.getFileManagerTheme());
            background_of_layout.setBackground(bg);
        }
        playlist_alert_recyclerView.setLayoutManager(new GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false));
        playlist_alert_recyclerView.setHasFixedSize(true);
        playlist_alert_recyclerView.setNestedScrollingEnabled(false);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(false);
        alert.setView(alertLayout);

//        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });


        all_playlist(context);
        final AlertDialog dialog = alert.create();
        dialog.show();
//        alertDialog.show();
        add_to_playlist_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm.init(context);
                RealmConfiguration main_data_config = new RealmConfiguration.Builder().name("playlists.realm").deleteRealmIfMigrationNeeded().build();
                playlist_database = Realm.getInstance(main_data_config);

                if(sessionManager.getDupilcatePlaylistSongId()==1){
                    if(playlistAddAdapter.playlistId_list.size()>0)
                    {
                        for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++){




                            // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
                            long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));
                            for(int val =0 ; val<audioModelList.size();val++) {


                                playlist_database.beginTransaction();
                                PlaylistAudioModel playlistAudioModel=playlist_database.createObject(PlaylistAudioModel.class);
                                playlistAudioModel.setPlaylist_id(a);
                                playlistAudioModel.setaPath(audioModelList.get(val).getaPath());
                                playlistAudioModel.setaName(audioModelList.get(val).getaName());
                                playlistAudioModel.setaAlbum(audioModelList.get(val).getaAlbum());
                                playlistAudioModel.setAlbum_id(audioModelList.get(val).getAlbum_id());
                                playlistAudioModel.setaArtist(audioModelList.get(val).getaArtist());
                                playlistAudioModel.setArtist_id(audioModelList.get(val).getArtist_id());
                                playlistAudioModel.setTrack(audioModelList.get(val).getTrack());
                                playlistAudioModel.setLength(audioModelList.get(val).getLength());
                                playlistAudioModel.setSong_added_on(audioModelList.get(val).getSong_added_on());
                                playlist_database.commitTransaction();
                                playlistAddAdapter.notifyDataSetChanged();
                                System.out.println("aji " + i + " pl_ID " + playlistAddAdapter.playlistId_list.get(i));
                                System.out.println("aji " + playlist_database.toString());


                            }

                        }

                    }
                }else if(sessionManager.getDupilcatePlaylistSongId()==3){
                    if(playlistAddAdapter.playlistId_list.size()>0)
                    {
                        for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++){




                            // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
                            long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));

                            for(int val =0 ; val<audioModelList.size();val++) {

                                PlaylistAudioModel result = playlist_database.where(PlaylistAudioModel.class).equalTo("playlist_id", a).equalTo("aPath", audioModelList.get(val).getaPath()).findFirst();
                                if (result == null) {
                                    playlist_database.beginTransaction();
                                    PlaylistAudioModel playlistAudioModel = playlist_database.createObject(PlaylistAudioModel.class);
                                    playlistAudioModel.setPlaylist_id(a);
                                    playlistAudioModel.setaPath(audioModelList.get(val).getaPath());
                                    playlistAudioModel.setaName(audioModelList.get(val).getaName());
                                    playlistAudioModel.setaAlbum(audioModelList.get(val).getaAlbum());
                                    playlistAudioModel.setAlbum_id(audioModelList.get(val).getAlbum_id());
                                    playlistAudioModel.setaArtist(audioModelList.get(val).getaArtist());
                                    playlistAudioModel.setArtist_id(audioModelList.get(val).getArtist_id());
                                    playlistAudioModel.setTrack(audioModelList.get(val).getTrack());
                                    playlistAudioModel.setLength(audioModelList.get(val).getLength());
                                    playlistAudioModel.setSong_added_on(audioModelList.get(val).getSong_added_on());
                                    playlist_database.commitTransaction();
                                    playlistAddAdapter.notifyDataSetChanged();
                                    System.out.println("aji " + i + " pl_ID " + playlistAddAdapter.playlistId_list.get(i));
                                    System.out.println("aji " + playlist_database.toString());
                                }

                            }

                        }

                    }
                }
                else if(sessionManager.getDupilcatePlaylistSongId()==2){
                    if(playlistAddAdapter.playlistId_list.size()>0)
                    {
                        for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++){




                            // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
                            long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));

                            for(int val =0 ; val<audioModelList.size();val++) {

                                PlaylistAudioModel result = playlist_database.where(PlaylistAudioModel.class).equalTo("playlist_id", a).equalTo("aPath", audioModelList.get(val).getaPath()).findFirst();
                                if (result == null) {
                                    playlist_database.beginTransaction();
                                    PlaylistAudioModel playlistAudioModel = playlist_database.createObject(PlaylistAudioModel.class);
                                    playlistAudioModel.setPlaylist_id(a);
                                    playlistAudioModel.setaPath(audioModelList.get(val).getaPath());
                                    playlistAudioModel.setaName(audioModelList.get(val).getaName());
                                    playlistAudioModel.setaAlbum(audioModelList.get(val).getaAlbum());
                                    playlistAudioModel.setAlbum_id(audioModelList.get(val).getAlbum_id());
                                    playlistAudioModel.setaArtist(audioModelList.get(val).getaArtist());
                                    playlistAudioModel.setArtist_id(audioModelList.get(val).getArtist_id());
                                    playlistAudioModel.setTrack(audioModelList.get(val).getTrack());
                                    playlistAudioModel.setLength(audioModelList.get(val).getLength());
                                    playlistAudioModel.setSong_added_on(audioModelList.get(val).getSong_added_on());
                                    playlist_database.commitTransaction();
                                    playlistAddAdapter.notifyDataSetChanged();
                                    System.out.println("aji " + i + " pl_ID " + playlistAddAdapter.playlistId_list.get(i));
                                    System.out.println("aji " + playlist_database.toString());
                                }
                                else{
                                    PlaylistAudioModel playlistAudioModel = new PlaylistAudioModel();
                                    playlistAudioModel.setPlaylist_id(a);
                                    playlistAudioModel.setaPath(audioModelList.get(val).getaPath());
                                    playlistAudioModel.setaName(audioModelList.get(val).getaName());
                                    playlistAudioModel.setaAlbum(audioModelList.get(val).getaAlbum());
                                    playlistAudioModel.setAlbum_id(audioModelList.get(val).getAlbum_id());
                                    playlistAudioModel.setaArtist(audioModelList.get(val).getaArtist());
                                    playlistAudioModel.setArtist_id(audioModelList.get(val).getArtist_id());
                                    playlistAudioModel.setTrack(audioModelList.get(val).getTrack());
                                    playlistAudioModel.setLength(audioModelList.get(val).getLength());
                                    playlistAudioModel.setSong_added_on(audioModelList.get(val).getSong_added_on());
                                    DuplicatePlaylistAdapter.duplicates.add(playlistAudioModel);
                                }
                            }

                        }
                        if(DuplicatePlaylistAdapter.duplicates.size()!=0)
                        {
                            System.out.println("aji duplicatelist size "+DuplicatePlaylistAdapter.duplicates.size());

//                            LayoutInflater inflater = getLayoutInflater();
//                            View alertLayout = inflater.inflate(R.layout.duplicate_playlist_alert, null);
                            View alertLayout2 = LayoutInflater.from(context).inflate(R.layout.ask_first_alert, null);
                            background_of_layout = alertLayout2.findViewById(R.id.background_of_layout);
                            duplicate_recycler = alertLayout2.findViewById(R.id.duplicate_recycler);
                            skip_all = alertLayout2.findViewById(R.id.skip_all);
                            add_all = alertLayout2.findViewById(R.id.add_all);


                            if (sessionManager.getIsDefaultThemeSelected()) {
                                background_of_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
                            }
                            else {
                                Drawable bg;
                                Uri selectedImageUri = Uri.parse(sessionManager.getFileManagerTheme());
                                try {

                                    File f = new File(Global.getRealPathFromURI(context, selectedImageUri));
                                    bg = Drawable.createFromPath(f.getAbsolutePath());



//                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                encodeTobase64(bitmap);
                                } catch (Exception e) {
                                    bg = ContextCompat.getDrawable(context, R.drawable.theme_1);
                                    e.printStackTrace();

                                }
//            Drawable dr = new BitmapDrawable(sessionManager.getFileManagerTheme());
                                background_of_layout.setBackground(bg);
//                    background_of_layout.setBackgroundResource(R.drawable.theme_1);

                            }
                            android.app.AlertDialog.Builder alert2 = new android.app.AlertDialog.Builder(context);
                            alert2.setView(alertLayout2);
//                alert.setCancelable(false);
                            duplicate_recycler.setLayoutManager(new GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false));
                            duplicate_recycler.setHasFixedSize(true);
                            duplicate_recycler.setNestedScrollingEnabled(false);

                            final android.app.AlertDialog dialog2 = alert2.create();
                            duplicatePlaylistAdapter=new DuplicatePlaylistAdapter(context,DuplicatePlaylistAdapter.duplicates,dialog2);
                            duplicate_recycler.setAdapter(duplicatePlaylistAdapter);
                            dialog2.show();

                            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            skip_all.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DuplicatePlaylistAdapter.duplicates = new ArrayList<>();
                                    dialog2.dismiss();
                                }
                            });
                            add_all.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(DuplicatePlaylistAdapter.duplicates!=null)
                                    {
                                        for (int z=0;z<DuplicatePlaylistAdapter.duplicates.size();z++)
                                        {
                                            playlist_database.beginTransaction();

                                            PlaylistAudioModel playlistAudioModel=playlist_database.createObject(PlaylistAudioModel.class);

                                            // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());

                                            playlistAudioModel.setPlaylist_id(DuplicatePlaylistAdapter.duplicates.get(z).getPlaylist_id());
                                            playlistAudioModel.setaPath(DuplicatePlaylistAdapter.duplicates.get(z).getaPath());
                                            playlistAudioModel.setaName(DuplicatePlaylistAdapter.duplicates.get(z).getaName());
                                            playlistAudioModel.setaAlbum(DuplicatePlaylistAdapter.duplicates.get(z).getaAlbum());
                                            playlistAudioModel.setAlbum_id(DuplicatePlaylistAdapter.duplicates.get(z).getAlbum_id());
                                            playlistAudioModel.setaArtist(DuplicatePlaylistAdapter.duplicates.get(z).getaArtist());
                                            playlistAudioModel.setArtist_id(DuplicatePlaylistAdapter.duplicates.get(z).getArtist_id());
                                            playlistAudioModel.setTrack(DuplicatePlaylistAdapter.duplicates.get(z).getTrack());
                                            playlistAudioModel.setLength(DuplicatePlaylistAdapter.duplicates.get(z).getLength());
                                            playlistAudioModel.setSong_added_on(DuplicatePlaylistAdapter.duplicates.get(z).getSong_added_on());
                                            playlist_database.commitTransaction();
                                        }
                                        DuplicatePlaylistAdapter.duplicates = new ArrayList<>();
                                        duplicatePlaylistAdapter.notifyDataSetChanged();
                                    }
                                    dialog2.dismiss();
                                }
                            });


                        }

                    }
                }

                dialog.dismiss();


            }
        });
        add_to_playlist_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }
    public void all_playlist(Context context)
    {
        playlistModelList = music_database.where(PlaylistModel.class).findAll();
//        playlistAudioModel = playlist_database.where(PlaylistAudioModel.class).findAll();
//                .sort("dateTimeStart", Sort.DESCENDING);
        if(playlistModelList.size()==0)
        {
//            playlist_count.setVisibility(View.GONE);
            Number currentIdNum = music_database.where(PlaylistModel.class).max("playlist_id");
            int nextId;
            if(currentIdNum == null) {
                nextId = 1;
                System.out.println("aji nextId " + nextId);
                music_database.beginTransaction();
                PlaylistModel playlistModel = music_database.createObject(PlaylistModel.class, nextId);
                playlistModel.setPlaylist_name("Favourites");
                music_database.commitTransaction();
            }
        }
        else
        {
//            playlist_count.setText(""+playlistModelList.size());
        }
        playlistAddAdapter=new PlaylistAddAdapter(context,playlistModelList);
        playlist_alert_recyclerView.setAdapter(playlistAddAdapter);


    }
    private void shareFile(List<AudioModel> audioModelList_temp) {

        List<AudioModel> audioModelList1 =audioModelList_temp;

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);

        intent.setType("text/*"); /* This example is sharing jpeg images. */
        File f = new File(audioModelList1.get(0).getaPath());

        ArrayList<Uri> files = new ArrayList<Uri>();
        for (int z =0;z<audioModelList1.size();z++) {

            File file = new File(audioModelList1.get(z).getaPath());

            Uri uri = Uri.fromFile(file);
            files.add(uri);
        }
        intent.putExtra(Intent.EXTRA_TEXT, "-Shared via Muzio Player");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        intent.putExtra(Intent.EXTRA_SUBJECT,  f.getName());
        startActivity(Intent.createChooser(intent, f.getName()));

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
