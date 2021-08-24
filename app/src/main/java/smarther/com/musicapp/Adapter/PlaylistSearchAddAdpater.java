package smarther.com.musicapp.Adapter;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import smarther.com.musicapp.Activity.PlaylistAddSearch;
import smarther.com.musicapp.Activity.PlaylistSongsDisplay;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.SessionManager;

public class PlaylistSearchAddAdpater extends RecyclerView.Adapter<PlaylistSearchAddAdpater.MyViewHolder> {
    SessionManager sessionManager;
    Context context;
    public List<AudioModel> audioModelList;
    List<PlaylistAudioModel> playlistAudioModelList;
    public PlaylistSearchAddAdpater(Context context, List<AudioModel> audioModelList) {
        this.context = context;
        this.audioModelList = audioModelList;
    }


    @NonNull
    @Override
    public PlaylistSearchAddAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_search_add_adapter, parent, false);
        sessionManager=new SessionManager(context);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlaylistSearchAddAdpater.MyViewHolder holder, final int position) {
        int millSecond=0;
        holder.song_name.setText(audioModelList.get(position).getaName());
//        holder.check_circle.getDrawable(R.id.radio_circle);
//        if(PlaylistAddSearch.selectall) {
//            holder.check_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.tick));
//            holder.check_circle.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
//            //stringListslectedids = new ArrayList<>();
//
////            PlaylistAddSearch.playlist_songs_add.setVisibility(View.VISIBLE);
////            PlaylistAddSearch.playlist_songs_add.setText("ADD " + stringListslectedids.size() + " songs");
//           // System.out.println("aji>>>>>>> list size after "+stringListslectedids.size());
//        }



//            long a = PlaylistAddSearch.playlist_id;
//            for(int val =0 ; val<audioModelList.size();val++) {
//
//
//                playlist_database.beginTransaction();
//                PlaylistAudioModel playlistAudioModel = playlist_database.createObject(PlaylistAudioModel.class);
//                playlistAudioModel.setPlaylist_id(a);
//                playlistAudioModel.setaPath(audioModelList.get(val).getaPath());
//                playlistAudioModel.setaName(audioModelList.get(val).getaName());
//                playlistAudioModel.setaAlbum(audioModelList.get(val).getaAlbum());
//                playlistAudioModel.setAlbum_id(audioModelList.get(val).getAlbum_id());
//                playlistAudioModel.setaArtist(audioModelList.get(val).getaArtist());
//                playlistAudioModel.setArtist_id(audioModelList.get(val).getArtist_id());
//                playlistAudioModel.setTrack(audioModelList.get(val).getTrack());
//                playlistAudioModel.setLength(audioModelList.get(val).getLength());
//                playlistAudioModel.setSong_added_on(audioModelList.get(val).getSong_added_on());
//                playlist_database.commitTransaction();
//            }





//        else {
//            PlaylistAddSearch.playlist_songs_add.setVisibility(View.GONE);
//            holder.check_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_circle));
//            holder.check_circle.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
////            RealmResults<PlaylistAudioModel> result = playlist_database.where(PlaylistAudioModel.class).equalTo("playlist_id",PlaylistAddSearch.playlist_id).findAll();
////            playlist_database.beginTransaction();
////            result.deleteAllFromRealm();
////            playlist_database.commitTransaction();
//        }

        if(PlaylistAddSearch.stringListslectedids.size()!=0)
        {
            if(PlaylistAddSearch.stringListslectedids.contains(audioModelList.get(position).getTemp_id()))
            {
                holder.check_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.tick));
            holder.check_circle.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
            }else {
                holder.check_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_circle));
                holder.check_circle.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            }

        } else {
            holder.check_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_circle));
            holder.check_circle.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        holder.song_artist.setText(audioModelList.get(position).getaArtist());
        Uri uri = Uri.parse(audioModelList.get(position).getaPath());
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context,uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        millSecond =millSecond+Integer.parseInt(durationStr);
        holder.song_duration.setText(" "+String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millSecond), TimeUnit.MILLISECONDS.toMinutes(millSecond) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millSecond)), TimeUnit.MILLISECONDS.toSeconds(millSecond) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millSecond))));

//        holder.song_duration.setText(durationStr);


       holder.playlist_search_add_layout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(PlaylistAddSearch.stringListslectedids.contains(audioModelList.get(position).getTemp_id()))
               {
                   System.out.println("aji click contains");
                   holder.check_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_circle));
                   holder.check_circle.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                   PlaylistAddSearch.stringListslectedids.remove(audioModelList.get(position).getTemp_id());
               }

               else {
                   System.out.println("aji click not");
                   PlaylistAddSearch.stringListslectedids.add(audioModelList.get(position).getTemp_id());
                   holder.check_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.tick));
                   holder.check_circle.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);

               }
               if(PlaylistAddSearch.stringListslectedids.size()==0)
               {
                   PlaylistAddSearch.playlist_songs_add.setVisibility(View.GONE);

               }
               else {
                   PlaylistAddSearch.playlist_songs_add.setVisibility(View.VISIBLE);
                   PlaylistAddSearch.playlist_songs_add.setText("ADD "+PlaylistAddSearch.stringListslectedids.size()+" songs");

               }
           }
       });


    }
    public  void updatelist(List<AudioModel> temp)
    {
        audioModelList = temp;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return audioModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout playlist_search_add_layout;
        TextView song_name;
        TextView song_artist;
        TextView song_duration;
         ImageView check_circle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playlist_search_add_layout = itemView.findViewById(R.id.playlist_search_add_layout);
            song_name = itemView.findViewById(R.id.song_name);
            song_artist = itemView.findViewById(R.id.song_artist);
            song_duration = itemView.findViewById(R.id.song_duration);
            check_circle = itemView.findViewById(R.id.check_circle);

        }
    }
}
