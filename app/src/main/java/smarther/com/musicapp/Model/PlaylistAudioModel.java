package smarther.com.musicapp.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PlaylistAudioModel extends RealmObject {


    public long playlist_id;


    String aPath;
    String aName;
    String aAlbum;
    String aArtist;
    String aAlbumart;
    String track;
    public long getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(long playlist_id) {
        this.playlist_id = playlist_id;
    }


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

    public String getSong_added_on() {
        return song_added_on;
    }

    public void setSong_added_on(String song_added_on) {
        this.song_added_on = song_added_on;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    String song_added_on;
    String album_id;
    String artist_id;
    String length;

}
