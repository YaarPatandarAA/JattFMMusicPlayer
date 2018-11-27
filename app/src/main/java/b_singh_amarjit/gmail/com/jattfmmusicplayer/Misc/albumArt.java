package b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc;

public class albumArt {
    private String artistSong;
    private String thumbnail;
    private String type;
    private String link;

    public albumArt() {
    }

    public albumArt(String artistSong, String thumbnail, String type, String link) {
        this.artistSong = artistSong;
        this.thumbnail = thumbnail;
        this.type = type;
        this.link = link;
    }

    public String getArtistSong() {
        return artistSong;
    }

    public void setArtistSong(String artistSong) {
        this.artistSong = artistSong;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
