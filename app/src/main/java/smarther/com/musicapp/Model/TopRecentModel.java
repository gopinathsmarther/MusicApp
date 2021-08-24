package smarther.com.musicapp.Model;

import io.realm.RealmObject;

public class TopRecentModel extends RealmObject {

    String aPath;
    String aName;
    String aAlbum;
    String aArtist;
    String aAlbumart;
    String track;

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    int play_count;
    String played_time;
    String artist_id;


    public String getaPath() {
        return aPath;
    }

    public void setaPath(String aPath) {
        this.aPath = aPath;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaAlbum() {
        return aAlbum;
    }

    public void setaAlbum(String aAlbum) {
        this.aAlbum = aAlbum;
    }

    public String getaArtist() {
        return aArtist;
    }

    public void setaArtist(String aArtist) {
        this.aArtist = aArtist;
    }



    public String getaAlbumart() {
        return aAlbumart;
    }

    public void setaAlbumart(String aAlbumart) {
        this.aAlbumart = aAlbumart;
    }


    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public int getPlay_count() {
        return play_count;
    }

    public void setPlay_count(int play_count) {
        this.play_count = play_count;
    }

    public String getPlayed_time() {
        return played_time;
    }

    public void setPlayed_time(String played_time) {
        this.played_time = played_time;
    }
}