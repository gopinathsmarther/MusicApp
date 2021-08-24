package smarther.com.musicapp.Model;

import java.util.Comparator;

public class ArtistsModel {

    public final int albumCount;
    public final long id;
    public String name;
    public final int songCount;
    public final String  album_art;

    public ArtistsModel() {
        this.id = -1;
        this.name = "";
        this.songCount = -1;
        this.albumCount = -1;
        this.album_art = "";
    }
    public ArtistsModel(long _id, String _name, int _albumCount, int _songCount,String album_art) {
        this.id = _id;
        this.name = _name;
        this.songCount = _songCount;
        this.albumCount = _albumCount;
        this.album_art = album_art;
    }

    public int getAlbumCount() {
        return albumCount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSongCount() {
        return songCount;
    }

    public String getAlbum_art() {
        return album_art;
    }
    public static Comparator<ArtistsModel> namecomparator = new Comparator<ArtistsModel>() {

        public int compare(ArtistsModel s1, ArtistsModel s2) {

            String StudentName1
                    = s1.getName().toUpperCase();
            String StudentName2
                    = s2.getName().toUpperCase();

            // ascending order
            return StudentName1.compareTo(
                    StudentName2);


        }
    };
    public static Comparator<ArtistsModel> descending = new Comparator<ArtistsModel>() {

        public int compare(ArtistsModel s1, ArtistsModel s2) {

            String StudentName1
                    = s1.getName().toUpperCase();
            String StudentName2
                    = s2.getName().toUpperCase();

            // ascending order
            return StudentName2.compareTo(
                    StudentName1);


        }
    };
}