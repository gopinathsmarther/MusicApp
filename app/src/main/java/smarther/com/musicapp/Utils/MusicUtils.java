

package smarther.com.musicapp.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import smarther.com.musicapp.Activity.FolderSongActivity;
import smarther.com.musicapp.Activity.HomeScreen;
import smarther.com.musicapp.Activity.ThemeActivity;
import smarther.com.musicapp.Model.AlbumModel;
import smarther.com.musicapp.Model.ArtistsModel;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.GenresModel;
import smarther.com.musicapp.R;

import static smarther.com.musicapp.Activity.ThemeActivity.permission;
import static smarther.com.musicapp.Activity.ThemeActivity.proceed_permission;


public class MusicUtils {

    public static final String MUSIC_ONLY_SELECTION = MediaStore.Audio.AudioColumns.IS_MUSIC + "=1"
            + " AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''";

    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    public static boolean isJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean isJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static final int getSongCountForGenres(final Context context, final long playlistId) {
        Cursor c = context.getContentResolver().query(
                MediaStore.Audio.Genres.Members.getContentUri("external", playlistId),
                new String[]{BaseColumns._ID}, MUSIC_ONLY_SELECTION, null, null);

        if (c != null) {
            int count = 0;
            if (c.moveToFirst()) {
                count = c.getCount();
            }
            c.close();
            c = null;
            return count;
        }

        return 0;
    }


    //Search Song  using String

    public static List<AudioModel> searchSongs(Context context, String searchString, int limit) {
        ArrayList<AudioModel> result = getSongsForCursor(makeSongCursor(context, "title LIKE ?", new String[]{searchString + "%"}));
        if (result.size() < limit) {
            result.addAll(getSongsForCursor(makeSongCursor(context, "title LIKE ?", new String[]{"%_" + searchString + "%"})));
        }
        return result.size() < limit ? result : result.subList(0, limit);
    }

    public static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString) {
//        final String songSortOrder = PreferencesUtility.getInstance(context).getSongSortOrder();
        return makeSongCursor(context, selection, paramArrayOfString, null);
    }

    private static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString, String sortOrder) {
        String selectionStatement = "is_music=1 AND title != ''";
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST, "title"};
        if (!TextUtils.isEmpty(selection)) {
            selectionStatement = selectionStatement + " AND " + selection;
        }
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selectionStatement, paramArrayOfString, sortOrder);

    }

    public static ArrayList<AudioModel> getSongsForCursor(Cursor cursor) {
        ArrayList<AudioModel> arrayList = new ArrayList<>();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                AudioModel audioModel = new AudioModel();
                String album_art = "";
                String path = cursor.getString(0);
                String album = cursor.getString(1);
                String album_id = cursor.getString(2);
                String artist = cursor.getString(3);
                String title = cursor.getString(4);
                String name = path.substring(path.lastIndexOf("/") + 1);
                audioModel.setaName(name);
                audioModel.setaAlbum(album);
                audioModel.setaArtist(artist);
                audioModel.setaPath(path);
                audioModel.setaAlbumart(album_art);
                audioModel.setTrack(title);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);

                arrayList.add(audioModel);
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    //Search Artists using String
    public static List<ArtistsModel> getArtists(Context context, String paramString, int limit) {
        List<ArtistsModel> result = getArtistsForCursor(makeArtistCursor(context, "artist LIKE ?", new String[]{paramString + "%"}), context);
        if (result.size() < limit) {
            result.addAll(getArtistsForCursor(makeArtistCursor(context, "artist LIKE ?", new String[]{"%_" + paramString + "%"}), context));
        }
        return result.size() < limit ? result : result.subList(0, limit);
    }

    public static List<ArtistsModel> getArtistsForCursor(Cursor cursor, Context context) {
        String album_art = "";
        ArrayList<ArtistsModel> arrayList = new ArrayList();
        String start = "";
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                boolean value = false;
                Cursor album_art_cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"}, MediaStore.Audio.Albums.ARTIST_ID + "=?", new String[]{String.valueOf(cursor.getLong(0))}, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
                if ((album_art_cursor != null) && (album_art_cursor.moveToFirst()))
                    do {
                        Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(album_art_cursor.getLong(0))}, null);
                        if (cursorAlbum != null && cursorAlbum.moveToFirst()) {
                            album_art = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                            if (album_art != null && !album_art.equals("")) {
                                value = true;
                            } else {
                                value = false;
                            }
                        }

                    }
                    while (!value && album_art_cursor.moveToNext());
                if (album_art_cursor != null)
                    album_art_cursor.close();


                if (cursor.getString(1).startsWith("<")) {
                    start = cursor.getString(1).replaceAll("[<>]", "");

                    arrayList.add(new ArtistsModel(cursor.getLong(0), start, cursor.getInt(2), cursor.getInt(3), album_art));
                } else if (cursor.getString(1).startsWith("(")) {
                    start = cursor.getString(1).replaceAll("[()]", "");

                    arrayList.add(new ArtistsModel(cursor.getLong(0), start, cursor.getInt(2), cursor.getInt(3), album_art));
                } else {
                    arrayList.add(new ArtistsModel(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), album_art));
                }
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();

        return arrayList;
    }

    public static Cursor makeArtistCursor(Context context, String selection, String[] paramArrayOfString) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, new String[]{"_id", "artist", "number_of_albums", "number_of_tracks"}, selection, paramArrayOfString, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
        return cursor;
    }




