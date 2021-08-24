package smarther.com.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import smarther.com.musicapp.Activity.AlbumDetailsActivity;
import smarther.com.musicapp.Activity.ArtistsDetailsActivity;
import smarther.com.musicapp.Activity.EqualizerActivity;
import smarther.com.musicapp.Activity.FolderSongActivity;
import smarther.com.musicapp.Activity.HomeScreen;
import smarther.com.musicapp.Activity.SongPlayingActivity;
import smarther.com.musicapp.Fragments.FoldersFragment;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.FolderModel;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.Model.PlaylistModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicUtils;
import smarther.com.musicapp.Utils.SessionManager;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder> {

    LinearLayout background_of_layout;
    TextView add_to_playlist_add;
    TextView add_to_playlist_cancel;
    Realm music_database;
    Realm playlist_database;
    RecyclerView playlist_alert_recyclerView;
    PlaylistAddAdapter playlistAddAdapter;
    List<PlaylistAudioModel> playlistAudioModel =new ArrayList<>();
    List<PlaylistModel> playlistModelList =new ArrayList<>();
    List<AudioModel> audioModelList;

    TextView add_all;
    TextView skip_all;
    RecyclerView duplicate_recycler;
    DuplicatePlaylistAdapter duplicatePlaylistAdapter;




    List<FolderModel> folderModelList;
    Context context;
    SessionManager sessionManager;
    private List<AudioModel> mSongs;
    long[] selected_song;
    List<AudioModel> playing_audio_list;

    public FolderAdapter(Context context, List<FolderModel> folderModelList) {
        this.context = context;
        this.folderModelList = folderModelList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_list_item, parent, false);
        sessionManager=new SessionManager(context);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;


    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        holder.folder_title.setText(folderModelList.get(position).getName());
        holder.folder_song.setText(folderModelList.get(position).getSongs()+" Songs");
        holder.folder_select_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final File f = new File(folderModelList.get(position).getPath());
                if (f.isDirectory())
                {
//                    FoldersFragment.load_folder(f);
                    sessionManager.setFolderName(folderModelList.get(position).getName());
                    sessionManager.setFolderSongCount(folderModelList.get(position).getSongs());
                    sessionManager.setFolderFilePath(folderModelList.get(position).getPath());
                    Intent i = new Intent(context, FolderSongActivity.class);
                    context.startActivity(i);
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                else if (f.isFile())
                {

                }
            }
        });
        holder.other_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(context, R.style.popupMenuStyle);
