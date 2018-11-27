package b_singh_amarjit.gmail.com.jattfmmusicplayer.Misc;

public class albumSongs {
    private String webPageLink;
    private String mp3Link;
    private String hqMp3Link;
    private String artLink;

    private String artistName;
    private String songName;

    public albumSongs(String webPageLink, String mp3Link, String hqMp3Link, String artLink, String artistName, String songName) {
        this.webPageLink = webPageLink;
        this.mp3Link = mp3Link;
        this.hqMp3Link = hqMp3Link;
        this.artLink = artLink;
        this.artistName = artistName;
        this.songName = songName;
    }

    public String getWebPageLink() {
        return webPageLink;
    }

    public void setWebPageLink(String webPageLink) {
        this.webPageLink = webPageLink;
    }

    public String getMp3Link() {
        return mp3Link;
    }

    public void setMp3Link(String mp3Link) {
        this.mp3Link = mp3Link;
    }

    public String getHqMp3Link() {
        return hqMp3Link;
    }

    public void setHqMp3Link(String hqMp3Link) {
        this.hqMp3Link = hqMp3Link;
    }

    public String getArtLink() {
        return artLink;
    }

    public void setArtLink(String artLink) {
        this.artLink = artLink;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
