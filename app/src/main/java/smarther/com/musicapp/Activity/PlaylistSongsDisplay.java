package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import smarther.com.musicapp.Adapter.DuplicatePlaylistAdapter;
import smarther.com.musicapp.Adapter.PlaylistAddAdapter;
import smarther.com.musicapp.Adapter.PlaylistItemAdapter;
import smarther.com.musicapp.Adapter.PlaylistSongsAdapter;
import smarther.com.musicapp.Adapter.SongsAdapter;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.Model.PlaylistModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Service.BackgroundMusicService;
import smarther.com.musicapp.Utils.Constants;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.MusicUtils;
import smarther.com.musicapp.Utils.SessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlaylistSongsDisplay extends AppCompatActivity {

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
//    List<AudioModel> audioModelList;
    TextView add_all;
    TextView skip_all;
    RecyclerView duplicate_recycler;
    DuplicatePlaylistAdapter duplicatePlaylistAdapter;
    List<PlaylistAudioModel> playlistAudioModelList=new ArrayList<>();



    Realm playlist_songs_relam;
   // SongPlayingActivity songPlayingActivity;
    SessionManager sessionManager;
    JSONObject jsonObject;
    List<AudioModel> audioModelList = new ArrayList<>();
    int millSecond=0;
    RelativeLayout background_of_layout1;
   public LinearLayout back;
    LinearLayout plus;
    LinearLayout queue_music;
    LinearLayout folder_plus;
    LinearLayout scan_3dot_btn;
    LinearLayout currentSongpage;
    ImageView play_button;
    TextView playlist_name;
    public TextView playlist_detail;
    public   TextView playing_song_folder;


    public TextView playing_song_artist;
    public  TextView playing_song_name;
    RecyclerView playlist_song_recycler;
    public long playlist_id;
  //  List<PlaylistAudioModel> playlistAudioModel=new ArrayList<>();
//    List<AudioModel> audioModelList=new ArrayList<>();
 //   List<AudioModel> playing_audio_list=new ArrayList<>();
//    List<AudioModel> playing_audio_list=new ArrayList<>();
public static PlaylistSongsAdapter playlistItemAdapter;
    public MusicIntentReceiver myReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_songs_display);
        background_of_layout1=findViewById(R.id.background_of_layout1);
        back=findViewById(R.id.back);
        plus=findViewById(R.id.plus);

        folder_plus=findViewById(R.id.folder_plus);
        queue_music=findViewById(R.id.queue_music);
        play_button=findViewById(R.id.play_button);
        playlist_detail=findViewById(R.id.playlist_detail);
        currentSongpage=findViewById(R.id.currentSongpage);

        scan_3dot_btn=findViewById(R.id.scan_3dot_btn);
        playing_song_name=findViewById(R.id.playing_song_name);
        playing_song_folder=findViewById(R.id.playing_song_folder);
        playing_song_artist=findViewById(R.id.playing_song_artist);

        playlist_name=findViewById(R.id.playlist_name);
        myReceiver = new MusicIntentReceiver();

        playlist_song_recycler = (RecyclerView)findViewById(R.id.playlist_song_recycler);
        playlist_song_recycler.setLayoutManager(new GridLayoutManager(PlaylistSongsDisplay.this, 1, LinearLayoutManager.VERTICAL, false));
        playlist_song_recycler.setHasFixedSize(true);
        playlist_song_recycler.setNestedScrollingEnabled(false);
        Realm.init(PlaylistSongsDisplay.this);
        RealmConfiguration main_data_config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name("playlists.realm").build();
        playlist_songs_relam = Realm.getInstance(main_data_config);
        Realm.init(PlaylistSongsDisplay.this);
         main_data_config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name("music.realm").build();
        music_database = Realm.getInstance(main_data_config);
