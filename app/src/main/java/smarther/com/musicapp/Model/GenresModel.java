package smarther.com.musicapp.Model;

public class GenresModel {
    public long id;
    public String name;
    public int songCount;

    public GenresModel() {
        this.id = -1;
        this.name = "";
        this.songCount = -1;
    }

    public GenresModel(long _id, String _name, int _songCount) {
        this.id = _id;
        this.name = _name;
        this.songCount = _songCount;
    }

    public GenresModel(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }
}