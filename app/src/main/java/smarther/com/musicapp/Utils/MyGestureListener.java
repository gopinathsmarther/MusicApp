package smarther.com.musicapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.gson.Gson;

import smarther.com.musicapp.Activity.SongPlayingActivity;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Service.BackgroundMusicService;


public class MyGestureListener implements GestureDetector.OnGestureListener{

    private static final long VELOCITY_THRESHOLD = 500;
    Context context;
    SessionManager sessionManager;
//    SongPlayingActivity songPlayingActivity;
    public MyGestureListener(Context context) {
        this.context = context;
        sessionManager =new SessionManager(context);



    }









    @Override
    public boolean onDown(final MotionEvent e){ return false; }

    @Override
    public void onShowPress(final MotionEvent e){ }

    @Override
    public boolean onSingleTapUp(final MotionEvent e){ return false; }

    @Override
    public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX,
                            final float distanceY){ return false; }

    @Override
    public void onLongPress(final MotionEvent e){ }

    @Override
    public boolean onFling(final MotionEvent e1, final MotionEvent e2,
                           final float velocityX,
                           final float velocityY){



        if(Math.abs(velocityX) < VELOCITY_THRESHOLD
                && Math.abs(velocityY) < VELOCITY_THRESHOLD){
            return false;//if the fling is not fast enough then it's just like drag
        }

        //if velocity in X direction is higher than velocity in Y direction,
        //then the fling is horizontal, else->vertical
        if(Math.abs(velocityX) > Math.abs(velocityY)){
            if(velocityX >= 0){
                if (sessionManager.getSongPosition()==0) {
                    //sessionManager.setSongPosition(0);
                    System.out.println("aji position equal to playing queue");
                }
                else {

                    int next_song_position = sessionManager.getSongPosition();
                    next_song_position = next_song_position -1;
                    sessionManager.setSongPosition(next_song_position);
                    System.out.println("aji nxt position " + next_song_position);
                    Gson gson = new Gson();
                    String json = gson.toJson(SongPlayingActivity.audioModelList.get(sessionManager.getSongPosition()));
                    sessionManager.setSong_Json(json);
                    Intent intent = new Intent(context, SongPlayingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("value", 2);
                    context.startActivity(intent);
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                }

                Log.i("aji", "swipe right");

            }else{//if velocityX is negative, then it's towards left
                if (sessionManager.getSongPosition() + 1 == SongPlayingActivity.audioModelList.size()) {
                    //sessionManager.setSongPosition(0);
                    System.out.println("aji position equal to playing queue");
                }
                else {

                    int next_song_position = sessionManager.getSongPosition();
                    next_song_position = next_song_position + 1;
                    sessionManager.setSongPosition(next_song_position);
                    System.out.println("aji nxt position " + next_song_position);
                    Gson gson = new Gson();
                    String json = gson.toJson(SongPlayingActivity.audioModelList.get(sessionManager.getSongPosition()));
                    sessionManager.setSong_Json(json);
                    Intent intent = new Intent(context, SongPlayingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("value", 2);
                    context.startActivity(intent);
//                    SongPlayingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                }
                Log.i("aji", "swipe left");
            }
        }else{
            if(velocityY >= 0){
                Log.i("aji", "swipe down");
            }else{
                Log.i("aji", "swipe up");
            }
        }

        return true;
    }
}