package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Adapter.QueueSongAdapter;
import smarther.com.musicapp.Adapter.SongsAdapter;
import smarther.com.musicapp.Model.AudioModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.RecyclerItemTouchHelper;
import smarther.com.musicapp.Utils.SessionManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayingQueue extends AppCompatActivity {
    public MusicIntentReceiver myReceiver;

    LinearLayout queue_background;
LinearLayout back_option;
TextView activity_title;
TextView song_duration;
    RecyclerView queue_song_recycler;
    SessionManager sessionManager;
    List<AudioModel> audioModelList;
    int millSecond=0;
    int songPosition =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_queue);
        queue_background=(LinearLayout)findViewById(R.id.queue_background);
        back_option=(LinearLayout)findViewById(R.id.back_option);
        activity_title=(TextView) findViewById(R.id.activity_title);
        song_duration=(TextView) findViewById(R.id.song_duration);
        queue_song_recycler = (RecyclerView)findViewById(R.id.queue_song_recycler);
        queue_song_recycler.setLayoutManager(new GridLayoutManager(PlayingQueue.this, 1, LinearLayoutManager.VERTICAL, false));
        queue_song_recycler.setHasFixedSize(true);
        queue_song_recycler.setNestedScrollingEnabled(false);
        sessionManager=new SessionManager(PlayingQueue.this);
        myReceiver = new MusicIntentReceiver();

        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        activity_title.setText(title);
        if(sessionManager.getIsDefaultThemeSelected())
        {
            queue_background.setBackgroundResource(Integer.parseInt(sessionManager.getSelectedTheme()));
        }
        else
        {
            Drawable bg;
            Uri selectedImageUri=Uri.parse(sessionManager.getFileManagerTheme());
            try {

                File f = new File(Global.getRealPathFromURI(PlayingQueue.this,selectedImageUri));
                bg= Drawable.createFromPath(f.getAbsolutePath());


//                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                encodeTobase64(bitmap);
            }
            catch (Exception e)
            {
                bg = ContextCompat.getDrawable(this, R.drawable.theme_1);
                e.printStackTrace();
            }
//            Drawable dr = new BitmapDrawable(sessionManager.getFileManagerTheme());
            queue_background.setBackground(bg);
        }
        back_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Type type = new TypeToken<ArrayList<AudioModel>>(){}.getType();
        audioModelList = new GsonBuilder().create().fromJson(sessionManager.getSong_JsonList(), type);
        int size = audioModelList.size();
         songPosition = sessionManager.getSongPosition()+1;
for(int i=songPosition-1;i<audioModelList.size();i++)
{
    try {
        Uri uri = Uri.parse(audioModelList.get(i).getaPath());
        System.out.println("aji play q path "+audioModelList.get(i).getaPath());
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(PlayingQueue.this,uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        millSecond =millSecond+Integer.parseInt(durationStr);

    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

}
song_duration.setText("Up next . "+songPosition+"/"+size+" . "+String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millSecond), TimeUnit.MILLISECONDS.toMinutes(millSecond) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millSecond)), TimeUnit.MILLISECONDS.toSeconds(millSecond) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millSecond))));
        QueueSongAdapter songsAdapter=new QueueSongAdapter(PlayingQueue.this,audioModelList);
        queue_song_recycler.setAdapter(songsAdapter);
        ItemTouchHelper.Callback callback = new RecyclerItemTouchHelper(songsAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(queue_song_recycler);
    }
    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        super.onResume();
    }
    @Override public void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }
}