// Search Album using Strings

    public static List<AlbumModel> getAlbums(Context context, String paramString, int limit) {
        List<AlbumModel> result = getAlbumsForCursor(makeAlbumCursor(context, "album LIKE ?", new String[]{paramString + "%"}), context);
        if (result.size() < limit) {
            result.addAll(getAlbumsForCursor(makeAlbumCursor(context, "album LIKE ?", new String[]{"%_" + paramString + "%"}), context));
        }
        return result.size() < limit ? result : result.subList(0, limit);
    }

    public static List<AlbumModel> getAlbumsForCursor(Cursor cursor, Context context) {
        ArrayList<AlbumModel> arrayList = new ArrayList();
        String album_art = "";
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(cursor.getLong(0))}, null);
                if (cursorAlbum != null && cursorAlbum.moveToFirst()) {
                    album_art = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                }
                arrayList.add(new AlbumModel(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4), cursor.getInt(5), album_art));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static Cursor makeAlbumCursor(Context context, String selection, String[] paramArrayOfString) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"}, selection, paramArrayOfString, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);

        return cursor;
    }

    public static AudioModel getSongFromPath(String songPath, Context context) {
        ContentResolver cr = context.getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.DATA;
        String[] selectionArgs = {songPath};
        String[] projection = new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id"};
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = cr.query(uri, projection, selection + "=?", selectionArgs, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            AudioModel song = getSongForCursor(cursor, context);
            cursor.close();
            return song;
        } else return new AudioModel();
    }

    public static AudioModel getSongForCursor(Cursor cursor, Context context) {
        AudioModel audioModel = new AudioModel();
        if ((cursor != null) && (cursor.moveToFirst())) {
//            AudioModel audioModel = new AudioModel();
            String album_art = "";
            String path = cursor.getString(0);
            String album = cursor.getString(1);
            String album_id = cursor.getString(2);
            String artist = cursor.getString(3);
            String title = cursor.getString(4);
            Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(album_id)}, null);
            if (cursorAlbum != null && cursorAlbum.moveToFirst()) {
                album_art = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            }

            String name = path.substring(path.lastIndexOf("/") + 1);

            audioModel.setaName(name);
            audioModel.setaAlbum(album);
            audioModel.setaArtist(artist);
            audioModel.setaPath(path);
            audioModel.setaAlbumart(album_art);
            audioModel.setTrack(title);
        }

        if (cursor != null)
            cursor.close();
        return audioModel;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //FolderActivity
    public  static ArrayList<AudioModel> getAudioFileFolderSongs(Context context ,final String dirPath)
    {

        System.out.println("aji folder"+dirPath);
        String selection = MediaStore.Audio.Media.DATA + " like ?";
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST,"title"};
        String[] selectionArgs = {dirPath + "%"};
        Cursor cursor =context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor != null){

            ArrayList<AudioModel> arrayList = new ArrayList();
            if ((cursor != null) && (cursor.moveToFirst()))
                do {
                    if(cursor.getPosition()==0)
                    {
//                        AlbumModel albumModel= MusicUtils.getAlbum(context,cursor.getLong(2));
//                        if(albumModel!=null)
//                        {
//                            if(albumModel.getAlbum_art()==null || albumModel.getAlbum_art().equals(""))
//                            {
//                                album_art_display.setVisibility(View.GONE);
//                                no_album_image.setVisibility(View.VISIBLE);
//                            }
//                            else
//                            {
//                                no_album_image.setVisibility(View.GONE);
//                                album_art_display.setVisibility(View.VISIBLE);
//                                Bitmap bm= BitmapFactory.decodeFile(albumModel.getAlbum_art());
//                                album_art_display.setImageBitmap(bm);
//                            }
//
//                        }
                    }
                    AudioModel audioModel = new AudioModel();
                    String album_art="";
                    String path = cursor.getString(0);
                    String album = cursor.getString(1);
                    String album_id = cursor.getString(2);
                    String artist = cursor.getString(3);
                    String title = cursor.getString(4);
                    Cursor cursorAlbum  =context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID+ "=?", new String[] {String.valueOf(album_id)}, null);
                    if (cursorAlbum != null && cursorAlbum .moveToFirst())
                    {
                        album_art=cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                    }

                    String name = path.substring(path.lastIndexOf("/") + 1);

                    audioModel.setaName(name);
                    audioModel.setaAlbum(album);
                    audioModel.setaArtist(artist);
                    audioModel.setaPath(path);
                    audioModel.setaAlbumart(album_art);
                    audioModel.setTrack(title);

                    Log.e("Name :" + name, " Album :" + album);
                    Log.e("Path :" + path, " Artist :" + artist);

                    arrayList.add(audioModel);
                }
                while (cursor.moveToNext());
            if (cursor != null)
                cursor.close();
            return arrayList;
        }
        else {
            return null;
        }


    }
