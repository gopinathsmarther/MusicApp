package smarther.com.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import io.realm.RealmResults;
import smarther.com.musicapp.Activity.AlbumDetailsActivity;
import smarther.com.musicapp.Activity.ArtistsDetailsActivity;
import smarther.com.musicapp.Activity.PlaylistSongsDisplay;
import smarther.com.musicapp.Activity.RingtoneCutter.RingtoneCutter;
import smarther.com.musicapp.Activity.SongPlayingActivity;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.Model.PlaylistModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.SessionManager;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {

    Realm playlist_songs_relam;
    Realm music_database;
    List<PlaylistAudioModel> playlistAudioModel=new ArrayList<>();
    List<PlaylistAudioModel> playlistAudioModelList=new ArrayList<>();
    List<PlaylistModel> playlistModelList ;

    Context context;
    RecyclerView playlist_alert_recyclerView;
    LinearLayout background_of_layout;
    TextView add_to_playlist_add;
    TextView add_to_playlist_cancel;
    PlaylistAddAdapter playlistAddAdapter;
     Realm playlist_database;
    List<AudioModel> audioModelList;

    SessionManager sessionManager;

    int[] playlist_images = {R.drawable.playlist_image1, R.drawable.playlist_image2,R.drawable.playlist_image3,R.drawable.playlist_image4,R.drawable.playlist_image5,R.drawable.playlist_image6,R.drawable.playlist_image1,R.drawable.playlist_image2, R.drawable.playlist_image3,R.drawable.playlist_image4,R.drawable.playlist_image5,R.drawable.playlist_image6,R.drawable.playlist_image1,R.drawable.playlist_image2,R.drawable.playlist_image3, R.drawable.playlist_image4,R.drawable.playlist_image5,R.drawable.playlist_image6,R.drawable.playlist_image1,R.drawable.playlist_image2,R.drawable.playlist_image3,R.drawable.playlist_image4, R.drawable.playlist_image5,R.drawable.playlist_image6,R.drawable.playlist_image1,R.drawable.playlist_image2,R.drawable.playlist_image3,R.drawable.playlist_image4,R.drawable.playlist_image5, R.drawable.playlist_image6,R.drawable.playlist_image1,R.drawable.playlist_image2,R.drawable.playlist_image3,R.drawable.playlist_image4,R.drawable.playlist_image5, R.drawable.playlist_image6,R.drawable.playlist_image1,R.drawable.playlist_image2,R.drawable.playlist_image3,R.drawable.playlist_image4,R.drawable.playlist_image5, R.drawable.playlist_image6};
    public PlaylistAdapter(Context context, List<PlaylistModel> playlistModelList) {
        this.context = context;
        this.playlistModelList = playlistModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_list_item, parent, false);
        sessionManager=new SessionManager(context);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;


    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Realm.init(context);
        RealmConfiguration main_data_config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name("playlists.realm").build();
        playlist_songs_relam = Realm.getInstance(main_data_config);

        Realm.init(context);
        main_data_config = new RealmConfiguration.Builder().name("playlists.realm").deleteRealmIfMigrationNeeded().build();
        playlist_database = Realm.getInstance(main_data_config);
        Realm.init(context);
        main_data_config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name("music.realm").build();
        music_database = Realm.getInstance(main_data_config);

//        holder.no_audio_image.setVisibility(View.GONE);
//        holder.audio_image.setVisibility(View.VISIBLE);
        if(position==0){
            holder.itemView.setVisibility(View.GONE);
        }
        else {
            holder.itemView.setVisibility(View.VISIBLE);
        }
        holder.audio_image.setBackgroundResource(playlist_images[position % 40]);
        holder.audio_title.setText(playlistModelList.get(position).getPlaylist_name());

        holder.playlist_songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("aji playlistModelList.get(position).getPlaylist_id() "+ playlistModelList.get(position).getPlaylist_id());
//                playlistAudioModel = playlist_songs_relam.where(PlaylistAudioModel.class).findAll();
                playlistAudioModel =  playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id",playlistModelList.get(position).getPlaylist_id() ).findAll();
//                 playlistAudioModel.add(playlistAudioModelobj);
                if(playlistAudioModel.size()!=0) {
                    System.out.println("aji size " + playlistAudioModel.size());
                    Intent intent = new Intent(context,PlaylistSongsDisplay.class);
                    intent.putExtra("playlist_id",playlistModelList.get(position).getPlaylist_id());
                    intent.putExtra("playlist_name",playlistModelList.get(position).getPlaylist_name());
                    context.startActivity(intent);
                    //                for (int i=0;i<playlistAudioModel.size();i++){
//                    System.out.println("aji id loop"+i);
//                System.out.println("aji id loop"+playlistAudioModel.get(i).getPlaylist_id());
//                }

                }
                else{
                    System.out.println("aji "+null);}

            }
        });


