package smarther.com.musicapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.Model.PlaylistModel;
import smarther.com.musicapp.R;

public class DuplicatePlaylistAdapter extends RecyclerView.Adapter<DuplicatePlaylistAdapter.MyViewHolder> {
    Context context;
    List<PlaylistAudioModel> audioModelList;
    Realm playlist_database;
    Realm music_database;

    public static List<PlaylistAudioModel> duplicates= new ArrayList<>();
    AlertDialog dialog;


    public DuplicatePlaylistAdapter(Context context, List<PlaylistAudioModel> duplicates, AlertDialog dialog2) {

        this.audioModelList=duplicates;
        this.context=context;
        this.dialog=dialog2;
    }

    @NonNull
    @Override
    public DuplicatePlaylistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.duplicate_recycler_layout, parent, false);
         MyViewHolder vh = new MyViewHolder(v);
        Realm.init(context);
        RealmConfiguration main_data_config = new RealmConfiguration.Builder().name("playlists.realm").deleteRealmIfMigrationNeeded().build();
        playlist_database = Realm.getInstance(main_data_config);
        Realm.init(context);
         main_data_config = new RealmConfiguration.Builder().name("music.realm").deleteRealmIfMigrationNeeded().build();
        music_database = Realm.getInstance(main_data_config);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull DuplicatePlaylistAdapter.MyViewHolder holder, final int position) {
        holder.audio_title.setText(audioModelList.get(position).getaName());
        PlaylistModel playlistModel = music_database.where(PlaylistModel.class).equalTo("playlist_id",audioModelList.get(position).getPlaylist_id()).findFirst();

        holder.playlist_name.setText("Already added in "+playlistModel.getPlaylist_name());
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlist_database.beginTransaction();

                PlaylistAudioModel playlistAudioModel=playlist_database.createObject(PlaylistAudioModel.class);

                // playlistModel.setPlaylist_name(playlist_edit_text.getText().toString());

                playlistAudioModel.setPlaylist_id(audioModelList.get(position).getPlaylist_id());
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
//                duplicates.remove(position);
                if(duplicates.size()==1){
                    duplicates.remove(position);
                    dialog.dismiss();
                } else {
                    duplicates.remove(position);
                }
//                audioModelList.remove(position);
                notifyDataSetChanged();


            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(duplicates.size()==1){
                    duplicates.remove(position);
                    dialog.dismiss();
                }
                else {
                    duplicates.remove(position);
                }
                // audioModelList.remove(position);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return audioModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
      TextView audio_title;
      TextView playlist_name;
      ImageView cancel;
      ImageView add;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            audio_title = itemView.findViewById(R.id.audio_title);
            playlist_name = itemView.findViewById(R.id.playlist_name);
            cancel = itemView.findViewById(R.id.cancel);
            add = itemView.findViewById(R.id.add);
        }
    }
}