//                PopupMenu popup = new PopupMenu(wrapper, view);
                PopupMenu popup = new PopupMenu(wrapper, holder.other_option);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.folder_fragment_popupmenu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.play:
                                sessionManager.setFolderName(folderModelList.get(position).getName());
                                sessionManager.setFolderSongCount(folderModelList.get(position).getSongs());
                                sessionManager.setFolderFilePath(folderModelList.get(position).getPath());
                                List<AudioModel> arrayList=MusicUtils.getAudioFileFolderSongs(context, sessionManager.getFolderFilePath());
                                Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
                                playing_audio_list= new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
                                playing_audio_list=(arrayList);
                                System.out.println("aji album play "+arrayList.size());
                                Gson gson_list = new GsonBuilder().create();
                                JsonArray song_array = gson_list.toJsonTree(playing_audio_list).getAsJsonArray();
                                sessionManager.setSong_JsonList(song_array.toString());
                                AudioModel audioModel = arrayList.get(0);
                                Gson gson = new Gson();
                                String json = gson.toJson(audioModel);
                                sessionManager.setSong_Json(json);
                                sessionManager.setSongPosition(0);
                                // Global.intent_method(context, SongPlayingActivity.class);
                                Intent intent=new Intent(context,SongPlayingActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("value",2);
                                context.startActivity(intent);
                                break;
                            case R.id.play_next:
                                sessionManager.setFolderName(folderModelList.get(position).getName());
                                sessionManager.setFolderSongCount(folderModelList.get(position).getSongs());
                                sessionManager.setFolderFilePath(folderModelList.get(position).getPath());
                                arrayList=MusicUtils.getAudioFileFolderSongs(context, sessionManager.getFolderFilePath());
                                type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
                                playing_audio_list= new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
                                playing_audio_list.addAll(1,arrayList);
                                gson_list = new GsonBuilder().create();
                                song_array = gson_list.toJsonTree(playing_audio_list).getAsJsonArray();
                                sessionManager.setSong_JsonList(song_array.toString());
//                                final Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
//                                playing_audio_list= new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
//                                playing_audio_list.add(2,audioModelList.get(position));
//                                Gson gson_list = new GsonBuilder().create();
//                                JsonArray song_array = gson_list.toJsonTree(playing_audio_list).getAsJsonArray();
//                                sessionManager.setSong_JsonList(song_array.toString());
                                break;
                            case R.id.add_to_playing_queue:
                                sessionManager.setFolderName(folderModelList.get(position).getName());
                                sessionManager.setFolderSongCount(folderModelList.get(position).getSongs());
                                sessionManager.setFolderFilePath(folderModelList.get(position).getPath());
                                arrayList=MusicUtils.getAudioFileFolderSongs(context, sessionManager.getFolderFilePath());
                                type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
                                playing_audio_list= new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
                                playing_audio_list.addAll(arrayList);
                                gson_list = new GsonBuilder().create();
                                song_array = gson_list.toJsonTree(playing_audio_list).getAsJsonArray();
                                sessionManager.setSong_JsonList(song_array.toString());
//                                final Type types = new TypeToken<ArrayList<AudioModel>>(){}.getType();
//                                playing_audio_list= new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), types);
//                                playing_audio_list.add(audioModelList.get(position));
//                                Gson gson_lists = new GsonBuilder().create();
//                                JsonArray song_arrays = gson_lists.toJsonTree(playing_audio_list).getAsJsonArray();
//                                sessionManager.setSong_JsonList(song_arrays.toString());
                                break;
                            case R.id.add_to_playlist:
                                sessionManager.setFolderName(folderModelList.get(position).getName());
                                sessionManager.setFolderSongCount(folderModelList.get(position).getSongs());
                                sessionManager.setFolderFilePath(folderModelList.get(position).getPath());
                                audioModelList=MusicUtils.getAudioFileFolderSongs(context, sessionManager.getFolderFilePath());
                                add_to_playlist_alert(context,position);
                                break;



                            case R.id.delete_from_device:
//                                dayString = "Sunday";
                                break;
                            case R.id.share:
                                sessionManager.setFolderName(folderModelList.get(position).getName());
                                sessionManager.setFolderSongCount(folderModelList.get(position).getSongs());
                                sessionManager.setFolderFilePath(folderModelList.get(position).getPath());
                                arrayList=MusicUtils.getAudioFileFolderSongs(context, sessionManager.getFolderFilePath());
                                shareFile(arrayList);
                                break;
                            default:
//                                dayString = "Invalid day";
                        }

