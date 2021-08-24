package smarther.com.musicapp.Activity.RingtoneCutter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Adapter.RCSongSelectionAdapter;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.SessionManager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RCSongSelectionActivity extends AppCompatActivity
{
LinearLayout rc_songselection_background;
LinearLayout close_button;
SessionManager sessionManager;
RecyclerView rc_song_recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_c_song_selection);
        sessionManager=new SessionManager(RCSongSelectionActivity.this);
        rc_songselection_background=findViewById(R.id.rc_songselection_background);
        close_button=findViewById(R.id.close_button);



        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(sessionManager.getIsDefaultThemeSelected())
        {
            rc_songselection_background.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(RCSongSelectionActivity.this,selectedImageUri));
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
            rc_songselection_background.setBackground(bg);
        }

        rc_song_recycler = (RecyclerView)findViewById(R.id.rc_song_recycler);
        rc_song_recycler.setLayoutManager(new GridLayoutManager(RCSongSelectionActivity.this, 1, LinearLayoutManager.VERTICAL, false));
        rc_song_recycler.setHasFixedSize(true);
        rc_song_recycler.setNestedScrollingEnabled(false);
        getAllAudioFromDevice(RCSongSelectionActivity.this);
    }
    public List<AudioModel> getAllAudioFromDevice(final Context context) {

        final List<AudioModel> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ALBUM_ID,MediaStore.Audio.AudioColumns.ARTIST_ID, MediaStore.Audio.ArtistColumns.ARTIST,"title","_id"};

//        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST,"title"};
//        Cursor c = getActivity().getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%yourFolderName%"}, null);
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {

                AudioModel audioModel = new AudioModel();
                String album_art="";
                String path = c.getString(0);
                String album = c.getString(1);
                String album_id = c.getString(2);
                String artist_id = c.getString(3);
                String artist = c.getString(4);
                String title = c.getString(5);
                Cursor cursorAlbum  = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID+ "=?", new String[] {String.valueOf(album_id)}, null);
                if (cursorAlbum != null && cursorAlbum .moveToFirst())
                {
                    album_art=cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                }

                String name = path.substring(path.lastIndexOf("/") + 1);

                audioModel.setaName(name);
                audioModel.setaAlbum(album);
                audioModel.setAlbum_id(album_id);
                audioModel.setArtist_id(artist_id);
                audioModel.setaArtist(artist);
                audioModel.setaPath(path);
                audioModel.setaAlbumart(album_art);
                audioModel.setTrack(title);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);

                tempAudioList.add(audioModel);
            }
            c.close();
            RCSongSelectionAdapter songsAdapter=new RCSongSelectionAdapter(RCSongSelectionActivity.this,tempAudioList);
            rc_song_recycler.setAdapter(songsAdapter);
        }

        return tempAudioList;
    }
}