//---------------------------------------------------------------------------------------------------------------------------------------------

    //Get Album Details and Songs Based on Album Id
    public static AlbumModel getAlbum(Context context, long id) {
        Cursor cursor = makeAlbumCursor(context, "_id=?", new String[]{String.valueOf(id)});
        AlbumModel album = new AlbumModel();
        if (cursor != null) {
            String album_art = "";
            Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(id)}, null);
            if (cursorAlbum != null && cursorAlbum.moveToFirst()) {
                album_art = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            }
            if (cursor.moveToFirst())
                album = new AlbumModel(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4), cursor.getInt(5), album_art);
        }
        if (cursor != null)
            cursor.close();
        return album;
    }

    public static ArrayList<AudioModel> getSongsForAlbum(Context context, long albumID) {

        ContentResolver contentResolver = context.getContentResolver();
//        final String albumSongSortOrder = PreferencesUtility.getInstance(context).getAlbumSongSortOrder();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST, "title"};
        String string = "is_music=1 AND title != '' AND album_id=" + albumID;
        Cursor cursor = contentResolver.query(uri, projection, string, null, null);

//        Cursor cursor = makeAlbumSongCursor(context, albumID);
        ArrayList<AudioModel> arrayList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                AudioModel audioModel = new AudioModel();
                String album_art = "";
                String path = cursor.getString(0);
                String album = cursor.getString(1);
                String album_id = cursor.getString(2);
                String artist = cursor.getString(3);
                String title = cursor.getString(4);
                Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(album_id)}, null);
                if (cursorAlbum != null && cursorAlbum.moveToFirst()) {
                    album_art = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                }

                String name = path.substring(path.lastIndexOf("/") + 1);

                audioModel.setaName(name);
                audioModel.setaAlbum(album);
                audioModel.setaArtist(artist);
                audioModel.setaPath(path);
                audioModel.setaAlbumart(album_art);
                audioModel.setTrack(title);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);

                arrayList.add(audioModel);
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//Get Artist detail,album list,songs  based on artist id

    public static ArtistsModel getArtist(Context context, long id) {

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, new String[]{"_id", "artist", "number_of_albums", "number_of_tracks"}, "_id=?", new String[]{String.valueOf(id)}, null);
        boolean value = false;
        String album_art = "";
        Cursor album_art_cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"}, MediaStore.Audio.Albums.ARTIST_ID + "=?", new String[]{String.valueOf(id)}, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        if ((album_art_cursor != null) && (album_art_cursor.moveToFirst()))
            do {
                Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(album_art_cursor.getLong(0))}, null);
                if (cursorAlbum != null && cursorAlbum.moveToFirst()) {
                    album_art = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                    if (album_art != null && !album_art.equals("")) {
                        value = true;
                    } else {
                        value = false;
                    }
                }

            }
            while (!value && album_art_cursor.moveToNext());
        if (album_art_cursor != null)
            album_art_cursor.close();
        ArtistsModel artistsModel = new ArtistsModel();
        if (cursor != null) {
            if (cursor.moveToFirst())
                artistsModel = new ArtistsModel(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), album_art);
        }
        if (cursor != null)
            cursor.close();
        return artistsModel;
    }

    public static List<AlbumModel> LoadAllAlbums(final Context context, Long artist_id) {
        List<AlbumModel> album_list = new ArrayList<>();
        String album_art = "";
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"}, "artist_id=?", new String[]{String.valueOf(artist_id)}, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(cursor.getLong(0))}, null);
                if (cursorAlbum != null && cursorAlbum.moveToFirst()) {
                    album_art = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                }
                album_list.add(new AlbumModel(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4), cursor.getInt(5), album_art));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return album_list;
    }

    public static List<AudioModel> getAllSongBasedonArtist(final Context context, Long artist_id) {

        final List<AudioModel> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST, "title", "_id", MediaStore.Audio.AudioColumns.DURATION};