//        try {
//            jsonObject = new JSONObject(sessionManager.getSong_Json());
//            playing_song_artist.setText(jsonObject.optString("aArtist"));
//            playing_song_folder.setText(jsonObject.optString("aAlbum"));
//            playing_song_name.setText(jsonObject.optString("track"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        if(BackgroundMusicService.bg_mediaPlayer!=null)
        {
            System.out.println("aji bg notnull");
            if(BackgroundMusicService.bg_mediaPlayer.isPlaying())
            {
                System.out.println("aji playing");
                play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
            }
            else
            {
                System.out.println("aji bg notnull");
                play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
            }
        }
        else
        {
            System.out.println("aji bg null");
           // Global.intent_method(PlaylistSongsDisplay.this, SongPlayingActivity.class);
        }
        Intent intent = getIntent();
         playlist_id =intent.getLongExtra("playlist_id",-1);
        String playlist_nam =intent.getStringExtra("playlist_name");
        playlist_name.setText(playlist_nam);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global.intent_method(PlaylistSongsDisplay.this,PlaylistAddSearch.class);
                Intent intent=new Intent(PlaylistSongsDisplay.this,PlaylistAddSearch.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("playlist_id",playlist_id);
                PlaylistSongsDisplay.this.startActivity(intent);


            }
        });
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(BackgroundMusicService.bg_mediaPlayer!=null)
               {
                   System.out.println("aji not null");
                   if(BackgroundMusicService.bg_mediaPlayer.isPlaying())
                   {

                       play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                   }
                   else
                   {
                       play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                   }
                   Intent serviceIntent = new Intent(PlaylistSongsDisplay.this, BackgroundMusicService.class);
                   serviceIntent.setAction(Constants.ACTION.PLAY_ACTION);
                   startService(serviceIntent);
               }
               else
               {
                   play_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
//                   Global.intent_method(PlaylistSongsDisplay.this, SongPlayingActivity.class);
                   Intent intent=new Intent(PlaylistSongsDisplay.this,SongPlayingActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   intent.putExtra("value",2);
                   PlaylistSongsDisplay.this.startActivity(intent);
               }
            }
        });
        queue_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PlaylistSongsDisplay.this,PlayingQueue.class);
                intent.putExtra("title","Playing queue");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        currentSongpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   songPlayingActivity.on_song_play_UiChange("1");
               // Global.intent_method(PlaylistSongsDisplay.this, SongPlayingActivity.class);
                Intent intent=new Intent(PlaylistSongsDisplay.this,SongPlayingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("value",1);
                PlaylistSongsDisplay.this.startActivity(intent);
            }
        });
        scan_3dot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(PlaylistSongsDisplay.this, R.style.popupMenuStyle);
