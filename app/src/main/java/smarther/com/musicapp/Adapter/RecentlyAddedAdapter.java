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
import smarther.com.musicapp.Fragments.SuggestedFragment;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.SessionManager;

public class RecentlyAddedAdapter extends RecyclerView.Adapter<RecentlyAddedAdapter.MyViewHolder> {

    List<AudioModel> audioModelList;
    Context context;
    SessionManager sessionManager;
    SuggestedFragment suggestedFragment;
    public RecentlyAddedAdapter(Context context, List<AudioModel> audioModelList, SuggestedFragment suggestedFragment) {
        this.context = context;
        this.audioModelList = audioModelList;
        this.suggestedFragment = suggestedFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false);
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
        holder.audio_title.setText(audioModelList.get(position).getTrack());
        holder.audio_authur.setText(audioModelList.get(position).getSong_added_on());
        holder.song_layout.setOnClickListener(new View.OnClickListener() {
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
               // Global.intent_method(context, SongPlayingActivity.class);
                Intent intent=new Intent(context,SongPlayingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("value",2);
                context.startActivity(intent);
                suggestedFragment.getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }


    @Override
    public int getItemCount()
    {
        return audioModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView audio_image;
        RelativeLayout no_audio_image;
        TextView audio_title;
        TextView audio_authur;
        LinearLayout other_option;
        LinearLayout song_layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            audio_image = (ImageView) itemView.findViewById(R.id.audio_image);
            no_audio_image = (RelativeLayout) itemView.findViewById(R.id.no_audio_image);
            audio_title = (TextView) itemView.findViewById(R.id.audio_title);
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

}

