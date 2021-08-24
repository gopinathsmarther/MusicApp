package smarther.com.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import smarther.com.musicapp.Activity.ThemeActivity;
import smarther.com.musicapp.Model.ThemeObject;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.SessionManager;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.MyViewHolder> {

    List<ThemeObject> themeObjects;
    Context context;
    ThemeActivity themeActivity;
    SessionManager sessionManager;
    public ThemeAdapter(Context context, List<ThemeObject> themeObjects, ThemeActivity themeActivity) {
        this.context = context;
        this.themeObjects = themeObjects;
        this.themeActivity = themeActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_layout, parent, false);
sessionManager=new SessionManager(context);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;


    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

//        Glide.with(context).load(themeObjects.get(position).getImg()).into(holder.theme_image);
        holder.theme_image.setBackgroundResource(themeObjects.get(position).getImg());
        holder.theme_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sessionManager.setIsDefaultThemeSelected(true);
                sessionManager.setSelectedTheme(String.valueOf(themeObjects.get(position).getImg()));
                themeActivity.dummy_theme.setBackgroundResource(themeObjects.get(position).getImg());
                themeActivity.img_theme_bg.setBackgroundResource(themeObjects.get(position).getImg());
//                themeActivity.viewPager.setCurrentItem(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return themeObjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View theme_image;
        public MyViewHolder(View itemView) {
            super(itemView);
            theme_image =  itemView.findViewById(R.id.theme_image);
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

