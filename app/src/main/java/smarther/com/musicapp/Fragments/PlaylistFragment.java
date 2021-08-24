package smarther.com.musicapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import smarther.com.musicapp.Activity.FavouriteActivity;
import smarther.com.musicapp.Activity.HomeScreen;
import smarther.com.musicapp.Activity.PlayingQueue;
import smarther.com.musicapp.Activity.TopTrackActivity;
import smarther.com.musicapp.Adapter.FavouriteAdapter;
import smarther.com.musicapp.Adapter.HistoryAdapter;
import smarther.com.musicapp.Adapter.PlaylistAdapter;
import smarther.com.musicapp.Adapter.TopTrackAdapter;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.FavouriteModel;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.Model.PlaylistModel;
import smarther.com.musicapp.Model.TopRecentModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.SessionManager;

public class PlaylistFragment extends Fragment {

    RelativeLayout my_top_track_layout;
    RelativeLayout last_added_layout;
    RelativeLayout history_layout;
    RelativeLayout favourite_layout;
   public static LinearLayout smart_list;

    TextView my_top_track_song;
    TextView last_added_song;
    TextView history_song;
    TextView favourite_song;
    ImageView add_playlist;
    TextView playlist_count;
//    Realm main_data;

    RecyclerView playlist_recycler;

    List<PlaylistModel> playlistModelList=new ArrayList<>();
    public  static PlaylistAdapter playlistAdapter;

    Realm music_database;
SessionManager sessionManager;

    List<TopRecentModel> topRecentModelList;
    List<FavouriteModel> favouriteModels;
    ArrayList<AudioModel> mSongList;


    List<PlaylistAudioModel> playlistAudioModel=new ArrayList<>();
    Realm playlist_songs_relam;

