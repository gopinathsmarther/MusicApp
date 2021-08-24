package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import smarther.com.musicapp.Adapter.AlbumSongsAdapter;
import smarther.com.musicapp.Adapter.ArtistAlbumDetailAdapter;
import smarther.com.musicapp.Adapter.DuplicatePlaylistAdapter;
import smarther.com.musicapp.Adapter.PlaylistAddAdapter;
import smarther.com.musicapp.Adapter.SongsAdapter;
import smarther.com.musicapp.Model.AlbumModel;
import smarther.com.musicapp.Model.ArtistsModel;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArtistsDetailsActivity extends AppCompatActivity {
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



    LinearLayout navigate_back;
    LinearLayout artist_to_search;
    LinearLayout artist_details_background;

    RelativeLayout no_artist_image;

    ImageView album_art_display;
    ImageView shuffle_in_artist;
    ImageView artist_more_option;

    TextView artist_name;
    TextView artist_album_song;
    TextView songs_in_artist_count;


JSONObject jsonObject;
    RecyclerView artist_detail_albums_recycler;
    RecyclerView artist_detail_song_recycler;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists_details);


        navigate_back=findViewById(R.id.navigate_back);
        artist_to_search=findViewById(R.id.artist_to_search);
        artist_details_background=findViewById(R.id.artist_details_background);

        no_artist_image=findViewById(R.id.no_artist_image);

        album_art_display=findViewById(R.id.album_art_display);
        shuffle_in_artist=findViewById(R.id.shuffle_in_artist);
        artist_more_option=findViewById(R.id.artist_more_option);

        artist_name=findViewById(R.id.artist_name);
        artist_album_song=findViewById(R.id.artist_album_song);
        songs_in_artist_count=findViewById(R.id.songs_in_artist_count);
        myReceiver = new MusicIntentReceiver();


        artist_detail_albums_recycler = (RecyclerView)findViewById(R.id.artist_detail_albums_recycler);
        artist_detail_albums_recycler.setLayoutManager(new GridLayoutManager(ArtistsDetailsActivity.this, 1, LinearLayoutManager.HORIZONTAL, false));
        artist_detail_albums_recycler.setHasFixedSize(true);
        artist_detail_albums_recycler.setNestedScrollingEnabled(false);

        artist_detail_song_recycler = (RecyclerView)findViewById(R.id.artist_detail_song_recycler);
        artist_detail_song_recycler.setLayoutManager(new GridLayoutManager(ArtistsDetailsActivity.this, 1, LinearLayoutManager.VERTICAL, false));
        artist_detail_song_recycler.setHasFixedSize(true);
        artist_detail_song_recycler.setNestedScrollingEnabled(false);

        sessionManager=new SessionManager(ArtistsDetailsActivity.this);

        navigate_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        artist_to_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.intent_method(ArtistsDetailsActivity.this, SearchActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        if(sessionManager.getIsDefaultThemeSelected())
        {
            artist_details_background.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(ArtistsDetailsActivity.this,selectedImageUri));
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
            artist_details_background.setBackground(bg);
        }

        ArtistsModel artistsModel=MusicUtils.getArtist(ArtistsDetailsActivity.this,sessionManager.getArtistId());

        if(artistsModel.getAlbum_art()==null || artistsModel.getAlbum_art().equals(""))
        {
            album_art_display.setVisibility(View.GONE);
            no_artist_image.setVisibility(View.VISIBLE);
        }
        else
        {
            no_artist_image.setVisibility(View.GONE);
            album_art_display.setVisibility(View.VISIBLE);
            Bitmap bm= BitmapFactory.decodeFile(artistsModel.getAlbum_art());
            album_art_display.setImageBitmap(bm);
//            Glide.with(context).load().into(holder.audio_image);
        }
        artist_name.setText(artistsModel.getName());
        artist_album_song.setText(artistsModel.getAlbumCount()+" albums . "+artistsModel.getSongCount()+" songs");
        songs_in_artist_count.setText(artistsModel.getAlbumCount()+" albums . "+artistsModel.getSongCount()+" songs");






        ArtistAlbumDetailAdapter artistAlbumDetailAdapter=new ArtistAlbumDetailAdapter(ArtistsDetailsActivity.this,MusicUtils.LoadAllAlbums(ArtistsDetailsActivity.this,sessionManager.getArtistId()));
        artist_detail_albums_recycler.setAdapter(artistAlbumDetailAdapter);

        AlbumSongsAdapter songsAdapter=new AlbumSongsAdapter(ArtistsDetailsActivity.this,MusicUtils.getAllSongBasedonArtist(ArtistsDetailsActivity.this,sessionManager.getArtistId()));
        artist_detail_song_recycler.setAdapter(songsAdapter);
        shuffle_in_artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffle_all();
            }
        });
        artist_more_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(ArtistsDetailsActivity.this, R.style.popupMenuStyle);
