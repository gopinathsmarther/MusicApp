package smarther.com.musicapp.Adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import smarther.com.musicapp.Activity.AlbumDetailsActivity;
import smarther.com.musicapp.Activity.ArtistsDetailsActivity;
import smarther.com.musicapp.Activity.RingtoneCutter.RingtoneCutter;
import smarther.com.musicapp.Activity.SongPlayingActivity;
import smarther.com.musicapp.Activity.YoutubeActivity;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.Model.PlaylistModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.RingtoneUtils;
import smarther.com.musicapp.Utils.SessionManager;

public class GenreSongsAdapter extends RecyclerView.Adapter<GenreSongsAdapter.MyViewHolder> {
    int millSecond=0;

    LinearLayout background_of_layout;
    TextView add_to_playlist_add;
    TextView add_to_playlist_cancel;
    TextView add_all;
    TextView skip_all;
    Realm music_database;
    Realm playlist_database;
    RecyclerView playlist_alert_recyclerView;
    RecyclerView duplicate_recycler;
    PlaylistAddAdapter playlistAddAdapter;
    DuplicatePlaylistAdapter duplicatePlaylistAdapter;
    List<PlaylistAudioModel> playlistAudioModel =new ArrayList<>();
    List<PlaylistModel> playlistModelList =new ArrayList<>();
    public static List<AudioModel> audioModelList;
    TextView song_filename;
    TextView song_size;
    TextView song_path;
    TextView song_format;
    TextView song_length;
    TextView song_bitrateRate;
    TextView song_samplingRate;
    TextView song_tag_edit;
    TextView song_ok;



    Context context;
    SessionManager sessionManager;
    List<AudioModel> playing_audio_list;
    public GenreSongsAdapter(Context context, List<AudioModel> audioModelList) {
        this.context = context;
        this.audioModelList = audioModelList;
    }