    public PlaylistFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_playlist, container, false);


        Realm.init(getActivity());
        RealmConfiguration main_data_config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name("playlists.realm").build();
        playlist_songs_relam = Realm.getInstance(main_data_config);

        my_top_track_song=(TextView)view.findViewById(R.id.my_top_track_song);
        last_added_song=(TextView)view.findViewById(R.id.last_added_song);
        history_song=(TextView)view.findViewById(R.id.history_song);
        favourite_song=(TextView)view.findViewById(R.id.favourite_song);
        add_playlist=(ImageView)view.findViewById(R.id.add_playlist);
        playlist_count=(TextView)view.findViewById(R.id.playlist_count);

        my_top_track_layout=(RelativeLayout)view.findViewById(R.id.my_top_track_layout);
        last_added_layout=(RelativeLayout)view.findViewById(R.id.last_added_layout);
        history_layout=(RelativeLayout)view.findViewById(R.id.history_layout);
        favourite_layout=(RelativeLayout)view.findViewById(R.id.favourite_layout);
        smart_list=view.findViewById(R.id.smart_list);

        sessionManager=new SessionManager(getActivity());

        Realm.init(getActivity());
         main_data_config = new RealmConfiguration.Builder().name("music.realm").deleteRealmIfMigrationNeeded().build();
        music_database = Realm.getInstance(main_data_config);
        if(sessionManager.getShowSmartPlaylist()){
            smart_list.setVisibility(View.VISIBLE);
        }
        else {
            smart_list.setVisibility(View.GONE);

        }

            playlist_recycler = (RecyclerView)view.findViewById(R.id.playlist_recycler);
        playlist_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false));
        playlist_recycler.setHasFixedSize(true);
        playlist_recycler.setNestedScrollingEnabled(false);


        all_playlist();

        my_top_track_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson_list = new GsonBuilder().create();
                JSONArray jsonArray=new JSONArray();
                for (int i=0;i<topRecentModelList.size();i++)
                {
                    try {
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("aPath",topRecentModelList.get(i).getaPath());
                        jsonObject.put("aName",topRecentModelList.get(i).getaName());
                        jsonObject.put("aAlbum",topRecentModelList.get(i).getaAlbum());
                        jsonObject.put("aArtist",topRecentModelList.get(i).getaAlbum());
                        jsonObject.put("aAlbumart",topRecentModelList.get(i).getaAlbumart());
                        jsonObject.put("track",topRecentModelList.get(i).getTrack());
                        jsonObject.put("play_count",topRecentModelList.get(i).getPlay_count());
                        jsonObject.put("played_time",topRecentModelList.get(i).getPlayed_time());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


//                JsonArray song_array = gson_list.toJsonTree(topRecentModelList).getAsJsonArray();
                sessionManager.setTopTrackList(jsonArray.toString());
                Intent intent=new Intent(getActivity(), TopTrackActivity.class);
                intent.putExtra("title","My Top Track");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        last_added_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson_list = new GsonBuilder().create();
                JsonArray song_array = gson_list.toJsonTree(mSongList).getAsJsonArray();
                sessionManager.setTopTrackList(song_array.toString());
                Intent intent=new Intent(getActivity(), TopTrackActivity.class);
                intent.putExtra("title","Last Added");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        history_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray=new JSONArray();
                for (int i=0;i<topRecentModelList.size();i++)
                {
                    try {
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("aPath",topRecentModelList.get(i).getaPath());
                        jsonObject.put("aName",topRecentModelList.get(i).getaName());
                        jsonObject.put("aAlbum",topRecentModelList.get(i).getaAlbum());
                        jsonObject.put("aArtist",topRecentModelList.get(i).getaAlbum());
                        jsonObject.put("aAlbumart",topRecentModelList.get(i).getaAlbumart());
                        jsonObject.put("track",topRecentModelList.get(i).getTrack());
                        jsonObject.put("play_count",topRecentModelList.get(i).getPlay_count());
                        jsonObject.put("played_time",topRecentModelList.get(i).getPlayed_time());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                sessionManager.setTopTrackList(jsonArray.toString());
                Intent intent=new Intent(getActivity(), TopTrackActivity.class);
                intent.putExtra("title","History");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        favourite_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Gson gson_list = new GsonBuilder().create();
//
//                JSONArray jsonArray=new JSONArray();
//                for (int i=0;i<favouriteModels.size();i++)
//                {
//                    try {
//                        JSONObject jsonObject=new JSONObject();
//                        jsonObject.put("aPath",favouriteModels.get(i).getaPath());
//                        jsonObject.put("aName",favouriteModels.get(i).getaName());
//                        jsonObject.put("aAlbum",favouriteModels.get(i).getaAlbum());
//                        jsonObject.put("aArtist",favouriteModels.get(i).getaAlbum());
//                        jsonObject.put("aAlbumart",favouriteModels.get(i).getaAlbumart());
//                        jsonObject.put("track",favouriteModels.get(i).getTrack());
//                        jsonArray.put(jsonObject);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
////                JsonArray song_array = gson_list.toJsonTree(favouriteModels).getAsJsonArray();
//                sessionManager.setSong_JsonList(jsonArray.toString());
//                Intent intent=new Intent(getActivity(), TopTrackActivity.class);
//                intent.putExtra("title","Favourite");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                playlistAudioModel =  playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id",1 ).findAll();
//                 playlistAudioModel.add(playlistAudioModelobj);
                if(playlistAudioModel.size()!=0) {
                    System.out.println("aji size " + playlistAudioModel.size());
                    Intent intent = new Intent(getActivity(), FavouriteActivity.class);
                    intent.putExtra("playlist_id",1);
                    intent.putExtra("playlist_name","Favourites");
                    startActivity(intent);


                }
                else{
                    System.out.println("aji "+null);}

            }
        });



        add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LayoutInflater inflater =getActivity().getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.create_playlist_item, null);
                final EditText playlist_edit_text = alertLayout.findViewById(R.id.playlist_edit_text);
                final LinearLayout background_playlist_dialog_view = alertLayout.findViewById(R.id.background_playlist_dialog_view);
                final TextView cancel_dialog = alertLayout.findViewById(R.id.cancel_dialog);
                final TextView create_dialog = alertLayout.findViewById(R.id.create_dialog);

                if(sessionManager.getIsDefaultThemeSelected())
                {
                    background_playlist_dialog_view.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
                }
                else
                {
                    Drawable bg;
                    Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
                    try {
                        File f = new File(Global.getRealPathFromURI(getActivity(),selectedImageUri));
                        bg= Drawable.createFromPath(f.getAbsolutePath());
                    }
                    catch (Exception e)
                    {
                        bg = ContextCompat.getDrawable(getActivity(), R.drawable.theme_1);
                        e.printStackTrace();
                    }
                    background_playlist_dialog_view.setBackground(bg);
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setView(alertLayout);
                alert.setCancelable(false);
                final AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Number currentIdNum = music_database.where(PlaylistModel.class).max("playlist_id");
                int nextId;
                if(currentIdNum == null) {
                    nextId = 1;
                    System.out.println("aji nextId "+nextId);
                    music_database.beginTransaction();
                    PlaylistModel playlistModel=music_database.createObject(PlaylistModel.class,nextId);
                    playlistModel.setPlaylist_name("Favourites");
                    music_database.commitTransaction();
                }

                create_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (playlist_edit_text.getText().toString().trim().equals(""))
                        {
                            Toast.makeText(getActivity(), "Please enter playlist name", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Number currentIdNum = music_database.where(PlaylistModel.class).max("playlist_id");
                            int nextId;
                            if(currentIdNum == null) {
                                nextId = 1;
                            } else {
                                nextId = currentIdNum.intValue() + 1;
                            }
                            System.out.println("aji nextId "+nextId);
                            music_database.beginTransaction();
                            PlaylistModel playlistModel=music_database.createObject(PlaylistModel.class,nextId);
                            playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
                            music_database.commitTransaction();
                            playlistAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });

                cancel_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });




