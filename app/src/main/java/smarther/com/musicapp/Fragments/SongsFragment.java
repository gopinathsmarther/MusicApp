package smarther.com.musicapp.Fragments;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Activity.ArtistsDetailsActivity;
import smarther.com.musicapp.Activity.HomeScreen;
import smarther.com.musicapp.Activity.ThemeActivity;
import smarther.com.musicapp.Adapter.AlbumSongsAdapter;
import smarther.com.musicapp.Adapter.SongsAdapter;
import smarther.com.musicapp.Adapter.ThemeAdapter;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Service.BackgroundMusicService;
import smarther.com.musicapp.Utils.Constants;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.SessionManager;

public class SongsFragment extends Fragment {
    public static RecyclerView song_recycler;
    SessionManager sessionManager;
    public static SongsAdapter songsAdapter;
    List<AudioModel> audioModelList;
    JSONObject jsonObject;
    TextView shuffle_all;

    public SongsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view=inflater.inflate(R.layout.fragment_songs, container, false);
sessionManager = new SessionManager(getActivity());
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        song_recycler = (RecyclerView)view.findViewById(R.id.song_recycler);
        shuffle_all = view.findViewById(R.id.shuffle_all);
        shuffle_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffle_all();
            }
        });
        song_recycler.setLayoutManager(new GridLayoutManager(getActivity(),sessionManager.getSongsGrid() , LinearLayoutManager.VERTICAL, false));
        song_recycler.setHasFixedSize(true);
        song_recycler.setNestedScrollingEnabled(false);
        getAllAudioFromDevice(getActivity());
        return view;
    }

    public void shuffle_all(){

        audioModelList = SongsAdapter.audioModelList;
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
            HomeScreen.playing_song_artist.setText(jsonObject.optString("aArtist"));
            HomeScreen.playing_song_folder.setText(jsonObject.optString("aAlbum"));
            HomeScreen.playing_song_name.setText(jsonObject.optString("track"));

            Gson gson_lists = new GsonBuilder().create();
            JsonArray song_arrays = gson_lists.toJsonTree(audioModelList).getAsJsonArray();
            sessionManager.setSong_JsonList(song_arrays.toString());
            sessionManager.setBackgroundMusic(jsonObject.optString("aPath"));
            System.out.println("aji home shuffle "+jsonObject.optString("aPath"));
            System.out.println("aji home name "+jsonObject.optString("aName"));
//                startService(new Intent(SongPlayingActivity.this, BackgroundMusicService.class));
            Intent stopserviceIntent = new Intent(getActivity(), BackgroundMusicService.class);
            stopserviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            getActivity().stopService(stopserviceIntent);
            Intent serviceIntent = new Intent(getActivity(), BackgroundMusicService.class);
            serviceIntent.putExtra("song_name",jsonObject.optString("track"));
            serviceIntent.putExtra("artist_name",jsonObject.optString("aArtist"));
            serviceIntent.putExtra("song_count",current_song_count+"/"+audioModelList.size());
            serviceIntent.putExtra("album_art",jsonObject.optString("aAlbumart"));
            serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            getActivity().startService(serviceIntent);

        }
    }

    public List<AudioModel> getAllAudioFromDevice(final Context context) {

        final List<AudioModel> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//      Uri  uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ALBUM_ID,MediaStore.Audio.AudioColumns.ARTIST_ID, MediaStore.Audio.ArtistColumns.ARTIST,"title","_id",MediaStore.Audio.Media.DURATION};

//        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST,"title"};
//        Cursor c = getActivity().getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%yourFolderName%"}, null);
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                System.out.println("aji cursor "+ Arrays.toString(c.getColumnNames()));
                AudioModel audioModel = new AudioModel();
                String album_art="";
                String path = c.getString(0);
                String album = c.getString(1);
                String album_id = c.getString(2);
                String artist_id = c.getString(3);
                String artist = c.getString(4);
                String title = c.getString(5);
                long duration = c.getLong(7);
                System.out.println("aji duration "+duration);











                long song_minutes = (duration/ 1000) / 60;
                long song_seconds = (duration/ 1000) % 60;
                String song_minutes_string=String.valueOf(song_minutes);
                String song_seconds_string=String.valueOf(song_seconds);



                if(song_minutes_string.length()==1)
                {
                    song_minutes_string="0"+song_minutes;

                }
                else
                {
                    song_minutes_string=""+song_minutes;
                }
                if(song_seconds_string.length()==1)
                {
                    song_seconds_string="0"+song_seconds;

                }
                else
                {
                    song_seconds_string=""+song_seconds;
                }









                //  String length = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
                System.out.println("aji length "+ song_minutes_string+" : "+song_seconds_string);
//                MediaExtractor mex = new MediaExtractor();
//                try {
//                    mex.setDataSource(path);// the adresss location of the sound on sdcard.
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                MediaFormat mf = mex.getTrackFormat(0);
//                int bitRate = mf.getInteger(MediaFormat.KEY_BIT_RATE);
//                int sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);


                Cursor cursorAlbum  = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID+ "=?", new String[] {String.valueOf(album_id)}, null);
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
//                audioModel.setBitRate(bitRate);
//                audioModel.setSampleRate(sampleRate);
//                audioModel.setLength(length);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);

                System.out.println("<<<<<<<<<<<<<<<<<<<<<<COMPLETED>>>>>>>>>>>>>>>>>>>>>>>>>" + tempAudioList.size());

                Toast.makeText(context,path,Toast.LENGTH_SHORT);
                tempAudioList.add(audioModel);
            }
            c.close();

            System.out.println("COMPLETED>>>>>>>>>>>>>>>>>>>>>>>>>" + tempAudioList.size());
            if (sessionManager.getSongsSort()==1){
                Collections.sort(tempAudioList,AudioModel.namecomparator);

                songsAdapter=new SongsAdapter(getActivity(),tempAudioList,SongsFragment.this);
                song_recycler.setAdapter(songsAdapter);
            }else if (sessionManager.getSongsSort()==2){
                Collections.sort(tempAudioList,AudioModel.descending);

                songsAdapter=new SongsAdapter(getActivity(),tempAudioList,SongsFragment.this);
                song_recycler.setAdapter(songsAdapter);
            }else if (sessionManager.getSongsSort()==3){
                Collections.sort(tempAudioList,AudioModel.artist);

                songsAdapter=new SongsAdapter(getActivity(),tempAudioList,SongsFragment.this);
                song_recycler.setAdapter(songsAdapter);
            }else if (sessionManager.getSongsSort()==4){
                Collections.sort(tempAudioList,AudioModel.album);

                songsAdapter=new SongsAdapter(getActivity(),tempAudioList,SongsFragment.this);
                song_recycler.setAdapter(songsAdapter);
            }


        }

        return tempAudioList;
    }

//    public Bitmap getAlbumart(Context context, Long album_id)
//    {
//        Bitmap albumArtBitMap = null;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        try {
//
//            final Uri sArtworkUri = Uri
//                    .parse("content://media/external/audio/albumart");
//
//            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
//
//            ParcelFileDescriptor pfd = context.getContentResolver()
//                    .openFileDescriptor(uri, "r");
//
//            if (pfd != null) {
//                FileDescriptor fd = pfd.getFileDescriptor();
//                albumArtBitMap = BitmapFactory.decodeFileDescriptor(fd, null,
//                        options);
//                pfd = null;
//                fd = null;
//            }
//        } catch (Error ee) {
//        } catch (Exception e) {
//        }
//
////        if (null != albumArtBitMap) {
//        return albumArtBitMap;
////        }
////        return getDefaultAlbumArtEfficiently(context.getResources());
//    }



}

