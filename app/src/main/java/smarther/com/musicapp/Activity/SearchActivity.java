package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Adapter.AlbumsAdapter;
import smarther.com.musicapp.Adapter.ArtistsAdapter;
import smarther.com.musicapp.Adapter.SearchAlbumsAdapter;
import smarther.com.musicapp.Adapter.SearchArtistsAdapter;
import smarther.com.musicapp.Adapter.SearchSongsAdapter;
import smarther.com.musicapp.Adapter.SongsAdapter;
import smarther.com.musicapp.Model.AlbumModel;
import smarther.com.musicapp.Model.ArtistsModel;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.MusicUtils;
import smarther.com.musicapp.Utils.SessionManager;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    SessionManager sessionManager;
    LinearLayout search_background;
    LinearLayout sound_recorder;
    EditText search_edit_text;

    TextView song_title_textview;
    TextView artist_title_textview;
    TextView album_title_textview;

    RecyclerView search_song_recycler;
    RecyclerView search_artists_recycler;
    RecyclerView search_albums_recycler;
LinearLayout toggle_drawer;
    public MusicIntentReceiver myReceiver;

    int REQUEST_CODE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        sessionManager=new SessionManager(SearchActivity.this);
        search_background=(LinearLayout)findViewById(R.id.search_background);
        sound_recorder=(LinearLayout)findViewById(R.id.sound_recorder);
        toggle_drawer=(LinearLayout)findViewById(R.id.toggle_drawer);
        search_edit_text=(EditText)findViewById(R.id.search_edit_text);

        song_title_textview=(TextView)findViewById(R.id.song_title_textview);
        artist_title_textview=(TextView)findViewById(R.id.artist_title_textview);
        album_title_textview=(TextView)findViewById(R.id.album_title_textview);
        myReceiver = new MusicIntentReceiver();

        search_song_recycler = (RecyclerView)findViewById(R.id.search_song_recycler);
        search_song_recycler.setLayoutManager(new GridLayoutManager(SearchActivity.this, 1, LinearLayoutManager.VERTICAL, false));
        search_song_recycler.setHasFixedSize(true);
        search_song_recycler.setNestedScrollingEnabled(false);

        search_artists_recycler = (RecyclerView)findViewById(R.id.search_artists_recycler);
        search_artists_recycler.setLayoutManager(new GridLayoutManager(SearchActivity.this, 1, LinearLayoutManager.VERTICAL, false));
        search_artists_recycler.setHasFixedSize(true);
        search_artists_recycler.setNestedScrollingEnabled(false);

        search_albums_recycler = (RecyclerView)findViewById(R.id.search_albums_recycler);
        search_albums_recycler.setLayoutManager(new GridLayoutManager(SearchActivity.this, 1, LinearLayoutManager.VERTICAL, false));
        search_albums_recycler.setHasFixedSize(true);
        search_albums_recycler.setNestedScrollingEnabled(false);

        sound_recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                try {
                    startActivityForResult(intent, REQUEST_CODE);
                } catch (ActivityNotFoundException a) {

                }
            }
        });
        toggle_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        search_edit_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s)
            {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    getAllAudioFromDevice(SearchActivity.this,s.toString());
            }
        });



        if(sessionManager.getIsDefaultThemeSelected())
        {
            search_background.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(SearchActivity.this,selectedImageUri));
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
            search_background.setBackground(bg);
        }
    }




    public  void getAllAudioFromDevice(final Context context,String song_title)
    {

        List<AudioModel> songList = MusicUtils.searchSongs(SearchActivity.this,song_title, 10);
        if(songList.size()==0)
        {
            song_title_textview.setVisibility(View.GONE);
            search_song_recycler.setVisibility(View.GONE);
        }
        else
        {
            song_title_textview.setVisibility(View.VISIBLE);
            search_song_recycler.setVisibility(View.VISIBLE);
            SearchSongsAdapter songsAdapter=new SearchSongsAdapter(SearchActivity.this,songList);
            search_song_recycler.setAdapter(songsAdapter);
        }

        List<ArtistsModel> artistList = MusicUtils.getArtists(SearchActivity.this,song_title, 7);
        if(artistList.size()==0)
        {
            artist_title_textview.setVisibility(View.GONE);
            search_artists_recycler.setVisibility(View.GONE);
        }
        else
        {
            artist_title_textview.setVisibility(View.VISIBLE);
            search_artists_recycler.setVisibility(View.VISIBLE);
            SearchArtistsAdapter artistsAdapter=new SearchArtistsAdapter(SearchActivity.this,artistList);
            search_artists_recycler.setAdapter(artistsAdapter);
        }


        List<AlbumModel> albumModelList = MusicUtils.getAlbums(SearchActivity.this,song_title, 7);
        if(albumModelList.size()==0)
        {
            album_title_textview.setVisibility(View.GONE);
            search_albums_recycler.setVisibility(View.GONE);
        }
        else
        {
            album_title_textview.setVisibility(View.VISIBLE);
            search_albums_recycler.setVisibility(View.VISIBLE);
            SearchAlbumsAdapter albumsAdapter=new SearchAlbumsAdapter(SearchActivity.this,albumModelList);
            search_albums_recycler.setAdapter(albumsAdapter);
        }

    }


    @Override

//Define an OnActivityResult method in our intent caller Activity//

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0: {

//If RESULT_OK is returned...//

                if (resultCode == RESULT_OK && null != data) {

//...then retrieve the ArrayList//

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

//Update our TextView//

                    search_edit_text.setText(result.get(0));
                }
                break;
            }

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


