package smarther.com.musicapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "onboard";



    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public SessionManager() {

    }

    public Boolean getIsDefaultThemeSelected()
    {
        return pref.getBoolean("IsDefaultThemeSelected", false);
    }
    public void setIsDefaultThemeSelected(Boolean IsDefaultThemeSelected) {
        editor.putBoolean("IsDefaultThemeSelected", IsDefaultThemeSelected);
        editor.commit();
    }

    public Boolean getisplugged()
    {
        return pref.getBoolean("IsPlugged", false);
    }
    public void setisplugged(Boolean IsPlugged) {
        editor.putBoolean("IsPlugged", IsPlugged);
        editor.commit();
    }

    public Boolean getShakeEnable()
    {
        return pref.getBoolean("ShakeEnable", false);
    }
    public void setShakeEnable(Boolean ShakeEnable) {
        editor.putBoolean("ShakeEnable", ShakeEnable);
        editor.commit();
    }

    public Boolean getHeadsetPause()
    {
        return pref.getBoolean("HeadsetPause", false);
    }
    public void setHeadsetPause(Boolean HeadsetPause) {
        editor.putBoolean("HeadsetPause", HeadsetPause);
        editor.commit();
    }
    public Boolean getHeadsetResume()
    {
        return pref.getBoolean("HeadsetResume", false);
    }
    public void setHeadsetResume(Boolean HeadsetResume) {
        editor.putBoolean("HeadsetResume", HeadsetResume);
        editor.commit();
    }



    public String getSelectedTheme()
    {
        return pref.getString("SelectedTheme", "");
    }
    public void setSelectedTheme(String SelectedTheme) {
        editor.putString("SelectedTheme", SelectedTheme);
        editor.commit();
    }

 public String getFileManagerTheme()
    {
        return pref.getString("FileManagerTheme", "");
    }
    public void setFileManagerTheme(String FileManagerTheme) {
        editor.putString("FileManagerTheme", FileManagerTheme);
        editor.commit();
    }

    public Boolean getLoginStatus() {
        return pref.getBoolean("login_status", false);
    }

    public void setLoginStatus(Boolean b) {
        editor.putBoolean("login_status", b);
        editor.commit();
    }

    public String getSong_Json()
    {
        return pref.getString("Song_Json", "");
    }
    public void setSong_Json(String Song_Json) {
        editor.putString("Song_Json", Song_Json);
        editor.commit();
    }

