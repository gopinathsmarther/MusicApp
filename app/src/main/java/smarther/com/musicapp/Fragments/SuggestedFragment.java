package smarther.com.musicapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import smarther.com.musicapp.Activity.FavouriteActivity;
import smarther.com.musicapp.Activity.PlayingQueue;
import smarther.com.musicapp.Activity.PlaylistSongsDisplay;
import smarther.com.musicapp.Activity.SearchActivity;
import smarther.com.musicapp.Activity.SongPlayingActivity;
import smarther.com.musicapp.Activity.TopTrackActivity;
import smarther.com.musicapp.Adapter.FavouriteAdapter;
import smarther.com.musicapp.Adapter.FavouritesSongAdapter;
import smarther.com.musicapp.Adapter.HistoryAdapter;
import smarther.com.musicapp.Adapter.RecentlyAddedAdapter;
import smarther.com.musicapp.Adapter.TopTrackAdapter;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.FavouriteModel;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.Model.TopRecentModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.SessionManager;


public class SuggestedFragment extends Fragment {
    LinearLayout my_top_tracks;
    TextView my_top_tracks_see_all;
    LinearLayout favourites;
    TextView favourites_see_all;
    LinearLayout history;
    TextView history_see_all;
    LinearLayout last_added;
    TextView last_added_see_all;

    RecyclerView my_top_tracks_recycler;
    RecyclerView favourites_recycler;
    RecyclerView history_recycler;
    RecyclerView last_added_recycler;

    List<TopRecentModel> topRecentModelList;
    List<FavouriteModel> favouriteModels;
    ArrayList<AudioModel> mSongList;
    SessionManager sessionManager;

    Realm music_database;

    List<PlaylistAudioModel> playlistAudioModel=new ArrayList<>();
    Realm playlist_songs_relam;


    public SuggestedFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.e("abbas","nadakdakdnaksndkanskdnkandsdnkandnakdnkandkasdnkandnsdnkans11111111");
        View view=inflater.inflate(R.layout.fragment_suggested, container, false);
        Log.e("abbas","nadakdakdnaksndkanskdnkandsdnkandnakdnkandkasdnkandnsdnkans2222222222222");

        Realm.init(getActivity());
        RealmConfiguration main_data_config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name("playlists.realm").build();
        playlist_songs_relam = Realm.getInstance(main_data_config);

        my_top_tracks=(LinearLayout)view.findViewById(R.id.my_top_tracks);
        favourites=(LinearLayout)view.findViewById(R.id.favourites);
        history=(LinearLayout)view.findViewById(R.id.history);
        last_added=(LinearLayout)view.findViewById(R.id.last_added);

        my_top_tracks_see_all=(TextView)view.findViewById(R.id.my_top_tracks_see_all);
        favourites_see_all=(TextView)view.findViewById(R.id.favourites_see_all);
        history_see_all=(TextView)view.findViewById(R.id.history_see_all);
        last_added_see_all=(TextView)view.findViewById(R.id.last_added_see_all);

        my_top_tracks_recycler = (RecyclerView)view.findViewById(R.id.my_top_tracks_recycler);
        my_top_tracks_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false));
        my_top_tracks_recycler.setHasFixedSize(true);
        my_top_tracks_recycler.setNestedScrollingEnabled(false);

        favourites_recycler = (RecyclerView)view.findViewById(R.id.favourites_recycler);
        favourites_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false));
        favourites_recycler.setHasFixedSize(true);
        favourites_recycler.setNestedScrollingEnabled(false);

        history_recycler = (RecyclerView)view.findViewById(R.id.history_recycler);
        history_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false));
        history_recycler.setHasFixedSize(true);
        history_recycler.setNestedScrollingEnabled(false);

        last_added_recycler = (RecyclerView)view.findViewById(R.id.last_added_recycler);
        last_added_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false));
        last_added_recycler.setHasFixedSize(true);
        last_added_recycler.setNestedScrollingEnabled(false);
        Log.e("abbas","nadakdakdnaksndkanskdnkandsdnkandnakdnkandkasdnkandnsdnkans");
        Realm.init(getActivity());
         main_data_config = new RealmConfiguration.Builder().name("music.realm").deleteRealmIfMigrationNeeded().build();
        music_database = Realm.getInstance(main_data_config);
        top_tracks_list();
        history_list();
        favourite_list();
        getLastAddedSongs(getActivity());