//        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST,"title"};
//        Cursor c = getActivity().getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%yourFolderName%"}, null);
        Cursor c = context.getContentResolver().query(uri, projection, "artist_id=?", new String[]{String.valueOf(artist_id)}, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        if (c != null) {
            while (c.moveToNext()) {

                AudioModel audioModel = new AudioModel();
                String album_art = "";
                String path = c.getString(0);
                String album = c.getString(1);
                String album_id = c.getString(2);
                String artist = c.getString(3);
                String title = c.getString(4);
                String duration = c.getString(6);
                Log.e("abbas", "duration " + duration);
                Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(album_id)}, null);
                if (cursorAlbum != null && cursorAlbum.moveToFirst()) {
                    album_art = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                }

                String name = path.substring(path.lastIndexOf("/") + 1);

                audioModel.setaName(name);
                audioModel.setaAlbum(album);
                audioModel.setaArtist(artist);
                audioModel.setaPath(path);
                audioModel.setaAlbumart(album_art);
                audioModel.setAlbum_id(album_id);
                audioModel.setTrack(title);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);

                tempAudioList.add(audioModel);
            }
            c.close();

        }

        return tempAudioList;
    }



    public static void initSetAsRingTone2(Activity context, Uri song) {
        if (MusicUtils.isMarshmallow()) {
            MusicUtils.checkRingtonePermissionAndThenLoad2(context, song);
        } else {
            MusicUtils.CuttersetAsRingTone(context, song);
        }
    }

    public static void checkRingtonePermissionAndThenLoad2(final Activity context, Uri song) {
        //check for permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_SETTINGS}, 3);
//                final RingtonePermissionCallback2 ringtonePermissionCallback = new RingtonePermissionCallback2();
//                ringtonePermissionCallback.setContext(context);
//                ringtonePermissionCallback.setSong(song);
        } else {
            MusicUtils.CuttersetAsRingTone(context, song);
        }
    }

    public static void CuttersetAsRingTone(Activity context, Uri song) {

        try {
            RingtoneManager.setActualDefaultRingtoneUri(
                    context.getApplicationContext(), RingtoneManager.TYPE_RINGTONE,
                    song);

            Toast.makeText(context, R.string.success, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, "Your device does not support ringtone settings", Toast.LENGTH_SHORT).show();

        }
    }

    //---------------------------------------------------------------------------------------------------------------------
    class RingtonePermissionCallback2 implements PermissionCallback {
        Activity context;
        Uri song;

        public void setContext(Activity context) {
            this.context = context;
        }

        public void setSong(Uri song) {
            this.song = song;
        }

        @Override
        public void permissionGranted() {

            MusicUtils.CuttersetAsRingTone(this.context, this.song);
        }

        @Override
        public void permissionRefused() {
            Log.v("Ringtone", "Per refused");
        }
    }
