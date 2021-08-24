package smarther.com.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Activity.AlbumDetailsActivity;
import smarther.com.musicapp.Activity.ArtistsDetailsActivity;
import smarther.com.musicapp.Activity.PlaylistSongsDisplay;
import smarther.com.musicapp.Activity.RingtoneCutter.RingtoneCutter;
import smarther.com.musicapp.Activity.SongPlayingActivity;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.SessionManager;

public class PlaylistItemAdapter extends RecyclerView.Adapter<PlaylistItemAdapter.MyViewHolder> {

    SessionManager sessionManager;
    JSONObject jsonObject;
    Context context;
    List<AudioModel> audioModelList = new ArrayList<>();
    List<AudioModel> playing_audio_list;
    PlaylistSongsDisplay playlistSongsDisplay;


    List<PlaylistAudioModel> playlistAudioModelList;
    public PlaylistItemAdapter(Context context, List<PlaylistAudioModel> playlistAudioModelList, PlaylistSongsDisplay playlistSongsDisplay){
        this.context =context;
        this.playlistAudioModelList =playlistAudioModelList;
        this.playlistSongsDisplay = playlistSongsDisplay;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_songs_display_item, parent, false);
        sessionManager =new SessionManager(context);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.audio_title.setText(playlistAudioModelList.get(position).getTrack());
        holder.audio_authur.setText(playlistAudioModelList.get(position).getaArtist());
        for(int i =0;i<playlistAudioModelList.size();i++)
        {
            AudioModel audioModel=new AudioModel();
            audioModel.setaName(playlistAudioModelList.get(position).getaName());
            audioModel.setaAlbum(playlistAudioModelList.get(position).getaAlbum());
            audioModel.setAlbum_id(playlistAudioModelList.get(position).getAlbum_id());
            audioModel.setArtist_id(playlistAudioModelList.get(position).getArtist_id());
            audioModel.setaArtist(playlistAudioModelList.get(position).getaArtist());
            audioModel.setaPath(playlistAudioModelList.get(position).getaPath());
            audioModel.setaAlbumart(playlistAudioModelList.get(position).getaAlbumart());
            audioModel.setTrack(playlistAudioModelList.get(position).getTrack());
            audioModelList.add(audioModel);
        }
         holder.song_layout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        System.out.println("aji playlist song position "+position);
        AudioModel audioModel=new AudioModel();
        audioModel.setaName(playlistAudioModelList.get(position).getaName());
        audioModel.setaAlbum(playlistAudioModelList.get(position).getaAlbum());
        audioModel.setAlbum_id(playlistAudioModelList.get(position).getAlbum_id());
        audioModel.setArtist_id(playlistAudioModelList.get(position).getArtist_id());
        audioModel.setaArtist(playlistAudioModelList.get(position).getaArtist());
        audioModel.setaPath(playlistAudioModelList.get(position).getaPath());
        audioModel.setaAlbumart(playlistAudioModelList.get(position).getaAlbumart());
        audioModel.setTrack(playlistAudioModelList.get(position).getTrack());

        Gson gson = new Gson();
        String json = gson.toJson(audioModel);
        sessionManager.setSong_Json(json);

        try {
            jsonObject = new JSONObject(sessionManager.getSong_Json());
            playlistSongsDisplay.playing_song_artist.setText(jsonObject.optString("aArtist"));
            playlistSongsDisplay.playing_song_folder.setText(jsonObject.optString("aAlbum"));
            playlistSongsDisplay.playing_song_name.setText(jsonObject.optString("track"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Gson gson_list = new GsonBuilder().create();
        JsonArray song_array = gson_list.toJsonTree(audioModelList).getAsJsonArray();
        sessionManager.setSong_JsonList(song_array.toString());
        sessionManager.setSongPosition(position);
       // Global.intent_method(context, SongPlayingActivity.class);
        Intent intent=new Intent(context,SongPlayingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("value",2);
        context.startActivity(intent);
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
                    public boolean onMenuItemClick(MenuItem item) {

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
                                break;
                            case R.id.add_to_playlist:
                                //add_to_playlist_alert(context,position);
                                //all_playlist(context);
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
                                //shareFile(audioModelList.get(position).getaPath(),audioModelList.get(position).getaAlbumart());
                                break;
                            case R.id.tag_editor:
//                                dayString = "Sunday";
                                break;
                            case R.id.details:

//                                dayString = "Sunday";
                                break;
                            case R.id.set_as_ringtone:
//                                dayString = "Sunday";
                               // setRingtone(context,audioModelList.get(position).getaPath());
                                break;
                            case R.id.ringtone_cutter:
//                                dayString = "Sunday";
                                Intent intent=new Intent(context, RingtoneCutter.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("mFilename",audioModelList.get(position).getaPath());
                                context.startActivity(intent);
                                break;
                            case R.id.delete_from_device:
////                                dayString = "Sunday";
//                                File file = new File(audioModelList.get(position).getaPath());
//                                file.delete();
//                                if(file.exists()){
//                                    try {
//                                        file.getCanonicalFile().delete();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if(file.exists()){
//                                        context.deleteFile(file.getName());
//                                    }
//                                }
                                File file =new File(audioModelList.get(position).getaPath());
                                System.out.println("aji + song path "+audioModelList.get(position).getaPath());
                                if(file.exists())
                                {
                                    boolean k = file.delete();
                                    System.out.println("aji + album del res "+k);
                                    if(k){
                                        Toast.makeText(context, ""+audioModelList.get(position).getaName()+" is deleted", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(context, ""+audioModelList.get(position).getaName()+" is not deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }
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
        return playlistAudioModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout song_layout;
        LinearLayout other_option;
        LinearLayout song_scroll;
        TextView audio_title;
        TextView audio_authur;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            song_layout = itemView.findViewById(R.id.song_layout);
            audio_title = itemView.findViewById(R.id.audio_title);
            audio_authur = itemView.findViewById(R.id.audio_authur);
            other_option = itemView.findViewById(R.id.other_option);
            song_scroll = itemView.findViewById(R.id.song_scroll);

        }
    }
}
