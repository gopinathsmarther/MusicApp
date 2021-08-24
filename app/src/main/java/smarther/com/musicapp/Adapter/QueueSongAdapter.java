package smarther.com.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Activity.SongPlayingActivity;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.SessionManager;

public class QueueSongAdapter extends RecyclerView.Adapter<QueueSongAdapter.MyViewHolder> {

    List<AudioModel> audioModelList;
    Context context;
    SessionManager sessionManager;
    public QueueSongAdapter(Context context, List<AudioModel> audioModelList) {
        this.context = context;
        this.audioModelList = audioModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_list_item, parent, false);
        sessionManager=new SessionManager(context);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;


    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.e("abbas",position+" "+audioModelList.get(position).getaAlbumart());
        if(audioModelList.get(position).getaAlbumart()==null || audioModelList.get(position).getaAlbumart().equals(""))
        {
            holder.audio_image.setVisibility(View.GONE);
            holder.no_audio_image.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.no_audio_image.setVisibility(View.GONE);
            holder.audio_image.setVisibility(View.VISIBLE);
            Bitmap bm= BitmapFactory.decodeFile(audioModelList.get(position).getaAlbumart());
            holder.audio_image.setImageBitmap(bm);
//            Glide.with(context).load().into(holder.audio_image);
        }
        holder.queue_song_title.setText(audioModelList.get(position).getTrack());
        holder.queue_song_authur.setText(audioModelList.get(position).getaArtist());
        holder.queue_song_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Gson gson = new Gson();
                String json = gson.toJson(audioModelList.get(position));
                sessionManager.setSong_Json(json);

                Gson gson_list = new GsonBuilder().create();
                JsonArray song_array = gson_list.toJsonTree(audioModelList).getAsJsonArray();
                sessionManager.setSong_JsonList(song_array.toString());
                sessionManager.setSongPosition(position);
                //Global.intent_method(context, SongPlayingActivity.class);
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

    }
    public void onMove(RecyclerView recyclerView, int firstPos, int secondPos) {
        /*Do your stuff what you want
          Notify your adapter about change in positions using notifyItemMoved method
          Shift element e.g. insertion sort*/
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        /*Do your stuff what you want
          Swap element e.g. bubbleSort*/
    }

    @Override
    public int getItemCount()
    {
        return audioModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView audio_image;
        ImageView swap_image;
        ImageView close_image;
        RelativeLayout no_audio_image;
        TextView queue_song_title;
        TextView queue_song_authur;
        LinearLayout close_layout;
        LinearLayout other_option;
        LinearLayout queue_song_layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            audio_image = (ImageView) itemView.findViewById(R.id.audio_image);
            swap_image = (ImageView) itemView.findViewById(R.id.swap_image);
            close_image = (ImageView) itemView.findViewById(R.id.close_image);
            no_audio_image = (RelativeLayout) itemView.findViewById(R.id.no_audio_image);
            queue_song_title = (TextView) itemView.findViewById(R.id.queue_song_title);
            queue_song_authur = (TextView) itemView.findViewById(R.id.queue_song_authur);
            close_layout = (LinearLayout) itemView.findViewById(R.id.close_layout);
            other_option = (LinearLayout) itemView.findViewById(R.id.other_option);
            queue_song_layout = (LinearLayout) itemView.findViewById(R.id.queue_song_layout);
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