//                PopupMenu popup = new PopupMenu(wrapper, view);
                PopupMenu popup = new PopupMenu(wrapper, scan_3dot_btn);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.playlist_page_detail_title_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.play_next:

                                    long ids = playlist_id;
                                    playlistAudioModel = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id", ids).findAll();
                                    Type types = new TypeToken<ArrayList<AudioModel>>() {}.getType();
                                    List<AudioModel> audioModelList1 = new ArrayList<>();
                                    audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), types);
                                    int size = playlistAudioModel.size();
                                    for (int i = 0; i < size; i++) {
                                        AudioModel audioModel = new AudioModel();
                                        audioModel.setaName(playlistAudioModel.get(i).getaName());
                                        audioModel.setaAlbum(playlistAudioModel.get(i).getaAlbum());
                                        audioModel.setAlbum_id(playlistAudioModel.get(i).getAlbum_id());
                                        audioModel.setArtist_id(playlistAudioModel.get(i).getArtist_id());
                                        audioModel.setaArtist(playlistAudioModel.get(i).getaArtist());
                                        audioModel.setaPath(playlistAudioModel.get(i).getaPath());
                                        audioModel.setaAlbumart(playlistAudioModel.get(i).getaAlbumart());
                                        audioModel.setTrack(playlistAudioModel.get(i).getTrack());
                                        audioModelList1.add(audioModel);
                                    }
                                    audioModelList.addAll(1, audioModelList1);

                                    Gson gson_lists = new GsonBuilder().create();
                                    JsonArray song_arrays = gson_lists.toJsonTree(audioModelList).getAsJsonArray();
                                    sessionManager.setSong_JsonList(song_arrays.toString());


                                break;
                            case R.id.play:



                                     ids = playlist_id;
                                    playlistAudioModel = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id", ids).findAll();
                                     types = new TypeToken<ArrayList<AudioModel>>() {}.getType();
                                     audioModelList1 = new ArrayList<>();
                                    List<AudioModel> audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), types);
                                     size = playlistAudioModel.size();
                                    for (int i = 0; i < size; i++) {
                                        AudioModel audioModel = new AudioModel();
                                        audioModel.setaName(playlistAudioModel.get(i).getaName());
                                        audioModel.setaAlbum(playlistAudioModel.get(i).getaAlbum());
                                        audioModel.setAlbum_id(playlistAudioModel.get(i).getAlbum_id());
                                        audioModel.setArtist_id(playlistAudioModel.get(i).getArtist_id());
                                        audioModel.setaArtist(playlistAudioModel.get(i).getaArtist());
                                        audioModel.setaPath(playlistAudioModel.get(i).getaPath());
                                        audioModel.setaAlbumart(playlistAudioModel.get(i).getaAlbumart());
                                        audioModel.setTrack(playlistAudioModel.get(i).getTrack());
                                        audioModelList1.add(audioModel);
                                    }

                                    AudioModel audioModel = audioModelList1.get(0);
                                    Gson gson = new Gson();
                                    String json = gson.toJson(audioModel);
                                    sessionManager.setSong_Json(json);

                                    Gson gson_list = new GsonBuilder().create();
                                    JsonArray song_array = gson_list.toJsonTree(audioModelList1).getAsJsonArray();
                                    sessionManager.setSong_JsonList(song_array.toString());
                                    sessionManager.setSongPosition(0);
                                    //Global.intent_method(context, SongPlayingActivity.class);
                                    Intent intent = new Intent(PlaylistSongsDisplay.this, SongPlayingActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("value", 2);
                                    startActivity(intent);

                                break;
                            case R.id.add_to_playing_queue:

                                     ids = playlist_id;
                                    playlistAudioModel = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id", ids).findAll();
                                     types = new TypeToken<ArrayList<AudioModel>>() {}.getType();
                                    audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), types);
                                     size = playlistAudioModel.size();
                                    for (int i = 0; i < size; i++) {
                                         audioModel = new AudioModel();
                                        audioModel.setaName(playlistAudioModel.get(i).getaName());
                                        audioModel.setaAlbum(playlistAudioModel.get(i).getaAlbum());
                                        audioModel.setAlbum_id(playlistAudioModel.get(i).getAlbum_id());
                                        audioModel.setArtist_id(playlistAudioModel.get(i).getArtist_id());
                                        audioModel.setaArtist(playlistAudioModel.get(i).getaArtist());
                                        audioModel.setaPath(playlistAudioModel.get(i).getaPath());
                                        audioModel.setaAlbumart(playlistAudioModel.get(i).getaAlbumart());
                                        audioModel.setTrack(playlistAudioModel.get(i).getTrack());
                                        audioModelList.add(audioModel);
                                    }

                                     gson_lists = new GsonBuilder().create();
                                     song_arrays = gson_lists.toJsonTree(audioModelList).getAsJsonArray();
                                    sessionManager.setSong_JsonList(song_arrays.toString());
//                                final Type types = new TypeToken<ArrayList<AudioModel>>() {
//                                }.getType();
//                                playing_audio_list = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), types);
//                                playing_audio_list.add(audioModelList.get(position));
//                                Gson gson_lists = new GsonBuilder().create();
//                                JsonArray song_arrays = gson_lists.toJsonTree(playing_audio_list).getAsJsonArray();
//                                sessionManager.setSong_JsonList(song_arrays.toString());

                                break;
                            case R.id.add_to_playlist:

                                add_to_playlist_alert(PlaylistSongsDisplay.this);
                                playlistItemAdapter.notifyDataSetChanged();