//-----------------------------------------------------------------------------------------------------------------------------------
    //return all songs in the phone

    public static List<AudioModel> getAllAudioFromDevice(final Context context) {
        int temp_id = 0;
        final List<AudioModel> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//      Uri  uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.AudioColumns.ARTIST_ID, MediaStore.Audio.ArtistColumns.ARTIST, "title", "_id", MediaStore.Audio.Media.DURATION};

//        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST,"title"};
//        Cursor c = getActivity().getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%yourFolderName%"}, null);
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                System.out.println("aji cursor " + Arrays.toString(c.getColumnNames()));
                AudioModel audioModel = new AudioModel();
                String album_art = "";
                String path = c.getString(0);
                String album = c.getString(1);
                String album_id = c.getString(2);
                String artist_id = c.getString(3);
                String artist = c.getString(4);
                String title = c.getString(5);
                long duration = c.getLong(7);
                System.out.println("aji duration " + duration);


                long song_minutes = (duration / 1000) / 60;
                long song_seconds = (duration / 1000) % 60;
                String song_minutes_string = String.valueOf(song_minutes);
                String song_seconds_string = String.valueOf(song_seconds);


                if (song_minutes_string.length() == 1) {
                    song_minutes_string = "0" + song_minutes;

                } else {
                    song_minutes_string = "" + song_minutes;
                }
                if (song_seconds_string.length() == 1) {
                    song_seconds_string = "0" + song_seconds;

                } else {
                    song_seconds_string = "" + song_seconds;
                }


                //  String length = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
                System.out.println("aji length " + song_minutes_string + " : " + song_seconds_string);
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


                Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(album_id)}, null);
                if (cursorAlbum != null && cursorAlbum.moveToFirst()) {
                    album_art = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
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
                audioModel.setTemp_id(String.valueOf(temp_id++));
//                audioModel.setBitRate(bitRate);
//                audioModel.setSampleRate(sampleRate);
//                audioModel.setLength(length);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);

                System.out.println("<<<<<<<<<<<<<<<<<<<<<<COMPLETED>>>>>>>>>>>>>>>>>>>>>>>>>" + tempAudioList.size());

                Toast.makeText(context, path, Toast.LENGTH_SHORT);
                tempAudioList.add(audioModel);
            }
            c.close();

            System.out.println("COMPLETED>>>>>>>>>>>>>>>>>>>>>>>>>" + tempAudioList.size());


        }

        return tempAudioList;
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------
    public static GenresModel getGenre(final Context context, long _id) {

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, new String[]{BaseColumns._ID, MediaStore.Audio.GenresColumns.NAME}, "_id=?", new String[]{String.valueOf(_id)}, MediaStore.Audio.Genres.DEFAULT_SORT_ORDER);
//
        GenresModel album = new GenresModel();
        if (cursor != null) {
            String album_art = "";
            Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(_id)}, null);
            if (cursorAlbum != null && cursorAlbum.moveToFirst()) {
                album_art = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            }
            if (cursor.moveToFirst())

            //album = new GenresModel(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4), cursor.getInt(5),album_art);

            {
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                int songCount = getSongCountForGenres(context, id);
                 album = new GenresModel(id, name, songCount);

            }


            if (cursor != null)
                cursor.close();

        }
        return album;
    }

    public static ArrayList<AudioModel> getSongsForGenre(Context context, long albumID) {

        ContentResolver contentResolver = context.getContentResolver();
//        final String albumSongSortOrder = PreferencesUtility.getInstance(context).getAlbumSongSortOrder();
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_ID, MediaStore.Audio.ArtistColumns.ARTIST, "title"};
//        String string = "is_music=1 AND title != '' AND _id=" + albumID;
//        String[] STAR = { MediaStore.Audio.Genres._ID,
//                MediaStore.Audio.Genres.NAME };
//        String query = " _id in (select genre_id from audio_genres_map where audio_id in (select _id from audio_meta where is_music != 0))" ;
//        Cursor cursor = contentResolver.query(uri, STAR, query, null, null);
        Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", albumID);

//        String[] projection = new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media._ID};

        Cursor cursor = contentResolver.query(uri, projection, null, null, null);


//        Cursor cursor = makeAlbumSongCursor(context, albumID);
        ArrayList<AudioModel> arrayList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                AudioModel audioModel = new AudioModel();
                String album_art = "";
                String path = cursor.getString(0);
                String album = cursor.getString(1);
                String album_id = cursor.getString(2);
                String artist = cursor.getString(3);
                String title = cursor.getString(4);
                Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(album_id)}, null);
                if (cursorAlbum != null && cursorAlbum.moveToFirst()) {
                    album_art = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                }

                String name = path.substring(path.lastIndexOf("/") + 1);

                audioModel.setaName(name);
                audioModel.setaAlbum(album);
                audioModel.setaArtist(artist);
                audioModel.setaPath(path);
                audioModel.setAlbum_id(album_id);
                audioModel.setaAlbumart(album_art);
                audioModel.setTrack(title);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);

                arrayList.add(audioModel);
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }




}