//                PopupMenu popup = new PopupMenu(wrapper, view);
                PopupMenu popup = new PopupMenu(wrapper, artist_more_option);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.artist_page_detail_title_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.play_next:
                               List<AudioModel> arrayList=MusicUtils.getAllSongBasedonArtist(ArtistsDetailsActivity.this,sessionManager.getArtistId());
                               Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
                                playing_audio_list= new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
                                playing_audio_list.addAll(1,arrayList);
                                Gson  gson_list = new GsonBuilder().create();
                                JsonArray song_array = gson_list.toJsonTree(playing_audio_list).getAsJsonArray();
                                sessionManager.setSong_JsonList(song_array.toString());
                                System.out.println("aji playnext q size "+playing_audio_list.size());
                                break;
                            case R.id.add_to_playing_queue:
                                arrayList = MusicUtils.getAllSongBasedonArtist(ArtistsDetailsActivity.this,sessionManager.getArtistId());
                                type = new TypeToken<ArrayList<AudioModel>>() {}.getType();
                                playing_audio_list = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
                                playing_audio_list.addAll(arrayList);
                                gson_list = new GsonBuilder().create();
                                song_array = gson_list.toJsonTree(playing_audio_list).getAsJsonArray();
                                sessionManager.setSong_JsonList(song_array.toString());
                                break;
                            case R.id.add_to_playlist:
                                audioModelList = MusicUtils.getSongsForAlbum(ArtistsDetailsActivity.this, sessionManager.getAlbumId());
                                add_to_playlist_alert(ArtistsDetailsActivity.this);
                                //all_playlist(context);
                                break;
                            case R.id.share:
//                                sessionManager.setArtistId(artistsModelList.get(position).getId());
                                List<AudioModel> audioModelList1 = MusicUtils.getAllSongBasedonArtist(ArtistsDetailsActivity.this ,sessionManager.getArtistId());
                                shareFile(audioModelList1);
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //sho

            }
        });

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


    public void shuffle_all(){

        audioModelList = AlbumSongsAdapter.audioModelList;
        System.out.println("aji before shuffle "+audioModelList.get(0).getaName());
//        Random random = new Random();
        if(audioModelList.size()!=0) {
            Collections.shuffle(audioModelList);
            System.out.println("aji after shuffle "+audioModelList.get(0).getaName());
            sessionManager.setSongPosition(0);
            Gson gson = new Gson();
            int current_song_count=sessionManager.getSongPosition()+1;
            String json = gson.toJson(audioModelList.get(sessionManager.getSongPosition()));
            sessionManager.setSong_Json(json);
            try {
                jsonObject = new JSONObject(sessionManager.getSong_Json());
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            playing_song_artist.setText(jsonObject.optString("aArtist"));
//            playing_song_folder.setText(jsonObject.optString("aAlbum"));
//            playing_song_name.setText(jsonObject.optString("track"));

            Gson gson_lists = new GsonBuilder().create();
            JsonArray song_arrays = gson_lists.toJsonTree(audioModelList).getAsJsonArray();
            sessionManager.setSong_JsonList(song_arrays.toString());
            sessionManager.setBackgroundMusic(jsonObject.optString("aPath"));
            System.out.println("aji home shuffle "+jsonObject.optString("aPath"));
            System.out.println("aji home name "+jsonObject.optString("aName"));
//                startService(new Intent(SongPlayingActivity.this, BackgroundMusicService.class));
            Intent stopserviceIntent = new Intent(ArtistsDetailsActivity.this, BackgroundMusicService.class);
            stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            stopService(stopserviceIntent);
            Intent serviceIntent = new Intent(ArtistsDetailsActivity.this, BackgroundMusicService.class);
            serviceIntent.putExtra("song_name",jsonObject.optString("track"));
            serviceIntent.putExtra("artist_name",jsonObject.optString("aArtist"));
            serviceIntent.putExtra("song_count",current_song_count+"/"+audioModelList.size());
            serviceIntent.putExtra("album_art",jsonObject.optString("aAlbumart"));
            serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(serviceIntent);

        }
    }



}
