package smarther.com.musicapp.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Activity.AlbumDetailsActivity;
import smarther.com.musicapp.Model.AlbumModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.SessionManager;

public class SearchAlbumsAdapter extends RecyclerView.Adapter<SearchAlbumsAdapter.MyViewHolder> {

    List<AlbumModel> audioModelList;
    Context context;
    SessionManager sessionManager;
    public SearchAlbumsAdapter(Context context, List<AlbumModel> audioModelList) {
        this.context = context;
        this.audioModelList = audioModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        sessionManager=new SessionManager(context);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;


    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.other_option.setVisibility(View.GONE);

        if(audioModelList.get(position).getAlbum_art()==null || audioModelList.get(position).getAlbum_art().equals(""))
        {
            holder.audio_image.setVisibility(View.GONE);
            holder.no_audio_image.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.no_audio_image.setVisibility(View.GONE);
            holder.audio_image.setVisibility(View.VISIBLE);
            Bitmap bm= BitmapFactory.decodeFile(audioModelList.get(position).getAlbum_art());
            holder.audio_image.setImageBitmap(bm);
        }
        holder.audio_title.setText(audioModelList.get(position).getTitle());
        holder.audio_authur.setText(audioModelList.get(position).getArtistName()+" . "+audioModelList.get(position).getSongCount()+" songs");
        holder.search_artist_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setAlbumId(audioModelList.get(position).getId());
                Global.intent_method(context, AlbumDetailsActivity.class);
            }
        });

    }


    @Override
    public int getItemCount() {
        return audioModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView audio_image;
        RelativeLayout no_audio_image;
        TextView audio_title;
        TextView audio_authur;
        LinearLayout other_option;
        LinearLayout search_artist_layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            audio_image = (ImageView) itemView.findViewById(R.id.audio_image);
            no_audio_image = (RelativeLayout) itemView.findViewById(R.id.no_audio_image);
            audio_title = (TextView) itemView.findViewById(R.id.audio_title);
            audio_authur = (TextView) itemView.findViewById(R.id.audio_authur);
            other_option = (LinearLayout) itemView.findViewById(R.id.other_option);
            search_artist_layout = (LinearLayout) itemView.findViewById(R.id.search_artist_layout);
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

