package smarther.com.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Activity.SongPlayingActivity;
import smarther.com.musicapp.Fragments.SuggestedFragment;
import smarther.com.musicapp.Model.TopRecentModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.SessionManager;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    List<TopRecentModel> topRecentModelList;
    Context context;
    SessionManager sessionManager;
    SuggestedFragment suggestedFragment;
    public HistoryAdapter(Context context, List<TopRecentModel> topRecentModelList, SuggestedFragment suggestedFragment) {
        this.context = context;
        this.topRecentModelList = topRecentModelList;
        this.suggestedFragment = suggestedFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        sessionManager=new SessionManager(context);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;


    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.other_option.setVisibility(View.GONE);

        if(topRecentModelList.get(position).getaAlbumart()==null || topRecentModelList.get(position).getaAlbumart().equals(""))
        {
            holder.audio_image.setVisibility(View.GONE);
            holder.no_audio_image.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.no_audio_image.setVisibility(View.GONE);
            holder.audio_image.setVisibility(View.VISIBLE);
            Bitmap bm= BitmapFactory.decodeFile(topRecentModelList.get(position).getaAlbumart());
            holder.audio_image.setImageBitmap(bm);
        }
        holder.audio_title.setText(topRecentModelList.get(position).getTrack());
        holder.audio_authur.setText(topRecentModelList.get(position).getaArtist());

        holder.history_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Gson gson = new Gson();
//                String json = gson.toJson(topRecentModelList.get(position));


                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("aPath", topRecentModelList.get(position).getaPath());
                    jsonObject.put("aName", topRecentModelList.get(position).getaName());
                    jsonObject.put("aAlbum", topRecentModelList.get(position).getaAlbum());
                    jsonObject.put("aArtist", topRecentModelList.get(position).getaArtist());
                    jsonObject.put("aAlbumart", topRecentModelList.get(position).getaAlbumart());
                    jsonObject.put("track", topRecentModelList.get(position).getTrack());
                    jsonObject.put("play_count", topRecentModelList.get(position).getPlay_count());
                    jsonObject.put("played_time", topRecentModelList.get(position).getPlayed_time());
                    jsonObject.put("artist_id", topRecentModelList.get(position).getArtist_id());
                    sessionManager.setSong_Json(jsonObject.toString());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


//                Gson gson_list = new GsonBuilder().create();
//                JsonArray song_array = gson_list.toJsonTree(topRecentModelList).getAsJsonArray();

                JSONArray jsonArray=new JSONArray();
                for (int i=0;i<topRecentModelList.size();i++) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("aPath", topRecentModelList.get(i).getaPath());
                        jsonObject.put("aName", topRecentModelList.get(i).getaName());
                        jsonObject.put("aAlbum", topRecentModelList.get(i).getaAlbum());
                        jsonObject.put("aArtist", topRecentModelList.get(i).getaArtist());
                        jsonObject.put("aAlbumart", topRecentModelList.get(i).getaAlbumart());
                        jsonObject.put("track", topRecentModelList.get(i).getTrack());
                        jsonObject.put("play_count", topRecentModelList.get(i).getPlay_count());
                        jsonObject.put("played_time", topRecentModelList.get(i).getPlayed_time());
                        jsonObject.put("artist_id", topRecentModelList.get(i).getArtist_id());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                    sessionManager.setSong_JsonList(jsonArray.toString());
                sessionManager.setSongPosition(position);
                //Global.intent_method(context, SongPlayingActivity.class);
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
    public int getItemCount() {
        return topRecentModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView audio_image;
        RelativeLayout no_audio_image;
        TextView audio_title;
        TextView audio_authur;
        LinearLayout other_option;
        LinearLayout history_list_layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            audio_image = (ImageView) itemView.findViewById(R.id.audio_image);
            no_audio_image = (RelativeLayout) itemView.findViewById(R.id.no_audio_image);
            audio_title = (TextView) itemView.findViewById(R.id.audio_title);
            audio_authur = (TextView) itemView.findViewById(R.id.audio_authur);
            other_option = (LinearLayout) itemView.findViewById(R.id.other_option);
            history_list_layout = (LinearLayout) itemView.findViewById(R.id.history_list_layout);
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

