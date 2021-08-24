package smarther.com.musicapp.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import smarther.com.musicapp.Service.BackgroundMusicService;

public class MusicIntentReceiver  extends BroadcastReceiver {
    SessionManager sessionManager;
    @Override public void onReceive(Context context, Intent intent) {
        sessionManager = new SessionManager(context);
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    Log.d("aji", "Headset is unplugged");
                    if(sessionManager.getHeadsetPause()){
                        if(BackgroundMusicService.bg_mediaPlayer!= null)
                        {

                            if(BackgroundMusicService.bg_mediaPlayer.isPlaying() && sessionManager.getisplugged()){

                                System.out.println("aji isplaying "+BackgroundMusicService.bg_mediaPlayer.isPlaying());
                                System.out.println("aji isplugged "+sessionManager.getisplugged());
                                BackgroundMusicService.bg_mediaPlayer.pause();
                                System.out.println("aji isplaying after pause"+BackgroundMusicService.bg_mediaPlayer.isPlaying());
                                //System.out.println("aji isplugged pause"+sessionManager.getisplugged());
                            }

//
                        }
                    }

                    sessionManager.setisplugged(false);
                    break;
                case 1:
                    Log.d("aji", "Headset is plugged");
                    if(sessionManager.getHeadsetResume()){
                        if(BackgroundMusicService.bg_mediaPlayer!= null)
                        {

                            if(!BackgroundMusicService.bg_mediaPlayer.isPlaying() && !sessionManager.getisplugged()){
                                System.out.println("aji isplaying "+BackgroundMusicService.bg_mediaPlayer.isPlaying());
                                System.out.println("aji isplugged "+sessionManager.getisplugged());
                                BackgroundMusicService.bg_mediaPlayer.start();
                                System.out.println("aji isplaying after play"+BackgroundMusicService.bg_mediaPlayer.isPlaying());
                                System.out.println("aji isplugged play"+sessionManager.getisplugged());
                            }

//
                        }
                    }

                    sessionManager.setisplugged(true);
                    //BackgroundMusicService.bg_mediaPlayer.start();
                    break;
                default:
                    Log.d("aji", "I have no idea what the headset state is");
            }
        }
    }

}
