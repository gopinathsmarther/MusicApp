package smarther.com.musicapp.Model;

import io.realm.RealmObject;

public class MediaPlayerModel
{

    int song_position;
    String  song_timing;
    int type;

    public MediaPlayerModel(int song_position, String song_timing,int type)
    {
        this.song_position = song_position;
        this.song_timing = song_timing;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSong_position() {
        return song_position;
    }

    public void setSong_position(int song_position) {
        this.song_position = song_position;
    }

    public String getSong_timing() {
        return song_timing;
    }

    public void setSong_timing(String song_timing) {
        this.song_timing = song_timing;
    }
}