//        holder.audio_authur.setVisibility(View.GONE);
//        holder.song_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Gson gson = new Gson();
//                String json = gson.toJson(playlistModelList.get(position));
//                sessionManager.setSong_Json(json);
//
//                Gson gson_list = new GsonBuilder().create();
//                JsonArray song_array = gson_list.toJsonTree(playlistModelList).getAsJsonArray();
//                sessionManager.setSong_JsonList(song_array.toString());
//                sessionManager.setSongPosition(position);
//                Global.intent_method(context, SongPlayingActivity.class);
////                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            }
//        });
        holder.other_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("aji other");
                Context wrapper = new ContextThemeWrapper(context, R.style.popupMenuStyle);
//                PopupMenu popup = new PopupMenu(wrapper, view);
                PopupMenu popup = new PopupMenu(wrapper, holder.other_option);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.playlist_fragment_popupmenu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.play:
                                if(playlistModelList.get(position)!=null) {


                                    long ids = playlistModelList.get(position).getPlaylist_id();
                                    playlistAudioModel = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id", ids).findAll();
                                    Type types = new TypeToken<ArrayList<AudioModel>>() {}.getType();
                                    List<AudioModel> audioModelList1 = new ArrayList<>();
                                    List<AudioModel> audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), types);
                                    int size = playlistAudioModel.size();
                                    for (int i = 0; i < size; i++) {
                                        AudioModel audioModel = new AudioModel();
                                        audioModel.setaName(playlistAudioModel.get(i).getaName());
                                        audioModel.setaAlbum(playlistAudioModel.get(i).getaAlbum());
                                        audioModel.setAlbum_id(playlistAudioModel.get(i).getAlbum_id());
                                        audioModel.setArtist_id(playlistAudioModel.get(i).getArtist_id());
                                        audioModel.setaArtist(playlistAudioModel.get(i).getaArtist());
                                        audioModel.setaPath(playlistAudioModel.get(i).getaPath());
                                        audioModel.setaAlbumart(playlistAudioModel.get(i).getaAlbumart());
                                        audioModel.setTrack(playlistAudioModel.get(i).getTrack());
                                        audioModelList1.add(audioModel);
                                    }

                                    AudioModel audioModel = audioModelList1.get(0);
                                    Gson gson = new Gson();
                                    String json = gson.toJson(audioModel);
                                    sessionManager.setSong_Json(json);

                                    Gson gson_list = new GsonBuilder().create();
                                    JsonArray song_array = gson_list.toJsonTree(audioModelList1).getAsJsonArray();
                                    sessionManager.setSong_JsonList(song_array.toString());
                                    sessionManager.setSongPosition(0);
                                    //Global.intent_method(context, SongPlayingActivity.class);
                                    Intent intent = new Intent(context, SongPlayingActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("value", 2);
                                    context.startActivity(intent);
                                }
                                break;
                            case R.id.play_next:
//                                final Type type = new TypeToken<ArrayList<AudioModel>>() {
//                                }.getType();
//                                playing_audio_list = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
//                                playing_audio_list.add(2, audioModelList.get(position));
//                                Gson gson_list = new GsonBuilder().create();
//                                JsonArray song_array = gson_list.toJsonTree(playing_audio_list).getAsJsonArray();
//                                sessionManager.setSong_JsonList(song_array.toString());
                                if(playlistModelList.get(position)!=null) {
                                    long ids = playlistModelList.get(position).getPlaylist_id();
                                    playlistAudioModel = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id", ids).findAll();
                                    Type types = new TypeToken<ArrayList<AudioModel>>() {
                                    }.getType();
                                    List<AudioModel> audioModelList1 = new ArrayList<>();
                                    audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), types);
                                    int size = playlistAudioModel.size();
                                    for (int i = 0; i < size; i++) {
                                        AudioModel audioModel = new AudioModel();
                                        audioModel.setaName(playlistAudioModel.get(i).getaName());
                                        audioModel.setaAlbum(playlistAudioModel.get(i).getaAlbum());
                                        audioModel.setAlbum_id(playlistAudioModel.get(i).getAlbum_id());
                                        audioModel.setArtist_id(playlistAudioModel.get(i).getArtist_id());
                                        audioModel.setaArtist(playlistAudioModel.get(i).getaArtist());
                                        audioModel.setaPath(playlistAudioModel.get(i).getaPath());
                                        audioModel.setaAlbumart(playlistAudioModel.get(i).getaAlbumart());
                                        audioModel.setTrack(playlistAudioModel.get(i).getTrack());
                                        audioModelList1.add(audioModel);
                                    }
                                    audioModelList.addAll(1, audioModelList1);

                                    Gson gson_lists = new GsonBuilder().create();
                                    JsonArray song_arrays = gson_lists.toJsonTree(audioModelList).getAsJsonArray();
                                    sessionManager.setSong_JsonList(song_arrays.toString());

                                }


                                break;
                            case R.id.add_to_playing_queue:

                                if(playlistModelList.get(position)!=null) {
                                    long ids = playlistModelList.get(position).getPlaylist_id();
                                    playlistAudioModel = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id", ids).findAll();
                                    Type types = new TypeToken<ArrayList<AudioModel>>() {
                                    }.getType();
                                    audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), types);
                                    int size = playlistAudioModel.size();
                                    for (int i = 0; i < size; i++) {
                                        AudioModel audioModel = new AudioModel();
                                        audioModel.setaName(playlistAudioModel.get(i).getaName());
                                        audioModel.setaAlbum(playlistAudioModel.get(i).getaAlbum());
                                        audioModel.setAlbum_id(playlistAudioModel.get(i).getAlbum_id());
                                        audioModel.setArtist_id(playlistAudioModel.get(i).getArtist_id());
                                        audioModel.setaArtist(playlistAudioModel.get(i).getaArtist());
                                        audioModel.setaPath(playlistAudioModel.get(i).getaPath());
                                        audioModel.setaAlbumart(playlistAudioModel.get(i).getaAlbumart());
                                        audioModel.setTrack(playlistAudioModel.get(i).getTrack());
                                        audioModelList.add(audioModel);
                                    }

                                    Gson gson_lists = new GsonBuilder().create();
                                    JsonArray song_arrays = gson_lists.toJsonTree(audioModelList).getAsJsonArray();
                                    sessionManager.setSong_JsonList(song_arrays.toString());
//                                final Type types = new TypeToken<ArrayList<AudioModel>>() {
//                                }.getType();
//                                playing_audio_list = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), types);
//                                playing_audio_list.add(audioModelList.get(position));
//                                Gson gson_lists = new GsonBuilder().create();
//                                JsonArray song_arrays = gson_lists.toJsonTree(playing_audio_list).getAsJsonArray();
//                                sessionManager.setSong_JsonList(song_arrays.toString());
                                }
                                break;
                            case R.id.add_to_playlist:
                                add_to_playlist_alert(context, position);
//                                //all_playlist(context);
                                break;
                            case R.id.go_to_album:
//                                System.out.println("aji album name" + audioModelList.get(position).getAlbum_id());
//                                sessionManager.setAlbumId(Long.parseLong(audioModelList.get(position).getAlbum_id()));
//                                Global.intent_method(context, AlbumDetailsActivity.class);
//                                break;
                            case R.id.go_to_artist:
                            case R.id.rename:



                                break;