//                                audioModelList = MusicUtils.getSongsForAlbum(PlaylistSongsDisplay.this, sessionManager.getAlbumId());
//                                add_to_playlist_alert(PlaylistSongsDisplay.this);
                                //all_playlist(context);
                                break;
                            case R.id.clear_playlist:
                              long id = playlist_id;
                                RealmResults<PlaylistAudioModel> result = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id",id).findAll();
                                playlist_songs_relam.beginTransaction();
                                result.deleteAllFromRealm();
                                playlist_songs_relam.commitTransaction();
                                onBackPressed();
                                break;
                            case R.id.share:
                                 ids = playlist_id;
                                playlistAudioModel = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id", ids).findAll();
                                 audioModelList1 = new ArrayList<>();
                                 size = playlistAudioModel.size();
                                for (int i = 0; i < size; i++) {
                                     audioModel = new AudioModel();
                                    audioModel.setaName(playlistAudioModel.get(i).getaName());
                                    audioModel.setaAlbum(playlistAudioModel.get(i).getaAlbum());
                                    audioModel.setAlbum_id(playlistAudioModel.get(i).getAlbum_id());
                                    audioModel.setArtist_id(playlistAudioModel.get(i).getArtist_id());
                                    audioModel.setaArtist(playlistAudioModel.get(i).getaArtist());
                                    audioModel.setaPath(playlistAudioModel.get(i).getaPath());
                                    audioModel.setaAlbumart(playlistAudioModel.get(i).getaAlbumart());
                                    audioModel.setTrack(playlistAudioModel.get(i).getTrack());
                                    audioModelList1.add(audioModel);
                                }
                                if(playlistAudioModel.size()>0)
                                    shareFile(audioModelList1);
                                break;
                            case R.id.delete:


                                 id = playlist_id;
                                 result = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id",id).findAll();
                                playlist_songs_relam.beginTransaction();
                                result.deleteAllFromRealm();
                                playlist_songs_relam.commitTransaction();
                                //final PlaylistModel playlistModel = music_database.where(PlaylistModel.class).equalTo("playlist_id",id).findFirst();
                                RealmResults<PlaylistModel> res = music_database.where(PlaylistModel.class).equalTo("playlist_id",id).findAll();

//                                music_database.executeTransaction(new Realm.Transaction() {
//                                    @Override
//                                    public void execute(Realm realm) {
//                                        // on below line we are calling a method for deleting this course
//                                        playlistModel.deleteFromRealm();
//                                    }
//                                });
//
                                music_database.beginTransaction();
                                res.deleteAllFromRealm();
                                music_database.commitTransaction();
                                onBackPressed();
                                break;

                        }
                        return true;
                    }
                });

                popup.show(); //sho


            }
        });
        folder_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Type types = new TypeToken<ArrayList<AudioModel>>(){}.getType();
                playing_audio_list= new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), types);
                int size = playlistAudioModel.size();
                for (int i = 0;i<size;i++) {
                    AudioModel audioModel = new AudioModel();
                    audioModel.setaName(playlistAudioModel.get(i).getaName());
                    audioModel.setaAlbum(playlistAudioModel.get(i).getaAlbum());
                    audioModel.setAlbum_id(playlistAudioModel.get(i).getAlbum_id());
                    audioModel.setArtist_id(playlistAudioModel.get(i).getArtist_id());
                    audioModel.setaArtist(playlistAudioModel.get(i).getaArtist());
                    audioModel.setaPath(playlistAudioModel.get(i).getaPath());
                    audioModel.setaAlbumart(playlistAudioModel.get(i).getaAlbumart());
                    audioModel.setTrack(playlistAudioModel.get(i).getTrack());
                    playing_audio_list.add(audioModel);
                }

                Gson gson_lists = new GsonBuilder().create();
                JsonArray song_arrays = gson_lists.toJsonTree(playing_audio_list).getAsJsonArray();
                sessionManager.setSong_JsonList(song_arrays.toString());
                Toast.makeText(PlaylistSongsDisplay.this,"Added "+size+" Tiles to the playing queue",Toast.LENGTH_SHORT).show();
            }
        });

        sessionManager=new SessionManager(PlaylistSongsDisplay.this);
        if(sessionManager.getIsDefaultThemeSelected())
        {
            background_of_layout1.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(PlaylistSongsDisplay.this,selectedImageUri));
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
            background_of_layout1.setBackground(bg);
        }

        //playing_song_name.setText("hello");

