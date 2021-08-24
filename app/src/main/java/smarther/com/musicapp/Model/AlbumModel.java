package smarther.com.musicapp.Model;

import java.util.Comparator;

public class AlbumModel {
    public final long artistId;
    public final String artistName;
    public final long id;
    public final int songCount;
    public final String title;
    public final int year;
    public final String album_art;

    public AlbumModel() {

        this.id = -1;
        this.title = "";
        this.artistName = "";
        this.artistId = -1;
        this.songCount = -1;
        this.year = -1;
        this.album_art = "";
    }

    public AlbumModel(long _id, String _title, String _artistName, long _artistId, int _songCount, int _year,String _album_art) {
        this.id = _id;
        this.title = _title;
        this.artistName = _artistName;
        this.artistId = _artistId;
        this.songCount = _songCount;
        this.year = _year;
        this.album_art = _album_art;
    }

    public long getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public long getId() {
        return id;
    }

    public int getSongCount() {
        return songCount;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getAlbum_art() {
        return album_art;
    }
    public static Comparator<AlbumModel> namecomparator = new Comparator<AlbumModel>() {

        public int compare(AlbumModel s1, AlbumModel s2) {

            String StudentName1
                    = s1.getTitle().toUpperCase();
            String StudentName2
                    = s2.getTitle().toUpperCase();

            // ascending order
            return StudentName1.compareTo(
                    StudentName2);


        }
    };
    public static Comparator<AlbumModel> descending = new Comparator<AlbumModel>() {

        public int compare(AlbumModel s1, AlbumModel s2) {

            String StudentName1
                    = s1.getTitle().toUpperCase();
            String StudentName2
                    = s2.getTitle().toUpperCase();

            // ascending order
            return StudentName2.compareTo(
                    StudentName1);


        }
    };
    public static Comparator<AlbumModel> artist = new Comparator<AlbumModel>() {

        public int compare(AlbumModel s1, AlbumModel s2) {

            String StudentName1
                    = s1.getArtistName().toUpperCase();
            String StudentName2
                    = s2.getArtistName().toUpperCase();

            // ascending order
            return StudentName1.compareTo(
                    StudentName2);


        }
    };
    public static Comparator<AlbumModel> year_sort = new Comparator<AlbumModel>() {

        public int compare(AlbumModel s1, AlbumModel s2) {

//            String StudentName1
//                    = s1.g().toUpperCase();
//            String StudentName2
//                    = s2.getArtistName().toUpperCase();

            // ascending order
            return s1.getYear()-s2.getYear();


        }
    };
}