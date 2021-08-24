package smarther.com.musicapp.Fragments;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Activity.ThemeActivity;
import smarther.com.musicapp.Adapter.AlbumsAdapter;
import smarther.com.musicapp.Adapter.ArtistsAdapter;
import smarther.com.musicapp.Adapter.SongsAdapter;
import smarther.com.musicapp.Adapter.ThemeAdapter;
import smarther.com.musicapp.Model.AlbumModel;
import smarther.com.musicapp.Model.ArtistsModel;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.SessionManager;

public class ArtistsFragment extends Fragment {
   public static RecyclerView artists_recycler;
   public static ArtistsAdapter artistsAdapter;
   SessionManager sessionManager;
    List<String> artists_list=new ArrayList<>();
    //    ArrayList<String> songs_list = new ArrayList<>();
    String album_art="";
    int album_count=0;
    String[] projections = {MediaStore.Audio.Media.TITLE,};
    String[] album_projections = {MediaStore.Audio.AudioColumns.ALBUM};
    final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
   final static String[] projection = {MediaStore.Audio.ArtistColumns.ARTIST,MediaStore.Audio.ArtistColumns.ARTIST_KEY,MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS,MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS,};
    public ArtistsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(getActivity());
        View view=inflater.inflate(R.layout.fragment_artists, container, false);
        artists_recycler = (RecyclerView)view.findViewById(R.id.artists_recycler);
        artists_recycler.setLayoutManager(new GridLayoutManager(getActivity(),sessionManager.getArtistGrid() , LinearLayoutManager.VERTICAL, false));
        artists_recycler.setHasFixedSize(true);
        artists_recycler.setNestedScrollingEnabled(false);
        List<ArtistsModel> artistsModelList =getAllArtists(getActivity());
        if (sessionManager.getArtistSort()==1)
            Collections.sort(artistsModelList, ArtistsModel.namecomparator);
        else
            Collections.sort(artistsModelList, ArtistsModel.descending);

        artistsAdapter=new ArtistsAdapter(getActivity(), artistsModelList);
        artists_recycler.setAdapter(artistsAdapter);
        return view;
    }

    public  List<ArtistsModel> getAllArtists(Context context) {
        List<ArtistsModel> all_artist_list=new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,new String[]{"_id", "artist", "number_of_albums", "number_of_tracks"}, null, null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
        String start = "";
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                boolean value=false;
                Cursor album_art_cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"}, MediaStore.Audio.Albums.ARTIST_ID+ "=?",  new String[] {String.valueOf(cursor.getLong(0))}, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
                if ((album_art_cursor != null) && (album_art_cursor.moveToFirst()))
                    do {
                        Cursor cursorAlbum  = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID+ "=?", new String[] {String.valueOf(album_art_cursor.getLong(0))}, null);
                        if (cursorAlbum != null && cursorAlbum .moveToFirst())
                        {
                            album_art=cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                            if(album_art!=null && !album_art.equals(""))
                            {
                                value=true;
                            }
                            else
                            {
                                value=false;
                            }
                        }

                    }
                    while (!value && album_art_cursor.moveToNext() );
                if (album_art_cursor != null)
                    album_art_cursor.close();







                if (cursor.getString(1).startsWith("<")) {
                    start = cursor.getString(1).replaceAll("[<>]", "");

                    all_artist_list.add(new ArtistsModel(cursor.getLong(0), start, cursor.getInt(2), cursor.getInt(3),album_art));
                }
                else if (cursor.getString(1).startsWith("("))
                {
                    start = cursor.getString(1).replaceAll("[()]", "");

                    all_artist_list.add(new ArtistsModel(cursor.getLong(0), start, cursor.getInt(2), cursor.getInt(3),album_art));
                }
                else

                {
                    all_artist_list.add(new ArtistsModel(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3),album_art));
                }
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return all_artist_list;
    }









//    public List<AudioModel> getAllAudioFromDevice(final Context context) {
//
//        final List<AudioModel> tempAudioList = new ArrayList<>();
//
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST, MediaStore.Audio.AudioColumns.ARTIST_ID,};
////        Cursor c = getActivity().getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%yourFolderName%"}, null);
//        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
//        if (c != null) {
//            while (c.moveToNext()) {
//                String selection = "is_music != 0";
//                String album_selection = "";
//                AudioModel audioModel = new AudioModel();
//                String album_art="";
//
//                String path = c.getString(0);
//                String album = c.getString(1);
//                String album_id = c.getString(2);
//                String artist = c.getString(3);
//                String artist_id = c.getString(4);
//                Cursor cursorAlbum  = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID+ "=?", new String[] {String.valueOf(album_id)}, null);
//                if (cursorAlbum != null && cursorAlbum .moveToFirst())
//                {
//                    album_art=cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
//                }
//
//                if (Integer.valueOf(album_id) > 0) {
//                    selection = selection + " and artist_id = " + artist_id;
//                }
//
//                if (Integer.valueOf(album_id) > 0) {
//                    album_selection =" artist_id = " + artist_id;
//                }
//                Cursor cursor_song_list = null;
//                try {
//                    Uri song_uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                    cursor_song_list = getActivity().getContentResolver().query(song_uri, projections, selection, null, sortOrder);
//                    if (cursor_song_list != null) {
//                        cursor_song_list.moveToFirst();
//                        int position = 1;
////                        songs_list=new ArrayList<>();
//                        song_count=0;
//                        while (!cursor_song_list.isAfterLast()) {
//                            song_count++;
//                            cursor_song_list.moveToNext();
//                        }
//                    }
//
//                } catch (Exception e) {
//                    Log.e("Media", e.toString());
//                } finally {
//                    if (cursor_song_list != null) {
//                        cursor_song_list.close();
//                    }
//                }
//
//
//
//
//                Cursor cursor_album_list = null;
//                try {
//                    Uri Albums_uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
//                    cursor_album_list = getActivity().getContentResolver().query(Albums_uri, album_projections, album_selection, null, sortOrder);
//                    if (cursor_album_list != null) {
//                        cursor_album_list.moveToFirst();
//                        album_count=0;
//                        while (!cursor_album_list.isAfterLast()) {
//                            album_count++;
//                            cursor_album_list.moveToNext();
//                        }
//                    }
//
//                } catch (Exception e) {
//                    Log.e("Media", e.toString());
//                } finally {
//                    if (cursor_album_list != null) {
//                        cursor_album_list.close();
//                    }
//                }
//
//
//
//
//
//
//
//
//
//                String name = path.substring(path.lastIndexOf("/") + 1);
//
//                if(!artists_list.contains(album_id))
//                {
//                    artists_list.add(album_id);
//                    audioModel.setaName(name);
//                    audioModel.setaAlbum(album);
//                    audioModel.setaArtist(artist);
//                    audioModel.setaPath(path);
//                    audioModel.setaAlbumart(album_art);
//                    audioModel.setAlbum_song_size(String.valueOf(song_count));
//                    audioModel.setAlbum_size(String.valueOf(album_count));
//                    Log.e("Name :" + name, " Album :" + album);
//                    Log.e("Path :" + path, " Artist :" + artist);
//
//                    tempAudioList.add(audioModel);
//                }
//
//            }
//            c.close();
//            ArtistsAdapter artistsAdapter=new ArtistsAdapter(getActivity(),tempAudioList);
//            artists_recycler.setAdapter(artistsAdapter);
//        }
//
//        return tempAudioList;
//    }
}
