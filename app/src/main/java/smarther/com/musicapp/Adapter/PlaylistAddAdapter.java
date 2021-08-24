package smarther.com.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import smarther.com.musicapp.Model.PlaylistAudioModel;
import smarther.com.musicapp.Model.PlaylistModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.SessionManager;

public class PlaylistAddAdapter extends RecyclerView.Adapter<PlaylistAddAdapter.MyViewHolder> {
    List<PlaylistModel> playlistModelList;
    public   List<String> playlistId_list = new ArrayList<>();
    int i=0;
    Context context;
    SessionManager sessionManager;
    int[] playlist_images = {R.drawable.playlist_image1, R.drawable.playlist_image2,R.drawable.playlist_image3,R.drawable.playlist_image4,R.drawable.playlist_image5,R.drawable.playlist_image6,R.drawable.playlist_image1,R.drawable.playlist_image2, R.drawable.playlist_image3,R.drawable.playlist_image4,R.drawable.playlist_image5,R.drawable.playlist_image6,R.drawable.playlist_image1,R.drawable.playlist_image2,R.drawable.playlist_image3, R.drawable.playlist_image4,R.drawable.playlist_image5,R.drawable.playlist_image6,R.drawable.playlist_image1,R.drawable.playlist_image2,R.drawable.playlist_image3,R.drawable.playlist_image4, R.drawable.playlist_image5,R.drawable.playlist_image6,R.drawable.playlist_image1,R.drawable.playlist_image2,R.drawable.playlist_image3,R.drawable.playlist_image4,R.drawable.playlist_image5, R.drawable.playlist_image6,R.drawable.playlist_image1,R.drawable.playlist_image2,R.drawable.playlist_image3,R.drawable.playlist_image4,R.drawable.playlist_image5, R.drawable.playlist_image6,R.drawable.playlist_image1,R.drawable.playlist_image2,R.drawable.playlist_image3,R.drawable.playlist_image4,R.drawable.playlist_image5, R.drawable.playlist_image6};
    public PlaylistAddAdapter(Context context, List<PlaylistModel> playlistModelList) {
        this.context = context;
        this.playlistModelList = playlistModelList;
    }

    @NonNull
    @Override
    public PlaylistAddAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_add_adapter_layout, parent, false);
        sessionManager=new SessionManager(context);

        PlaylistAddAdapter.MyViewHolder vh = new PlaylistAddAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlaylistAddAdapter.MyViewHolder holder, final int position) {
        holder.playlist_image.setBackgroundResource(playlist_images[position % 40]);
        holder.playlist_name.setText(playlistModelList.get(position).getPlaylist_name());
        holder.playlist_add_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!holder.ischeck){

                    holder.radio_img.setImageResource(R.drawable.tick_icon);
                    holder.ischeck = true;

                    playlistId_list.add(String.valueOf(playlistModelList.get(position).getPlaylist_id()));

                }
                else
                {
                    holder.radio_img.setImageResource(R.drawable.radio_circle);
                    holder.ischeck = false;
                    playlistId_list.remove(String.valueOf(playlistModelList.get(position).getPlaylist_id()));

                }

                System.out.println("aji alert playadd "+ playlistModelList.get(position).getPlaylist_name());
//                if(positionss==null) {
//
//                }
//                else
//                {
//
//                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return playlistModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView playlist_image;
        ImageView radio_img;
        //        RelativeLayout no_audio_image;
        TextView playlist_name;
        boolean ischeck=false;
                TextView playlist_song_count;
        LinearLayout playlist_add_layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
          playlist_image =itemView.findViewById(R.id.playlist_image);
            playlist_name =itemView.findViewById(R.id.playlist_name);
            playlist_song_count =itemView.findViewById(R.id.playlist_song_count);
            radio_img =itemView.findViewById(R.id.radio_img);
            playlist_add_layout =itemView.findViewById(R.id.playlist_add_layout);

        }
    }
}