//                                sessionManager.setArtistId(Long.parseLong(audioModelList.get(position).getArtist_id()));
//                                Global.intent_method(context, ArtistsDetailsActivity.class);
//                                break;
                            case R.id.share:
                                if(playlistModelList.get(position)!=null) {


                                    long ids = playlistModelList.get(position).getPlaylist_id();
                                    playlistAudioModel = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id", ids).findAll();
                                    Type types = new TypeToken<ArrayList<AudioModel>>() {}.getType();
                                    List<AudioModel> audioModelList1 = new ArrayList<>();
                                    int size = playlistAudioModel.size();
                                    for (int i = 0; i < size; i++) {
                                        AudioModel audioModel = new AudioModel();
                                        audioModel.setaName(playlistAudioModel.get(i).getaName());
                                        audioModel.setaAlbum(playlistAudioModel.get(i).getaAlbum());
                                        audioModel.setAlbum_id(playlistAudioModel.get(i).getAlbum_id());
                                        audioModel.setArtist_id(playlistAudioModel.get(i).getArtist_id());
                                        audioModel.setaArtist(playlistAudioModel.get(i).getaArtist());
                                        audioModel.setaPath(playlistAudioModel.get(i).getaPath());
                                        audioModel.setaAlbumart(playlistAudioModel.get(i).getaAlbumart());
                                        audioModel.setTrack(playlistAudioModel.get(i).getTrack());
                                        audioModelList1.add(audioModel);
                                    }
                                    if(playlistAudioModel.size()>0)
                                    shareFile(audioModelList1);


                                }

//                                shareFile(audioModelList.get(position).getaPath(), audioModelList.get(position).getaAlbumart());
//                                break;
                            case R.id.tag_editor:
//                                dayString = "Sunday";
                                break;
                            case R.id.details:

//                                dayString = "Sunday";
                                break;
                            case R.id.set_as_ringtone:
//                                dayString = "Sunday";
//                                setRingtone(context, audioModelList.get(position).getaPath());
                                break;
                            case R.id.clear_playlist:
                                long id = playlistModelList.get(position).getPlaylist_id();
//                                playlistAudioModel = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id",id).findAll();
//                                System.out.println("aji playlist size"+playlistAudioModel.size());
//                                for (int i=0;i<playlistAudioModel.size();i++){
//                                    final PlaylistAudioModel playlistAudioModelt = playlistAudioModel.get(i);
//                                    playlist_songs_relam.executeTransaction(new Realm.Transaction() {
//                                        @Override
//                                        public void execute(Realm realm) {
//                                            playlistAudioModelt.deleteFromRealm();
//                                        }
//                                    });

                                    RealmResults<PlaylistAudioModel> result = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id",id).findAll();
                                    playlist_songs_relam.beginTransaction();
                                    result.deleteAllFromRealm();
                                    playlist_songs_relam.commitTransaction();

//                                    System.out.println("aji clearplaylist i "+i);
//                                }
                                notifyDataSetChanged();
                                break;
                            case R.id.delete:
//                                Realm music_database;
//                                Realm.init(context);
//                                 RealmConfiguration main_data_config = new RealmConfiguration.Builder().name("music.realm").deleteRealmIfMigrationNeeded().build();
//                                music_database = Realm.getInstance(main_data_config);
//                                 id = playlistModelList.get(position).getPlaylist_id();
//                                playlistAudioModel = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id",id).findAll();
//                                for (int i=0;i<playlistAudioModel.size();i++){
//                                    final PlaylistAudioModel playlistAudioModelt = playlistAudioModel.get(i);
//                                    playlist_songs_relam.executeTransaction(new Realm.Transaction() {
//                                        @Override
//                                        public void execute(Realm realm) {
//                                            playlistAudioModelt.deleteFromRealm();
//                                        }
//                                    });
//                                }

                                id = playlistModelList.get(position).getPlaylist_id();

                                result = playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id",id).findAll();
                                playlist_songs_relam.beginTransaction();
                                result.deleteAllFromRealm();
                                playlist_songs_relam.commitTransaction();
                                //final PlaylistModel playlistModel = music_database.where(PlaylistModel.class).equalTo("playlist_id",id).findFirst();
                                RealmResults<PlaylistModel> res = music_database.where(PlaylistModel.class).equalTo("playlist_id",id).findAll();