sessionManager=new SessionManager(getActivity());

        my_top_tracks_see_all.setOnClickListener(new View.OnClickListener() {
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
        favourites_see_all.setOnClickListener(new View.OnClickListener() {
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
//                sessionManager.setTopTrackList(jsonArray.toString()); //aji
//                Intent intent=new Intent(getActivity(), TopTrackActivity.class);
//                intent.putExtra("title","Favourite");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);



               // System.out.println("aji playlistModelList.get(position).getPlaylist_id() "+ playlistModelList.get(position).getPlaylist_id());
//                playlistAudioModel = playlist_songs_relam.where(PlaylistAudioModel.class).findAll();
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
        history_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson_list = new GsonBuilder().create();
//                JsonArray song_array = gson_list.toJsonTree(topRecentModelList).getAsJsonArray();

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
        last_added_see_all.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    public void top_tracks_list()
    {
         topRecentModelList = music_database.where(TopRecentModel.class).greaterThan("play_count",2).findAll();

//                .sort("dateTimeStart", Sort.DESCENDING);
        if(topRecentModelList.size()==0)
        {
            my_top_tracks.setVisibility(View.GONE);
            my_top_tracks_recycler.setVisibility(View.GONE);
        }
        else
        {
            my_top_tracks.setVisibility(View.VISIBLE);
            my_top_tracks_recycler.setVisibility(View.VISIBLE);
            TopTrackAdapter adapter = new TopTrackAdapter(getActivity(),topRecentModelList,SuggestedFragment.this);
            my_top_tracks_recycler.setAdapter(adapter);
        }

    }
    public void history_list()
    {
         topRecentModelList = music_database.where(TopRecentModel.class).findAll();
//                .sort("dateTimeStart", Sort.DESCENDING);
        if(topRecentModelList.size()==0)
        {
            history.setVisibility(View.GONE);
            history_recycler.setVisibility(View.GONE);
        }
        else
        {
            history.setVisibility(View.VISIBLE);
            history_recycler.setVisibility(View.VISIBLE);
            HistoryAdapter adapter = new HistoryAdapter(getActivity(),topRecentModelList,SuggestedFragment.this);
            history_recycler.setAdapter(adapter);
        }

    }
    public void favourite_list()
    {
        //favouriteModels = music_database.where(FavouriteModel.class).findAll();
//                .sort("dateTimeStart", Sort.DESCENDING);
        playlistAudioModel =  playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id",1 ).findAll();

        if(playlistAudioModel.size()==0)
        {
            favourites.setVisibility(View.GONE);
            favourites_recycler.setVisibility(View.GONE);
        }
        else
        {
            favourites.setVisibility(View.VISIBLE);
            favourites_recycler.setVisibility(View.VISIBLE);
//            FavouriteAdapter adapter = new FavouriteAdapter(getActivity(),favouriteModels,SuggestedFragment.this);
//            favourites_recycler.setAdapter(adapter);
            FavouritesSongAdapter playlistItemAdapter=new FavouritesSongAdapter(getActivity(),playlistAudioModel);
            favourites_recycler.setAdapter(playlistItemAdapter);
        }

    }
    private void getLastAddedSongs(Context context) {
        //four weeks ago
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST,MediaStore.Audio.Media.DATE_ADDED};
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
        Cursor mCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection, selection.toString(), null, MediaStore.Audio.Media.DATE_ADDED + " DESC");

        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                AudioModel audioModel = new AudioModel();
                String album_art="";
                String path = mCursor.getString(0);
                String album = mCursor.getString(1);
                String album_id = mCursor.getString(2);
                String artist = mCursor.getString(3);
                String date_added = mCursor.getString(4);
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
                audioModel.setSong_added_on(date_added);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);
                mSongList.add(audioModel);
            } while (mCursor.moveToNext());
            last_added.setVisibility(View.VISIBLE);
            last_added_recycler.setVisibility(View.VISIBLE);
            RecentlyAddedAdapter adapter = new RecentlyAddedAdapter(getActivity(),mSongList,SuggestedFragment.this);
            last_added_recycler.setAdapter(adapter);
        }
        else
        {
            last_added.setVisibility(View.GONE);
            last_added_recycler.setVisibility(View.GONE);
        }
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }

    }

}
