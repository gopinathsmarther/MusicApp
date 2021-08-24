package smarther.com.musicapp.Model;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PlaylistModel extends RealmObject
{
    @PrimaryKey
    public long playlist_id;
    public String playlist_name;


    public long getPlaylist_id()
    {
        return playlist_id;
    }

    public void setPlaylist_id(long playlist_id)
    {
        this.playlist_id = playlist_id;
    }

    public String getPlaylist_name() {
        return playlist_name;
    }

    public void setPlaylist_name(String playlist_name) {
        this.playlist_name = playlist_name;
    }

}