//                                music_database.executeTransaction(new Realm.Transaction() {
//                                    @Override
//                                    public void execute(Realm realm) {
//                                        // on below line we are calling a method for deleting this course
//                                        playlistModel.deleteFromRealm();
//                                    }
//                                });
//
                                music_database.beginTransaction();
                                res.deleteAllFromRealm();
                                music_database.commitTransaction();
                                notifyDataSetChanged();




                                break;

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
    public void add_to_playlist_alert(Context context, final int position){



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
                if(playlistAddAdapter.playlistId_list.size()>0)
                {
                    for(int i=0;i<playlistAddAdapter.playlistId_list.size();i++){





                        // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());
                        long a = Long.parseLong(playlistAddAdapter.playlistId_list.get(i));  //aji
                        playlistAudioModelList =  playlist_songs_relam.where(PlaylistAudioModel.class).equalTo("playlist_id",playlistModelList.get(position).getPlaylist_id() ).findAll();
                        for (int j=0;j<playlistAudioModelList.size();j++){
                            try {
                                System.out.println("aji playlist addplaylist "+j);
                                playlist_database.beginTransaction();
                                PlaylistAudioModel playlistAudioModel=playlist_database.createObject(PlaylistAudioModel.class);
                                playlistAudioModel.setPlaylist_id(a);
                                playlistAudioModel.setaPath(playlistAudioModelList.get(j).getaPath());
                                playlistAudioModel.setaName(playlistAudioModelList.get(j).getaName());
                                playlistAudioModel.setaAlbum(playlistAudioModelList.get(j).getaAlbum());
                                playlistAudioModel.setAlbum_id(playlistAudioModelList.get(j).getAlbum_id());
                                playlistAudioModel.setaArtist(playlistAudioModelList.get(j).getaArtist());
                                playlistAudioModel.setArtist_id(playlistAudioModelList.get(j).getArtist_id());
                                playlistAudioModel.setTrack(playlistAudioModelList.get(j).getTrack());
                                playlistAudioModel.setLength(playlistAudioModelList.get(j).getLength());
                                playlistAudioModel.setSong_added_on(playlistAudioModelList.get(j).getSong_added_on());
                                playlist_database.commitTransaction();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
//                        playlistAudioModel.setPlaylist_id(a);
//                        playlistAudioModel.setaPath(audioModelList.get(position).getaPath());
//                        playlistAudioModel.setaName(audioModelList.get(position).getaName());
//                        playlistAudioModel.setaAlbum(audioModelList.get(position).getaAlbum());
//                        playlistAudioModel.setAlbum_id(audioModelList.get(position).getAlbum_id());
//                        playlistAudioModel.setaArtist(audioModelList.get(position).getaArtist());
//                        playlistAudioModel.setArtist_id(audioModelList.get(position).getArtist_id());
//                        playlistAudioModel.setTrack(audioModelList.get(position).getTrack());
//                        playlistAudioModel.setLength(audioModelList.get(position).getLength());
//                        playlistAudioModel.setSong_added_on(audioModelList.get(position).getSong_added_on());
//                        playlist_database.commitTransaction();
                        playlistAddAdapter.notifyDataSetChanged();
                        System.out.println("aji "+i+" pl_ID "+ playlistAddAdapter.playlistId_list.get(i));
                        System.out.println("aji "+playlist_database.toString());

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


    @Override
    public int getItemCount()
    {
        return playlistModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView audio_image;
//        RelativeLayout no_audio_image;
        TextView audio_title;
//        TextView audio_authur;
        LinearLayout other_option;
        LinearLayout playlist_songs;
        LinearLayout song_layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            audio_image = (ImageView) itemView.findViewById(R.id.audio_image);
//            no_audio_image = (RelativeLayout) itemView.findViewById(R.id.no_audio_image);
            audio_title = (TextView) itemView.findViewById(R.id.audio_title);
//            audio_authur = (TextView) itemView.findViewById(R.id.audio_authur);
            other_option = (LinearLayout) itemView.findViewById(R.id.other_option);
            song_layout = (LinearLayout) itemView.findViewById(R.id.song_layout);
            playlist_songs = (LinearLayout) itemView.findViewById(R.id.playlist_songs);
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

}