//                        Toast.makeText(
//                                MainActivity.this,
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();
                        return true;
                    }
                });

                popup.show(); //sho
            }
        });
    }


    private void shareFile(List<AudioModel> audioModelList_temp) {

        List<AudioModel> audioModelList1 =audioModelList_temp;

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);

        intent.setType("text/*"); /* This example is sharing jpeg images. */
        File f = new File(audioModelList1.get(0).getaPath());

        ArrayList<Uri> files = new ArrayList<Uri>();
        for (int z =0;z<audioModelList1.size();z++) {

            File file = new File(audioModelList1.get(z).getaPath());

            Uri uri = Uri.fromFile(file);
            files.add(uri);
        }
        intent.putExtra(Intent.EXTRA_TEXT, "-Shared via Muzio Player");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        intent.putExtra(Intent.EXTRA_SUBJECT,  f.getName());
        context.startActivity(Intent.createChooser(intent, f.getName()));

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
                Realm.init(context);
                RealmConfiguration main_data_config = new RealmConfiguration.Builder().name("playlists.realm").deleteRealmIfMigrationNeeded().build();
                playlist_database = Realm.getInstance(main_data_config);

                if(sessionManager.getDupilcatePlaylistSongId()==1){
                    if(playlistAddAdapter.playlistId_list.size()>0)
                    {
                        for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++){




                            // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
                            long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));
                            for(int val =0 ; val<audioModelList.size();val++) {


                                playlist_database.beginTransaction();
                                PlaylistAudioModel playlistAudioModel=playlist_database.createObject(PlaylistAudioModel.class);
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
                                playlistAddAdapter.notifyDataSetChanged();
                                System.out.println("aji " + i + " pl_ID " + playlistAddAdapter.playlistId_list.get(i));
                                System.out.println("aji " + playlist_database.toString());


                            }

                        }

                    }
                }else if(sessionManager.getDupilcatePlaylistSongId()==3){
                    if(playlistAddAdapter.playlistId_list.size()>0)
                    {
                        for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++){




                            // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
                            long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));

                            for(int val =0 ; val<audioModelList.size();val++) {

                                PlaylistAudioModel result = playlist_database.where(PlaylistAudioModel.class).equalTo("playlist_id", a).equalTo("aPath", audioModelList.get(position).getaPath()).findFirst();
                                if (result == null) {
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
                                    playlistAddAdapter.notifyDataSetChanged();
                                    System.out.println("aji " + i + " pl_ID " + playlistAddAdapter.playlistId_list.get(i));
                                    System.out.println("aji " + playlist_database.toString());
                                }

                            }

                        }

                    }
                }
                else if(sessionManager.getDupilcatePlaylistSongId()==2){
                    if(playlistAddAdapter.playlistId_list.size()>0)
                    {
                        for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++){




                            // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
                            long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));

                            for(int val =0 ; val<audioModelList.size();val++) {

                                PlaylistAudioModel result = playlist_database.where(PlaylistAudioModel.class).equalTo("playlist_id", a).equalTo("aPath", audioModelList.get(val).getaPath()).findFirst();
                                if (result == null) {
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
                                    playlistAddAdapter.notifyDataSetChanged();
                                    System.out.println("aji " + i + " pl_ID " + playlistAddAdapter.playlistId_list.get(i));
                                    System.out.println("aji " + playlist_database.toString());
                                }
                                else{
                                    PlaylistAudioModel playlistAudioModel = new PlaylistAudioModel();
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
                                    DuplicatePlaylistAdapter.duplicates.add(playlistAudioModel);
                                }



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



//                if(playlistAddAdapter.playlistId_list.size()>0)
//                {
//                    for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++){
//
//
//
//
//                        // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
//                        long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));
//                        for(int val =0 ; val<audioModelList.size();val++) {
//                            playlist_database.beginTransaction();
//
//                            PlaylistAudioModel playlistAudioModel=playlist_database.createObject(PlaylistAudioModel.class);
//                            playlistAudioModel.setPlaylist_id(a);
//                            playlistAudioModel.setaPath(audioModelList.get(val).getaPath());
//                            playlistAudioModel.setaName(audioModelList.get(val).getaName());
//                            playlistAudioModel.setaAlbum(audioModelList.get(val).getaAlbum());
//                            playlistAudioModel.setAlbum_id(audioModelList.get(val).getAlbum_id());
//                            playlistAudioModel.setaArtist(audioModelList.get(val).getaArtist());
//                            playlistAudioModel.setArtist_id(audioModelList.get(val).getArtist_id());
//                            playlistAudioModel.setTrack(audioModelList.get(val).getTrack());
//                            playlistAudioModel.setLength(audioModelList.get(val).getLength());
//                            playlistAudioModel.setSong_added_on(audioModelList.get(val).getSong_added_on());
//                            playlist_database.commitTransaction();
//                            playlistAddAdapter.notifyDataSetChanged();
//                            System.out.println("aji " + i + " pl_ID " + playlistAddAdapter.playlistId_list.get(i));
//                            System.out.println("aji " + playlist_database.toString());
//                        }
//
//
//                    }
//
//                }
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


    @Override
    public int getItemCount()
    {
        return folderModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView folder_title;
        TextView folder_song;
        LinearLayout other_option;
        LinearLayout folder_select_layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            folder_title = (TextView) itemView.findViewById(R.id.folder_title);
            folder_song = (TextView) itemView.findViewById(R.id.folder_song);
            other_option = (LinearLayout) itemView.findViewById(R.id.other_option);
            folder_select_layout = (LinearLayout) itemView.findViewById(R.id.folder_select_layout);
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
    public void getSongsForFiles(List<File> files,Context context) {
        mSongs.clear();
        for (File file : files) {
            mSongs.add(MusicUtils.getSongFromPath(file.getAbsolutePath(), context));
            selected_song = new long[mSongs.size()];

            for (int i = 0; i < mSongs.size(); i++) {
                selected_song[i] = mSongs.get(i).getId();
            }

        }
    }
}

