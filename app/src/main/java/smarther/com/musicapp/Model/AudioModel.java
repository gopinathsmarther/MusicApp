package smarther.com.musicapp.Model;

import android.os.Build;

import java.util.ArrayList;
import java.util.Comparator;

import androidx.annotation.RequiresApi;

public class AudioModel {


    String aPath;
    String aName;
    String aAlbum;
    String aArtist;
    String aAlbumart;
    String track;
    String song_added_on;
    String album_id;
    String artist_id;
    String length;

    public String getTemp_id() {
        return temp_id;
    }

    public void setTemp_id(String temp_id) {
        this.temp_id = temp_id;
    }

    String temp_id;
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    long id;
    int bitRate;
    int sampleRate;

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }


    public static Comparator<AudioModel> namecomparator = new Comparator<AudioModel>() {

        public int compare(AudioModel s1, AudioModel s2) {

            String StudentName1
                    = s1.getaName().toUpperCase();
            String StudentName2
                    = s2.getaName().toUpperCase();

            // ascending order
            return StudentName1.compareTo(
                    StudentName2);


        }
    };
    public static Comparator<AudioModel> descending = new Comparator<AudioModel>() {

        public int compare(AudioModel s1, AudioModel s2) {

            String StudentName1
                    = s1.getaName().toUpperCase();
            String StudentName2
                    = s2.getaName().toUpperCase();

            // ascending order
            return StudentName2.compareTo(
                    StudentName1);


        }
    };
    public static Comparator<AudioModel> artist = new Comparator<AudioModel>() {

        public int compare(AudioModel s1, AudioModel s2) {

            String StudentName1
                    = s1.getaArtist().toUpperCase();
            String StudentName2
                    = s2.getaArtist().toUpperCase();

            // ascending order
            return StudentName1.compareTo(
                    StudentName2);


        }
    };
    public static Comparator<AudioModel> album = new Comparator<AudioModel>() {

        public int compare(AudioModel s1, AudioModel s2) {

            String StudentName1
                    = s1.getaAlbum().toUpperCase();
            String StudentName2
                    = s2.getaAlbum().toUpperCase();

            // ascending order
            return StudentName1.compareTo(
                    StudentName2);


        }
    };


}