//                final EditText input = new EditText(getActivity());
////                input.setInputType(InputType.TYPE_CLASS_NUMBER);
//                input.setGravity(Gravity.CENTER);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT);
//                input.setLayoutParams(lp);
//                AlertDialog dialog = new AlertDialog.Builder(getActivity())
//                        .setMessage("New Playlist...")
//                        .setView(input)
//                        .setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//
//                            }
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .create();
//                dialog.show();




            }
        });

        top_tracks_list();
        history_list();
        favourite_list();
        getLastAddedSongs(getActivity());

        return view;
    }


    public void top_tracks_list()
    {
        topRecentModelList = music_database.where(TopRecentModel.class).greaterThan("play_count",2).findAll();

//                .sort("dateTimeStart", Sort.DESCENDING);
        if(topRecentModelList.size()==0)
        {
            my_top_track_song.setText("0 Songs");
        }
        else
        {
            my_top_track_song.setText(topRecentModelList.size()+" Songs");
        }

    }

    public void history_list()
    {
        topRecentModelList = music_database.where(TopRecentModel.class).findAll();
//                .sort("dateTimeStart", Sort.DESCENDING);
        if(topRecentModelList.size()==0)
        {
            history_song.setText("0 Songs");
        }
        else
        {
            history_song.setText(topRecentModelList.size()+" Songs");
        }

    }
    public void favourite_list()
    {
        favouriteModels = music_database.where(FavouriteModel.class).findAll();
//                .sort("dateTimeStart", Sort.DESCENDING);
        if(favouriteModels.size()==0)
        {
            favourite_song.setText("0 Songs");
        }
        else
        {
            favourite_song.setText(favouriteModels.size()+" Songs");
        }

    }

    private void getLastAddedSongs(Context context) {
        //four weeks ago
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST,};
        long fourWeeksAgo = (System.currentTimeMillis() / 1000) - (4 * 3600 * 24 * 7);
//        long cutoff = PreferencesUtility.getInstance(context).getLastAddedCutoff();
//        // use the most recent of the two timestamps
//        if (cutoff < fourWeeksAgo) {
//            cutoff = fourWeeksAgo;
//        }

        final StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Audio.AudioColumns.IS_MUSIC + "=1");
        selection.append(" AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''");
        selection.append(" AND " + MediaStore.Audio.Media.DATE_ADDED + ">");
        selection.append(fourWeeksAgo);

         mSongList = new ArrayList<>();
        Cursor  mCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection, selection.toString(), null, MediaStore.Audio.Media.DATE_ADDED + " DESC");

        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                AudioModel audioModel = new AudioModel();
                String album_art="";
                String path = mCursor.getString(0);
                String album = mCursor.getString(1);
                String album_id = mCursor.getString(2);
                String artist = mCursor.getString(3);
//                Cursor cursorAlbum  = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID+ "=?", new String[] {String.valueOf(album_id)}, null);
//                if (cursorAlbum != null && cursorAlbum .moveToFirst())
//                {
//                    album_art=cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
//                }

                String name = path.substring(path.lastIndexOf("/") + 1);

                audioModel.setaName(name);
                audioModel.setaAlbum(album);
                audioModel.setaArtist(artist);
                audioModel.setaPath(path);
                audioModel.setaAlbumart(album_art);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);
                mSongList.add(audioModel);
            } while (mCursor.moveToNext());

            last_added_song.setText(mSongList.size()+" Songs");
        }
        else
        {
            last_added_song.setText("0 Songs");
        }
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }

    }
    public void all_playlist()
    {
        playlistModelList = music_database.where(PlaylistModel.class).findAll();
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
            playlist_count.setText(""+playlistModelList.size());
        }
        playlistAdapter = new PlaylistAdapter(getActivity(),playlistModelList);
        playlist_recycler.setAdapter(playlistAdapter);
    }

}
