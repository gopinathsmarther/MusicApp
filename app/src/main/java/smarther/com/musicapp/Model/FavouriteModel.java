package smarther.com.musicapp.Model;

import io.realm.RealmObject;

public class FavouriteModel extends RealmObject {

    String aPath;
    String aName;
    String aAlbum;
    String aArtist;
    String aAlbumart;
    String track;

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
}