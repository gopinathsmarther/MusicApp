package smarther.com.musicapp.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import smarther.com.musicapp.Adapter.PlaylistSearchAddAdpater;
import smarther.com.musicapp.Adapter.PlaylistSongsAdapter;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicUtils;
import smarther.com.musicapp.Utils.SessionManager;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PlaylistAddSearch extends AppCompatActivity {

    public  static  Boolean selectall;
    SessionManager sessionManager;
    LinearLayout background_of_layout;
    public static TextView playlist_songs_add;
    LinearLayout playlist_select_all;
    LinearLayout edit_text_layout;
    ImageView navigate_back;
    ImageView ischeck_img;
    RecyclerView search_song_recycler;
    EditText search_edit_text;
    PlaylistSearchAddAdpater playlistSearchAddAdpater;
    List<AudioModel> audioModelList = new ArrayList<>();
    public static long playlist_id = -1;
    Realm playlist_database;

    public static List<String> stringListslectedids=new ArrayList<>();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("aji playlist sesarch "+selectall);
        setContentView(R.layout.activity_playlist_add_search);
        navigate_back=findViewById(R.id.navigate_back);
        navigate_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sessionManager=new SessionManager(PlaylistAddSearch.this);
        background_of_layout=findViewById(R.id.background_of_layout);
        search_song_recycler=findViewById(R.id.search_song_recycler);
        playlist_select_all=findViewById(R.id.playlist_select_all);
        playlist_songs_add=findViewById(R.id.playlist_songs_add);
        search_edit_text=findViewById(R.id.search_edit_text);
        edit_text_layout=findViewById(R.id.edit_text_layout);
        playlist_songs_add.setVisibility(View.GONE);
        ischeck_img=findViewById(R.id.ischeck_img);
        selectall=false;

        Realm.init(PlaylistAddSearch.this);
        RealmConfiguration main_data_config = new RealmConfiguration.Builder().name("playlists.realm").deleteRealmIfMigrationNeeded().build();
        playlist_database = Realm.getInstance(main_data_config);

        Intent intent = getIntent();
         playlist_id =intent.getLongExtra("playlist_id",-1);
        if(sessionManager.getIsDefaultThemeSelected())
        {
            background_of_layout.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(PlaylistAddSearch.this,selectedImageUri));
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

        search_song_recycler.setLayoutManager(new GridLayoutManager(PlaylistAddSearch.this, 1, LinearLayoutManager.VERTICAL, false));
        search_song_recycler.setHasFixedSize(true);
        search_song_recycler.setNestedScrollingEnabled(false);
        audioModelList = MusicUtils.getAllAudioFromDevice(PlaylistAddSearch.this);
        playlistSearchAddAdpater = new PlaylistSearchAddAdpater(PlaylistAddSearch.this,audioModelList);
       // for(int z=0;z<audioModelList.size();z++)
            //System.out.println("aji tempis "+audioModelList.get(z).getTemp_id());
        search_song_recycler.setAdapter(playlistSearchAddAdpater);
        playlist_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectall) {
                    selectall = false;
                    ischeck_img.setImageDrawable(getResources().getDrawable(R.drawable.radio_circle));
//                    ischeck_img.setColorFilter(R.color.white);
                    ischeck_img.setColorFilter(ContextCompat.getColor(PlaylistAddSearch.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    playlist_songs_add.setVisibility(View.GONE);
                    stringListslectedids = new ArrayList<>();
                    System.out.println("aji stringListslectedids.size "+stringListslectedids.size());


                }
                else{
                selectall = true;
                stringListslectedids = new ArrayList<>();
                    ischeck_img.setImageDrawable(getResources().getDrawable(R.drawable.tick));
                    for (int i = 0; i < audioModelList.size(); i++) {
                        stringListslectedids.add(audioModelList.get(i).getTemp_id());

                    }
                    playlist_songs_add.setVisibility(View.VISIBLE);
                    playlist_songs_add.setText("ADD " + stringListslectedids.size() + " songs");
//                    PlaylistAddSearch.playlist_songs_add.setVisibility(View.VISIBLE);
//                    PlaylistAddSearch.playlist_songs_add.setText("ADD "+stringListslectedids.size()+" songs");
//                    ischeck_img.setColorFilter(R.color.colorAccent);
                    ischeck_img.setColorFilter(ContextCompat.getColor(PlaylistAddSearch.this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                    System.out.println("aji>>>>>>> listsize "+stringListslectedids.size());


                }
                playlistSearchAddAdpater.notifyDataSetChanged();

//                if(PlaylistAddSearch.selectall) {
//
//                    playlist_songs_add.setVisibility(View.VISIBLE);
//                    playlist_songs_add.setText("ADD " + playlistSearchAddAdpater.stringListslectedids.size() + " songs");
//
//
//                }
//                else {
//                    playlist_songs_add.setVisibility(View.GONE);
//
//                }
            }
        });
        search_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlist_songs_add.setVisibility(View.GONE);
                search_song_recycler.setVisibility(View.GONE);


            }
        });

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0) {
                    search_song_recycler.setVisibility(View.VISIBLE);
                    List<AudioModel> temp = new ArrayList<>();
                    for (int val = 0; val < audioModelList.size(); val++) {
                        String s1 = audioModelList.get(val).getaName();
                        if (s1.toLowerCase().contains(String.valueOf(charSequence).toLowerCase())) {
                            temp.add(audioModelList.get(val));


                        }
                    }
                    System.out.println("aji temp size "+temp.size());
//                    playlistSearchAddAdpater = new PlaylistSearchAddAdpater(PlaylistAddSearch.this,temp);
//                    search_song_recycler.setAdapter(playlistSearchAddAdpater);
//                    playlistSearchAddAdpater.notifyDataSetChanged();
                    playlistSearchAddAdpater.updatelist(temp);

                }
                else{
                    playlistSearchAddAdpater.updatelist(audioModelList);
//                    search_song_recycler.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        playlist_songs_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long a = playlist_id;

//                    AudioModel audioModel= audioModelList.stream().filter(new Predicate<AudioModel>() {
//                        @Override
//                        public boolean test(AudioModel o) {
//                            return o.getaName().equals(stringListslectedids.get(1));
//                        }
//                    }).findFirst();

            for(int val =0 ; val<audioModelList.size();val++) {

//                AudioModel audioModel =
                if (stringListslectedids.contains(audioModelList.get(val).getTemp_id())) {
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
                }
            }
                search_song_recycler.setVisibility(View.VISIBLE);
                PlaylistSongsDisplay.playlistItemAdapter.notifyDataSetChanged();

            onBackPressed();

            }
        });

    }

    @Override
    protected void onPause() {
        stringListslectedids = new ArrayList<>();
        super.onPause();
    }
}