//        final Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
//        audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_Json(), type);
//        audioModelList.
        try {
            jsonObject = new JSONObject(sessionManager.getSong_Json());
            playing_song_artist.setText(jsonObject.optString("aArtist"));
            playing_song_folder.setText(jsonObject.optString("aAlbum"));
            playing_song_name.setText(jsonObject.optString("track"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        final Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
//        audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
//        for(int i=0;i<audioModelList.size();i++)
//        {
//
//            Uri uri = Uri.parse(audioModelList.get(i).getaPath());
//            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//            mmr.setDataSource(PlaylistSongsDisplay.this,uri);
//            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//            millSecond =millSecond+Integer.parseInt(durationStr);
//        }

        playlistAudioModel =  playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id",playlist_id ).findAll();
        String size = String.valueOf(playlistAudioModel.size());
        for(int i=0;i<playlistAudioModel.size();i++)
        {
            Uri uri = Uri.parse(playlistAudioModel.get(i).getaPath());
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(PlaylistSongsDisplay.this,uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            millSecond =millSecond+Integer.parseInt(durationStr);
        }
//        int song = sessionManager.getSongPosition();
//        System.out.println("aji song pos "+song);
//        playlist_detail.setText(size);

        playlist_detail.setText(" "+size+" Songs"+" . "+String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millSecond), TimeUnit.MILLISECONDS.toMinutes(millSecond) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millSecond)), TimeUnit.MILLISECONDS.toSeconds(millSecond) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millSecond))));

        playlistItemAdapter=new PlaylistSongsAdapter(PlaylistSongsDisplay.this,playlistAudioModel,PlaylistSongsDisplay.this);
        playlist_song_recycler.setAdapter(playlistItemAdapter);

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
    public void add_to_playlist_alert(Context context){

        Realm.init(context);
        RealmConfiguration   main_data_config = new RealmConfiguration.Builder().name("playlists.realm").deleteRealmIfMigrationNeeded().build();
        playlist_database = Realm.getInstance(main_data_config);

//        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = LayoutInflater.from(context).inflate(R.layout.add_to_playlist_alert, null);

        Realm.init(context);
         main_data_config = new RealmConfiguration.Builder().name("music.realm").deleteRealmIfMigrationNeeded().build();
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
                if(playlistAddAdapter.playlistId_list.size()>0)
                {
                    for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++){





                        // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
                        long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));  //aji
                        playlistAudioModelList =  new ArrayList<>(playlistAudioModel);
                        for (int j=0;j<playlistAudioModelList.size();j++){
                            try {
                                System.out.println("aji playlist addplaylist "+j);
                                playlist_database.beginTransaction();
                                PlaylistAudioModel playlistAudioModel=playlist_database.createObject(PlaylistAudioModel.class);
                                playlistAudioModel.setPlaylist_id(a);
                                playlistAudioModel.setaPath(playlistAudioModelList.get(j).getaPath());
                                playlistAudioModel.setaName(playlistAudioModelList.get(j).getaName());
                                playlistAudioModel.setaAlbum(playlistAudioModelList.get(j).getaAlbum());
                                playlistAudioModel.setAlbum_id(playlistAudioModelList.get(j).getAlbum_id());
                                playlistAudioModel.setaArtist(playlistAudioModelList.get(j).getaArtist());
                                playlistAudioModel.setArtist_id(playlistAudioModelList.get(j).getArtist_id());
                                playlistAudioModel.setTrack(playlistAudioModelList.get(j).getTrack());
                                playlistAudioModel.setLength(playlistAudioModelList.get(j).getLength());
                                playlistAudioModel.setSong_added_on(playlistAudioModelList.get(j).getSong_added_on());
                                playlist_database.commitTransaction();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
//                        playlistAudioModel.setPlaylist_id(a);
//                        playlistAudioModel.setaPath(audioModelList.get(position).getaPath());
//                        playlistAudioModel.setaName(audioModelList.get(position).getaName());
//                        playlistAudioModel.setaAlbum(audioModelList.get(position).getaAlbum());
//                        playlistAudioModel.setAlbum_id(audioModelList.get(position).getAlbum_id());
//                        playlistAudioModel.setaArtist(audioModelList.get(position).getaArtist());
//                        playlistAudioModel.setArtist_id(audioModelList.get(position).getArtist_id());
//                        playlistAudioModel.setTrack(audioModelList.get(position).getTrack());
//                        playlistAudioModel.setLength(audioModelList.get(position).getLength());
//                        playlistAudioModel.setSong_added_on(audioModelList.get(position).getSong_added_on());
//                        playlist_database.commitTransaction();
                        playlistAddAdapter.notifyDataSetChanged();
                        System.out.println("aji "+i+" pl_ID "+ playlistAddAdapter.playlistId_list.get(i));
                        System.out.println("aji "+playlist_database.toString());

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