public String getSong_JsonList()
    {
        return pref.getString("Song_JsonList", "");
    }
    public void setSong_JsonList(String Song_JsonList) {
        editor.putString("Song_JsonList", Song_JsonList);
        editor.commit();
    }
    public void removeSong_JsonList() {
        editor.remove("Song_JsonList");
        editor.commit();
    }

    public String getTopTrackList()
    {
        return pref.getString("TopTrackList", "");
    }
    public void setTopTrackList(String TopTrackList) {
        editor.putString("TopTrackList", TopTrackList);
        editor.commit();
    }
    public void removeTopTrackList() {
        editor.remove("TopTrackList");
        editor.commit();
    }

    public int getSongPosition()
    {
        return pref.getInt("SongPosition", -1);
    }
    public void setSongPosition(int SongPosition) {
        editor.putInt("SongPosition", SongPosition);
        editor.commit();
    }
    public int getSongsSort()
    {
        return pref.getInt("SongsSort", 1);
    }
    public void setSongsSort(int SongsSort) {
        editor.putInt("SongsSort", SongsSort);
        editor.commit();
    }
    public int getAlbumsSort()
    {
        return pref.getInt("AlbumsSort", 1);
    }
    public void setAlbumsSort(int AlbumsSort) {
        editor.putInt("AlbumsSort", AlbumsSort);
        editor.commit();
    }
    public int getArtistSort()
    {
        return pref.getInt("ArtistSort", 1);
    }
    public void setArtistSort(int ArtistSort) {
        editor.putInt("ArtistSort", ArtistSort);
        editor.commit();
    }
 public long getAlbumId()
    {
        return pref.getLong("AlbumId", -1);
    }
    public void setAlbumId(Long AlbumId) {
        editor.putLong("AlbumId", AlbumId);
        editor.commit();
    }

    public long getGenreId()
    {
        return pref.getLong("GenreId", -1);
    }
    public void setGenreId(Long GenreId) {
        editor.putLong("GenreId", GenreId);
        editor.commit();
    }

 public long getArtistId()
    {
        return pref.getLong("ArtistId", -1);
    }
    public void setArtistId(Long ArtistId) {
        editor.putLong("ArtistId", ArtistId);
        editor.commit();
    }
    public int getSongsGrid()
    {
        return pref.getInt("SongsGrid", 1);
    }
    public void setSongsGrid(int SongsGrid) {
        editor.putInt("SongsGrid", SongsGrid);
        editor.commit();
    }
    public int getAlbumsGrid()
    {
        return pref.getInt("AlbumsGrid", 1);
    }
    public void setAlbumsGrid(int AlbumsGrid) {
        editor.putInt("AlbumsGrid", AlbumsGrid);
        editor.commit();
    }
    public int getArtistGrid()
    {
        return pref.getInt("ArtistGrid", 1);
    }
    public void setArtistGrid(int ArtistGrid) {
        editor.putInt("ArtistGrid", ArtistGrid);
        editor.commit();
    }
    public int getArtistGridStyle()
    {
        return pref.getInt("ArtistGridStyle", 1);
    }
    public void setArtistGridStyle(int ArtistGridStyle) {
        editor.putInt("ArtistGridStyle", ArtistGridStyle);
        editor.commit();
    }
    public boolean getShowSmartPlaylist()
    {
        return pref.getBoolean("ShowSmartPlaylist", true);
    }
    public void setShowSmartPlaylist(boolean ShowSmartPlaylist) {
        editor.putBoolean("ShowSmartPlaylist", ShowSmartPlaylist);
        editor.commit();
    }

    public String getFolderName()
    {
        return pref.getString("FolderName", "");
    }
    public void setFolderName(String FolderName) {
        editor.putString("FolderName", FolderName);
        editor.commit();
    }
    public String getFolderSongCount()
    {
        return pref.getString("FolderSongCount", "");
    }
    public void setFolderSongCount(String FolderSongCount) {
        editor.putString("FolderSongCount", FolderSongCount);
        editor.commit();
    }
    public String getFolderFilePath()
    {
        return pref.getString("FolderFilePath", "");
    }

    public void setFolderFilePath(String FolderFilePath)
    {
        editor.putString("FolderFilePath", FolderFilePath);
        editor.commit();
    }
    public Boolean getRememberShuffle()
    {
        return pref.getBoolean("RememberShuffle", false);
    }

    public void setRememberShuffle(Boolean RememberShuffle)
    {
        editor.putBoolean("RememberShuffle", RememberShuffle);
        editor.commit();
    }
    public Boolean getIsRepeatSong()
    {
        return pref.getBoolean("IsRepeatSong", false);
    }

    public void setIsRepeatSong(Boolean IsRepeatSong)
    {
        editor.putBoolean("IsRepeatSong", IsRepeatSong);
        editor.commit();
    }
public Boolean getIsRepeatWholeSongs()
    {
        return pref.getBoolean("IsRepeatWholeSongs", false);
    }

    public void setIsRepeatWholeSongs(Boolean IsRepeatWholeSongs)
    {
        editor.putBoolean("IsRepeatWholeSongs", IsRepeatWholeSongs);
        editor.commit();
    }


public Boolean getIsFavourite()
    {
        return pref.getBoolean("IsFavourite", false);
    }
    public void setIsFavourite(Boolean IsFavourite) {
        editor.putBoolean("IsFavourite", IsFavourite);
        editor.commit();
    }

public Boolean getInTimerAlready()
    {
        return pref.getBoolean("in_timer", false);
    }
    public void setInTimerAlready(Boolean in_timer) {
        editor.putBoolean("in_timer", in_timer);
        editor.commit();
    }
    public String getBackgroundMusic()
    {
        return pref.getString("bgm_music", "");
    }
    public void setBackgroundMusic(String bgm_music) {
        editor.putString("bgm_music", bgm_music);
        editor.commit();
    }
 public String getFolderType()
    {
        return pref.getString("FolderType", "");
    }
    public void setFolderType(String FolderType) {
        editor.putString("FolderType", FolderType);
        editor.commit();
    }
    public int getSessionId()
    {
        return pref.getInt("sessionId",-1);
    }
    public void setSessionId(int SessionId) {
        editor.putInt("sessionId", SessionId);
        editor.commit();
    }
    public int getPosition() {
        return pref.getInt("Position", -1);
    }

    public void setPosition(int b) {
        editor.putInt("Position", b);
        editor.commit();
    }
    public int getPreverbPosition() {
        return pref.getInt("PreverbPosition", -1);
    }

    public void setPreverbPosition(int b) {
        editor.putInt("PreverbPosition", b);
        editor.commit();
    }
    public int getDupilcatePlaylistSongId() {
        return pref.getInt("DupilcatePlaylistSongId", 1);
    }

    public void setDupilcatePlaylistSongId(int b) {
        editor.putInt("DupilcatePlaylistSongId", b);
        editor.commit();
    }


    public void clear_session() {
        editor.clear();
        editor.commit();
    }

}
