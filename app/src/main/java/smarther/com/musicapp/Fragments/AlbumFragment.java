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
import smarther.com.musicapp.Adapter.AlbumsAdapter;
import smarther.com.musicapp.Adapter.SongsAdapter;
import smarther.com.musicapp.Model.AlbumModel;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.SessionManager;

public class AlbumFragment extends Fragment {
    public static RecyclerView albums_recycler;
    public static AlbumsAdapter albumsAdapter;
    SessionManager sessionManager;

    //    ArrayList<String> songs_list = new ArrayList<>();
    int album_count=0;
    String[] projections = {MediaStore.Audio.Media.TITLE,};
    final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(getActivity());
        View view=inflater.inflate(R.layout.fragment_album, container, false);
        albums_recycler = (RecyclerView)view.findViewById(R.id.albums_recycler);
        albums_recycler.setLayoutManager(new GridLayoutManager(getActivity(), sessionManager.getAlbumsGrid(), LinearLayoutManager.VERTICAL, false));
        albums_recycler.setHasFixedSize(true);
        albums_recycler.setNestedScrollingEnabled(false);
        List<AlbumModel> albumModelList = LoadAllAlbums(getActivity());
        if(sessionManager.getAlbumsSort()==1)
        {
            Collections.sort(albumModelList, AlbumModel.namecomparator);

        }else if(sessionManager.getAlbumsSort()==2)
        {
            Collections.sort(albumModelList, AlbumModel.descending);

        }else if(sessionManager.getAlbumsSort()==3)
        {
            Collections.sort(albumModelList, AlbumModel.artist);

        }else if(sessionManager.getAlbumsSort()==4)
        {
            Collections.sort(albumModelList, AlbumModel.year_sort);

        }

                albumsAdapter=new AlbumsAdapter(getActivity(),albumModelList);
        albums_recycler.setAdapter(albumsAdapter);
        return view;
    }

    public List<AlbumModel> LoadAllAlbums(final Context context)

    {
        List<AlbumModel> album_list=new ArrayList<>();
        String album_art="";
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{"_id","album", "artist", "artist_id", "numsongs", "minyear"}, null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                Cursor cursorAlbum  = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID+ "=?", new String[] {String.valueOf(cursor.getLong(0))}, null);
                if (cursorAlbum != null && cursorAlbum .moveToFirst())
                {
                    album_art=cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                }
                album_list.add(new AlbumModel(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4), cursor.getInt(5),album_art));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return album_list;
    }





//    public List<AudioModel> getAllAudioFromDevice(final Context context) {
//
//        final List<AudioModel> tempAudioList = new ArrayList<>();
//
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST,};
////        Cursor c = getActivity().getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%yourFolderName%"}, null);
//        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
//        if (c != null) {
//            while (c.moveToNext()) {
//                String selection = "is_music != 0";
//
//                AudioModel audioModel = new AudioModel();
//                String album_art="";
//
//                String path = c.getString(0);
//                String album = c.getString(1);
//                String album_id = c.getString(2);
//                String artist = c.getString(3);
//                Cursor cursorAlbum  = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID+ "=?", new String[] {String.valueOf(album_id)}, null);
//                if (cursorAlbum != null && cursorAlbum .moveToFirst())
//                {
//                    album_art=cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
//                }
//
//                if (Integer.valueOf(album_id) > 0) {
//                    selection = selection + " and album_id = " + album_id;
//                }
//                Cursor cursor_song_list = null;
//                try {
//                    Uri song_uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                    cursor_song_list = getActivity().getContentResolver().query(song_uri, projections, selection, null, sortOrder);
//                    if (cursor_song_list != null) {
//                        cursor_song_list.moveToFirst();
//                        int position = 1;
////                        songs_list=new ArrayList<>();
//                        album_count=0;
//                        while (!cursor_song_list.isAfterLast()) {
//                            album_count++;
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
//                String name = path.substring(path.lastIndexOf("/") + 1);
//
//                if(!album_list.contains(album_id))
//                {
//                    album_list.add(album_id);
//                    audioModel.setaName(name);
//                    audioModel.setaAlbum(album);
//                    audioModel.setaArtist(artist);
//                    audioModel.setaPath(path);
//                    audioModel.setaAlbumart(album_art);
//                    audioModel.setAlbum_song_size(String.valueOf(album_count));
//                    Log.e("Name :" + name, " Album :" + album);
//                    Log.e("Path :" + path, " Artist :" + artist);
//
//                    tempAudioList.add(audioModel);
//                }
//
//            }
//            c.close();
//            AlbumsAdapter songsAdapter=new AlbumsAdapter(getActivity(),tempAudioList);
//            albums_recycler.setAdapter(songsAdapter);
//        }
//
//        return tempAudioList;
//    }

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