    @NonNull
    @Override
    public GenreSongsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.albumsong_list_item, parent, false);
        sessionManager=new SessionManager(context);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull final GenreSongsAdapter.MyViewHolder holder, final int position) {
        Log.e("abbas",position+" "+audioModelList.get(position).getaAlbumart());
//        if(audioModelList.get(position).getaAlbumart()==null || audioModelList.get(position).getaAlbumart().equals(""))
//        {
//            holder.audio_image.setVisibility(View.GONE);
//            holder.no_audio_image.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            holder.no_audio_image.setVisibility(View.GONE);
//            holder.audio_image.setVisibility(View.VISIBLE);
//            Bitmap bm= BitmapFactory.decodeFile(audioModelList.get(position).getaAlbumart());
//            holder.audio_image.setImageBitmap(bm);
////            Glide.with(context).load().into(holder.audio_image);
//        }
        holder.audio_title.setText(audioModelList.get(position).getTrack());
        holder.audio_authur.setText(audioModelList.get(position).getaArtist());
        holder.album_song_count.setText(String.valueOf(position+1));
        holder.song_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                List<AudioModel> shuffle_audioModelList = new ArrayList<>(audioModelList);
                sessionManager.setSongPosition(position);
                if(sessionManager.getRememberShuffle()) {
                    Collections.shuffle(shuffle_audioModelList);
                    System.out.println("aji shuffle " + shuffle_audioModelList.get(0).getaName());
                    int shuffle_position=0;
//                int a = shuffle_audioModelList.indexOf(audioModelList.get(position));
//                System.out.println("aji shuffle "+a);
                    for (int z =0;z<shuffle_audioModelList.size();z++)
                    {
                        System.out.println("aji shuffle for "+shuffle_audioModelList.get(z).getaName());
                        System.out.println("aji shuffle for "+audioModelList.get(position).getaName());
                        System.out.println("aji shuffle >>>>>");
                        if(shuffle_audioModelList.get(z).getaPath().equals(audioModelList.get(position).getaPath()) ){

                            shuffle_position = z;
                            System.out.println("aji shuffle pos "+shuffle_position);
                            break;
                        }
                    }
                    sessionManager.setSongPosition(0);
                    Collections.swap(shuffle_audioModelList,0,shuffle_position);
                    System.out.println("aji shuffle after swap "+shuffle_audioModelList.get(0).getaName());
                    System.out.println("aji shuffle after swap pos"+shuffle_audioModelList.get(shuffle_position).getaName());

                }
                else {

                }

                Gson gson = new Gson();
                String json = gson.toJson(shuffle_audioModelList.get(0));
                sessionManager.setSong_Json(json);
                Gson gson_list = new GsonBuilder().create();
                JsonArray song_array = gson_list.toJsonTree(shuffle_audioModelList).getAsJsonArray();
                sessionManager.setSong_JsonList(song_array.toString());


//                Global.intent_method(context, SongPlayingActivity.class);

                Intent intent=new Intent(context,SongPlayingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("value",2);
                context.startActivity(intent);
//                if(BackgroundMusicService.bg_mediaPlayer!=null)
//                {
//                    BackgroundMusicService.bg_mediaPlayer.stop();
//                }
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        holder.other_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(context, R.style.popupMenuStyle);
//                PopupMenu popup = new PopupMenu(wrapper, view);
                PopupMenu popup = new PopupMenu(wrapper, holder.other_option);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.song_fragment_popupmenu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(final MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.play_next:
                                final Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
                                playing_audio_list= new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
                                playing_audio_list.add(2,audioModelList.get(position));
                                Gson gson_list = new GsonBuilder().create();
                                JsonArray song_array = gson_list.toJsonTree(playing_audio_list).getAsJsonArray();
                                sessionManager.setSong_JsonList(song_array.toString());
                                break;
                            case R.id.add_to_playing_queue:
                                final Type types = new TypeToken<ArrayList<AudioModel>>(){}.getType();
                                playing_audio_list= new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), types);
                                playing_audio_list.add(audioModelList.get(position));
                                Gson gson_lists = new GsonBuilder().create();
                                JsonArray song_arrays = gson_lists.toJsonTree(playing_audio_list).getAsJsonArray();
                                sessionManager.setSong_JsonList(song_arrays.toString());
                                System.out.println("aji added to list"+audioModelList.get(position).getaName());
                                break;
                            case R.id.add_to_playlist:
                                add_to_playlist_alert(context,position);
                                //all_playlist(context);
                                break;
                            case R.id.youtube:

                                // Global.intent_method(context, YoutubeActivity.class);
                                String search = audioModelList.get(position).getaName().replaceAll(" ","+");
                                search = search.replaceAll(".mp3","");
                                Intent intent=new Intent(context, YoutubeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("url","https://www.youtube.com/results?search_query="+search);
                                context.startActivity(intent);
                                break;
                            case R.id.go_to_album:
                                System.out.println("aji album name"+audioModelList.get(position).getAlbum_id());
                                sessionManager.setAlbumId(Long.parseLong(audioModelList.get(position).getAlbum_id()));
                                Global.intent_method(context, AlbumDetailsActivity.class);
                                break;
                            case R.id.go_to_artist:
                                sessionManager.setArtistId(Long.parseLong(audioModelList.get(position).getArtist_id()));
                                Global.intent_method(context, ArtistsDetailsActivity.class);
                                break;
                            case R.id.share:
                                shareFile(audioModelList.get(position).getaPath(),audioModelList.get(position).getaAlbumart());
                                break;
                            case R.id.tag_editor:
                                View alertLayout = LayoutInflater.from(context).inflate(R.layout.song_tag_editor, null);
                                background_of_layout = alertLayout.findViewById(R.id.background_of_layout);
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

                                    }
                                    catch (Exception e)
                                    {
                                        bg = ContextCompat.getDrawable(context, R.drawable.theme_1);
                                        e.printStackTrace();

                                    }
                                    background_of_layout.setBackground(bg);
                                }
                                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setCancelable(false);
                                alert.setView(alertLayout);
                                alert.setCancelable(true);




                                final AlertDialog dialog = alert.create();
                                dialog.show();

                                break;
                            case R.id.details:
                                alert_song_details(position);
                                break;
                            case R.id.set_as_ringtone:
//                                dayString = "Sunday";
                                setRingtone(context,audioModelList.get(position).getaPath());
                                break;
                            case R.id.ringtone_cutter:
//                                dayString = "Sunday";
                                intent=new Intent(context, RingtoneCutter.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("mFilename",audioModelList.get(position).getaPath());
                                context.startActivity(intent);
                                break;
                            case R.id.delete_from_device:
////                                dayString = "Sunday";
                                File file = new File(audioModelList.get(position).getaPath());
                                file.delete();
                                context.deleteFile(file.getName());
                                if(file.exists()){
                                    try {
                                        file.getCanonicalFile().delete();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if(file.exists()){
                                        context.deleteFile(file.getName());
                                    }
                                }
                                notifyDataSetChanged();
//                                File file =new File(audioModelList.get(position).getaPath());
//                                System.out.println("aji + song path "+audioModelList.get(position).getaPath());
//                                if(file.exists())
//                                {
//                                    boolean k = file.delete();
//                                    System.out.println("aji + album del res "+k);
//                                    if(k)
//                                    {
//                                        Toast.makeText(context, ""+audioModelList.get(position).getaName()+" is deleted", Toast.LENGTH_SHORT).show();
//                                    }
//                                    else
//                                        {
//                                        Toast.makeText(context, ""+audioModelList.get(position).getaName()+" is not deleted", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
                                break;





                            default:
//                                dayString = "Invalid day";
                        }
                        return true;
                    }
                });

                popup.show(); //sho
            }
        });







    }

    @Override
    public int getItemCount() {
         return audioModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        //ImageView audio_image;
        //        RelativeLayout no_audio_image;
        TextView audio_title;
        TextView audio_authur;
        TextView album_song_count;
        LinearLayout other_option;
        LinearLayout song_layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //            audio_image = (ImageView) itemView.findViewById(R.id.audio_image);
//            no_audio_image = (RelativeLayout) itemView.findViewById(R.id.no_audio_image);
            audio_title = (TextView) itemView.findViewById(R.id.audio_title);
            album_song_count = (TextView) itemView.findViewById(R.id.album_song_count);
            audio_authur = (TextView) itemView.findViewById(R.id.audio_authur);
            other_option = (LinearLayout) itemView.findViewById(R.id.other_option);
            song_layout = (LinearLayout) itemView.findViewById(R.id.song_layout);
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    public void add_to_playlist_alert(final Context context, final int position){



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
                if(sessionManager.getDupilcatePlaylistSongId()==1){
                    if(playlistAddAdapter.playlistId_list.size()>0)
                    {
                        for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++){


                            playlist_database.beginTransaction();

                            PlaylistAudioModel playlistAudioModel=playlist_database.createObject(PlaylistAudioModel.class);

                            // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
                            long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));
                            playlistAudioModel.setPlaylist_id(a);
                            playlistAudioModel.setaPath(audioModelList.get(position).getaPath());
                            playlistAudioModel.setaName(audioModelList.get(position).getaName());
                            playlistAudioModel.setaAlbum(audioModelList.get(position).getaAlbum());
                            playlistAudioModel.setAlbum_id(audioModelList.get(position).getAlbum_id());
                            playlistAudioModel.setaArtist(audioModelList.get(position).getaArtist());
                            playlistAudioModel.setArtist_id(audioModelList.get(position).getArtist_id());
                            playlistAudioModel.setTrack(audioModelList.get(position).getTrack());
                            playlistAudioModel.setLength(audioModelList.get(position).getLength());
                            playlistAudioModel.setSong_added_on(audioModelList.get(position).getSong_added_on());
                            playlist_database.commitTransaction();
                            playlistAddAdapter.notifyDataSetChanged();
                            System.out.println("aji "+i+" pl_ID "+ playlistAddAdapter.playlistId_list.get(i));
                            System.out.println("aji "+playlist_database.toString());

                        }

                    }
                }
                else if(sessionManager.getDupilcatePlaylistSongId()==3){
                    if(playlistAddAdapter.playlistId_list.size()>0)
                    {
                        for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++) {
                            long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));

                            PlaylistAudioModel result = playlist_database.where(PlaylistAudioModel.class).equalTo("playlist_id", a).equalTo("aPath", audioModelList.get(position).getaPath()).findFirst();
                            if (result == null){
                                playlist_database.beginTransaction();

                                PlaylistAudioModel playlistAudioModel = playlist_database.createObject(PlaylistAudioModel.class);

                                // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
//                            long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));
                                playlistAudioModel.setPlaylist_id(a);
                                playlistAudioModel.setaPath(audioModelList.get(position).getaPath());
                                playlistAudioModel.setaName(audioModelList.get(position).getaName());
                                playlistAudioModel.setaAlbum(audioModelList.get(position).getaAlbum());
                                playlistAudioModel.setAlbum_id(audioModelList.get(position).getAlbum_id());
                                playlistAudioModel.setaArtist(audioModelList.get(position).getaArtist());
                                playlistAudioModel.setArtist_id(audioModelList.get(position).getArtist_id());
                                playlistAudioModel.setTrack(audioModelList.get(position).getTrack());
                                playlistAudioModel.setLength(audioModelList.get(position).getLength());
                                playlistAudioModel.setSong_added_on(audioModelList.get(position).getSong_added_on());
                                playlist_database.commitTransaction();
                                playlistAddAdapter.notifyDataSetChanged();
                                System.out.println("aji " + i + " pl_ID " + playlistAddAdapter.playlistId_list.get(i));
                                System.out.println("aji " + playlist_database.toString());
                            }
                        }

                    }
                }
                else if(sessionManager.getDupilcatePlaylistSongId()==2){
                    System.out.println("aji duplicate");
                    if(playlistAddAdapter.playlistId_list.size()>0)
                    {
                        for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++) {
                            long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));

                            PlaylistAudioModel result = playlist_database.where(PlaylistAudioModel.class).equalTo("playlist_id", a).equalTo("aPath", audioModelList.get(position).getaPath()).findFirst();
                            if (result == null){
                                playlist_database.beginTransaction();

                                PlaylistAudioModel playlistAudioModel = playlist_database.createObject(PlaylistAudioModel.class);

                                // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
//                            long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));
                                playlistAudioModel.setPlaylist_id(a);
                                playlistAudioModel.setaPath(audioModelList.get(position).getaPath());
                                playlistAudioModel.setaName(audioModelList.get(position).getaName());
                                playlistAudioModel.setaAlbum(audioModelList.get(position).getaAlbum());
                                playlistAudioModel.setAlbum_id(audioModelList.get(position).getAlbum_id());
                                playlistAudioModel.setaArtist(audioModelList.get(position).getaArtist());
                                playlistAudioModel.setArtist_id(audioModelList.get(position).getArtist_id());
                                playlistAudioModel.setTrack(audioModelList.get(position).getTrack());
                                playlistAudioModel.setLength(audioModelList.get(position).getLength());
                                playlistAudioModel.setSong_added_on(audioModelList.get(position).getSong_added_on());
                                playlist_database.commitTransaction();
                                playlistAddAdapter.notifyDataSetChanged();
                                System.out.println("aji " + i + " pl_ID " + playlistAddAdapter.playlistId_list.get(i));
                                System.out.println("aji " + playlist_database.toString());
                            }
                            else{
                                PlaylistAudioModel playlistAudioModel = new PlaylistAudioModel();
                                playlistAudioModel.setPlaylist_id(a);
                                playlistAudioModel.setaPath(audioModelList.get(position).getaPath());
                                playlistAudioModel.setaName(audioModelList.get(position).getaName());
                                playlistAudioModel.setaAlbum(audioModelList.get(position).getaAlbum());
                                playlistAudioModel.setAlbum_id(audioModelList.get(position).getAlbum_id());
                                playlistAudioModel.setaArtist(audioModelList.get(position).getaArtist());
                                playlistAudioModel.setArtist_id(audioModelList.get(position).getArtist_id());
                                playlistAudioModel.setTrack(audioModelList.get(position).getTrack());
                                playlistAudioModel.setLength(audioModelList.get(position).getLength());
                                playlistAudioModel.setSong_added_on(audioModelList.get(position).getSong_added_on());
                                DuplicatePlaylistAdapter.duplicates.add(playlistAudioModel);
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
    private void shareFile(String filePath,String album_filepath) {

        File f = new File(filePath);

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(filePath);

        if (fileWithinMyDir.exists()) {
            intentShareFile.setType("text/*");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, f.getName());
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "-Shared via Muzio Player");
            context.startActivity(Intent.createChooser(intentShareFile, f.getName()));
        }
    }
    public void alert_song_details(int position){
        View alertLayout = LayoutInflater.from(context).inflate(R.layout.song_details_alert, null);
        background_of_layout = alertLayout.findViewById(R.id.background_of_layout);
        song_filename = alertLayout.findViewById(R.id.song_filename);
        song_path = alertLayout.findViewById(R.id.song_path);
        song_size = alertLayout.findViewById(R.id.song_size);
        song_ok = alertLayout.findViewById(R.id.song_ok);
        song_length = alertLayout.findViewById(R.id.song_length);
        song_samplingRate = alertLayout.findViewById(R.id.song_samplingRate);
        song_bitrateRate = alertLayout.findViewById(R.id.song_bitrateRate);
        song_size = alertLayout.findViewById(R.id.song_size);




        try {

            Uri uri = Uri.parse(audioModelList.get(position).getaPath());
            System.out.println("aji play q path "+audioModelList.get(position).getaPath());
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(context,uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            millSecond = millSecond + Integer.parseInt(durationStr);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

            }
            catch (Exception e)
            {
                bg = ContextCompat.getDrawable(context, R.drawable.theme_1);
                e.printStackTrace();

            }
            background_of_layout.setBackground(bg);
        }


        song_filename.setText(audioModelList.get(position).getaName());
        song_path.setText(audioModelList.get(position).getaPath());
        song_length.setText(""+String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millSecond), TimeUnit.MILLISECONDS.toMinutes(millSecond) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millSecond)), TimeUnit.MILLISECONDS.toSeconds(millSecond) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millSecond))));
        MediaExtractor mex = new MediaExtractor();
        try {
            mex.setDataSource(audioModelList.get(position).getaPath());// the adresss location of the sound on sdcard.
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MediaFormat mf = mex.getTrackFormat(0);
        int bitRate = mf.getInteger(MediaFormat.KEY_BIT_RATE);
        int sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        song_samplingRate.setText(sampleRate+"Hz");
        song_bitrateRate.setText((bitRate/1000)+"kb/s");
        System.out.println("aji songs bitrate "+bitRate+" "+sampleRate);

        File file = new File(audioModelList.get(position).getaPath());
        float file_size = Float.parseFloat(String.valueOf(file.length()));
        if(file_size/(1024*1024)>0)
        {
            song_size.setText(String.format("%.2f", file_size/(1024*1024))+"MB");
        }
        else if(file_size/(1024)>0)
            song_size.setText(file_size/1024+"KB");
        else {
            song_size.setText(file_size + "B");
        }
        System.out.println("aji songs size "+file_size);




        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(false);
        alert.setView(alertLayout);




        final AlertDialog dialog = alert.create();
        dialog.show();
        song_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    private void setRingtone(Context context, String path) {
        if (path == null) {
            System.out.println("aji null");
            return;
        }
        System.out.println("aji path "+path);

        File file = new File(path);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        String filterName = path.substring(path.lastIndexOf("/") + 1);
        contentValues.put(MediaStore.MediaColumns.TITLE, filterName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        contentValues.put(MediaStore.MediaColumns.SIZE, file.length());
        contentValues.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        System.out.println("aji content "+contentValues.toString());
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(path);
        Cursor cursor = context.getContentResolver().query(uri, null, MediaStore.MediaColumns.DATA + "=?", new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            System.out.println("aji cursor "+cursor.toString());
            String id = cursor.getString(21);
//        for(int i=0;i<10;i++)
//            System.out.println("aji "+cursor.getString(i));
//        Long id = cursor.getLong(0);
            contentValues.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            context.getContentResolver().update(uri, contentValues, MediaStore.MediaColumns.DATA + "=?", new String[]{path});
            System.out.println("aji id"+id);
            Uri newuri = ContentUris.withAppendedId(uri, Long.parseLong(id));
            try {
//            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newuri);
                RingtoneUtils.setRingtone(context,newuri);
                Toast.makeText(context, "Set as Ringtone Successfully.", Toast.LENGTH_SHORT).show();
            } catch (Throwable t) {
                Toast.makeText(context, "Set as not.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
            cursor.close();
        }
    }
}
